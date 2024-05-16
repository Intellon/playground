package tutorials.processhandling;

import java.util.logging.Logger;

/**
 *  @author Intellon
 *  @date 16.05.2024
 *
 * UnixProcessHandler implements the ProcessHandler interface for Unix-based operating systems
 * such as Linux and macOS. This class uses the `ps` command executed via a shell
 * to find and analyze active Chrome processes. Similar to the WindowsProcessHandler,
 * this implementation focuses on detecting and terminating automated processes.
 */
public class UnixProcessHandler implements IProcessHandler {
    private final Logger logger;
    private final ProcessHandler scanner;

    public UnixProcessHandler() {
        this.logger = LoggerManager.getLogger(UnixProcessHandler.class);
        this.scanner = new ProcessHandler(logger);
    }

    @Override
    public void execute() {
        logger.info("Executing Unix Process Handler");
        scanner.executeProcessScan("sh -c ps aux | grep chrome", false);
    }
}