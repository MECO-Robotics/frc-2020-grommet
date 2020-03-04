/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import javax.lang.model.util.ElementScanner6;

import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PWMVictorSPX;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

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
  Victor liftRetracker = new Victor(Map.LIFT_RETRACTER);
  DigitalInput bottomLimitSwitch,topLimitSwitch;

  private final Joystick m_stick = new Joystick(0);
  private final Joystick m_stick2 = new Joystick(1);
  private final Timer m_timer = new Timer();
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
  }

  /**
   * This function is run once each time the robot enters autonomous mode.
   */
  @Override
  public void autonomousInit() {
    m_timer.reset();
    m_timer.start();
    driveSubsystem.resetEncoders();
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    
    
    /*while (driveSubsystem.leftEncoder.getDistance()<32){
      driveSubsystem.tankDrive(-.3, -.3);  
      updateTelemtry();
    }
    driveSubsystem.tankDrive(0, 0);
    while (m_timer.get()<10 && 5<m_timer.get()){
      intake.set(1);
    }*/
    while(bottomLimitSwitch.get()){
      armLift.set(-.3);
    }
    armLift.set(0);
    while (driveSubsystem.leftEncoder.getDistance()<32){
      driveSubsystem.tankDrive(-.3, -.3);
      intake.set(-1);  
      updateTelemtry();
    }
    intake.set(0);
    driveSubsystem.tankDrive(0, 0);
    while (topLimitSwitch.get()){
      armLift.set(.5);
      m_timer.delay(1);
    }

    armLift.set(0);
    driveSubsystem.tankDrive(0, 0);
     // stop robot
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

  /**
   * This function is called periodically during teleoperated mode.
   */

   @Override
   public void disabledPeriodic() {
     //System.out.println("Left encoder: " + driveSubsystem.leftEncoder.getDistance());
     //System.out.println("Right encoder: " + driveSubsystem.rightEncoder.getDistance());
   }

  public static final int INTAKE_ARM_UP = 0; // TODO
  public static final int INTAKE_ARM_DOWN = 0; // TODO 
  public int intakeArmPosition = INTAKE_ARM_DOWN;
  public boolean intakeButtonWasHeld = false;

  @Override
  public void teleopPeriodic() {
    double rightTrigger1 = Utils.deadzone(m_stick.getRawAxis(3), 0.1);
    double leftTrigger1 = Utils.deadzone(m_stick.getRawAxis(2), 0.1);
    double rightTrigger2 = Utils.deadzone(m_stick2.getRawAxis(3), 0.1);
    double leftTrigger2 = Utils.deadzone(m_stick2.getRawAxis(2), 0.1);
    double yAxisLeftStick = Utils.deadzone(m_stick.getY(), 0.1);
    double xAxisRightStick = Utils.deadzone(m_stick.getRawAxis(4), 0.1);
    boolean aButton = m_stick2.getRawButton(1);
    boolean yButton = m_stick2.getRawButton(4);
    boolean select = m_stick.getRawButton(7);
    boolean leftBumper = m_stick.getRawButton(5);
    boolean bButton = m_stick2.getRawButton(2);
    double throttle = Math.pow(yAxisLeftStick, 2) * Utils.sign(yAxisLeftStick);
    double turn = Math.pow(xAxisRightStick, 2) * Utils.sign(xAxisRightStick);
    driveSubsystem.arcadeDrive(throttle, turn);
    armLift.set(0);
    liftExtender.set(rightTrigger1);
    liftRetracker.set(leftTrigger1);

    if (leftTrigger2 > 0)
      intake.setSpeed(-leftTrigger2);
    else if (rightTrigger2 > 0)
      intake.setSpeed(rightTrigger2);
    else
      intake.setSpeed(0);
    
    final double armLiftPow = 0.5;
 
    if (yButton && topLimitSwitch.get()) {
      armLift.set(armLiftPow);
    } else if (aButton && bottomLimitSwitch.get()) {
      armLift.set(-armLiftPow);
    } else if (bButton) {
      armLift.set(.1);
    } else {
      armLift.set(0);
    }
    
    if (leftBumper){
      armLift.set(armLiftPow);
    }
    /*if (yButton && bottomLimitSwitch.get()){
      while (!topLimitSwitch.get()){
        armLift.set(1);
      }
      armLift.set(0);
    }else if (aButton && topLimitSwitch.get()){
      while (!bottomLimitSwitch.get()){
        armLift.set(-1);
      }
      armLift.set(0);
    }else if (aButton && topLimitSwitch.get() && bottomLimitSwitch.get()){
      while (!bottomLimitSwitch.get()){
        armLift.set(-1);
      }
      armLift.set(0);
    }else if (select){
      armLift.set(0);
    }*/
    updateTelemtry();
    if (select){
      armLift.set(0);
    }
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
   public void lift(){

   }
}
