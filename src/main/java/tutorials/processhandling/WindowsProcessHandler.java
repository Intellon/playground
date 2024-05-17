package tutorials.processhandling;

import java.util.logging.Logger;

/**
 * @author Intellon
 * @date 16.05.2024
 *
 * WindowsProcessHandler implements the ProcessHandler interface specifically for Windows operating systems.
 * This class uses the WMIC command to retrieve and analyze information about running Chrome processes.
 * It initiates the execution of a process scan and carries out specific logic
 * to identify and potentially terminate automated Chrome processes.
 */
public class WindowsProcessHandler implements IProcessHandler {
    private final ProcessHandler scanner;
    private static final LoggerManager loggerManager = new LoggerManager(ProcessHandler.class);
    private static final Logger logger = loggerManager.getLogger();

    /**
     * Constructs a {@link WindowsProcessHandler} with a default {@link ProcessHandler} for scanning processes.
     */
    public WindowsProcessHandler() {
        this.scanner = new ProcessHandler();
    }

    /**
     * Executes the process handler. This method initiates a process scan specific to Windows systems,
     * looking for processes related to Chrome and terminating them based on specific criteria.
     */
    @Override
    public void execute() {
        logger.info("Executing Windows Process Handler");
        scanner.executeProcessScan("cmd.exe /c wmic process where \"name='chrome.exe'\" get CommandLine, ProcessId", true);
    }
}