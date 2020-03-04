package frc.robot;

public class Utils {

    public static double deadzone(double value, double deadzone) {
        if (Math.abs(value) < deadzone)
            return 0;
        return value;
    }

    public static double sign(double value) {
        if (value >= 0)
            return 1;
        return -1;
    }
}