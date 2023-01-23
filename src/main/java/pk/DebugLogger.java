package pk;

import java.util.Formatter;

public class DebugLogger {
    private static Formatter formatter = new Formatter();
    public static void log(String logArgs) {
        System.out.printf(" (DEBUG) %s\n", logArgs);
    }
    public static void logFormat(String format, Object ... args) {
        System.out.printf(" (DEBUG) %s\n", (formatter.format(format, args)).toString());
    }
}
