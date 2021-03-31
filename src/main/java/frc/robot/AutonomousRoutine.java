package frc.robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.IDriveSubsystem;

/**
 * Stores an autonomous program the robot can execute.
 */
public class AutonomousRoutine {

    public enum ArmStatus {
        DOWN,
        UP,
        GOING_UP,
        GOING_DOWN
    }

    public enum RollerStatus {
        STOPPED,
        COLLECTING,
        EJECTING
    }

    /**
     * This inner class is used to pack and unpack 4 values into a single value
     */
    private class Record {
        // Downcast to save space. Timer is in seconds, and 4 significant digits of precision should be more than enough
        // a float provides 7 significant digits
        public float time;
        public int left; 
        public int right;
        public ArmStatus arm; 
        public RollerStatus roller;
    }
    
    
    // 5 minute program! Each entry is worth 20ms.
    private static int MAX_ENTRIES = 15000;
    
    // Program storage. An entry is recorded every 20ms. Each entry is:
    //  - 4 bytes for the float program time.
    //  - 4 bytes for the pointer reference 
    //  - 16 bytes for the Record object (4 ints)
    // TOTAL: 24 bytes
    // So, 50 entries = 1 second
    //     3,000 entries = 1 minute
    //     15,000 entries = 5 minutes = 120,000 bytes = 352K

    Record[] program = new Record[MAX_ENTRIES];
    
    // The position in the program where the next step will be recorded or played back
    int index = 0;
    
    int programLength = 0;

    Timer programTimer = new Timer();
        
    String programName;

    public AutonomousRoutine(String programName) {
        this.programName = programName;
        for(int i=0; i<MAX_ENTRIES; i++) {
            program[i] = new Record();
        }
    }
    
    /**************** Teleop Record Methods **************/
    
    public void startRecording() {
        index = 0;
        programLength = 0;
        programTimer.reset();
        programTimer.start();
    }

    public void stopRecording() {
        index = 0;
        programTimer.stop();

        if (programLength > 0) {
            String fileName = Filesystem.getLaunchDirectory().getAbsolutePath() + "/auto-" + programName + ".csv";

            try {
                FileWriter myWriter = new FileWriter(fileName);
                myWriter.write("Time,Left,Right,Arm,Roller\n");
                for(int i=0; i<programLength; i++) {
                    myWriter.write(
                        String.format(
                            "%3.3f,%d,%d,%s,%s\n", 
                            program[i].time, 
                            program[i].left, 
                            program[i].right, 
                            program[i].arm.name(), 
                            program[i].roller.name()));
                }
                myWriter.flush();
                myWriter.close();
                System.out.println("Successfully wrote " + fileName);
            } catch (IOException e) {
                System.out.println("An error occurred writing file " + fileName);
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Record the state of the bot for the current bot position
     */
    public void recordState(
        int leftMotorPosition, 
        int rightMotorPosition, 
        ArmStatus arm, 
        RollerStatus roller) {
        
        if(index < MAX_ENTRIES) {
            program[index].time = (float)programTimer.get();
            program[index].left = leftMotorPosition; 
            program[index].right = rightMotorPosition;
            program[index].arm = arm;
            program[index].roller = roller;

            index++;
            programLength++;
        }
    }

    /*****************  Autonomous Methods ****************/
    
    
    public void startAutonomous() {
        index = 0;
    }
    
    public boolean next(){
        index++;
        return index < programLength;
    }
    
    public boolean isDone() {
        return index < 0 || index >= programLength;
    }

    public double getTime() {
        if(isDone()) { return 0; }
        return program[index].time;
    }

    public int getLeftMotor() {
        if(isDone()) { return 0; }
        return program[index].left;
    }

    public int getRightMotor() {
        if(isDone()) { return 0; }
        return program[index].right;
    }
    
    public ArmStatus getBallCollectionArm() {
        if(isDone()) { return ArmStatus.DOWN; }
        return program[index].arm;
    }
    
    public RollerStatus getBallCollectionIntakeRoller() {
        if(isDone()) { return RollerStatus.STOPPED; }
        return program[index].roller;
    }

    /**
     * Periodically, this method is called to update the robot to execute the stored routine.
     * @param driveSubsystem
     */
    public void runPeriodic(IDriveSubsystem driveSubsystem) {

        // Step 1: Calculate a ticks-per-second speed for each wheel based on the difference bewteen current encoder and desired encoder values
        // Step 2: Convert ticks-per-second into a motor speed using an empircally determined, tunable conversion factor
        // Step 3: Move in that direction until the encoders match the routine (exit method, and check on next call)

        double t = getTime();
        int left = getLeftMotor();
        int right = getRightMotor();

        boolean notDone = true;
        // Advance the routine forward 1/4 second.
        while((getTime() - t) < 0.25 && notDone) {
            
            //controlCollectionSystem();

            notDone = next();
        }

        double deltaT = getTime() - t;

        if(deltaT <= 0 || isDone()) {
            driveSubsystem.tankDrive(0, 0);

            System.out.println("Autonomous Replay: Routine complete.");
        } else {
            
            double deltaLeft = getLeftMotor() - left;
            double deltaRight = getRightMotor() - right;
            double leftSpeedEstimate = deltaLeft / deltaT / 360.0;      // Revs per second
            double rightSpeedEstimate = deltaRight / deltaT / 360.0;    // Revs per second  abs() =~ 8.0
            double leftMotorEstimate = leftSpeedEstimate / 8.0;
            double rightMotorEstimate = rightSpeedEstimate / 8.0;

            // How far away are we?
            double leftDelta = getLeftMotor() - driveSubsystem.getLeftEncoderValue();
            double rightDelta = getRightMotor() - driveSubsystem.getRightEncoderValue();

            System.out.printf("Autonomous Replay: Driving to encoder pos %d/%d. Currently at %d/%d. Setting motor to %.2f/%.2f\n",
                getLeftMotor(),
                getRightMotor(),
                driveSubsystem.getLeftEncoderValue(),
                driveSubsystem.getRightEncoderValue(),
                leftMotorEstimate,
                rightMotorEstimate);

            double leftFractionRemaining = 1, rightFractionRemaining = 1;
            int iterations = 0;
            // Repeat until the difference between where we are and where we're going 
            // is only 25% left (leave to the next iteration)
            // For a 0.25 seconds, this would be about 60 milliseconds 
            // Also, make sure we have a break out - don't run the loop more than 100 times.
            while(leftFractionRemaining > 0.25 && rightFractionRemaining > 0.25 && iterations++ < 100) {
                System.out.printf("Autonomous Replay: Driving tank with %.1f/%.1f portion of the distance remaining per wheel.\n",
                    leftFractionRemaining, rightFractionRemaining);
                driveSubsystem.tankDrive(leftMotorEstimate, rightMotorEstimate);
                Timer.delay(0.02);
                leftFractionRemaining = (getLeftMotor() - driveSubsystem.getLeftEncoderValue()) / leftDelta;
                rightFractionRemaining = (getRightMotor() - driveSubsystem.getRightEncoderValue()) / rightDelta;

                if((leftFractionRemaining - rightFractionRemaining) > 0.1) {
                    // If left is ahead of right by 10%, reduce left speed by 10%
                    leftMotorEstimate *= 0.1;
                    System.out.printf("Autonomous Replay: Adjusting left motor to %f\n", leftMotorEstimate);
                } else if((rightFractionRemaining - leftFractionRemaining) > 0.1) {
                    // If right is ahead of left by 10%, reduce right speed by 10%
                    rightMotorEstimate *= 0.1;
                    System.out.printf("Autonomous Replay: Adjusting right motor to %f\n", rightMotorEstimate);
                }
            }
        
            System.out.println("Autonomous Replay: Motor update complete.");
        }
    }
}
