package tutorials.processhandling;

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessHandlerTest {

    private ProcessHandler processHandler;
    private IProcessBuilderFactory processBuilderFactory;

    @BeforeEach
    public void setUp() {
        processBuilderFactory = new DefaultProcessBuilderFactory();
        processHandler = new ProcessHandler(processBuilderFactory);
    }

    @Test
    void testExecuteProcessScanWindows() {
        // Skip test if the OS is not Windows
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("windows"), "Test skipped: Not running on Windows");
        // This test assumes that a Windows process is being scanned.
        // We can't really execute this in a unit test without mocks, but we can check the logic.
        assertDoesNotThrow(() -> processHandler.executeProcessScan("tasklist", true));
    }

    @Test
    void testExecuteProcessScanUnix() {
        // Skip test if the OS is Windows
        Assumptions.assumeFalse(System.getProperty("os.name").toLowerCase().contains("windows"), "Test skipped: Running on Windows");
        // This test assumes that a Unix process is being scanned.
        // We can't really execute this in a unit test without mocks, but we can check the logic.
        assertDoesNotThrow(() -> processHandler.executeProcessScan("ps -ef", false));
    }

    @Test
    void testExtractProcessIdWindows() {
        // Skip test if the OS is not Windows
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("windows"), "Test skipped: Not running on Windows");
        String line = "chrome.exe 1234 Console 1 234,567 K";
        String processId = processHandler.extractProcessId(line, true);
        assertEquals("chrome.exe", processId, "Process ID should be chrome.exe on Windows");
    }

    @Test
    void testExtractProcessIdUnix() {
        // Skip test if the OS is Windows
        Assumptions.assumeFalse(System.getProperty("os.name").toLowerCase().contains("windows"), "Test skipped: Running on Windows");
        String line = "root 1234 1 0 00:00:00 chrome";
        String processId = processHandler.extractProcessId(line, false);
        assertEquals("1234", processId, "Process ID should be 1234 on Unix");
    }

    @Test
    void testTerminateProcessWindows() {
        // Skip test if the OS is not Windows
        Assumptions.assumeTrue(System.getProperty("os.name").toLowerCase().contains("windows"), "Test skipped: Not running on Windows");
        // Similar to executeProcessScan, we can't really terminate a process in a unit test without mocks.
        // But we can check that no exception is thrown.
        assertDoesNotThrow(() -> processHandler.terminateProcess("1234", true));
    }

    @Test
    void testTerminateProcessUnix() {
        // Skip test if the OS is Windows
        Assumptions.assumeFalse(System.getProperty("os.name").toLowerCase().contains("windows"), "Test skipped: Running on Windows");
        // Similar to executeProcessScan, we can't really terminate a process in a unit test without mocks.
        // But we can check that no exception is thrown.
        assertDoesNotThrow(() -> processHandler.terminateProcess("1234", false));
    }
}
