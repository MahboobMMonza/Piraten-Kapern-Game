package pk;

public class DebugLogger {
    public static void log(String logArgs) {
        System.out.println(" (DEBUG) " + logArgs);
    }

    public static void logFormat(String format, Object... args) {
        System.out.println(" (DEBUG) " + String.format(format, args));
    }
}
