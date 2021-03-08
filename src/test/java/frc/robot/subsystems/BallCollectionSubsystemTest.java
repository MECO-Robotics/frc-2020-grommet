package frc.robot.subsystems;

import org.junit.Test;

public class BallCollectionSubsystemTest {
    
    @Test
    public void testFoo() {
        
        BallCollectionSubsystem collector = new BallCollectionSubsystem();
   
        
        
        
        double z=  collector.computeArmMotorSpeed(true, false, false, false);
        org.junit.Assert.assertEquals(1.0, z, 0.001);

        
        
        z=collector.computeArmMotorSpeed(false, true, true, true);
         org.junit.Assert.assertEquals(-1.0, z, 0.001);
    }
}
