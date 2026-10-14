package com.carina.demo.utils;

import com.zebrunner.carina.utils.config.Configuration;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * =============================================================================
 * WaitUtils — Custom waiting utilities beyond Carina's built-ins
 * =============================================================================
 *
 * KEY LEARNING: WHEN TO WRITE CUSTOM UTILS
 * ------------------------------------------
 * Carina's ExtendedWebElement already handles most waits automatically.
 * Write custom utils ONLY for scenarios Carina doesn't cover, like:
 *   - Waiting for an AJAX spinner to disappear
 *   - Waiting for page JavaScript to finish executing
 *   - Custom polling logic with complex conditions
 *
 * CARINA'S BUILT-IN WAITS (don't reinvent these!):
 * --------------------------------------------------
 *   element.isElementPresent(seconds)   → waits for element to appear
 *   element.waitUntilVisible(seconds)   → waits for visibility
 *   element.waitUntilClickable(seconds) → waits until clickable
 *   element.waitUntilNotVisible(seconds)→ waits for element to hide
 *
 * =============================================================================
 */
public final class WaitUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitUtils.class);

    // Private constructor — utility class should not be instantiated
    private WaitUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Waits for the page to be fully loaded (document.readyState = "complete").
     * Useful after navigation to ensure all resources are loaded.
     *
     * @param driver         The WebDriver instance
     * @param timeoutSeconds Maximum seconds to wait
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        LOGGER.debug("Waiting for page to fully load (max {}s)...", timeoutSeconds);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until((ExpectedCondition<Boolean>) wd -> {
            String readyState = ((JavascriptExecutor) wd)
                .executeScript("return document.readyState").toString();
            return "complete".equals(readyState);
        });
        LOGGER.debug("Page fully loaded.");
    }

    /**
     * Waits for an AJAX loading spinner/overlay to disappear.
     * Common in modern SPAs (React, Angular, Vue.js applications).
     *
     * Usage: Call this after triggering an action that shows a loader.
     *
     * @param driver         WebDriver instance
     * @param spinnerCss     CSS selector for the loading spinner element
     * @param timeoutSeconds Maximum seconds to wait
     */
    public static void waitForSpinnerToDisappear(WebDriver driver, String spinnerCss, int timeoutSeconds) {
        LOGGER.debug("Waiting for spinner '{}' to disappear...", spinnerCss);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        wait.until((ExpectedCondition<Boolean>) wd -> {
            // Spinner is "gone" if no elements match the selector
            return wd.findElements(org.openqa.selenium.By.cssSelector(spinnerCss)).isEmpty();
        });
        LOGGER.debug("Spinner disappeared.");
    }

    /**
     * Scrolls an element into view using JavaScript.
     * Useful for elements below the fold that Selenium refuses to click.
     *
     * @param driver  WebDriver instance
     * @param element The element to scroll into view
     */
    public static void scrollToElement(WebDriver driver, org.openqa.selenium.WebElement element) {
        LOGGER.debug("Scrolling element into view: {}", element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    /**
     * Gets the configured explicit timeout from _config.properties.
     * Centralizes config reading so you don't hardcode timeouts in tests.
     *
     * @return Configured explicit_timeout value in seconds
     */
    public static int getExplicitTimeout() {
        // Reads from _config.properties: explicit_timeout=20
        return Configuration.get(Configuration.Parameter.EXPLICIT_TIMEOUT)
            .map(Integer::parseInt)
            .orElse(20);
    }
}
