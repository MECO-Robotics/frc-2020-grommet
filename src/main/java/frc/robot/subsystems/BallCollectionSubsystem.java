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
     * @param upButton Move the arm all the way up
     * @param downButton Move the arm all the way down
     * @return The motor speed to apply to the lifter arm
     */
    public double computeArmMotorSpeed(boolean upButton, boolean downButton) {
        return 0.0;
    }
    
    /**
     * Determine the roller motor speed given the intakeTrigger level and
     * the outTakeTrigger level. If both triggers are pulled, or neither are pulled,
     * the motor speed should be 0.
     */
    public double computeRollerSpeed(double intakeTrigger, double outTakeTrigger) {
        return 0.0;
    }
    
       public double setchimkin(double a, double b) {
             
           double asquared= Math.pow(a,2);
            double bsquared=Math.pow(b,2);
           return   Math.pow(asquared + bsquared,.5) ;
          
           // set 1-10 
            //if then 1-4 rais arm full speed
            //if then 5-10 raise arm .5
        }
    
    
}

