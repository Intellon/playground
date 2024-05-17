package tutorials.database;

import java.io.IOException;
import java.util.logging.*;

public class LoggerManager {
    private final Logger logger;

    /**
     * Constructs a {@link LoggerManager} for the specified class.
     * Initializes the logger for the provided class.
     *
     * @param cls the class for which the logger is to be created
     */
    public LoggerManager(Class<?> cls) {
        this.logger = Logger.getLogger(cls.getName());
        initializeLogger();
    }

    /**
     * Initializes the logger with handlers and formatting.
     */
    private void initializeLogger() {
        try {
            // Reset any previous configuration
            LogManager.getLogManager().reset();
            logger.setLevel(Level.ALL);

            // Console Handler
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.ALL);
            consoleHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(consoleHandler);

            // File Handler
            FileHandler fileHandler = new FileHandler("app.log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);

        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error setting up logger: {0}", e.toString());
        }
    }

    /**
     * Log a debug message.
     *
     * @param msg the message to log
     */
    public void debug(String msg) {
        logger.log(Level.FINE, msg);
    }

    /**
     * Log a trace message.
     *
     * @param msg the message to log
     */
    public void trace(String msg) {
        logger.log(Level.FINER, msg);
    }

    /**
     * Log an info message.
     *
     * @param msg the message to log
     */
    public void info(String msg) {
        logger.log(Level.INFO, msg);
    }

    /**
     * Log a warning message.
     *
     * @param msg the message to log
     */
    public void warn(String msg) {
        logger.log(Level.WARNING, msg);
    }

    /**
     * Log a severe message.
     *
     * @param msg the message to log
     */
    public void error(String msg) {
        logger.log(Level.SEVERE, msg);
    }

    /**
     * Log a severe message with an exception.
     *
     * @param msg the message to log
     * @param throwable the exception to log
     */
    public void error(String msg, Throwable throwable) {
        logger.log(Level.SEVERE, msg, throwable);
    }

    /**
     * Returns the {@link Logger} instance associated with this {@link LoggerManager}.
     *
     * @return the {@link Logger} instance
     */
    public synchronized Logger getLogger() {
        return logger;
    }
}
