package frc.robot.subsystems;
import frc.robot.subsystems.BallCollectionSubsystemState;
import edu.wpi.first.wpilibj.Victor;
public class BallCollectionSubsystem implements IBallCollectionSubsystem {

    BallCollectionSubsystemState ballCollectionSubsystem;
    Victor armLift;


    public void controlArm(boolean upButton, boolean downButton, boolean topLimitSwitch, 
    boolean bottomLimitSwitch) {
        double armMotorSpeed = ballCollectionSubsystem.computeArmMotorSpeed(
            upButton,            
            downButton,            
            false,            // up slow button
            topLimitSwitch,      // top limit switch HAS NOT been reached (normally closed switch)
            bottomLimitSwitch); // btm limit switch HAS NOT been reached (normally closed switch)
        
        armLift.set(armMotorSpeed);
                

    }


    @Override
    public void engageBallCollectionRoller(double leftLevel, double rightLevel) {
        // TODO Auto-generated method stub
        
        
    }
    
}
