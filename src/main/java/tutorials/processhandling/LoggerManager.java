package tutorials.processhandling;

import java.util.logging.Logger;

/**
 * @author Intellon
 * @date 16.05.2024
 *
 * A manager class for handling {@link Logger} instances.
 * This class provides a mechanism to obtain a {@link Logger} for a specific class.
 */
public class LoggerManager {
    private Logger logger;

    /**
     * Constructs a {@link LoggerManager} for the specified class.
     * Initializes the logger for the provided class.
     *
     * @param cls the class for which the logger is to be created
     */
    public LoggerManager(Class<?> cls) {
        this.logger = Logger.getLogger(cls.getName());
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