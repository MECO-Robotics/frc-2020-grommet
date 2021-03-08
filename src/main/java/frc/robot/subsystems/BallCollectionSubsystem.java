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
    public double computeArmMotorSpeed(boolean upButton, boolean downButton, boolean topLimintSwich, boolean bottomLimitSwich) {
       
        if (upButton == true){
            return (1);
        } 
        else {
            
            if (downButton == true){
                return (-1);
            }
                
            else    {
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
        return 0.0;
    }
    
}

