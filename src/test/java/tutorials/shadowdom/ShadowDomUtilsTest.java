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
 * Unit Tests for ShadowDomUtils
 */
class ShadowDomUtilsTest {

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
        String html = createTestHTML();
        File tempFile = File.createTempFile("shadow-dom-utils-test", ".html");
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
    
    private String createTestHTML() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Shadow DOM Test Page</title>
            </head>
            <body>
                <!-- Standard Input Elements -->
                <input id="text-input" type="text" value="test-value" />
                <textarea id="textarea-element">textarea content</textarea>
                <select id="select-element">
                    <option value="option1">Option 1</option>
                    <option value="option2" selected>Option 2</option>
                </select>
                
                <!-- Text Content Elements -->
                <div id="div-element">div text content</div>
                <span id="span-element">span text content</span>
                <p id="p-element">paragraph content</p>
                <h1 id="h1-element">heading content</h1>
                
                <!-- Elements with Attributes -->
                <div id="data-element" data-value="custom-data" data-id="123">Element with data</div>
                <input id="placeholder-input" placeholder="Enter text here" />
                <a id="link-element" href="https://example.com" title="Example Link">Link</a>
                
                <!-- Numeric Elements -->
                <input id="number-input" type="number" value="42" />
                <div id="price-element">99.99</div>
                <span id="count-element">150</span>
                
                <!-- Empty/Null Elements -->
                <input id="empty-input" type="text" value="" />
                <div id="empty-div"></div>
                <span id="null-span"></span>
                
                <!-- Shadow DOM Host (will be created via JavaScript) -->
                <div id="shadow-host"></div>
                <div id="closed-shadow-host"></div>
                
                <!-- Complex Selectors -->
                <ul>
                    <li id="first-li">First Item</li>
                    <li id="second-li">Second Item</li>
                </ul>
                <table>
                    <tr>
                        <td id="table-cell">Table Content</td>
                    </tr>
                </table>
                
                <script>
                    // Create Open Shadow DOM
                    const shadowHost = document.getElementById('shadow-host');
                    const shadowRoot = shadowHost.attachShadow({mode: 'open'});
                    shadowRoot.innerHTML = `
                        <input id="shadow-input" value="shadow-value" />
                        <span id="shadow-span">shadow text</span>
                        <button id="shadow-button">Shadow Button</button>
                    `;
                    
                    // Create Closed Shadow DOM (simulated)
                    const closedHost = document.getElementById('closed-shadow-host');
                    const closedRoot = closedHost.attachShadow({mode: 'closed'});
                    closedRoot.innerHTML = `
                        <input id="closed-input" value="closed-value" />
                        <span id="closed-span">closed text</span>
                    `;
                    // Store reference for testing purposes
                    closedHost._closedShadowRoot = closedRoot;
                </script>
            </body>
            </html>
        """;
    }
    
    // ========================================
    // BASIC VALUE EXTRACTION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should extract value from input element")
    void testGetValueFromInput() {
        String value = ShadowDomUtils.getValue("#text-input");
        assertEquals("test-value", value);
    }
    
    @Test
    @DisplayName("Should extract text from div element")
    void testGetValueFromDiv() {
        String value = ShadowDomUtils.getValue("#div-element");
        assertEquals("div text content", value);
    }
    
    @Test
    @DisplayName("Should extract text from span element")
    void testGetValueFromSpan() {
        String value = ShadowDomUtils.getValue("#span-element");
        assertEquals("span text content", value);
    }
    
    @Test
    @DisplayName("Should extract content from textarea")
    void testGetValueFromTextarea() {
        String value = ShadowDomUtils.getValue("#textarea-element");
        assertEquals("textarea content", value);
    }
    
    @Test
    @DisplayName("Should extract selected option from select")
    void testGetValueFromSelect() {
        String value = ShadowDomUtils.getValue("#select-element");
        assertTrue(value.contains("Option 2") || value.equals("option2"));
    }
    
    // ========================================
    // SPECIALIZED METHOD TESTS
    // ========================================
    
    @Test
    @DisplayName("Should extract text using getText method")
    void testGetText() {
        String text = ShadowDomUtils.getText("#div-element");
        assertEquals("div text content", text);
    }
    
    @Test
    @DisplayName("Should extract span text using getSpanText")
    void testGetSpanText() {
        String text = ShadowDomUtils.getSpanText("#span-element");
        assertEquals("span text content", text);
    }
    
    @Test
    @DisplayName("Should extract div text using getDivText")
    void testGetDivText() {
        String text = ShadowDomUtils.getDivText("#div-element");
        assertEquals("div text content", text);
    }
    
    @Test
    @DisplayName("Should extract input value using getInputValue")
    void testGetInputValue() {
        String value = ShadowDomUtils.getInputValue("#text-input");
        assertEquals("test-value", value);
    }
    
    // ========================================
    // ATTRIBUTE EXTRACTION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should extract data attribute")
    void testGetDataAttribute() {
        String value = ShadowDomUtils.getAttribute("#data-element", "data-value");
        assertEquals("custom-data", value);
    }
    
    @Test
    @DisplayName("Should extract data-id attribute")
    void testGetDataIdAttribute() {
        String value = ShadowDomUtils.getAttribute("#data-element", "data-id");
        assertEquals("123", value);
    }
    
    @Test
    @DisplayName("Should extract placeholder attribute")
    void testGetPlaceholderAttribute() {
        String value = ShadowDomUtils.getAttribute("#placeholder-input", "placeholder");
        assertEquals("Enter text here", value);
    }
    
    @Test
    @DisplayName("Should extract href attribute")
    void testGetHrefAttribute() {
        String value = ShadowDomUtils.getAttribute("#link-element", "href");
        assertEquals("https://example.com", value);
    }
    
    @Test
    @DisplayName("Should extract title attribute")
    void testGetTitleAttribute() {
        String value = ShadowDomUtils.getAttribute("#link-element", "title");
        assertEquals("Example Link", value);
    }
    
    // ========================================
    // NUMERIC VALUE TESTS
    // ========================================
    
    @Test
    @DisplayName("Should extract integer value")
    void testGetIntValue() {
        Integer value = ShadowDomUtils.getIntValue("#number-input");
        assertEquals(42, value);
    }
    
    @Test
    @DisplayName("Should extract integer from text element")
    void testGetIntValueFromText() {
        Integer value = ShadowDomUtils.getIntValue("#count-element");
        assertEquals(150, value);
    }
    
    @Test
    @DisplayName("Should extract double value")
    void testGetDoubleValue() {
        Double value = ShadowDomUtils.getDoubleValue("#price-element");
        assertEquals(99.99, value, 0.001);
    }
    
    @Test
    @DisplayName("Should return null for invalid integer")
    void testGetIntValueInvalid() {
        Integer value = ShadowDomUtils.getIntValue("#div-element");
        assertNull(value);
    }
    
    @Test
    @DisplayName("Should return null for invalid double")
    void testGetDoubleValueInvalid() {
        Double value = ShadowDomUtils.getDoubleValue("#span-element");
        assertNull(value);
    }
    
    // ========================================
    // SHADOW DOM TESTS
    // ========================================
    
    @Test
    @DisplayName("Should extract value from open shadow DOM")
    void testGetValueFromOpenShadowDOM() {
        String value = ShadowDomUtils.getValue("#shadow-host", "input", "value");
        assertEquals("shadow-value", value);
    }
    
    @Test
    @DisplayName("Should extract text from open shadow DOM")
    void testGetTextFromOpenShadowDOM() {
        String text = ShadowDomUtils.getValue("#shadow-host", "span", "textContent");
        assertEquals("shadow text", text);
    }
    
    @Test
    @DisplayName("Should handle closed shadow DOM gracefully")
    void testGetValueFromClosedShadowDOM() {
        // This might return null or the value depending on implementation
        String value = ShadowDomUtils.getValue("#closed-shadow-host", "input", "value");
        // Accept either null (closed) or actual value (if reflection works)
        assertTrue(value == null || "closed-value".equals(value));
    }
    
    // ========================================
    // NULL/EMPTY VALUE TESTS
    // ========================================
    
    @Test
    @DisplayName("Should handle empty input gracefully")
    void testGetValueFromEmptyInput() {
        String value = ShadowDomUtils.getValue("#empty-input");
        assertTrue(value == null || value.isEmpty());
    }
    
    @Test
    @DisplayName("Should handle empty div gracefully")
    void testGetValueFromEmptyDiv() {
        String value = ShadowDomUtils.getValue("#empty-div");
        assertTrue(value == null || value.isEmpty());
    }
    
    @Test
    @DisplayName("Should return null for non-existent element")
    void testGetValueFromNonExistentElement() {
        String value = ShadowDomUtils.getValue("#non-existent-element");
        assertNull(value);
    }
    
    @Test
    @DisplayName("Should return null for non-existent attribute")
    void testGetNonExistentAttribute() {
        String value = ShadowDomUtils.getAttribute("#div-element", "non-existent-attr");
        assertNull(value);
    }
    
    // ========================================
    // COMPLEX SELECTOR TESTS
    // ========================================
    
    @Test
    @DisplayName("Should work with CSS pseudo-selectors")
    void testComplexSelectors() {
        String value = ShadowDomUtils.getValue("li:first-child");
        assertEquals("First Item", value);
    }
    
    @Test
    @DisplayName("Should work with attribute selectors")
    void testAttributeSelectors() {
        String value = ShadowDomUtils.getValue("[data-id='123']");
        assertEquals("Element with data", value);
    }
    
    @Test
    @DisplayName("Should work with descendant selectors")
    void testDescendantSelectors() {
        String value = ShadowDomUtils.getValue("table td");
        assertEquals("Table Content", value);
    }
    
    // ========================================
    // CUSTOM PARAMETER TESTS
    // ========================================
    
    @Test
    @DisplayName("Should extract value with custom child and property")
    void testGetValueWithCustomParameters() {
        String value = ShadowDomUtils.getValue("#text-input", "input", "value");
        assertEquals("test-value", value);
    }
    
    @Test
    @DisplayName("Should extract textContent with custom parameters")
    void testGetTextContentWithCustomParameters() {
        String text = ShadowDomUtils.getValue("#div-element", "div", "textContent");
        assertEquals("div text content", text);
    }
    
    @Test
    @DisplayName("Should extract innerHTML with custom parameters")
    void testGetInnerHTMLWithCustomParameters() {
        String html = ShadowDomUtils.getValue("#div-element", "div", "innerHTML");
        assertEquals("div text content", html);
    }
    
    // ========================================
    // ERROR HANDLING TESTS
    // ========================================
    
    @Test
    @DisplayName("Should handle malformed selectors gracefully")
    void testMalformedSelector() {
        String value = ShadowDomUtils.getValue("###invalid-selector");
        assertNull(value);
    }
    
    @Test
    @DisplayName("Should handle null selector gracefully")
    void testNullSelector() {
        assertDoesNotThrow(() -> {
            String value = ShadowDomUtils.getValue(null);
            assertNull(value);
        });
    }
    
    @Test
    @DisplayName("Should handle empty selector gracefully")
    void testEmptySelector() {
        String value = ShadowDomUtils.getValue("");
        assertNull(value);
    }
    
    // ========================================
    // PERFORMANCE TESTS
    // ========================================
    
    @Test
    @DisplayName("Should complete extraction within reasonable time")
    void testPerformance() {
        long startTime = System.currentTimeMillis();
        
        for (int i = 0; i < 10; i++) {
            ShadowDomUtils.getValue("#text-input");
        }
        
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        
        assertTrue(duration < 5000, "Extraction should complete within 5 seconds");
    }
    
    // ========================================
    // STRATEGY TESTING
    // ========================================
    
    @Test
    @DisplayName("Should try multiple strategies for extraction")
    void testMultipleStrategies() {
        // This test verifies that different strategies are attempted
        // by checking elements that might work with some strategies but not others
        
        String inputValue = ShadowDomUtils.getValue("#text-input");
        assertNotNull(inputValue);
        
        String divText = ShadowDomUtils.getValue("#div-element");
        assertNotNull(divText);
        
        String selectValue = ShadowDomUtils.getValue("#select-element");
        assertNotNull(selectValue);
    }
    
    // ========================================
    // INTEGRATION TESTS
    // ========================================
    
    @Test
    @DisplayName("Should work in realistic form scenario")
    void testRealisticFormScenario() {
        // Simulate a form with various field types
        String username = ShadowDomUtils.getValue("#text-input");
        String description = ShadowDomUtils.getValue("#textarea-element");
        String selection = ShadowDomUtils.getValue("#select-element");
        String info = ShadowDomUtils.getValue("#div-element");
        
        assertNotNull(username);
        assertNotNull(description);
        assertNotNull(selection);
        assertNotNull(info);
        
        // Verify specific values
        assertEquals("test-value", username);
        assertEquals("textarea content", description);
        assertTrue(selection.contains("Option") || selection.contains("option"));
        assertEquals("div text content", info);
    }
    
    @Test
    @DisplayName("Should extract data for reporting purposes")
    void testDataExtractionForReporting() {
        // Test scenario where multiple data points are extracted
        Integer numericData = ShadowDomUtils.getIntValue("#number-input");
        Double priceData = ShadowDomUtils.getDoubleValue("#price-element");
        String customData = ShadowDomUtils.getAttribute("#data-element", "data-value");
        String textData = ShadowDomUtils.getText("#span-element");
        
        assertNotNull(numericData);
        assertNotNull(priceData);
        assertNotNull(customData);
        assertNotNull(textData);
        
        assertEquals(42, numericData);
        assertEquals(99.99, priceData, 0.001);
        assertEquals("custom-data", customData);
        assertEquals("span text content", textData);
    }
}