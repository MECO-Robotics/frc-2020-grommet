package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Map;
import java.lang.Math;
public class BallCollectionSubsystem {

    public BallCollectionSubsystem() {
 
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

