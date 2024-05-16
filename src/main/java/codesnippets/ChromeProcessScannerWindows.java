package codesnippets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Intellon
 * @date 16.05.2024
 *
 */
public class ChromeProcessScannerWindows {

    public static void main(String[] args) {
        List<String> chromeProcesses = getChromeProcesses();
        for (String process : chromeProcesses) {
            System.out.println(process);
        }
    }

    private static List<String> getChromeProcesses() {
        List<String> processes = new ArrayList<>();
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "wmic process where \"name='chrome.exe'\" get CommandLine, ProcessId");
            builder.redirectErrorStream(true);
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.isEmpty() && line.contains("chrome.exe")) {
                    boolean isAutomated = line.contains("--headless") || line.contains("--disable-gpu") || line.contains("--remote-debugging-port");
                    String outputLine = "Process: " + line + " | Likely Automated: " + (isAutomated ? "Yes" : "No");
                    processes.add(outputLine);
                }
            }
        } catch (IOException e) {
            System.out.println("Error while reading the processes: " + e.getMessage());
        }
        return processes;
    }
}
