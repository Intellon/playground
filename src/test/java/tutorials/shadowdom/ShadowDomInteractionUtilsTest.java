package tutorials.shadowdom;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import org.junit.jupiter.api.*;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Files;

import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for ShadowDomInteractionUtils
 */
class ShadowDomInteractionUtilsTest {

    @BeforeAll
    static void setupClass() {
        // Setup Chrome with headless mode for testing
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        
        Configuration.headless = true;
        Configuration.browser = "chrome";
        Configuration.timeout = 5000;
    }
    
    @BeforeEach
    void setup() throws Exception {
        // Write HTML to temp file — data: URLs don't reliably execute inline scripts in Chrome headless
        String html = createInteractiveTestHTML();
        File tempFile = File.createTempFile("shadow-dom-interaction-test", ".html");
        tempFile.deleteOnExit();
        Files.writeString(tempFile.toPath(), html);
        open(tempFile.toURI().toString());
    }
    
    @AfterAll
    static void teardownClass() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            closeWebDriver();
        }
    }
    
    // ========================================
    // TEST HTML CREATION
    // ========================================
    
    private String createInteractiveTestHTML() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Shadow DOM Interaction Test Page</title>
                <style>
                    .hidden { display: none; }
                    .visible { display: block; }
                    .clicked { background-color: lightgreen; }
                    .hovered { background-color: lightyellow; }
                    .focused { border: 2px solid blue; }
                </style>
            </head>
            <body>
                <!-- Input Elements for Typing -->
                <input id="text-input" type="text" placeholder="Enter text" />
                <input id="number-input" type="number" placeholder="Enter number" />
                <input id="email-input" type="email" placeholder="Enter email" />
                <textarea id="textarea-element" placeholder="Enter long text"></textarea>
                <input id="password-input" type="password" placeholder="Enter password" />
                
                <!-- Buttons for Clicking -->
                <button id="simple-button">Click Me</button>
                <button id="submit-button">Submit</button>
                <button id="cancel-button">Cancel</button>
                <div id="clickable-div" style="cursor: pointer; padding: 10px; border: 1px solid black;">Clickable Div</div>
                
                <!-- Select Elements -->
                <select id="simple-select">
                    <option value="">Choose option</option>
                    <option value="option1">Option 1</option>
                    <option value="option2">Option 2</option>
                    <option value="option3">Option 3</option>
                </select>
                
                <select id="country-select">
                    <option value="">Select Country</option>
                    <option value="us">United States</option>
                    <option value="de">Germany</option>
                    <option value="uk">United Kingdom</option>
                </select>
                
                <!-- Checkbox and Radio Elements -->
                <input id="terms-checkbox" type="checkbox" />
                <label for="terms-checkbox">Accept Terms</label>
                
                <input id="newsletter-checkbox" type="checkbox" />
                <label for="newsletter-checkbox">Subscribe to Newsletter</label>
                
                <input id="payment-card" type="radio" name="payment" value="card" />
                <label for="payment-card">Credit Card</label>
                
                <input id="payment-paypal" type="radio" name="payment" value="paypal" />
                <label for="payment-paypal">PayPal</label>
                
                <!-- Elements for Focus/Hover Testing -->
                <input id="focus-input" type="text" placeholder="Focus test" />
                <div id="hover-div" style="padding: 20px; background: lightgray;">Hover over me</div>
                
                <!-- Elements for Scrolling -->
                <div style="height: 1000px;">Spacer for scrolling</div>
                <div id="scroll-target" style="padding: 20px; background: lightblue;">Scroll target</div>
                <div style="height: 1000px;">More spacer</div>
                
                <!-- Status Display Elements -->
                <div id="status-display">Ready</div>
                <div id="result-display"></div>
                <div id="error-display" class="hidden"></div>
                
                <!-- Shadow DOM Host -->
                <div id="shadow-host"></div>
                <div id="interaction-shadow-host"></div>
                
                <script>
                    // Event Listeners for Testing
                    document.getElementById('simple-button').addEventListener('click', function() {
                        this.classList.add('clicked');
                        document.getElementById('status-display').textContent = 'Button Clicked';
                    });
                    
                    document.getElementById('clickable-div').addEventListener('click', function() {
                        this.classList.add('clicked');
                        document.getElementById('status-display').textContent = 'Div Clicked';
                    });
                    
                    document.getElementById('text-input').addEventListener('input', function() {
                        document.getElementById('result-display').textContent = 'Input: ' + this.value;
                    });
                    
                    document.getElementById('simple-select').addEventListener('change', function() {
                        document.getElementById('result-display').textContent = 'Selected: ' + this.value;
                    });
                    
                    document.getElementById('terms-checkbox').addEventListener('change', function() {
                        document.getElementById('result-display').textContent = 'Checkbox: ' + this.checked;
                    });
                    
                    document.getElementById('focus-input').addEventListener('focus', function() {
                        this.classList.add('focused');
                        document.getElementById('status-display').textContent = 'Input Focused';
                    });
                    
                    document.getElementById('focus-input').addEventListener('blur', function() {
                        this.classList.remove('focused');
                        document.getElementById('status-display').textContent = 'Input Blurred';
                    });
                    
                    document.getElementById('hover-div').addEventListener('mouseover', function() {
                        this.classList.add('hovered');
                        document.getElementById('status-display').textContent = 'Div Hovered';
                    });
                    
                    // Create Interactive Shadow DOM
                    const shadowHost = document.getElementById('shadow-host');
                    const shadowRoot = shadowHost.attachShadow({mode: 'open'});
                    shadowRoot.innerHTML = `
                        <input id="shadow-input" type="text" placeholder="Shadow input" />
                        <button id="shadow-button">Shadow Button</button>
                        <select id="shadow-select">
                            <option value="shadow1">Shadow Option 1</option>
                            <option value="shadow2">Shadow Option 2</option>
                        </select>
                        <style>
                            #shadow-button.clicked { background-color: lightcoral; }
                        </style>
                    `;
                    
                    shadowRoot.getElementById('shadow-button').addEventListener('click', function() {
                        this.classList.add('clicked');
                        document.getElementById('status-display').textContent = 'Shadow Button Clicked';
                    });
                    
                    shadowRoot.getElementById('shadow-input').addEventListener('input', function() {
                        document.getElementById('result-display').textContent = 'Shadow Input: ' + this.value;
                    });
                    
                    // Create another Shadow DOM for complex testing
                    const interactionHost = document.getElementById('interaction-shadow-host');
                    const interactionRoot = interactionHost.attachShadow({mode: 'open'});
                    interactionRoot.innerHTML = `
                        <form id="shadow-form">
                            <input id="shadow-username" type="text" placeholder="Username" required />
                            <input id="shadow-email" type="email" placeholder="Email" required />
                            <input id="shadow-terms" type="checkbox" />
                            <button id="shadow-submit" type="button">Submit</button>
                        </form>
                    `;
                    
                    interactionRoot.getElementById('shadow-submit').addEventListener('click', function() {
                        const username = interactionRoot.getElementById('shadow-username').value;
                        const email = interactionRoot.getElementById('shadow-email').value;
                        const terms = interactionRoot.getElementById('shadow-terms').checked;
                        
                        if (username && email && terms) {
                            document.getElementById('result-display').textContent = 'Shadow Form Submitted';
                        } else {
                            document.getElementById('result-display').textContent = 'Shadow Form Incomplete';
                        }
                    });
                </script>
            </body>
            </html>
        """;
    }
    
    // ========================================
    // CLICK OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should click simple button")
    void testClickSimpleButton() {
        boolean clicked = ShadowDomInteractionUtils.click("#simple-button");
        assertTrue(clicked);
        
        // Verify the click worked by checking status
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Button Clicked", status);
    }
    
    @Test
    @DisplayName("Should click clickable div")
    void testClickClickableDiv() {
        boolean clicked = ShadowDomInteractionUtils.click("#clickable-div");
        assertTrue(clicked);
        
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Div Clicked", status);
    }
    
    @Test
    @DisplayName("Should click shadow DOM button")
    void testClickShadowButton() {
        boolean clicked = ShadowDomInteractionUtils.click("#shadow-host", "button");
        assertTrue(clicked);
        
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Shadow Button Clicked", status);
    }
    
    @Test
    @DisplayName("Should return false for non-existent element click")
    void testClickNonExistentElement() {
        boolean clicked = ShadowDomInteractionUtils.click("#non-existent-button");
        assertFalse(clicked);
    }
    
    @Test
    @DisplayName("Should wait and click with timeout")
    void testWaitAndClick() {
        boolean clicked = ShadowDomInteractionUtils.waitAndClick("#simple-button", 3);
        assertTrue(clicked);
        
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Button Clicked", status);
    }
    
    // ========================================
    // INPUT/TYPING OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should type text into input field")
    void testTypeIntoInput() {
        boolean typed = ShadowDomInteractionUtils.type("#text-input", "Hello World");
        assertTrue(typed);
        
        // Verify the text was entered
        String value = ShadowDomUtils.getValue("#text-input");
        assertEquals("Hello World", value);
        
        // Verify event was triggered
        String result = ShadowDomUtils.getValue("#result-display");
        assertEquals("Input: Hello World", result);
    }
    
    @Test
    @DisplayName("Should type into textarea")
    void testTypeIntoTextarea() {
        boolean typed = ShadowDomInteractionUtils.type("#textarea-element", "Multi-line\\ntext content");
        assertTrue(typed);
        
        String value = ShadowDomUtils.getValue("#textarea-element");
        assertNotNull(value);
        assertTrue(value.contains("Multi-line"));
    }
    
    @Test
    @DisplayName("Should type into number input")
    void testTypeIntoNumberInput() {
        boolean typed = ShadowDomInteractionUtils.type("#number-input", "12345");
        assertTrue(typed);
        
        String value = ShadowDomUtils.getValue("#number-input");
        assertEquals("12345", value);
    }
    
    @Test
    @DisplayName("Should type into email input")
    void testTypeIntoEmailInput() {
        boolean typed = ShadowDomInteractionUtils.type("#email-input", "test@example.com");
        assertTrue(typed);
        
        String value = ShadowDomUtils.getValue("#email-input");
        assertEquals("test@example.com", value);
    }
    
    @Test
    @DisplayName("Should type into password input")
    void testTypeIntoPasswordInput() {
        boolean typed = ShadowDomInteractionUtils.type("#password-input", "secretPassword123");
        assertTrue(typed);
        
        String value = ShadowDomUtils.getValue("#password-input");
        assertEquals("secretPassword123", value);
    }
    
    @Test
    @DisplayName("Should type into shadow DOM input")
    void testTypeIntoShadowInput() {
        boolean typed = ShadowDomInteractionUtils.type("#shadow-host", "input", "Shadow Text");
        assertTrue(typed);
        
        // Verify the text was entered
        String value = ShadowDomUtils.getValue("#shadow-host", "input", "value");
        assertEquals("Shadow Text", value);
        
        // Verify event was triggered
        String result = ShadowDomUtils.getValue("#result-display");
        assertEquals("Shadow Input: Shadow Text", result);
    }
    
    @Test
    @DisplayName("Should return false for typing into non-existent element")
    void testTypeIntoNonExistentElement() {
        boolean typed = ShadowDomInteractionUtils.type("#non-existent-input", "test");
        assertFalse(typed);
    }
    
    // ========================================
    // CLEAR OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should clear input field")
    void testClearInput() {
        // First type something
        ShadowDomInteractionUtils.type("#text-input", "Initial Text");
        String initialValue = ShadowDomUtils.getValue("#text-input");
        assertEquals("Initial Text", initialValue);
        
        // Then clear it
        boolean cleared = ShadowDomInteractionUtils.clear("#text-input");
        assertTrue(cleared);
        
        String clearedValue = ShadowDomUtils.getValue("#text-input");
        assertTrue(clearedValue == null || clearedValue.isEmpty());
    }
    
    @Test
    @DisplayName("Should clear textarea")
    void testClearTextarea() {
        ShadowDomInteractionUtils.type("#textarea-element", "Text to be cleared");
        boolean cleared = ShadowDomInteractionUtils.clear("#textarea-element", "textarea");
        assertTrue(cleared);
        
        String value = ShadowDomUtils.getValue("#textarea-element");
        assertTrue(value == null || value.isEmpty());
    }
    
    // ========================================
    // SELECT OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should select option by text")
    void testSelectByText() {
        boolean selected = ShadowDomInteractionUtils.selectByText("#simple-select", "Option 2");
        assertTrue(selected);

        // getValue on a select returns the textContent of the selected option
        String value = ShadowDomUtils.getValue("#simple-select");
        assertEquals("Option 2", value);
    }
    
    @Test
    @DisplayName("Should select option by value")
    void testSelectByValue() {
        boolean selected = ShadowDomInteractionUtils.selectByValue("#country-select", "de");
        assertTrue(selected);

        // getValue on a select returns the textContent of the selected option
        String value = ShadowDomUtils.getValue("#country-select");
        assertEquals("Germany", value);
    }
    
    @Test
    @DisplayName("Should return false for non-existent option text")
    void testSelectNonExistentOptionText() {
        boolean selected = ShadowDomInteractionUtils.selectByText("#simple-select", "Non-existent Option");
        assertFalse(selected);
    }
    
    @Test
    @DisplayName("Should return false for non-existent option value")
    void testSelectNonExistentOptionValue() {
        boolean selected = ShadowDomInteractionUtils.selectByValue("#country-select", "nonexistent");
        assertFalse(selected);
    }
    
    // ========================================
    // CHECKBOX/RADIO OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should check checkbox")
    void testCheckCheckbox() {
        boolean checked = ShadowDomInteractionUtils.setChecked("#terms-checkbox", true);
        assertTrue(checked);
        
        // Verify checkbox is checked via event
        String result = ShadowDomUtils.getValue("#result-display");
        assertEquals("Checkbox: true", result);
    }
    
    @Test
    @DisplayName("Should uncheck checkbox")
    void testUncheckCheckbox() {
        // First check it
        ShadowDomInteractionUtils.setChecked("#newsletter-checkbox", true);
        
        // Then uncheck it
        boolean unchecked = ShadowDomInteractionUtils.setChecked("#newsletter-checkbox", false);
        assertTrue(unchecked);
    }
    
    @Test
    @DisplayName("Should select radio button")
    void testSelectRadioButton() {
        boolean selected = ShadowDomInteractionUtils.setChecked("#payment-card", true);
        assertTrue(selected);
    }
    
    @Test
    @DisplayName("Should deselect radio button")
    void testDeselectRadioButton() {
        boolean deselected = ShadowDomInteractionUtils.setChecked("#payment-paypal", false);
        assertTrue(deselected);
    }
    
    @Test
    @DisplayName("Should return false for non-existent checkbox")
    void testCheckNonExistentCheckbox() {
        boolean checked = ShadowDomInteractionUtils.setChecked("#non-existent-checkbox", true);
        assertFalse(checked);
    }
    
    // ========================================
    // FOCUS OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should focus on input element")
    void testFocusInput() {
        boolean focused = ShadowDomInteractionUtils.focus("#focus-input");
        assertTrue(focused);
        
        // Verify focus event was triggered
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Input Focused", status);
    }
    
    @Test
    @DisplayName("Should blur input element")
    void testBlurInput() {
        // First focus
        ShadowDomInteractionUtils.focus("#focus-input");
        
        // Then blur
        boolean blurred = ShadowDomInteractionUtils.blur("#focus-input");
        assertTrue(blurred);
        
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Input Blurred", status);
    }
    
    @Test
    @DisplayName("Should return false for focusing non-existent element")
    void testFocusNonExistentElement() {
        boolean focused = ShadowDomInteractionUtils.focus("#non-existent-input");
        assertFalse(focused);
    }
    
    // ========================================
    // HOVER OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should hover over element")
    void testHoverElement() {
        boolean hovered = ShadowDomInteractionUtils.hover("#hover-div");
        assertTrue(hovered);
        
        // Verify hover event was triggered
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Div Hovered", status);
    }
    
    @Test
    @DisplayName("Should return false for hovering non-existent element")
    void testHoverNonExistentElement() {
        boolean hovered = ShadowDomInteractionUtils.hover("#non-existent-div");
        assertFalse(hovered);
    }
    
    // ========================================
    // SCROLL OPERATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should scroll element into view")
    void testScrollIntoView() {
        boolean scrolled = ShadowDomInteractionUtils.scrollIntoView("#scroll-target");
        assertTrue(scrolled);
        
        // Note: Verifying actual scroll position is complex in headless mode
        // We mainly test that the method executes without error
    }
    
    @Test
    @DisplayName("Should return false for scrolling non-existent element")
    void testScrollNonExistentElement() {
        boolean scrolled = ShadowDomInteractionUtils.scrollIntoView("#non-existent-element");
        assertFalse(scrolled);
    }
    
    // ========================================
    // COMPLEX WORKFLOW TESTS
    // ========================================
    
    @Test
    @DisplayName("Should complete form filling workflow")
    void testCompleteFormWorkflow() {
        // Fill out a complete form
        boolean nameTyped = ShadowDomInteractionUtils.type("#text-input", "John Doe");
        boolean emailTyped = ShadowDomInteractionUtils.type("#email-input", "john@example.com");
        boolean countrySelected = ShadowDomInteractionUtils.selectByText("#country-select", "Germany");
        boolean termsChecked = ShadowDomInteractionUtils.setChecked("#terms-checkbox", true);
        boolean submitted = ShadowDomInteractionUtils.click("#submit-button");
        
        assertTrue(nameTyped);
        assertTrue(emailTyped);
        assertTrue(countrySelected);
        assertTrue(termsChecked);
        assertTrue(submitted);
        
        // Verify values
        assertEquals("John Doe", ShadowDomUtils.getValue("#text-input"));
        assertEquals("john@example.com", ShadowDomUtils.getValue("#email-input"));
        assertEquals("Germany", ShadowDomUtils.getValue("#country-select"));
    }
    
    @Test
    @DisplayName("Should complete shadow DOM form workflow")
    void testShadowDOMFormWorkflow() {
        // Fill shadow DOM form
        boolean usernameTyped = ShadowDomInteractionUtils.type("#interaction-shadow-host", "#shadow-username", "testuser");
        boolean emailTyped = ShadowDomInteractionUtils.type("#interaction-shadow-host", "#shadow-email", "test@example.com");
        boolean termsChecked = ShadowDomInteractionUtils.click("#interaction-shadow-host", "#shadow-terms");
        boolean submitted = ShadowDomInteractionUtils.click("#interaction-shadow-host", "#shadow-submit");
        
        assertTrue(usernameTyped);
        assertTrue(emailTyped);
        assertTrue(termsChecked);
        assertTrue(submitted);
        
        // Verify form submission result
        String result = ShadowDomUtils.getValue("#result-display");
        assertEquals("Shadow Form Submitted", result);
    }
    
    @Test
    @DisplayName("Should handle incomplete shadow DOM form")
    void testIncompleteShadowDOMForm() {
        // Submit form without filling required fields
        boolean submitted = ShadowDomInteractionUtils.click("#interaction-shadow-host", "#shadow-submit");
        assertTrue(submitted);
        
        String result = ShadowDomUtils.getValue("#result-display");
        assertEquals("Shadow Form Incomplete", result);
    }
    
    // ========================================
    // ERROR HANDLING TESTS
    // ========================================
    
    @Test
    @DisplayName("Should handle null selector gracefully")
    void testNullSelector() {
        assertDoesNotThrow(() -> {
            boolean result = ShadowDomInteractionUtils.click(null);
            assertFalse(result);
        });
    }
    
    @Test
    @DisplayName("Should handle empty selector gracefully")
    void testEmptySelector() {
        boolean clicked = ShadowDomInteractionUtils.click("");
        assertFalse(clicked);
        
        boolean typed = ShadowDomInteractionUtils.type("", "test");
        assertFalse(typed);
    }
    
    @Test
    @DisplayName("Should handle malformed selectors gracefully")
    void testMalformedSelector() {
        boolean clicked = ShadowDomInteractionUtils.click("###invalid-selector");
        assertFalse(clicked);
        
        boolean typed = ShadowDomInteractionUtils.type("invalid[[[selector", "test");
        assertFalse(typed);
    }
    
    @Test
    @DisplayName("Should handle special characters in text input")
    void testSpecialCharacters() {
        String specialText = "Special chars: !@#$%^&*()_+-={}[]|\\:;\"'<>?,./";
        boolean typed = ShadowDomInteractionUtils.type("#text-input", specialText);
        assertTrue(typed);
        
        // Note: Some special characters might be escaped, so we just verify typing worked
        String value = ShadowDomUtils.getValue("#text-input");
        assertNotNull(value);
        assertFalse(value.isEmpty());
    }
    
    @Test
    @DisplayName("Should handle unicode characters in text input")
    void testUnicodeCharacters() {
        String unicodeText = "Unicode: äöüß αβγ 中文 🚀";
        boolean typed = ShadowDomInteractionUtils.type("#text-input", unicodeText);
        assertTrue(typed);
        
        String value = ShadowDomUtils.getValue("#text-input");
        assertNotNull(value);
    }
    
    // ========================================
    // INTEGRATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should integrate reading and interaction operations")
    void testReadAndInteractIntegration() {
        // Read initial value
        String initialValue = ShadowDomUtils.getValue("#text-input");
        assertTrue(initialValue == null || initialValue.isEmpty());
        
        // Interact (type)
        boolean typed = ShadowDomInteractionUtils.type("#text-input", "New Value");
        assertTrue(typed);
        
        // Read updated value
        String newValue = ShadowDomUtils.getValue("#text-input");
        assertEquals("New Value", newValue);
        
        // Clear
        boolean cleared = ShadowDomInteractionUtils.clear("#text-input");
        assertTrue(cleared);
        
        // Read cleared value
        String clearedValue = ShadowDomUtils.getValue("#text-input");
        assertTrue(clearedValue == null || clearedValue.isEmpty());
    }
    
    @Test
    @DisplayName("Should handle dynamic content updates")
    void testDynamicContentUpdates() {
        // Initial status
        String initialStatus = ShadowDomUtils.getValue("#status-display");
        assertEquals("Ready", initialStatus);
        
        // Perform action that changes status
        boolean clicked = ShadowDomInteractionUtils.click("#simple-button");
        assertTrue(clicked);
        
        // Verify status changed
        String updatedStatus = ShadowDomUtils.getValue("#status-display");
        assertEquals("Button Clicked", updatedStatus);
    }
    
    // ========================================
    // PERFORMANCE TESTS
    // ========================================
    
    @Test
    @DisplayName("Should complete interactions within reasonable time")
    void testInteractionPerformance() {
        long startTime = System.currentTimeMillis();
        
        // Perform multiple interactions
        for (int i = 0; i < 5; i++) {
            ShadowDomInteractionUtils.type("#text-input", "Test " + i);
            ShadowDomInteractionUtils.click("#simple-button");
            ShadowDomInteractionUtils.clear("#text-input");
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 10000, "Interactions should complete within 10 seconds");
    }
    
    // ========================================
    // TIMEOUT AND RETRY TESTS
    // ========================================
    
    @Test
    @DisplayName("Should timeout appropriately for wait and click")
    void testWaitAndClickTimeout() {
        long startTime = System.currentTimeMillis();
        
        boolean result = ShadowDomInteractionUtils.waitAndClick("#non-existent-element", 2);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertFalse(result);
        assertTrue(duration >= 2000, "Should wait at least 2 seconds");
        assertTrue(duration < 3000, "Should not wait much longer than timeout");
    }
    
    @Test
    @DisplayName("Should succeed immediately if element is available")
    void testWaitAndClickImmediate() {
        long startTime = System.currentTimeMillis();
        
        boolean result = ShadowDomInteractionUtils.waitAndClick("#simple-button", 5);
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(result);
        assertTrue(duration < 1000, "Should succeed quickly when element is available");
        
        // Verify the click actually worked
        String status = ShadowDomUtils.getValue("#status-display");
        assertEquals("Button Clicked", status);
    }
}