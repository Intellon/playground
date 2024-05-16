package codesnippets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author Intellon
 * @date 16.05.2024
 *
 */
public class CrossBrowserProcessHandlerSimple {

    public static void main(String[] args) {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        String command = getOSCommand(osName);
        System.out.println("Current OS: "+ osName + "OS Command: " + command);
        if (command != null) {
            executeProcessScan(command, osName.contains("windows"));
        } else {
            System.out.println("Unsupported operating system: " + osName);
        }
    }


    /**
     * get the current OS Name
     * */
    private static String getOSCommand(String osName) {
        if (osName.contains("windows")) {
            return "cmd.exe /c wmic process where \"name='chrome.exe'\" get CommandLine, ProcessId";
        } else if (osName.contains("mac") || osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return "sh -c ps aux | grep chrome";
        }
        return null;
    }

    /**
     * scan the Processes and identify the automation processes
     * */
    private static void executeProcessScan(String command, boolean isWindows) {
        try {
            ProcessBuilder builder = new ProcessBuilder(command.split(" "));
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("chrome") && (line.contains("--headless") || line.contains("--disable-gpu") || line.contains("--remote-debugging-port") || line.contains("--enable-automation") || line.contains("--no-sandbox"))) {
                    String processId = extractProcessId(line, isWindows);
                    System.out.println("Automated Chrome PID's: " + processId);
                    if (processId != null) {
                        terminateProcess(processId, isWindows);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error scanning processes: " + e.getMessage());
        }
    }

    /**
     * extract the corresponding process ID's
     * */
    private static String extractProcessId(String line, boolean isWindows) {
        String[] parts = line.trim().split("\\s+");
        return isWindows ? parts[parts.length - 1] : parts[1]; // Process ID is last on Windows, second on Unix
    }

    /**
     * terminate the process
     * */
    private static void terminateProcess(String processId, boolean isWindows) {
        try {
            String[] cmd = isWindows ? new String[]{"cmd.exe", "/c", "taskkill", "/F", "/PID", processId} : new String[]{"kill", "-9", processId};
            new ProcessBuilder(cmd).start();
            System.out.println("Termination Command incl Session PID: " + Arrays.toString(cmd));
        } catch (IOException e) {
            System.out.println("Failed to terminate process " + processId + ": " + e.getMessage());
        }
    }
}