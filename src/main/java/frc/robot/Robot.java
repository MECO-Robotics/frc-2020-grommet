/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import frc.robot.subsystems.BallCollectionSubsystem;
import frc.robot.subsystems.DriveSubsystem;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {

    //
    // Subsystems
    //

    DriveSubsystem driveSubsystem;
    BallCollectionSubsystem ballCollectionSubsystem;

    //
    // Inputs / Outputs
    //

    Victor armLift = new Victor(Map.ARM_LIFT);
    Victor liftExtender = new Victor(Map.LIFT_EXTENDER);
    Victor intake = new Victor(Map.INTAKE);
    DigitalInput bottomLimitSwitch, topLimitSwitch;
    private final Joystick pilotStick = new Joystick(0);
    private final Joystick copilotStick = new Joystick(1);

    //
    // Autonomous
    //
    private Timer armTimer = new Timer();
    SendableChooser<String> autoSelector; // List of autonomous modes
    String autonomousMode; // Current mode selected at drivers station
    double rightAngle = 59.28185337;
    double turnAround = 118.5637067;
    private final Timer timer = new Timer();
    boolean cycleEnded = false;

    //
    // Code Version selector
    // The code version selector on the smart dashboard allows different
    // versions of the code to coexist to allow the programming team to
    // test new versions of the code without having to copy a new jar
    // file to the robot.
    //

    SendableChooser<String> codeSelector; // List of code versions available
    String selectedCodeVersion; // Current version selected at drivers station

    AutonomousRoutine autoRoutineA = new AutonomousRoutine("A");
    AutonomousRoutine autoRoutineB = new AutonomousRoutine("B");

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {

        driveSubsystem = new DriveSubsystem();
        ballCollectionSubsystem = new BallCollectionSubsystem();

        armLift.setInverted(false);
        bottomLimitSwitch = new DigitalInput(5);
        topLimitSwitch = new DigitalInput(4);

        autoSelector = new SendableChooser<>();
        autoSelector.addOption("Spin shoot demo", "spin");
        autoSelector.addOption("Drive 30in and eject", "score");
        autoSelector.addOption("Celebration", "celebrate");
        autoSelector.addOption("Test sequence", "test");
        autoSelector.addOption("Nate", "Nate");
        autoSelector.addOption("test time motion", "test time motion");
        SmartDashboard.putData("Auto Selector", autoSelector);
        SmartDashboard.putNumber("Grab Balls Distance", 42);
        SmartDashboard.putString("important message", "make sure that the arm is up for Nate auto");

        SmartDashboard.putBoolean("Alternate Code", false);
        codeSelector = new SendableChooser<>();
        codeSelector.setDefaultOption("Default", "default");
        codeSelector.addOption("Alternate", "alt");
        SmartDashboard.putData("Code Selector", codeSelector);

    }

    /**
     * This function is run once each time the robot enters autonomous mode.
     */
    @Override
    public void autonomousInit() {
        selectedCodeVersion = codeSelector.getSelected();

        autonomousMode = autoSelector.getSelected();
        timer.reset();
        timer.start();
        driveSubsystem.resetEncoders();
        cycleEnded = false;
        autoRoutineA.startAutonomous();
    }

    /**
     * This function is called periodically during autonomous. By default, called
     * every 20ms.
     */
    @Override
    public void autonomousPeriodic() {
        if (!cycleEnded) {
            if (autonomousMode.equals("score")) {
                //
                // Infinite recharge 2019 autonomous routine
                //
                // Drive straight from the starting line to the lower
                // power port and eject balls
                driveSubsystem.resetEncoders();
                driveForward(32); // not actual distance, but close
                intake.set(1);
                System.out.println("intake set");
                Timer.delay(.5);
                System.out.println("timer delay");
                intake.set(0);
                System.out.println("intake set");
                cycleEnded = true;
            } else if (autonomousMode.equals("spin")) {
                //
                // Demo routine for outreach
                //
                // Eject balls while spinning 360 degreees
                //

                // 1. Turn on the intake motor to eject balls
                // 2. Spin 360 degrees
                // 3. Turn off the intake motor
                intake.set(.5);
                turnRight(60);
                intake.set(0);
                cycleEnded = true;
            } else if (autonomousMode.equals("celebrate")) {
                driveForward(30);
                // turnRight(120);

                for (int i = 0; i < 4; i++) {
                    driveForward(10);
                    driveBack(20);
                    driveForward(10);
                    turnRight(15);
                    driveForward(10);
                    driveBack(20);
                    driveForward(10);
                    turnRight(42);

                }

                turnRight(60);

                cycleEnded = true;

            } else if (autonomousMode.equals("test")) {

                // driveBacktest(30);
                // driveForwardtest(30);
                // driveSubsystem.resetEncoders();
                turnRight(30);

                driveForwardSlow(30);
                driveBackSlow(30);
                turnRight(30);

                turnLeft(30);

                cycleEnded = true;

            } else if (autonomousMode.equals("test time motion")) {
                // remember that in tank drive negative values mean the robot moves forward and
                // positive values mean that the robot move backwards.
                double speed;

                for (int x = 0; x < 3; x++) {
                    speed = 0;

                    // accelerate forwards from 0 to -0.5 over 1 second
                    for (int i = 0; i < 4; i++) {
                        driveSubsystem.tankDrive(speed, speed);
                        Timer.delay(.1);
                        speed = speed - .2;
                    }

                    // accelerate backwards from 0 to 0.5 over 1 second
                    speed = 0;
                    for (int i = 0; i < 4; i++) {
                        driveSubsystem.tankDrive(speed, speed);
                        Timer.delay(.1);
                        speed = speed + .2;
                    }

                    // Spin slowly for 2 seconds
                    driveSubsystem.tankDrive(-.2, .2);
                    Timer.delay(2);
                    driveSubsystem.tankDrive(0, 0);
                }

                driveSubsystem.tankDrive(-.3, .3);
                Timer.delay(5);
                driveSubsystem.tankDrive(0, 0);
                // lines 207-209 = robot turning 
                cycleEnded = true;
            } else if (autonomousMode.equals("Nate")) {
                intakeDown();
                intakeUp();
                driveSubsystem.tankDrive(-2, -2);
                Timer.delay(5);
                driveSubsystem.tankDrive(-2, 2);
                Timer.delay(2.5);
                driveSubsystem.tankDrive(-2, -2);
                Timer.delay(5);
                driveSubsystem.tankDrive(-2, 2);
                Timer.delay(2.5);
                driveSubsystem.tankDrive(-2, -2);
                Timer.delay(5);
                driveSubsystem.tankDrive(-2, 2);
                Timer.delay(2.5);
                driveSubsystem.tankDrive(-2, -2);
                Timer.delay(5);
                driveSubsystem.tankDrive(-2, 2);
                Timer.delay(10);
                //should go in a square
                cycleEnded = true;

            }

            updateTelemetry();
        }
    }

    @Override
    public void simulationInit() {

    }

    @Override
    public void simulationPeriodic() {

    }

    /**
     * initialization code which will be called each time the robot enters disabled
     * mode.
     */
    @Override
    public void disabledInit() {
        autoRoutineA.stopRecording();
    }

    /**
     * This function is called periodically while the robot is disabled
     */
    @Override
    public void disabledPeriodic() {

        // System.out.println("Left encoder: " +
        // driveSubsystem.leftEncoder.getDistance());
        // System.out.println("Right encoder: " +
        // driveSubsystem.rightEncoder.getDistance());
    }

    /**
     * This function is called once each time the robot enters teleoperated mode.
     */
    @Override
    public void teleopInit() {
        selectedCodeVersion = codeSelector.getSelected();
        armLift.set(0);
        intake.set(0);

        driveSubsystem.resetEncoders();
        autoRoutineA.startRecording();
    }

    /**
     * Called periodically during teleop mode. By default, called every 20ms.
     */
    @Override
    public void teleopPeriodic() {
        if (selectedCodeVersion.equals("default")) {
            teleopPeriodicDefault();
        } else if (selectedCodeVersion.equals("alt")) {
            teleopPeriodicLegacy();
        }
    }

    /**
     * Alternate, legacy code version for teleopPeriodic
     */
    private void teleopPeriodicLegacy() {
        double rightTrigger1 = Utils.deadzone(pilotStick.getRawAxis(3), 0.1);
        double rightTrigger2 = Utils.deadzone(copilotStick.getRawAxis(3), 0.1);
        double leftTrigger1 = Utils.deadzone(pilotStick.getRawAxis(2), 0.1);
        double leftTrigger2 = Utils.deadzone(copilotStick.getRawAxis(2), 0.1);
        double yAxisLeftStick = Utils.deadzone(pilotStick.getY(), 0.1);
        double xAxisRightStick = Utils.deadzone(pilotStick.getRawAxis(4), 0.1);
        boolean aButton = copilotStick.getRawButton(1);
        boolean yButton = copilotStick.getRawButton(4);
        boolean leftBumper = pilotStick.getRawButton(5);
        boolean bButton = copilotStick.getRawButton(2);
        double throttle = Math.pow(yAxisLeftStick, 2) * Math.signum(yAxisLeftStick);
        double turn = Math.pow(xAxisRightStick, 2) * Math.signum(xAxisRightStick);
        double armLiftPow = 0.5;

        driveSubsystem.arcadeDrive(throttle, turn);

        if (leftTrigger1 > 0)
            liftExtender.setSpeed(-leftTrigger1);
        else if (rightTrigger1 > 0)
            liftExtender.setSpeed(rightTrigger1);
        else
            liftExtender.setSpeed(0);

        if (leftTrigger2 > 0)
            intake.setSpeed(-leftTrigger2);
        else if (rightTrigger2 > 0)
            intake.setSpeed(rightTrigger2);
        else
            intake.setSpeed(0);

        if (yButton && topLimitSwitch.get()) {
            intakeUp();
        } else if (aButton && bottomLimitSwitch.get()) {
            intakeDown();
        } else if (bButton) { // go up slow
            armLift.set(.1);
        } else {
            armLift.set(0);
        }

        if (leftBumper) {
            armLift.set(armLiftPow);
        }
        updateTelemetry();
    }

    /**
     * Default code version for teleopPeriodic
     */
    private void teleopPeriodicDefault() {

        // Pilot controls
        double pilotRightTrigger = Utils.deadzone(pilotStick.getRawAxis(3), 0.1);
        double pilotLeftTrigger = Utils.deadzone(pilotStick.getRawAxis(2), 0.1);
        double pilotLeftStickY = Utils.deadzone(pilotStick.getY(), 0.1);
        double pilotRightStickX = Utils.deadzone(pilotStick.getRawAxis(4), 0.1);
        boolean pilotLeftBumper = pilotStick.getRawButton(5);

        // Copilot controls
        double copilotRightTrigger = Utils.deadzone(copilotStick.getRawAxis(3), 0.1);
        double copilotLeftTrigger = Utils.deadzone(copilotStick.getRawAxis(2), 0.1);
        boolean copilotAButton = copilotStick.getRawButton(1);
        boolean copilotYButton = copilotStick.getRawButton(4);
        boolean copilotBButton = copilotStick.getRawButton(2);

        //
        // Drive Subsystem - Compute split arcade values
        //
        // Square the stick values and restore the sign
        // TODO: improve this. We could be capping our throttle or turn, or not maxing
        // out.

        double throttle = Math.pow(pilotLeftStickY, 2) * Math.signum(pilotLeftStickY);
        double turn = Math.pow(pilotRightStickX, 2) * Math.signum(pilotRightStickX);

        driveSubsystem.arcadeDrive(throttle, turn);

        //
        // Lift Extendor
        //

        if (pilotLeftTrigger > 0) {
            liftExtender.setSpeed(-pilotLeftTrigger);
        } else if (pilotRightTrigger > 0) {
            liftExtender.setSpeed(pilotRightTrigger);
        } else {
            liftExtender.setSpeed(0);
        }

        //
        // Ball Collection Subsystem
        //

        // Arm

        double armMotorSpeed = ballCollectionSubsystem.computeArmMotorSpeed(copilotYButton, // up button
                copilotAButton, // dn button
                copilotBButton, // up slow button
                topLimitSwitch.get(), // top limit switch HAS NOT been reached (normally closed switch)
                bottomLimitSwitch.get()); // btm limit switch HAS NOT been reached (normally closed switch)

        armLift.set(armMotorSpeed);

        // Intake roller

        double intakeMotorSpeed = ballCollectionSubsystem.computeRollerSpeed(copilotLeftTrigger, // intake level
                copilotRightTrigger); // outtake level

        intake.setSpeed(intakeMotorSpeed);

        autoRoutineA.recordState(driveSubsystem.getLeftEncoderValue(), driveSubsystem.getRightEncoderValue(),
                AutonomousRoutine.ArmStatus.DOWN, // todo: Get from ball collection subsys
                AutonomousRoutine.RollerStatus.STOPPED);

        updateTelemetry();
    }

    /**
     * This function is called once when entering test mode. Test mode is a special
     * mode that can be used to perform a self-test of the robot to make sure
     * everything is powered on, connected, and working.
     */
    @Override
    public void testInit() {
        driveSubsystem.resetEncoders();
        driveForward(36); // Drive forward a yard

        System.out.printf("TEST: Moved forward 1 yard at 1/2 speed. Encoder ticks (L/R): %d/%d\n",
                driveSubsystem.getLeftEncoderValue(), driveSubsystem.getRightEncoderValue());

        driveBack(36); // Drive backward a yard

        System.out.printf("TEST: Moved backward 1 yard at 1/2 speed. Encoder ticks (L/R): %d/%d\n",
                driveSubsystem.getLeftEncoderValue(), driveSubsystem.getRightEncoderValue());
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }

    /**
     * Updating the telemtry
     */
    private void updateTelemetry() {
        SmartDashboard.putNumber("Right Encoder Count", driveSubsystem.getRightDistance());
        SmartDashboard.putNumber("Left Encoder Count", driveSubsystem.getLeftDistance());
    }

    private void intakeDown() {
        armLift.set(-.5);
        timer.delay(.7);
        armLift.set(0);
    }

    private void intakeUp() {
        armLift.set(.5);
        timer.delay(.7);
        armLift.set(0);
    }

    private void driveForward(double far) {
        while (driveSubsystem.getLeftDistance() < far) {
            driveSubsystem.tankDrive(-.25, -.25);
        }
        driveSubsystem.tankDrive(0, 0);
    }

    private void turnRight(double turn) {
        while (driveSubsystem.getLeftDistance() < turn) {
            driveSubsystem.tankDrive(-1, 1);
        }
        driveSubsystem.tankDrive(0, 0);
    }

    private void turnLeft(double turn) {
        while (driveSubsystem.getLeftDistance() < turn) {
            driveSubsystem.tankDrive(1, -1);
        }
        driveSubsystem.tankDrive(0, 0);
    }

    private void driveBack(double far) {
        while (driveSubsystem.getLeftDistance() < far) {
            driveSubsystem.tankDrive(1, 1);
        }
        driveSubsystem.tankDrive(0, 0);
    }

    // private void circleLeft(double d, double distance) {
    // double I = d / (d + 38);
    // double o = 1;
    // double inc = 3.14*d;
    // driveSubsystem.tankDrive(1, I);
    /// 1 or o is the left wheel
    /// I is the right wheel
    // while (driveSubsystem.tankdrive.getRightDistance() < far)
    // }

    private void driveForwardSlow(double far) {
        double destination = driveSubsystem.getLeftDistance() + far;
        while (driveSubsystem.getLeftDistance() < destination) {
            driveSubsystem.tankDrive(-0.25, -0.25);
        }
        driveSubsystem.tankDrive(0, 0);
    }

    private void driveBackSlow(double far) {
        double destination = driveSubsystem.getLeftDistance() - far;
        while (driveSubsystem.getLeftDistance() > destination) {
            driveSubsystem.tankDrive(0.25, 0.25);
        }
        driveSubsystem.tankDrive(0, 0);
    }

    // driveSubsystem.tankDrive(0,0)
}