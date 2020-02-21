package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class DriveSubsystem {

    SpeedControllerGroup leftMotors, rightMotors;
    public Encoder leftEncoder, rightEncoder;

    int ticksPerRev = 360; // TODO: confirm this is right
    double diameter = 6.0; // inches
    double distancePerRev = diameter * Math.PI;

    public DriveSubsystem() {
        leftMotors = new SpeedControllerGroup(new WPI_VictorSPX(3), new WPI_VictorSPX(4));
        rightMotors = new SpeedControllerGroup(new WPI_VictorSPX(1), new WPI_VictorSPX(2));

        leftMotors.setInverted(true);
       

        leftEncoder = new Encoder(3,4);
        rightEncoder = new Encoder(1,2);
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