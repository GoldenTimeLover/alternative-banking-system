package core.utils;

public class utils {

    public static double round (double value) {
        int scale = (int) Math.pow(10, 1);
        return (double) Math.round(value * scale) / scale;
    }
}
