package frc.robot;

public interface IDriveSubsystem {
    void tankDrive(double left, double right);

    int getLeftEncoderValue();

    int getRightEncoderValue();
}
