package frc.robot.subsystems;

import org.junit.Test;
import org.junit.Assert;

public class BallCollectionSubsystemTest {
    
    @Test
    public void testFoo() {
        double speed;
        
        BallCollectionSubsystem collector = new BallCollectionSubsystem();
        
        speed =  collector.computeArmMotorSpeed(
            true,        // up button 
            false,       // down button
            true,        // up slow button
            false,       // top limit switch
            false);      // bottom limit switch
        //org.junit.Assert.assertEquals(1.0, speed, 0.001);

        
        speed = collector.computeArmMotorSpeed(false, true, false, true, true);
        //org.junit.Assert.assertEquals(-1.0, speed, 0.001);
    }
}
