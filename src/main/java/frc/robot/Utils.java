package frc.robot;

public class Utils {

    public static double deadzone(double value, double deadzone) {
        if (Math.abs(value) < deadzone)
            return 0;
        return value;
    }
}