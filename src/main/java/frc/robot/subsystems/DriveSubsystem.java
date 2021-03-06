package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import frc.robot.Map;

public class DriveSubsystem {

    SpeedControllerGroup leftMotors, rightMotors;
    public Encoder leftEncoder, rightEncoder;

    int ticksPerRev = 360;
    double diameter = 6.0; // inches
    double distancePerRev = diameter * Math.PI;

    public DriveSubsystem() {
        leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(Map.LEFT_DRIVE_1), 
                                            new WPI_VictorSPX(Map.LEFT_DRIVE_2));

        rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(Map.RIGHT_DRIVE_1), 
                                            new WPI_VictorSPX(Map.RIGHT_DRIVE_2));

        leftMotors.setInverted(true);       

        leftEncoder = new Encoder(Map.LEFT_ENCODER_1, Map.LEFT_ENCODER_2, true);
        rightEncoder = new Encoder(Map.RIGHT_ENCODER_1, Map.RIGHT_ENCODER_2);
        leftEncoder.setDistancePerPulse(distancePerRev / ticksPerRev);
        rightEncoder.setDistancePerPulse(distancePerRev / ticksPerRev);
    }

    public void resetEncoders() {
        leftEncoder.reset();
        rightEncoder.reset();
    }

    public void tankDrive(double left, double right) {
        leftMotors.set(left);
        rightMotors.set(right);
    }

    public void arcadeDrive(double y, double x) {
        leftMotors.set(y - x);
        rightMotors.set(y + x);
    }
}