package tutorials.processhandling;

import java.util.logging.Logger;

/**
 *  @author Intellon
 *  @date 16.05.2024
 *
 * WindowsProcessHandler implements the ProcessHandler interface specifically for Windows operating systems.
 * This class uses the WMIC command to retrieve and analyze information about running Chrome processes.
 * It initiates the execution of a process scan and carries out specific logic
 * to identify and potentially terminate automated Chrome processes.
 */
public class WindowsProcessHandler implements IProcessHandler {
    private final Logger logger;
    private final ProcessHandler scanner;

    public WindowsProcessHandler() {
        this.logger = LoggerManager.getLogger(WindowsProcessHandler.class);
        this.scanner = new ProcessHandler(logger);
    }

    @Override
    public void execute() {
        logger.info("Executing Windows Process Handler");
        scanner.executeProcessScan("cmd.exe /c wmic process where \"name='chrome.exe'\" get CommandLine, ProcessId", true);
    }
}