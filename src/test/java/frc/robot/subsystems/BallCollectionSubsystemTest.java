package frc.robot.subsystems;

import org.junit.Test;

public class BallCollectionSubsystemTest {
    
    @Test
    public void testFoo() {
        
        BallCollectionSubsystem collector = new BallCollectionSubsystem();
      double z=  collector.setchimkin(3.0, 4.0);
        org.junit.Assert.assertEquals(5.0, z, 0.001);

    }
}
