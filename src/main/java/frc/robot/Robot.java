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
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import frc.robot.subsystems.BallCollectionSubsystem;


/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends TimedRobot {
  
  public DriveSubsystem driveSubsystem;

  Victor armLift = new Victor(Map.ARM_LIFT);
  Victor liftExtender = new Victor(Map.LIFT_EXTENDER);
  Victor intake = new Victor(Map.INTAKE);
  DigitalInput bottomLimitSwitch,topLimitSwitch;
  double rightAngle = 59.28185337;
  double turnAround = 118.5637067;
  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_stick2 = new Joystick(1);
  private final Timer m_timer = new Timer();

  SendableChooser<String> autoSelector;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    
    driveSubsystem = new DriveSubsystem();
    armLift.setInverted(false);
    bottomLimitSwitch = new DigitalInput(5);
    topLimitSwitch = new DigitalInput(4); 

    autoSelector = new SendableChooser<>();
    autoSelector.addOption("Grab Balls", "grab");
    autoSelector.addOption("Score Balls", "score");
    autoSelector.addOption("grab and score", "grab and score");
    SmartDashboard.putData("Auto Selector", autoSelector);
    SmartDashboard.putNumber("Grab Balls Distance", 42);
    
  }

  String auto;

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    auto = autoSelector.getSelected();
    m_timer.reset();
    m_timer.start();
    driveSubsystem.resetEncoders();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    if (auto.equals("score")) {
      driveForward(32);
      intake.set(1);   
      m_timer.delay(.5);
      intake.set(0);
      driveBack(0); 
    } else if (auto.equals("grab")) {
      double grabBallsDistance = SmartDashboard.getNumber("Grab Balls Distance", 0);
      intakeDown();
      intake.set(-1);
      driveForward(grabBallsDistance);
      intake.set(0);
      intakeUp();
      m_timer.delay(10);
    } else if (auto.equals("grab and score")){
      driveForward(86.63);
      intakeDown();
      intake.set(.5);
      driveForward(159.63);
      intake.set(0);
      turnRight(218.941);
      driveForward(316.911);
      turnLeft(257.63);
      driveForward(285.38);
      turnRight(344.661);
      driveForward(367.461);
      intake.set(-.5);
      m_timer.delay(.5);
      intake.set(0);
    }
    updateTelemtry();
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
    armLift.set(0);
    intake.set(0);
  }

 // 
    
    /** 
  
  
   * This function is called periodically during teleoperated mode.
   */

   @Override
   public void disabledPeriodic() {
     //System.out.println("Left encoder: " + driveSubsystem.leftEncoder.getDistance());
     //System.out.println("Right encoder: " + driveSubsystem.rightEncoder.getDistance());
   }

  public boolean intakeButtonWasHeld = false;

  @Override
  public void teleopPeriodic() {
    double rightTrigger1 = Utils.deadzone(m_stick.getRawAxis(3), 0.1);
    double rightTrigger2 = Utils.deadzone(m_stick2.getRawAxis(3), 0.1);
    double leftTrigger1 = Utils.deadzone(m_stick.getRawAxis(2), 0.1);
    double leftTrigger2 = Utils.deadzone(m_stick2.getRawAxis(2), 0.1);
    double yAxisLeftStick = Utils.deadzone(m_stick.getY(), 0.1);
    double xAxisRightStick = Utils.deadzone(m_stick.getRawAxis(4), 0.1);
    boolean aButton = m_stick2.getRawButton(1);
    boolean yButton = m_stick2.getRawButton(4);
    boolean leftBumper = m_stick.getRawButton(5);
    boolean bButton = m_stick2.getRawButton(2);
    double throttle = Math.pow(yAxisLeftStick, 2) * Utils.sign(yAxisLeftStick);
    double turn = Math.pow(xAxisRightStick, 2) * Utils.sign(xAxisRightStick);
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
    } else if (bButton) {
      armLift.set(.1);
    } else {
      armLift.set(0);
    }

    if (leftBumper){
      armLift.set(armLiftPow);
    }
    updateTelemtry();
  }

    public void teleopPeriodicNew() {
        //pilot controls
        double pilotRightTrigger = Utils.deadzone(m_stick.getRawAxis(3), 0.1);
        double pilotLeftTrigger = Utils.deadzone(m_stick.getRawAxis(2), 0.1);
        double pilotLeftStickY = Utils.deadzone(m_stick.getY(), 0.1);
        double pilotRightStickX = Utils.deadzone(m_stick.getRawAxis(4), 0.1);
        boolean pilotLeftBumper = m_stick.getRawButton(5);

        // Copilot controls
        double copilotRightTrigger = Utils.deadzone(m_stick2.getRawAxis(3), 0.1);
        double copilotLeftTrigger = Utils.deadzone(m_stick2.getRawAxis(2), 0.1);
        boolean copilotAButton = m_stick2.getRawButton(1);
        boolean copilotYButton = m_stick2.getRawButton(4);
        boolean copilotBButton = m_stick2.getRawButton(2);
        
        double throttle = Math.pow(pilotLeftStickY, 2) * Utils.sign(pilotLeftStickY);
        double turn = Math.pow(pilotRightStickX, 2) * Utils.sign(pilotRightStickX);
        double armLiftPower = 0.5;
   
        BallCollectionSubsystem collector=new BallCollectionSubsystem();
        
        double armMotorSpeed= collector.computeArmMotorSpeed(
            copilotYButton, copilotAButton, topLimitSwitch.get(), bottomLimitSwitch.get() );
        
        
        
        updateTelemtry();  
    }
    
    
    
    
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }


  /** 
   * Start work of telemtry
   * */
   public void telemtry(){
   }
   //Updating the telemtry
   public void updateTelemtry(){
     SmartDashboard.putNumber("Right Encoder Count", driveSubsystem.rightEncoder.getDistance());
     SmartDashboard.putNumber("Left Encoder Count", driveSubsystem.leftEncoder.getDistance());
   }
   
   public void intakeDown(){
      armLift.set(-.5);
      m_timer.delay(.5);
      armLift.set(0);
   
  }
   
   public void intakeUp(){
       
       //armLift1-10
            //chimkin
      armLift.set(.5);
      m_timer.delay(.7);
      armLift.set(0);
     
   }
   public void driveForward(double far){
    while (driveSubsystem.leftEncoder.getDistance()<far){
      driveSubsystem.tankDrive(-.5, -.5);
    }
      driveSubsystem.tankDrive(0, 0);
   }
   public void turnRight(double turn){
    while (driveSubsystem.leftEncoder.getDistance()<turn){
      driveSubsystem.tankDrive(-1, 1);
    }
      driveSubsystem.tankDrive(0, 0);
   }
   public void turnLeft(double turn){
     while (driveSubsystem.leftEncoder.getDistance()<turn){
       driveSubsystem.tankDrive(1, -1);
     }
      driveSubsystem.tankDrive(0, 0);
   }
   public void driveBack(double far){
    while (driveSubsystem.leftEncoder.getDistance()<far){
      driveSubsystem.tankDrive(1, 1);
    }
      driveSubsystem.tankDrive(0, 0);
   }
}
