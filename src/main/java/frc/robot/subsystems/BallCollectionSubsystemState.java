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
public class BallCollectionSubsystemState {

    double currentArmMotorSpeed = 0.0;
    
    // Speed to use when slowing 
    private static final double ARM_MOTOR_MIN_SPEED = 0.1;
    
     // Speed to use when slowing 
    private static final double ARM_MOTOR_MAX_SPEED = 0.5;

    // The a, b, and c coeeficients of the 2nd order polynomial equation that
    // computes motor speed based on time.
    private double speedEquationA = -7.0;
    private double speedEquationB = 4.0;
    private double speedEquationC = 0.3;

    // Number of seconds it took to last raise the arm
    private double lastTimeToRaiseArm = 0.7;

    // Number of seconds it took to last lower the arm
    private double lastTimeToLowerArm = 0.7;

    // Timer to track the time spent raising or lowering
    private Timer armTimer = new Timer();

    public BallCollectionSubsystemState() {
    }

    public double getCurrentArmMotorSpeed() {
        return currentArmMotorSpeed;
    }
    
    private double computeArmMotorSpeedFromTime(double t) {
        double m = 0.0;
        
        m = speedEquationA * t * t  +  speedEquationB * t  +  speedEquationC  ;
        
        // TODO: NATE - STEP 2: Change the coefficients of the polynomial equation to use
        //       the constants defined on this class

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
        //   IF up limit NOT reached AND the motor is moving down or is stopped THEN
        //     Set motor speed to 0.5
        //   ELSE IF up limit switch reached
        //     Set motor speed to 0
        
        if (upButton == true) {
            if (topLimitSwitch == true && currentArmMotorSpeed <= 0.0 ) {
                // start moving up
                currentArmMotorSpeed = 0.5;

                // track when we started moving
                armTimer.reset();
                armTimer.start();

                System.out.println("COLLECTION ARM: Up button pressed. Moving up");

            } else if(topLimitSwitch == false) {

                System.out.printf("COLLECTION ARM: Upper limit reached in %.3f seconds. Speed was %f. Now stopped.\n", armTimer.get(), currentArmMotorSpeed);

                // We're at the top, but the user pressed the button, so just 
                // make sure the motor is off (already should be)
                currentArmMotorSpeed = 0;
            }
        }

        // ELSE IF down button pressed THEN
        //   IF down limit NOT reached THEN
        //     Set motor speed to 0.5
        //   ELSE
        //     Set motor speed to 0.0
        
       
        else if (downButton == true  ) {
            if ( bottomLimitSwich == true && currentArmMotorSpeed >= 0.0 ) {
                // start moving down
                currentArmMotorSpeed = -0.5;

                // track when we started moving
                armTimer.reset();
                armTimer.start();

                System.out.println("COLLECTION ARM: Down button pressed. Moving up");

            } else if (bottomLimitSwich == false) {

                System.out.printf("COLLECTION ARM: Down limit reached in %.3f seconds. Speed was %f. Now stopped.\n", armTimer.get(), currentArmMotorSpeed);

                // We're at the bottom, but the user pressed the down button, so just
                // make sure the motor is off (already should be)
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
        
        // ELSE IF the arm is moving up and the user hasn't pressed any buttons THEN
        //   IF up limit switch reached THEN
        //     Set set motor speed to 0

        else if ( currentArmMotorSpeed > 0.0) {
            if ( topLimitSwitch == false) {
                System.out.printf("COLLECTION ARM: Upper limit reached in %.3f seconds. Speed was %f. Now stopped.\n", armTimer.get(), currentArmMotorSpeed);
                currentArmMotorSpeed = 0.0;
            } else {
                // We're moving up, but haven't hit the limit switch yet. If we've traveled for 
                // 0.7 seconds, move the remaining distance to the limit switch very slowly
                if (armTimer.hasElapsed(0.7)) {
                    currentArmMotorSpeed = 0.1;
                    System.out.printf("COLLECTION ARM: %.3f seconds elapsed while raising arm. Switching to slow speed. \n", armTimer.get());
                }
            }
        }
        
        // ELSE IF motor speed LESS THAN 0 THEN
        //   IF down limit switch reached THEN
        //     Set motor speed to 0

        else if (currentArmMotorSpeed < 0) {
            if (bottomLimitSwich == false) {
                System.out.printf("COLLECTION ARM: Down limit reached in %.3f seconds. Speed was %f. Now stopped.\n", armTimer.get(), currentArmMotorSpeed);
                currentArmMotorSpeed = 0.0;
            } else {
                // We're moving down, but haven't hit the limit switch yet. If we've traveled for 
                // 0.7 seconds, move the remaining distance to the limit switch very slowly
                if (armTimer.hasElapsed(0.7)) {
                    System.out.printf("COLLECTION ARM: %.3f seconds elapsed while lowering arm. Switching to slow speed. \n", armTimer.get());
                    currentArmMotorSpeed = -0.1;
                }
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
        //chmimkin rules
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