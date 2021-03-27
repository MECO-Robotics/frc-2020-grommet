package frc.robot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.Timer;

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
    
    public double getTime() {
        return program[index].time;
    }

    public int getLeftMotor() {
        return program[index].left;
    }
    
    public int getRightMotor() {
        return program[index].right;
    }
    
    public ArmStatus getBallCollectionArm() {
        return program[index].arm;
    }
    
    public RollerStatus getBallCollectionIntakeRoller() {
        return program[index].roller;
    }
}
