package tutorials.shadowdom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

/**
 * Generic utility class for Shadow DOM and complex element access
 */
public class ShadowDomUtils {
    
    private static final Logger log = LoggerFactory.getLogger(ShadowDomUtils.class);
    
    /**
     * Extracts value using smart auto-detection of element type
     * 
     * @param hostSelector Selector for the host element
     * @return Value or null
     */
    public static String getValue(String hostSelector) {
        // Quick existence check to avoid slow Selenide timeout for non-existent elements
        try {
            Boolean exists = (Boolean) executeJavaScript(
                "return document.querySelector(arguments[0]) !== null", hostSelector);
            if (!Boolean.TRUE.equals(exists)) return null;
        } catch (Exception e) {
            return null;
        }

        // First try to detect element type and use appropriate strategy
        // Smart extraction is the most accurate — trust non-null results (even empty string)
        String value = trySmartExtraction(hostSelector);
        if (value != null) return value;

        // Fallback: Try all common combinations
        String[] childSelectors = {"input", "span", "div", "textarea", "select", "*"};
        String[] properties = {"value", "textContent", "innerText", "innerHTML"};
        
        for (String childSelector : childSelectors) {
            for (String property : properties) {
                value = getValue(hostSelector, childSelector, property);
                if (isValid(value)) {
                    return value;
                }
            }
        }
        return null;
    }
    
    /**
     * Smart extraction based on element type detection
     */
    private static String trySmartExtraction(String selector) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return null;
                
                var tagName = elem.tagName.toLowerCase();
                
                // Input elements — elem.value reflects current state (always a string, never null)
                if (tagName === 'input' || tagName === 'textarea') {
                    return elem.value;
                }
                
                // Select elements  
                if (tagName === 'select') {
                    var selected = elem.selectedOptions[0];
                    return selected ? selected.textContent : elem.value;
                }
                
                // Text content elements
                if (['div', 'span', 'p', 'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'td', 'th'].includes(tagName)) {
                    return elem.textContent || elem.innerText;
                }
                
                // Generic fallback
                return elem.value || elem.textContent || elem.innerText || elem.innerHTML;
                """, selector);
            
            return (String) executeJavaScript(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Extracts value with custom child selector and property
     * 
     * @param hostSelector Selector for the host element
     * @param childSelector Selector for the child element in Shadow DOM
     * @param property Property to extract (value, textContent, etc.)
     * @return Value or null
     */
    public static String getValue(String hostSelector, String childSelector, String property) {
        // Strategy 1: Shadow DOM (open) — try first to avoid host textContent leaking
        String value = tryShadowDomOpen(hostSelector, childSelector, property);
        if (isValid(value)) return value;

        // Strategy 2: Shadow DOM (closed) - Reflection
        value = tryShadowDomClosed(hostSelector, childSelector, property);
        if (isValid(value)) return value;

        // Strategy 3: Standard Selenide
        value = trySelenideStandard(hostSelector);
        if (isValid(value)) return value;

        // Strategy 4: JavaScript Standard
        value = tryJavaScriptStandard(hostSelector);
        if (isValid(value)) return value;

        // Strategy 5: Event-based
        value = tryEventBased(hostSelector);
        if (isValid(value)) return value;

        // Strategy 6: Deep DOM traversal
        value = tryDeepTraversal(hostSelector, property);
        if (isValid(value)) return value;

        return null;
    }
    
    /**
     * Strategy 1: Standard Selenide access
     */
    private static String trySelenideStandard(String selector) {
        try {
            var element = $(selector);
            String value = element.getAttribute("value");
            if (isValid(value)) return value;
            
            value = element.getValue();
            if (isValid(value)) return value;
            
            return element.getText();
        } catch (Exception | AssertionError e) {
            return null;
        }
    }

    /**
     * Strategy 2: JavaScript standard properties
     */
    private static String tryJavaScriptStandard(String selector) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return null;
                return elem.value || elem.getAttribute('value') || 
                       elem.textContent || elem.innerText || elem.innerHTML;
                """, selector);
            
            return (String) executeJavaScript(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Strategy 3: Shadow DOM (open)
     */
    private static String tryShadowDomOpen(String hostSelector, String childSelector, String property) {
        try {
            String script = String.format("""
                var host = document.querySelector('%s');
                if (!host || !host.shadowRoot) return null;
                
                var child = host.shadowRoot.querySelector('%s');
                if (!child) return null;
                
                return child['%s'] || child.getAttribute('%s') || 
                       child.textContent || child.innerText;
                """, hostSelector, childSelector, property, property);
            
            return (String) executeJavaScript(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Strategy 4: Shadow DOM (closed) - Reflection/Internal Properties
     */
    private static String tryShadowDomClosed(String hostSelector, String childSelector, String property) {
        try {
            String script = String.format("""
                var host = document.querySelector('%s');
                if (!host) return null;
                
                // Search all internal properties
                var keys = Object.getOwnPropertyNames(host);
                for (var key of keys) {
                    try {
                        var prop = host[key];
                        if (prop && typeof prop === 'object' && prop.querySelector) {
                            var child = prop.querySelector('%s');
                            if (child) {
                                var value = child['%s'] || child.getAttribute('%s');
                                if (value) return value;
                            }
                        }
                    } catch(e) {}
                }
                
                // Look for Shadow Root via internal APIs
                try {
                    var shadowRoot = host.shadowRoot || 
                                   host._shadowRoot || 
                                   host.__shadowRoot ||
                                   host.attachShadow;
                    if (shadowRoot && shadowRoot.querySelector) {
                        var child = shadowRoot.querySelector('%s');
                        if (child) {
                            return child['%s'] || child.getAttribute('%s');
                        }
                    }
                } catch(e) {}
                
                return null;
                """, hostSelector, childSelector, property, property, 
                     childSelector, property, property);
            
            return (String) executeJavaScript(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Strategy 5: Event-based access
     */
    private static String tryEventBased(String selector) {
        try {
            String script = String.format("""
                return new Promise((resolve) => {
                    var elem = document.querySelector('%s');
                    if (!elem) {
                        resolve(null);
                        return;
                    }
                    
                    var originalValue = elem.value || elem.getAttribute('value');
                    if (originalValue) {
                        resolve(originalValue);
                        return;
                    }
                    
                    // Event listener for value changes
                    var timeout = setTimeout(() => resolve(null), 1000);
                    
                    elem.addEventListener('input', function(e) {
                        clearTimeout(timeout);
                        resolve(e.target.value);
                    });
                    
                    elem.addEventListener('change', function(e) {
                        clearTimeout(timeout);
                        resolve(e.target.value);
                    });
                    
                    // Trigger events
                    elem.focus();
                    elem.blur();
                    elem.click();
                });
                """, selector);
            
            return (String) executeJavaScript(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Strategy 6: Deep DOM Traversal
     */
    private static String tryDeepTraversal(String selector, String property) {
        try {
            String script = String.format("""
                function deepSearch(element, property) {
                    if (!element) return null;
                    
                    // Check current element
                    var value = element[property] || element.getAttribute(property);
                    if (value) return value;
                    
                    // Search children
                    for (var child of element.children || []) {
                        value = deepSearch(child, property);
                        if (value) return value;
                    }
                    
                    // Search Shadow Root
                    if (element.shadowRoot) {
                        for (var shadowChild of element.shadowRoot.children || []) {
                            value = deepSearch(shadowChild, property);
                            if (value) return value;
                        }
                    }
                    
                    return null;
                }
                
                var host = document.querySelector('%s');
                return deepSearch(host, '%s');
                """, selector, property);
            
            return (String) executeJavaScript(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Extracts text content (for non-input elements)
     */
    public static String getText(String selector) {
        return getValue(selector, "*", "textContent");
    }
    
    /**
     * Extracts attribute value
     */
    public static String getAttribute(String selector, String attribute) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return null;
                return elem.getAttribute('%s');
                """, selector, attribute);
            return (String) executeJavaScript(script);
        } catch (Exception e) {
            return null;
        }
    }
    
    /**
     * Special methods for common element types
     */
    public static String getInputValue(String selector) {
        return getValue(selector, "input", "value");
    }
    
    public static String getSpanText(String selector) {
        return getValue(selector, "span", "textContent");
    }
    
    public static String getDivText(String selector) {
        return getValue(selector, "div", "textContent");
    }
    
    /**
     * Conversion to numeric values
     */
    public static Integer getIntValue(String selector) {
        String value = getValue(selector);
        if (isValid(value)) {
            try {
                return Integer.parseInt(value.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    public static Double getDoubleValue(String selector) {
        String value = getValue(selector);
        if (isValid(value)) {
            try {
                return Double.parseDouble(value.trim());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    
    /**
     * Debug method: Show all available strategies
     */
    public static void debugAllStrategies(String selector) {
        log.info("=== Debug for Selector: {} ===", selector);
        log.info("Selenide Standard: {}", trySelenideStandard(selector));
        log.info("JavaScript Standard: {}", tryJavaScriptStandard(selector));
        log.info("Shadow DOM Open: {}", tryShadowDomOpen(selector, "input", "value"));
        log.info("Shadow DOM Closed: {}", tryShadowDomClosed(selector, "input", "value"));
        log.info("Event-based: {}", tryEventBased(selector));
        log.info("Deep Traversal: {}", tryDeepTraversal(selector, "value"));
        log.info("=======================================");
    }
    
    /**
     * Checks if value is valid
     */
    private static boolean isValid(String value) {
        return value != null && !value.trim().isEmpty();
    }
}