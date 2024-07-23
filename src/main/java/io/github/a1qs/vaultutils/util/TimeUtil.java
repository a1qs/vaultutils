package io.github.a1qs.vaultutils.util;

public class TimeUtil {
    public static int timeInSeconds(int ticks) {
        return ticks/20;
    }

    public static boolean isBelowSecond(int ticks) {
        return Math.abs(ticks) <= 20;
    }

    public static String secondsText(int ticks) {
        return timeInSeconds(ticks) == Math.abs(1) ? " second" : " seconds";
    }

    public static String ticksText(int ticks) {
        return ticks == Math.abs(1) ? " tick" : " ticks";
    }
}
