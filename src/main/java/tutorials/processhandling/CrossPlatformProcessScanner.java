package tutorials.processhandling;

import java.util.logging.Logger;

/**
 *  @author Intellon
 *  @date 16.05.2024
 *
 * CrossPlatformProcessScanner is the main class that serves as the entry point of the program.
 * This class determines the user's operating system and selects the appropriate ProcessHandler implementation.
 * It is responsible for initializing and executing the specific ProcessHandler.
 */
public class CrossPlatformProcessScanner {
    private final Logger logger;

    public CrossPlatformProcessScanner() {
        this.logger = LoggerManager.getLogger(CrossPlatformProcessScanner.class);
    }

    public static void main(String[] args) {
        new CrossPlatformProcessScanner().run();
    }

    private void run() {
        String osName = System.getProperty("os.name").toLowerCase();
        logger.info("Detected OS: " + osName);
        IProcessHandler handler = getProcessHandler(osName);
        if (handler != null) {
            handler.execute();
        } else {
            logger.warning("Unsupported operating system: " + osName);
        }
    }

    private IProcessHandler getProcessHandler(String osName) {
        if (osName.contains("windows")) {
            return new WindowsProcessHandler();
        } else if (osName.contains("mac") || osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return new UnixProcessHandler();
        }
        return null;
    }
}
