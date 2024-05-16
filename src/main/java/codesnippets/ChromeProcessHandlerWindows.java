package codesnippets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Intellon
 * @date 03.05.2024
 *
 * Diese Klasse demonstriert, wie man Chrome-Prozesse unter Windows findet, deren PIDs abruft,
 * sie zwischenspeichert und dann nach einer gewissen Zeit erneut nach Chrome-PIDs sucht, um
 * festzustellen, ob neue Prozesse gestartet wurden. Neue PIDs werden gespeichert und Prozesse,
 * die nicht in der ursprünglichen Liste enthalten waren, werden terminiert.
 */
public class ChromeProcessHandlerWindows {
    public static void main(String[] args) {
        List<String> initialPids = getChromePids();
        // Hier kannst du weitere Aktionen ausführen, z.B. warten und dann erneut nach Chrome-PIDs suchen

        List<String> newPids = getChromePids();
        // Hier hast du eine Liste der neuen Chrome-PIDs

        // Zum Beispiel kannst du sie ausgeben
        System.out.println("New Chrome PIDs:");
        for (String pid : newPids) {
            System.out.println(pid);
        }

        // Hier könntest du die PIDs vergleichen, um zu sehen, ob einige Prozesse neu hinzugekommen sind, und dann entsprechende Aktionen ausführen
        // Zum Beispiel: Termination von Prozessen, die nicht in der initialen Liste enthalten sind
        for (String newPid : newPids) {
            if (!initialPids.contains(newPid)) {
                // Führe Aktion zur Beendigung des Prozesses durch
                terminateProcess(newPid);
            }
        }
    }

    private static List<String> getChromePids() {
        List<String> chromePids = new ArrayList<>();
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "tasklist /FI \"IMAGENAME eq chrome.exe\" /FO CSV /NH");
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 2) {
                    String pid = parts[1].replaceAll("\"", "");
                    chromePids.add(pid);
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return chromePids;
    }

    private static void terminateProcess(String pid) {
        try {
            ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "taskkill /PID " + pid);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

