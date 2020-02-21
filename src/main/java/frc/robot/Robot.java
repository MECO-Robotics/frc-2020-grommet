/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.Encoder;
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

  Victor armLift = new Victor(5);
  Victor liftExtender = new Victor(6);
  Victor intake = new Victor(7);
  Victor liftRetracker = new Victor(8);
  
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
    while (driveSubsystem.leftEncoder.getDistance()<120) {
      driveSubsystem.tankDrive(1, 1);  
      updateTelemtry();
    }
    driveSubsystem.tankDrive(0, 0); // stop robot
    updateTelemtry();
    
  }

  /**
   * This function is called once each time the robot enters teleoperated mode.
   */
  @Override
  public void teleopInit() {
  }

  /**
   * This function is called periodically during teleoperated mode.
   */

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
    boolean bButton = m_stick2.getRawButton(2);

    driveSubsystem.arcadeDrive(yAxisLeftStick, xAxisRightStick);//Setting the motors to half speed based off the joystick inputs
    //m_robotDrive.tankDrive(-m_stick.getY()*.5, -m_stick.getRawAxis(5)*.5);

    liftExtender.set(rightTrigger1);
    liftRetracker.set(leftTrigger1);

    intake.setSpeed(rightTrigger2);

    if (bButton && !intakeButtonWasHeld) {
      if (intakeArmPosition == INTAKE_ARM_UP) {
        armLift.setPosition(INTAKE_ARM_DOWN);
        intakeArmPosition = INTAKE_ARM_DOWN;
      } else {
        armLift.setPosition(INTAKE_ARM_UP);
        intakeArmPosition = INTAKE_ARM_UP;
      }
    }

    intakeButtonWasHeld = bButton;

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
}
