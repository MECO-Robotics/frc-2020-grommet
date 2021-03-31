package frc.robot.subsystems;

import org.junit.Assert;
import org.junit.Test;

import edu.wpi.first.wpilibj.Timer;
import frc.robot.AutonomousRoutine;
import frc.robot.IDriveSubsystem;

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

    private class StubDriveSubsystem implements IDriveSubsystem {

        int leftEnc, rightEnc;
        double leftMotor, rightMotor;

        @Override
        public int getLeftEncoderValue() {
            return leftEnc;
        }

        @Override
        public int getRightEncoderValue() {
            return rightEnc;
        }

        @Override
        public void tankDrive(double left, double right) {
            leftEnc += left * 100.0;
            rightEnc += right * 100.0;
        }
    }

    @Test
    public void testRunPeriodic() {

        IDriveSubsystem stubDriveSubsystem = new StubDriveSubsystem();

        AutonomousRoutine routine = new AutonomousRoutine("bob");
        routine.startRecording();

        // Flat spin clockwise for 0.8 seconds at max speed
        // Max speed is about 3000 ticks per second or about 300 ticks every 0.1 seconds
        // The runPeriodic method computes motor speed for a 0.25 seconds worth of the program then exits
        routine.recordState(0, 0, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 0
        Timer.delay(0.1);
        routine.recordState(300, -300, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 1
        Timer.delay(0.1);
        routine.recordState(300, -300, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 2
        Timer.delay(0.1);
        routine.recordState(300, -300, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 3
        Timer.delay(0.1);
        routine.recordState(300, -300, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 4
        Timer.delay(0.1);
        routine.recordState(300, -300, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 5
        Timer.delay(0.1);
        routine.recordState(300, -300, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 6
        Timer.delay(0.1);
        routine.recordState(0, 0, AutonomousRoutine.ArmStatus.GOING_DOWN, AutonomousRoutine.RollerStatus.COLLECTING); // 7
        Timer.delay(0.1);
        routine.stopRecording();

        routine.startAutonomous();

        routine.runPeriodic(stubDriveSubsystem);
        routine.runPeriodic(stubDriveSubsystem);
        routine.runPeriodic(stubDriveSubsystem);
        routine.runPeriodic(stubDriveSubsystem);
    }
}
