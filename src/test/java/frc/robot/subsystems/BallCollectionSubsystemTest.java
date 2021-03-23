package frc.robot.subsystems;

import org.junit.Test;
import org.junit.Assert;

public class BallCollectionSubsystemTest {
    
    @Test
    public void testComputeArmMotorSpeed() {
        
        BallCollectionSubsystem collector = new BallCollectionSubsystem();
        
        boolean[] up  = new boolean[] { false, true,  false, false, false, true,  false, false, false, true,  false, false };
        boolean[] dwn = new boolean[] { false, false, true,  false, false, false, true, false, false, false, true,  false };
        boolean[] slw = new boolean[] { false, false, false, true,  false, false, false, true,  false, false, false, true };
        boolean[] top = new boolean[] { true,  true,  true,  true,  false, false, false, false, true,  true,  true,  true };
        boolean[] btm = new boolean[] { true,  true,  true,  true,  true,  true,  true, true,  false, false, false, false };
        double[]  spd = new double[]  { 0,     0.5,   -0.5,  0.1,   0,     0.0,   -0.5, 0,     0,     0.5,   0,     0.1 };
        
        // NOTE: For array index 0, the output is actually "no change",
        //       but for an initialized BallCollectionSubsystem, it should
        //       be zero
        
        for( int i = 0; i<12; i++) {
            double actualSpeed =  collector.computeArmMotorSpeed(
                up[i],        // up button 
                dwn[i],       // down button
                slw[i],       // up slow button
                top[i],       // top limit switch
                btm[i]);      // bottom limit switch
            String msg = String.format("\nERROR: Iteration %d\n", i);
            org.junit.Assert.assertEquals(msg, spd[i], actualSpeed, 0.001);
        }
    }
}
