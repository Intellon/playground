package tutorials.processhandling;

import java.util.logging.Logger;

/**
 *  @author Intellon
 *  @date 16.05.2024
 *
 * The LoggerManager class provides a centralized and singleton approach to managing logger instances
 * across the application. This class ensures that only one Logger instance is created and reused
 * throughout the application, which helps in maintaining a consistent logging strategy and reduces
 * resource overhead.
 *
 * By using a synchronized method to create and return the logger instance, the LoggerManager ensures
 * that the Logger is thread-safe and can be accessed by multiple threads concurrently without issues.
 *
 * Usage:
 * To get a Logger instance, call the getLogger method with the desired class as the argument.
 * Example:
 *     Logger logger = LoggerManager.getLogger(MyClass.class);
 *
 * This approach of managing loggers ensures that the logging configuration is centralized, and any
 * changes to logging behaviors need to be made in a single place, making maintenance and updates easier.
 */
public class LoggerManager {
    private static Logger logger = null;

    public static synchronized Logger getLogger(Class<?> cls) {
        if (logger == null) {
            logger = Logger.getLogger(cls.getName());
        }
        return logger;
    }
}