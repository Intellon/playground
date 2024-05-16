package tutorials.processhandling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

/**
 *  @author Intellon
 *  @date 16.05.2024
 *
 * The ProcessHandler class provides methods for scanning, analyzing, and terminating processes
 * based on command-line arguments. This class is utilized by specific ProcessHandler implementations
 * to initiate the process, analyze the output, and support both Windows and Unix-based systems
 * by adapting the command line syntax accordingly.
 */
public class ProcessHandler {
    private final Logger logger;

    public ProcessHandler(Logger logger) {
        this.logger = logger;
    }

    public void executeProcessScan(String command, boolean isWindows) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command.split(" "));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("chrome") && (line.contains("--headless") || line.contains("--disable-gpu") || line.contains("--remote-debugging-port") || line.contains("--enable-automation") || line.contains("--no-sandbox"))) {
                    String processId = extractProcessId(line, isWindows);
                    if (processId != null) {
                        logger.info("Terminating process: " + processId);
                        terminateProcess(processId, isWindows);
                    }
                }
            }
        } catch (IOException e) {
            logger.severe("Error scanning processes: " + e.getMessage());
        }
    }

    private String extractProcessId(String line, boolean isWindows) {
        String[] parts = line.trim().split("\\s+");
        return isWindows ? parts[parts.length - 1] : parts[1]; // Process ID is last on Windows, second on Unix
    }

    private void terminateProcess(String processId, boolean isWindows) {
        try {
            String[] cmd = isWindows ? new String[]{"cmd.exe", "/c", "taskkill", "/F", "/PID", processId}
                    : new String[]{"kill", "-9", processId};
            new ProcessBuilder(cmd).start();
            logger.info("Successfully terminated process: " + processId);
        } catch (IOException e) {
            logger.severe("Failed to terminate process " + processId + ": " + e.getMessage());
        }
    }
}
