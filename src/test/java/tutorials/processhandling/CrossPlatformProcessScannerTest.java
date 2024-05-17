package tutorials.processhandling;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CrossPlatformProcessScannerTest {

    private CrossPlatformProcessScanner scanner;

    @BeforeEach
    public void setUp() {
        scanner = new CrossPlatformProcessScanner();
    }

    @Test
    void testGetProcessHandlerWindows() {
        IProcessHandler handler = scanner.getProcessHandler("windows");
        assertTrue(handler instanceof WindowsProcessHandler, "Handler should be an instance of WindowsProcessHandler");
    }

    @Test
    void testGetProcessHandlerMac() {
        IProcessHandler handler = scanner.getProcessHandler("mac");
        assertTrue(handler instanceof UnixProcessHandler, "Handler should be an instance of UnixProcessHandler");
    }

    @Test
    void testGetProcessHandlerUnix() {
        IProcessHandler handler = scanner.getProcessHandler("nix");
        assertTrue(handler instanceof UnixProcessHandler, "Handler should be an instance of UnixProcessHandler");
    }

    @Test
    void testGetProcessHandlerLinux() {
        IProcessHandler handler = scanner.getProcessHandler("nux");
        assertTrue(handler instanceof UnixProcessHandler, "Handler should be an instance of UnixProcessHandler");
    }

    @Test
    void testGetProcessHandlerAIX() {
        IProcessHandler handler = scanner.getProcessHandler("aix");
        assertTrue(handler instanceof UnixProcessHandler, "Handler should be an instance of UnixProcessHandler");
    }

    @Test
    void testGetProcessHandlerUnsupported() {
        IProcessHandler handler = scanner.getProcessHandler("unsupported_os");
        assertNull(handler, "Handler should be null for unsupported operating systems");
    }
}

