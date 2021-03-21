package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.Timer;
import frc.robot.Map;
import java.lang.Math;


/**
 * Motor computation class for the Ball Collection subsystems
 * 
 * To support easier testing using a unit testing approach, this class
 * should NOT control the robot. Instead, results should all simply be
 * returned from the methods as return values.
 */
public class BallCollectionSubsystem {

    double currentArmMotorSpeed = 0.0;
    
    // Speed to use when slowing 
    private double ARM_MOTOR_SLOW_SPEED = 0.1;
    
     // Speed to use when slowing 
     private double ARM_MOTOR_FAST_SPEED = 0.5;

    // Number of seconds it took to last raise the arm
    private double lastTimeToRaiseArm = 0.7;

    // Number of seconds it took to last lower the arm
    private double lastTimeToLowerArm = 0.7;

    // What fraction of the time spent to raise or lower should be
    // done at high speed vs. low speed
    private static double HI_TO_LOW_ARM_SPEED_RATIO = 0.9;

    // Timer to track the time spent raising or lowering
    private Timer armTimer = new Timer();

    public BallCollectionSubsystem() {
    }

    
    private double computeArmMotorSpeedFromTime(double t) {
        double m = 0.0;
        
        // NATE: Finish the statement to compute x using the polynomial:
        //            m = -7t^2 + 4t + 0.3
        //
        // m = ?????????  ;
        
        return m;
    }
    /**
     * Determine the arm motor speed given the game pad inputs. Only one button should 
     * be pressed at a time, but the condition is possible, so it needs to be
     * handled. If neither button is currently pressed, it may mean the arm
     * needs to keep moving or it could mean it's time to stop it.
     *
     * @param upButton         Move the arm all the way up
     * @param downButton       Move the arm all the way down
     * @param topLimintSwich   Normally closed limit switch. When arm reaches top, this goes false
     * @param bottomLimitSwich Normally closed bottom limit switch. When arm reaches bottom, this goes false
     *
     * @return The motor speed to apply to the lifter arm
     */
    public double computeArmMotorSpeed(
        boolean upButton, 
        boolean downButton,
        boolean upSlowButton,
        boolean topLimitSwitch, 
        boolean bottomLimitSwich) {
                   
        
        // IF up button pressed THEN
        //   IF up limit NOT reached THEN
        //     Set motor speed to 0.5
        //   ELSE
        //     Set motor speed to 0
        
        if (upButton == true) {
            if (topLimitSwitch == true) {
                 currentArmMotorSpeed = 0.5;
            } else {
                 currentArmMotorSpeed = 0;
            }
        }

        // ELSE IF down button pressed THEN
        //   IF down limit NOT reached THEN
        //     Set motor speed to 0.5
        //   ELSE
        //     Set motor speed to 0.0
        
       
        else if (downButton == true  ) {
            if ( bottomLimitSwich == true ) {
                currentArmMotorSpeed = -0.5;
            } else {
                currentArmMotorSpeed = 0;
            }
        }
        
        
        // ELSE IF slow up button pressed THEN
        //   IF up limit NOT reached THEN
        //     Set motor speed to 0.1
        //   ELSE
        //     Set motor speed to 0.0
        
    
        else if ( upSlowButton == true ) {
            if ( topLimitSwitch == true ) {
                currentArmMotorSpeed = 0.1 ;
            } else {
                currentArmMotorSpeed = 0.0 ;
            }
        }
        
        
        // ELSE IF motor speed IS GREATER THAN 0.0 THEN
        //   IF up limit switch reached THEN
        //     Set set motor speed to 0

        else if ( currentArmMotorSpeed > 0.0) {
            if ( topLimitSwitch == false) {
                currentArmMotorSpeed = 0.0;
            }
        }
        
        // ELSE IF motor speed LESS THAN 0 THEN
        //   IF down limit switch reached THEN
        //     Set motor speed to 0

        else if (currentArmMotorSpeed < 0) {
            if (bottomLimitSwich == false) {
                currentArmMotorSpeed = 0.0;
            }    
        }
        
        return currentArmMotorSpeed;
    }        
    
    /**
     * Determine the roller motor speed given the intakeTrigger level and
     * the outTakeTrigger level. If both triggers are pulled, or neither are pulled,
     * the motor speed should be 0.
     */
    public double computeRollerSpeed(double intakeTriggerLevel, double outTakeTriggerLevel) {
        
        double intakeMotorSpeed = 0;
        
        // IF intake level GREATER THAN 0 THEN
        //   Set intake motor speed to the negative of the intake level
        // ELSE IF outtake level GREATER THAN 0 THEN
        //   Set the intake motor speed to the outtake trigger level
        
        if ( intakeTriggerLevel > 0 )  {
            intakeMotorSpeed = -intakeTriggerLevel;            
        } else if ( outTakeTriggerLevel > 0 ) {
            intakeMotorSpeed = outTakeTriggerLevel;
        }
        
        
        return intakeMotorSpeed;
    }
    
}
/*
start at 25% and increase by 10% each time. 
allows the robot AI to find the perfect speed at witch to raise or lower the arm
before hitting the top or bottom limit switch
step 1: guess x = run at 0.4 seconds
step 2: run motor for .4 seconds
step 3: redude to .25 (keep at .25 until we reach the limit switch)
step 4: get new "T"  (T = time to the limit switch)
step 5: figure out what "T" is worth
step 6: calculate new "X"  (X is the time at the 1.0 speed
We have to do the same thing for each up and down
*/