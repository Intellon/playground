package tutorials.processhandling;

import java.util.logging.Logger;

/**
 * @author Intellon
 * @date 16.05.2024
 *
 * CrossPlatformProcessScanner is the main class that serves as the entry point of the program.
 * This class determines the user's operating system and selects the appropriate ProcessHandler implementation.
 * It is responsible for initializing and executing the specific ProcessHandler.
 */
public class CrossPlatformProcessScanner {

    private static final LoggerManager loggerManager = new LoggerManager(CrossPlatformProcessScanner.class);
    private static final Logger logger = loggerManager.getLogger();

    /**
     * The main method to run the cross-platform process scanner.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        new CrossPlatformProcessScanner().run();
    }

    /**
     * Runs the process scanner. Detects the operating system and executes the appropriate process handler.
     */
    public void run() {
        String osName = System.getProperty("os.name").toLowerCase();
        logger.info("Detected OS: " + osName);
        IProcessHandler handler = getProcessHandler(osName);
        if (handler != null) {
            handler.execute();
        } else {
            logger.warning("Unsupported operating system: " + osName);
        }
    }

    /**
     * Returns an appropriate {@link IProcessHandler} based on the detected operating system name.
     *
     * @param osName the name of the operating system
     * @return an instance of {@link IProcessHandler} suitable for the operating system, or null if unsupported
     */
    IProcessHandler getProcessHandler(String osName) {
        if (osName.contains("windows")) {
            return new WindowsProcessHandler();
        } else if (osName.contains("mac") || osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return new UnixProcessHandler();
        }
        return null;
    }
}
