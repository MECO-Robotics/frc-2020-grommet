package frc.robot.subsystems;

import org.junit.Assert;
import org.junit.Test;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.AutonomousRoutine;

public class AutonomousRoutineTest {
    
    @Test
    public void testRecord() {

        AutonomousRoutine routine = new AutonomousRoutine("bob");

        Assert.assertFalse(routine.next());

        routine.startRecording();

        // index is 0, pointing to a default constructed Record, so all values should be default
        Assert.assertEquals(0, routine.getLeftMotor());

        routine.recordState(1, 1, AutonomousRoutine.ArmStatus.DOWN, AutonomousRoutine.RollerStatus.STOPPED);
        Timer.delay(0.020); // simulate the delay between calls to teleopPeriodic
        routine.recordState(1, 2, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING);
        Timer.delay(0.020);
        routine.recordState(2, 1, AutonomousRoutine.ArmStatus.GOING_UP, AutonomousRoutine.RollerStatus.EJECTING);
        Timer.delay(0.020);
        routine.recordState(1000, 2000, AutonomousRoutine.ArmStatus.UP, AutonomousRoutine.RollerStatus.STOPPED);
        Timer.delay(0.020);
        routine.recordState(2000, 2000, AutonomousRoutine.ArmStatus.DOWN, AutonomousRoutine.RollerStatus.STOPPED);
        Timer.delay(0.020);

        routine.stopRecording();

        routine.startAutonomous();

        // Should be pointing at the first recorded record
        Assert.assertEquals(1, routine.getLeftMotor());
        Assert.assertTrue(routine.next());
        Assert.assertEquals(2, routine.getRightMotor());
        Assert.assertEquals(AutonomousRoutine.ArmStatus.GOING_DOWN, routine.getBallCollectionArm());
        Assert.assertTrue(routine.next());
        Assert.assertTrue(routine.next());
        Assert.assertTrue(routine.next());
        Assert.assertFalse(routine.next());
    }

    @Test
    public void testGetLeftDirection() {

        AutonomousRoutine routine = new AutonomousRoutine("bob");
        routine.startRecording();
        routine.recordState(1, 1, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 0
        routine.recordState(2, 1, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 1
        routine.recordState(4, 1, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 2
        routine.recordState(8, 1, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 3
        routine.recordState(7, 1, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 4
        routine.recordState(7, 1, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 5
        routine.recordState(2, 1, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 6
        routine.stopRecording();

        routine.startAutonomous();

        // Should be pointing at the first recorded record
        Assert.assertEquals(1, routine.getLeftDirection());  // 0 .. 1
        routine.next();
        Assert.assertEquals(1, routine.getLeftDirection()); // 1 .. 2
        routine.next();
        Assert.assertEquals(1, routine.getLeftDirection()); // 2 .. 3
        routine.next();
        Assert.assertEquals(-1, routine.getLeftDirection()); // 3 .. 4
        routine.next();
        Assert.assertEquals(0, routine.getLeftDirection()); // 4 .. 5
        
    }
}
