package tutorials.shadowdom;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.executeJavaScript;

/**
 * Extension to ShadowDomUtils for interactions (click, type, etc.)
 */
public class ShadowDomInteractionUtils {
    
    // ========================================
    // CLICK OPERATIONS
    // ========================================
    
    /**
     * Clicks element using all available strategies
     */
    public static boolean click(String selector) {
        return click(selector, "*");
    }
    
    /**
     * Clicks element with specific child selector
     */
    public static boolean click(String hostSelector, String childSelector) {
        // Quick existence check to avoid slow Selenide timeout
        try {
            Boolean exists = (Boolean) executeJavaScript(
                "return document.querySelector(arguments[0]) !== null", hostSelector);
            if (!Boolean.TRUE.equals(exists)) return false;
        } catch (Exception e) {
            return false;
        }

        // Strategy 1: Shadow DOM click (open)
        if (tryShadowDomClick(hostSelector, childSelector)) return true;

        // Strategy 2: Shadow DOM click (closed)
        if (tryClosedShadowDomClick(hostSelector, childSelector)) return true;

        // Strategy 3: JavaScript click (reliable event triggering)
        if (tryJavaScriptClick(hostSelector)) return true;

        // Strategy 4: Standard Selenide click
        if (trySelenideClick(hostSelector)) return true;

        // Strategy 5: Event dispatch
        if (tryEventDispatchClick(hostSelector)) return true;

        return false;
    }
    
    private static boolean trySelenideClick(String selector) {
        try {
            $(selector).click();
            return true;
        } catch (Exception | AssertionError e) {
            return false;
        }
    }
    
    private static boolean tryJavaScriptClick(String selector) {
        try {
            String script = String.format("document.querySelector('%s').click();", selector);
            executeJavaScript(script);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean tryShadowDomClick(String hostSelector, String childSelector) {
        try {
            String script = String.format("""
                var host = document.querySelector('%s');
                if (!host || !host.shadowRoot) return false;
                
                var child = host.shadowRoot.querySelector('%s');
                if (child) {
                    child.click();
                    return true;
                }
                return false;
                """, hostSelector, childSelector);
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean tryClosedShadowDomClick(String hostSelector, String childSelector) {
        try {
            String script = String.format("""
                var host = document.querySelector('%s');
                if (!host) return false;
                
                // Search internal properties for closed shadow DOM
                var keys = Object.getOwnPropertyNames(host);
                for (var key of keys) {
                    try {
                        var prop = host[key];
                        if (prop && typeof prop === 'object' && prop.querySelector) {
                            var child = prop.querySelector('%s');
                            if (child) {
                                child.click();
                                return true;
                            }
                        }
                    } catch(e) {}
                }
                return false;
                """, hostSelector, childSelector);
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean tryEventDispatchClick(String selector) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return false;
                
                var event = new MouseEvent('click', {
                    bubbles: true,
                    cancelable: true,
                    view: window
                });
                elem.dispatchEvent(event);
                return true;
                """, selector);
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ========================================
    // INPUT OPERATIONS
    // ========================================
    
    /**
     * Types text into element using all available strategies
     */
    public static boolean type(String selector, String text) {
        return type(selector, "input", text);
    }
    
    /**
     * Types text with specific child selector
     */
    public static boolean type(String hostSelector, String childSelector, String text) {
        // Quick existence check to avoid slow Selenide timeout
        try {
            Boolean exists = (Boolean) executeJavaScript(
                "return document.querySelector(arguments[0]) !== null", hostSelector);
            if (!Boolean.TRUE.equals(exists)) return false;
        } catch (Exception e) {
            return false;
        }

        // Strategy 1: Shadow DOM type (open)
        if (tryShadowDomType(hostSelector, childSelector, text)) return true;

        // Strategy 2: Shadow DOM type (closed)
        if (tryClosedShadowDomType(hostSelector, childSelector, text)) return true;

        // Strategy 3: JavaScript setValue (reliable event dispatching)
        if (tryJavaScriptType(hostSelector, text)) return true;

        // Strategy 4: Standard Selenide
        if (trySelenideType(hostSelector, text)) return true;

        // Strategy 5: Event-based typing
        if (tryEventBasedType(hostSelector, text)) return true;

        return false;
    }
    
    private static boolean trySelenideType(String selector, String text) {
        try {
            $(selector).clear();
            $(selector).type(text);
            return true;
        } catch (Exception | AssertionError e) {
            return false;
        }
    }
    
    private static boolean tryJavaScriptType(String selector, String text) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return false;
                elem.value = '%s';
                elem.dispatchEvent(new Event('input', {bubbles: true}));
                elem.dispatchEvent(new Event('change', {bubbles: true}));
                return true;
                """, selector, text.replace("'", "\\'"));
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean tryShadowDomType(String hostSelector, String childSelector, String text) {
        try {
            String script = String.format("""
                var host = document.querySelector('%s');
                if (!host || !host.shadowRoot) return false;
                
                var child = host.shadowRoot.querySelector('%s');
                if (child) {
                    child.value = '%s';
                    child.dispatchEvent(new Event('input', {bubbles: true}));
                    child.dispatchEvent(new Event('change', {bubbles: true}));
                    return true;
                }
                return false;
                """, hostSelector, childSelector, text.replace("'", "\\'"));
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean tryClosedShadowDomType(String hostSelector, String childSelector, String text) {
        try {
            String script = String.format("""
                var host = document.querySelector('%s');
                if (!host) return false;
                
                var keys = Object.getOwnPropertyNames(host);
                for (var key of keys) {
                    try {
                        var prop = host[key];
                        if (prop && typeof prop === 'object' && prop.querySelector) {
                            var child = prop.querySelector('%s');
                            if (child) {
                                child.value = '%s';
                                child.dispatchEvent(new Event('input', {bubbles: true}));
                                child.dispatchEvent(new Event('change', {bubbles: true}));
                                return true;
                            }
                        }
                    } catch(e) {}
                }
                return false;
                """, hostSelector, childSelector, text.replace("'", "\\'"));
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean tryEventBasedType(String selector, String text) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return false;
                
                elem.focus();
                elem.value = '%s';
                
                // Dispatch various events
                ['input', 'keyup', 'keydown', 'change'].forEach(eventType => {
                    var event = new Event(eventType, {bubbles: true, cancelable: true});
                    elem.dispatchEvent(event);
                });
                
                elem.blur();
                return true;
                """, selector, text.replace("'", "\\'"));
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ========================================
    // CLEAR OPERATIONS
    // ========================================
    
    /**
     * Clears input field content
     */
    public static boolean clear(String selector) {
        return clear(selector, "input");
    }
    
    public static boolean clear(String hostSelector, String childSelector) {
        // Strategy 1: JavaScript clear (most reliable for JS-set values)
        try {
            Boolean result = (Boolean) executeJavaScript(
                "var elem = document.querySelector(arguments[0]);" +
                "if (!elem) return false;" +
                "elem.value = '';" +
                "elem.dispatchEvent(new Event('input', {bubbles: true}));" +
                "elem.dispatchEvent(new Event('change', {bubbles: true}));" +
                "return true;", hostSelector);
            if (Boolean.TRUE.equals(result)) return true;
        } catch (Exception e) {
            // fall through
        }

        // Strategy 2: Selenide clear
        try {
            $(hostSelector).clear();
            return true;
        } catch (Exception | AssertionError e) {
            // Strategy 3: type empty string (handles shadow DOM)
            return type(hostSelector, childSelector, "");
        }
    }
    
    // ========================================
    // SELECT OPERATIONS
    // ========================================
    
    /**
     * Selects option by text in dropdown
     */
    public static boolean selectByText(String selector, String text) {
        try {
            String script = String.format("""
                var select = document.querySelector('%s');
                if (!select) return false;
                
                for (var option of select.options) {
                    if (option.textContent.trim() === '%s') {
                        select.value = option.value;
                        select.dispatchEvent(new Event('change', {bubbles: true}));
                        return true;
                    }
                }
                return false;
                """, selector, text.replace("'", "\\'"));
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Selects option by value in dropdown
     */
    public static boolean selectByValue(String selector, String value) {
        try {
            String script = String.format("""
                var select = document.querySelector('%s');
                if (!select) return false;

                select.value = '%s';
                if (select.value !== '%s') return false;
                select.dispatchEvent(new Event('change', {bubbles: true}));
                return true;
                """, selector, value.replace("'", "\\'"), value.replace("'", "\\'"));

            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ========================================
    // CHECKBOX/RADIO OPERATIONS
    // ========================================
    
    /**
     * Checks or unchecks checkbox/radio button
     */
    public static boolean setChecked(String selector, boolean checked) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return false;
                
                elem.checked = %s;
                elem.dispatchEvent(new Event('change', {bubbles: true}));
                return true;
                """, selector, checked);
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ========================================
    // HOVER OPERATIONS
    // ========================================
    
    /**
     * Hovers over element
     */
    public static boolean hover(String selector) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return false;
                
                var event = new MouseEvent('mouseover', {
                    bubbles: true,
                    cancelable: true,
                    view: window
                });
                elem.dispatchEvent(event);
                return true;
                """, selector);
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ========================================
    // FOCUS OPERATIONS
    // ========================================
    
    /**
     * Sets focus on element
     */
    public static boolean focus(String selector) {
        try {
            String script = String.format("document.querySelector('%s').focus(); return true;", selector);
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Removes focus from element
     */
    public static boolean blur(String selector) {
        try {
            String script = String.format("document.querySelector('%s').blur(); return true;", selector);
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    // ========================================
    // UTILITY METHODS
    // ========================================
    
    /**
     * Scrolls element into view
     */
    public static boolean scrollIntoView(String selector) {
        try {
            String script = String.format("""
                var elem = document.querySelector('%s');
                if (!elem) return false;
                elem.scrollIntoView({behavior: 'smooth', block: 'center'});
                return true;
                """, selector);
            
            Boolean result = (Boolean) executeJavaScript(script);
            return Boolean.TRUE.equals(result);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Waits for element to be clickable and then clicks
     */
    public static boolean waitAndClick(String selector, int timeoutSeconds) {
        for (int i = 0; i < timeoutSeconds; i++) {
            if (click(selector)) {
                return true;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}