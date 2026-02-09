package tutorials.shadowdom;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;

import java.io.File;
import java.nio.file.Files;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.open;

/**
 * Runnable examples for ShadowDomUtils and ShadowDomInteractionUtils.
 * Starts Chrome headless, loads a test page, and demonstrates all operations.
 */
public class ShadowDomExamples {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        // Setup Chrome headless
        Configuration.headless = true;
        Configuration.browser = "chrome";
        Configuration.timeout = 5000;

        // Write HTML to temp file and open it
        File tempFile = File.createTempFile("shadow-dom-examples", ".html");
        tempFile.deleteOnExit();
        Files.writeString(tempFile.toPath(), createDemoHTML());
        open(tempFile.toURI().toString());

        try {
            System.out.println("=== Shadow DOM Utils — Complete Examples ===\n");

            runReadingExamples();
            runAttributeExamples();
            runNumericExamples();
            runClickExamples();
            runTypingExamples();
            runSelectExamples();
            runCheckboxRadioExamples();
            runHoverFocusExamples();
            runScrollExamples();
            runShadowDomExamples();
            runErrorHandlingExamples();
            runCombinedWorkflow();

            System.out.println("\n=== Results: " + passed + " passed, " + failed + " failed ===");
        } finally {
            if (WebDriverRunner.hasWebDriverStarted()) {
                closeWebDriver();
            }
        }
    }

    // ========================================
    // 1. READING OPERATIONS
    // ========================================

    private static void runReadingExamples() {
        System.out.println("--- 1. Reading Operations ---");

        // Smart auto-detection (input)
        check("getValue input", "test-value",
                ShadowDomUtils.getValue("#text-input"));

        // Smart auto-detection (div)
        check("getValue div", "Hello World",
                ShadowDomUtils.getValue("#info-div"));

        // Smart auto-detection (span)
        check("getValue span", "Status OK",
                ShadowDomUtils.getValue("#status-span"));

        // Smart auto-detection (textarea)
        check("getValue textarea", "Some notes here",
                ShadowDomUtils.getValue("#notes-textarea"));

        // Smart auto-detection (select)
        check("getValue select", "Germany",
                ShadowDomUtils.getValue("#country-select"));

        // Specialized methods
        check("getText", "Hello World",
                ShadowDomUtils.getText("#info-div"));

        check("getInputValue", "test-value",
                ShadowDomUtils.getInputValue("#text-input"));

        check("getSpanText", "Status OK",
                ShadowDomUtils.getSpanText("#status-span"));

        check("getDivText", "Hello World",
                ShadowDomUtils.getDivText("#info-div"));

        System.out.println();
    }

    // ========================================
    // 2. ATTRIBUTE EXTRACTION
    // ========================================

    private static void runAttributeExamples() {
        System.out.println("--- 2. Attribute Extraction ---");

        check("getAttribute data-value", "custom-123",
                ShadowDomUtils.getAttribute("#data-element", "data-value"));

        check("getAttribute data-id", "42",
                ShadowDomUtils.getAttribute("#data-element", "data-id"));

        check("getAttribute placeholder", "Enter text here",
                ShadowDomUtils.getAttribute("#placeholder-input", "placeholder"));

        check("getAttribute href", "https://example.com",
                ShadowDomUtils.getAttribute("#link-element", "href"));

        check("getAttribute title", "Example Link",
                ShadowDomUtils.getAttribute("#link-element", "title"));

        check("getAttribute non-existent", null,
                ShadowDomUtils.getAttribute("#info-div", "non-existent-attr"));

        System.out.println();
    }

    // ========================================
    // 3. NUMERIC VALUES
    // ========================================

    private static void runNumericExamples() {
        System.out.println("--- 3. Numeric Values ---");

        check("getIntValue", 42,
                ShadowDomUtils.getIntValue("#number-input"));

        check("getDoubleValue", 99.99,
                ShadowDomUtils.getDoubleValue("#price-element"));

        check("getIntValue from span", 150,
                ShadowDomUtils.getIntValue("#count-span"));

        check("getIntValue invalid", null,
                ShadowDomUtils.getIntValue("#info-div"));

        check("getDoubleValue invalid", null,
                ShadowDomUtils.getDoubleValue("#status-span"));

        System.out.println();
    }

    // ========================================
    // 4. CLICK OPERATIONS
    // ========================================

    private static void runClickExamples() {
        System.out.println("--- 4. Click Operations ---");

        // Click button
        boolean clicked = ShadowDomInteractionUtils.click("#action-button");
        check("click button", true, clicked);
        check("click triggered event", "Button Clicked",
                ShadowDomUtils.getValue("#status-display"));

        // Click div
        boolean divClicked = ShadowDomInteractionUtils.click("#clickable-div");
        check("click div", true, divClicked);
        check("div click event", "Div Clicked",
                ShadowDomUtils.getValue("#status-display"));

        // Click non-existent
        check("click non-existent", false,
                ShadowDomInteractionUtils.click("#does-not-exist"));

        // Wait and click
        boolean waitClicked = ShadowDomInteractionUtils.waitAndClick("#action-button", 2);
        check("waitAndClick", true, waitClicked);

        System.out.println();
    }

    // ========================================
    // 5. TYPING OPERATIONS
    // ========================================

    private static void runTypingExamples() {
        System.out.println("--- 5. Typing Operations ---");

        // Type into text input
        boolean typed = ShadowDomInteractionUtils.type("#text-input", "Hello World");
        check("type text", true, typed);
        check("typed value", "Hello World",
                ShadowDomUtils.getValue("#text-input"));

        // Type into email input
        typed = ShadowDomInteractionUtils.type("#email-input", "test@example.com");
        check("type email", true, typed);
        check("email value", "test@example.com",
                ShadowDomUtils.getValue("#email-input"));

        // Type into number input
        typed = ShadowDomInteractionUtils.type("#number-input", "12345");
        check("type number", true, typed);

        // Type into password
        typed = ShadowDomInteractionUtils.type("#password-input", "secret123");
        check("type password", true, typed);

        // Clear and re-type
        ShadowDomInteractionUtils.clear("#text-input");
        String cleared = ShadowDomUtils.getValue("#text-input");
        check("clear input", true, cleared == null || cleared.isEmpty());

        ShadowDomInteractionUtils.type("#text-input", "New Value");
        check("re-type after clear", "New Value",
                ShadowDomUtils.getValue("#text-input"));

        // Type non-existent
        check("type non-existent", false,
                ShadowDomInteractionUtils.type("#does-not-exist", "test"));

        System.out.println();
    }

    // ========================================
    // 6. SELECT / DROPDOWN
    // ========================================

    private static void runSelectExamples() {
        System.out.println("--- 6. Select / Dropdown ---");

        // Select by text
        boolean selected = ShadowDomInteractionUtils.selectByText("#country-select", "United Kingdom");
        check("selectByText", true, selected);
        check("selected text", "United Kingdom",
                ShadowDomUtils.getValue("#country-select"));

        // Select by value
        selected = ShadowDomInteractionUtils.selectByValue("#country-select", "de");
        check("selectByValue", true, selected);
        check("selected after value", "Germany",
                ShadowDomUtils.getValue("#country-select"));

        // Select non-existent option
        check("selectByText non-existent", false,
                ShadowDomInteractionUtils.selectByText("#country-select", "Atlantis"));

        check("selectByValue non-existent", false,
                ShadowDomInteractionUtils.selectByValue("#country-select", "xx"));

        System.out.println();
    }

    // ========================================
    // 7. CHECKBOX / RADIO
    // ========================================

    private static void runCheckboxRadioExamples() {
        System.out.println("--- 7. Checkbox / Radio ---");

        // Check checkbox
        check("setChecked true", true,
                ShadowDomInteractionUtils.setChecked("#terms-checkbox", true));

        // Uncheck checkbox
        check("setChecked false", true,
                ShadowDomInteractionUtils.setChecked("#terms-checkbox", false));

        // Radio button
        check("radio select", true,
                ShadowDomInteractionUtils.setChecked("#payment-card", true));

        // Non-existent checkbox
        check("setChecked non-existent", false,
                ShadowDomInteractionUtils.setChecked("#does-not-exist", true));

        System.out.println();
    }

    // ========================================
    // 8. HOVER / FOCUS
    // ========================================

    private static void runHoverFocusExamples() {
        System.out.println("--- 8. Hover / Focus ---");

        // Focus
        boolean focused = ShadowDomInteractionUtils.focus("#focus-input");
        check("focus", true, focused);
        check("focus event", "Input Focused",
                ShadowDomUtils.getValue("#status-display"));

        // Blur
        boolean blurred = ShadowDomInteractionUtils.blur("#focus-input");
        check("blur", true, blurred);
        check("blur event", "Input Blurred",
                ShadowDomUtils.getValue("#status-display"));

        // Hover
        boolean hovered = ShadowDomInteractionUtils.hover("#hover-div");
        check("hover", true, hovered);
        check("hover event", "Div Hovered",
                ShadowDomUtils.getValue("#status-display"));

        // Non-existent
        check("focus non-existent", false,
                ShadowDomInteractionUtils.focus("#does-not-exist"));
        check("hover non-existent", false,
                ShadowDomInteractionUtils.hover("#does-not-exist"));

        System.out.println();
    }

    // ========================================
    // 9. SCROLL
    // ========================================

    private static void runScrollExamples() {
        System.out.println("--- 9. Scroll ---");

        check("scrollIntoView", true,
                ShadowDomInteractionUtils.scrollIntoView("#scroll-target"));

        check("scroll non-existent", false,
                ShadowDomInteractionUtils.scrollIntoView("#does-not-exist"));

        System.out.println();
    }

    // ========================================
    // 10. SHADOW DOM
    // ========================================

    private static void runShadowDomExamples() {
        System.out.println("--- 10. Shadow DOM ---");

        // Read from open shadow DOM
        check("shadow input value", "shadow-value",
                ShadowDomUtils.getValue("#shadow-host", "input", "value"));

        check("shadow span text", "shadow text",
                ShadowDomUtils.getValue("#shadow-host", "span", "textContent"));

        // Click shadow DOM button
        boolean clicked = ShadowDomInteractionUtils.click("#shadow-host", "button");
        check("shadow click", true, clicked);
        check("shadow click event", "Shadow Button Clicked",
                ShadowDomUtils.getValue("#status-display"));

        // Type into shadow DOM input
        boolean typed = ShadowDomInteractionUtils.type("#shadow-host", "input", "new shadow text");
        check("shadow type", true, typed);
        check("shadow typed value", "new shadow text",
                ShadowDomUtils.getValue("#shadow-host", "input", "value"));

        // Closed shadow DOM — graceful handling
        String closedValue = ShadowDomUtils.getValue("#closed-shadow-host", "input", "value");
        check("closed shadow (null or value)", true,
                closedValue == null || "closed-value".equals(closedValue));

        System.out.println();
    }

    // ========================================
    // 11. ERROR HANDLING / NULL SAFETY
    // ========================================

    private static void runErrorHandlingExamples() {
        System.out.println("--- 11. Error Handling ---");

        // Non-existent element
        check("getValue non-existent", null,
                ShadowDomUtils.getValue("#does-not-exist"));

        // Null selector
        check("getValue null selector", null,
                ShadowDomUtils.getValue(null));

        // Empty selector
        check("getValue empty selector", null,
                ShadowDomUtils.getValue(""));

        // Malformed selector
        check("getValue malformed", null,
                ShadowDomUtils.getValue("###invalid"));

        // Attribute selector with quotes
        check("getValue attribute selector", "Custom Data Element",
                ShadowDomUtils.getValue("[data-id='42']"));

        System.out.println();
    }

    // ========================================
    // 12. COMBINED WORKFLOW
    // ========================================

    private static void runCombinedWorkflow() {
        System.out.println("--- 12. Combined Workflow ---");

        // Fill a form, read values, submit
        ShadowDomInteractionUtils.type("#text-input", "John Doe");
        ShadowDomInteractionUtils.type("#email-input", "john@example.com");
        ShadowDomInteractionUtils.selectByText("#country-select", "Germany");
        ShadowDomInteractionUtils.setChecked("#terms-checkbox", true);
        ShadowDomInteractionUtils.click("#action-button");

        check("form: name", "John Doe",
                ShadowDomUtils.getValue("#text-input"));
        check("form: email", "john@example.com",
                ShadowDomUtils.getValue("#email-input"));
        check("form: country", "Germany",
                ShadowDomUtils.getValue("#country-select"));
        check("form: button event", "Button Clicked",
                ShadowDomUtils.getValue("#status-display"));

        // Read + update numeric value
        Integer count = ShadowDomUtils.getIntValue("#count-span");
        check("read count", 150, count);
        System.out.println("  Count + 1 = " + (count != null ? count + 1 : "null"));

        System.out.println();
    }

    // ========================================
    // HTML PAGE
    // ========================================

    private static String createDemoHTML() {
        return """
            <!DOCTYPE html>
            <html>
            <head><title>Shadow DOM Examples</title></head>
            <body>
                <!-- Input Elements -->
                <input id="text-input" type="text" value="test-value" />
                <input id="email-input" type="email" placeholder="Enter email" />
                <input id="number-input" type="number" value="42" />
                <input id="password-input" type="password" />
                <input id="placeholder-input" placeholder="Enter text here" />
                <textarea id="notes-textarea">Some notes here</textarea>

                <!-- Text Elements -->
                <div id="info-div">Hello World</div>
                <span id="status-span">Status OK</span>
                <span id="count-span">150</span>
                <div id="price-element">99.99</div>

                <!-- Data Attributes -->
                <div id="data-element" data-value="custom-123" data-id="42">Custom Data Element</div>
                <a id="link-element" href="https://example.com" title="Example Link">Link</a>

                <!-- Select -->
                <select id="country-select">
                    <option value="">Choose</option>
                    <option value="us">United States</option>
                    <option value="de" selected>Germany</option>
                    <option value="uk">United Kingdom</option>
                </select>

                <!-- Checkbox / Radio -->
                <input id="terms-checkbox" type="checkbox" />
                <input id="payment-card" type="radio" name="payment" value="card" />
                <input id="payment-paypal" type="radio" name="payment" value="paypal" />

                <!-- Interactive Elements -->
                <button id="action-button">Click Me</button>
                <div id="clickable-div" style="cursor:pointer;padding:10px;border:1px solid black;">Clickable</div>
                <input id="focus-input" type="text" placeholder="Focus test" />
                <div id="hover-div" style="padding:20px;background:lightgray;">Hover me</div>

                <!-- Status -->
                <div id="status-display">Ready</div>

                <!-- Scroll Target -->
                <div style="height:1000px;">Spacer</div>
                <div id="scroll-target" style="padding:20px;background:lightblue;">Scroll target</div>
                <div style="height:500px;">More spacer</div>

                <!-- Shadow DOM Hosts -->
                <div id="shadow-host"></div>
                <div id="closed-shadow-host"></div>

                <script>
                    // Event Listeners
                    document.getElementById('action-button').addEventListener('click', function() {
                        document.getElementById('status-display').textContent = 'Button Clicked';
                    });
                    document.getElementById('clickable-div').addEventListener('click', function() {
                        document.getElementById('status-display').textContent = 'Div Clicked';
                    });
                    document.getElementById('focus-input').addEventListener('focus', function() {
                        document.getElementById('status-display').textContent = 'Input Focused';
                    });
                    document.getElementById('focus-input').addEventListener('blur', function() {
                        document.getElementById('status-display').textContent = 'Input Blurred';
                    });
                    document.getElementById('hover-div').addEventListener('mouseover', function() {
                        document.getElementById('status-display').textContent = 'Div Hovered';
                    });

                    // Open Shadow DOM
                    const shadowHost = document.getElementById('shadow-host');
                    const shadowRoot = shadowHost.attachShadow({mode: 'open'});
                    shadowRoot.innerHTML = '<input id="shadow-input" value="shadow-value" />'
                        + '<span id="shadow-span">shadow text</span>'
                        + '<button id="shadow-button">Shadow Button</button>';
                    shadowRoot.getElementById('shadow-button').addEventListener('click', function() {
                        document.getElementById('status-display').textContent = 'Shadow Button Clicked';
                    });

                    // Closed Shadow DOM
                    const closedHost = document.getElementById('closed-shadow-host');
                    const closedRoot = closedHost.attachShadow({mode: 'closed'});
                    closedRoot.innerHTML = '<input id="closed-input" value="closed-value" />';
                </script>
            </body>
            </html>
            """;
    }

    // ========================================
    // HELPER
    // ========================================

    private static void check(String label, Object expected, Object actual) {
        boolean ok;
        if (expected == null) {
            ok = actual == null;
        } else if (expected instanceof Double && actual instanceof Double) {
            ok = Math.abs((Double) expected - (Double) actual) < 0.001;
        } else {
            ok = expected.equals(actual);
        }

        if (ok) {
            passed++;
            System.out.println("  [OK]   " + label);
        } else {
            failed++;
            System.out.println("  [FAIL] " + label + " — expected: <" + expected + "> but was: <" + actual + ">");
        }
    }
}
