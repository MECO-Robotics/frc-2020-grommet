package frc.robot;

/**
 * Utility methods
 */
public class Utils {

    /**
     * Return value unless it is within deadzone of zero, then just
     * return zero.
     */
    public static double deadzone(double value, double deadzone) {
        if (Math.abs(value) < deadzone)
            return 0;
        return value;
    }
}