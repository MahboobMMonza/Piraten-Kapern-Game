package pk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DebugLogger {

    private static final Logger logger = LogManager.getFormatterLogger(DebugLogger.class);

    public static void log(String logArgs) {
        logger.debug(" (DEBUG) " + logArgs);
    }

    public static void logFormat(String format, Object... args) {
        logger.debug(" (DEBUG) " + String.format(format, args));
    }
}
