package net.kdt.pojavlaunch;

public class Mth {
    public static int sign(double value) {
        if (value == 0.0) {
            return 0;
        } else {
            return value > 0.0 ? 1 : -1;
        }
    }

    public static double clamp(double value, double min, double max) {
        if (value < min) {
            return min;
        } else {
            return value > max ? max : value;
        }
    }

}
