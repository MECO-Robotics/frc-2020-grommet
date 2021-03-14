package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
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
    
    public BallCollectionSubsystem() {
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
        //     Set motor speed to 0.7
        //   ELSE
        //     Set motor speed to 0
        
        if (upButton == true) {
            if (topLimitSwitch == true) {
                
                 currentArmMotorSpeed = 0.07;
            } else {
                
                 currentArmMotorSpeed = 0;
            }
        }

        // ELSE IF down button pressed THEN
        //   IF down limit NOT reached THEN
        //     Set motor speed to 0.7
        //   ELSE
        //     Set motor speed to 0.0
        
       
        else if (downButton == true  ) {
            if ( bottomLimitSwich == true ) {
                currentArmMotorSpeed = 0.07;
            } else {
                currentArmMotorSpeed = 0;
            }
        }
        
        
        // ELSE IF slow up button pressed THEN
        //   IF up limit NOT reached THEN
        //     Return a + 0.1 (slow) motor speed for raising fast
        //     Start a timer for the time 
        //   ELSE
        //     Set currentArmMotorSpeed = 0
        //     Return 0 motor speed - STOP
        // ELSE
        // IF currentArmMotorSpeed > 0.0 THEN
        //   IF up limit switch reached THEN
        //     Set currentArmMotorSpeed = 0
        //     Return 0 - STOP
        //   ELSE
        //     Return currentArmMotorSpeed - keep going!
        // ELSE IF currentArmMotorSpeed < 0.0 THEN
        //   IF down limit switch reached THEN
        //     Set currentArmMotorSpeed = 0
        //     Return 0 - STOP
        //   ELSE
        //     Return currentArmMotorSpeed - Keep going!
        
        /********** HERE IS THE CURRENT CODE IN ROBOT.JAVA. NEED TO RE-DO HERE **********
        
                double armLiftPow = 0.5;

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

        ***********************************************************************************/



        
        if (upButton == true){
            return (1);
        } 
        else {
            
            if (downButton == true){
                return (-1);
            }
                
            else     {
                  return (0);           
                  
 
            }
            
        }
    
    }
    
    
    
    /**
     * Determine the roller motor speed given the intakeTrigger level and
     * the outTakeTrigger level. If both triggers are pulled, or neither are pulled,
     * the motor speed should be 0.
     */
    public double computeRollerSpeed(double intakeTriggerLevel, double outTakeTriggerLevel) {
        
        /******** EXISTING CODE IN ROBOT.JAVA *********
        
            if (leftTrigger2 > 0)
                intake.setSpeed(-leftTrigger2);
            else if (rightTrigger2 > 0)
                intake.setSpeed(rightTrigger2);
            else
                intake.setSpeed(0);

        ***********************************************/
        
        
        
        
        return 0.0;
    }
    
}

