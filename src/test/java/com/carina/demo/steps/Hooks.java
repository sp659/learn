package com.carina.demo.steps;

import com.zebrunner.carina.webdriver.IDriverPool;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * Hooks — Cucumber lifecycle hooks for setup and teardown
 * =============================================================================
 *
 * WHAT ARE HOOKS?
 * ----------------
 * Hooks are methods that Cucumber calls automatically at specific points
 * in the test lifecycle. They are NOT tied to any specific Gherkin step.
 *
 * Common hooks:
 *   @Before    → Runs BEFORE each scenario
 *   @After     → Runs AFTER each scenario
 *   @BeforeStep → Runs before each STEP within a scenario
 *   @AfterStep  → Runs after each STEP within a scenario
 *
 * ORDER ATTRIBUTE:
 *   @Before(order = 10) → Lower number runs first
 *   Multiple @Before hooks can exist; order controls execution sequence.
 *
 * TAGS ON HOOKS:
 *   @Before("@mobile") → Only runs before scenarios tagged @mobile
 *   @After("@cleanup") → Only runs after scenarios tagged @cleanup
 *
 * IMPORTANT:
 *   In Carina + Cucumber, you typically DON'T need to start/stop the browser
 *   in hooks. Carina does this automatically via its TestNG integration.
 *   These hooks are for LOGGING and SCREENSHOT CAPTURE.
 *
 * =============================================================================
 */
public class Hooks implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(Hooks.class);

    /**
     * Runs BEFORE every scenario.
     * Use this for:
     *   - Logging the scenario name for easy log navigation
     *   - Setting up test data
     *   - Any pre-conditions that aren't suitable for Background steps
     *
     * @param scenario The Cucumber Scenario object (gives access to name, tags, etc.)
     */
    @Before(order = 1)
    public void beforeScenario(Scenario scenario) {
        LOGGER.info("╔══════════════════════════════════════════════════════════════╗");
        LOGGER.info("║  SCENARIO STARTING: {}", scenario.getName());
        LOGGER.info("║  Tags: {}", scenario.getSourceTagNames());
        LOGGER.info("║  ID: {}", scenario.getId());
        LOGGER.info("╚══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Runs AFTER every scenario.
     * Use this for:
     *   - Taking a screenshot if the scenario FAILED
     *   - Cleaning up test data
     *   - Logging scenario result
     *
     * @param scenario The Cucumber Scenario object
     */
    @After(order = 1)
    public void afterScenario(Scenario scenario) {
        // Log the scenario result clearly
        if (scenario.isFailed()) {
            LOGGER.error("╔══════════════════════════════════════════════════════════════╗");
            LOGGER.error("║  ❌ SCENARIO FAILED: {}", scenario.getName());
            LOGGER.error("╚══════════════════════════════════════════════════════════════╝");

            // Take a screenshot on failure and attach it to the Cucumber report
            try {
                // Carina's IDriverPool gives us access to the driver even in hooks
                if (isDriverRegistered()) {
                    byte[] screenshot = org.openqa.selenium.OutputType.BYTES.convertFromPngBytes(
                        ((org.openqa.selenium.TakesScreenshot) getDriver())
                            .getScreenshotAs(org.openqa.selenium.OutputType.BYTES)
                    );
                    // Attach screenshot to Cucumber HTML report
                    scenario.attach(screenshot, "image/png", "Screenshot on Failure");
                    LOGGER.info("Screenshot captured and attached to report");
                }
            } catch (Exception e) {
                LOGGER.warn("Could not capture failure screenshot: {}", e.getMessage());
            }
        } else {
            LOGGER.info("╔══════════════════════════════════════════════════════════════╗");
            LOGGER.info("║  ✅ SCENARIO PASSED: {}", scenario.getName());
            LOGGER.info("╚══════════════════════════════════════════════════════════════╝");
        }
    }

    /**
     * Runs before every individual STEP.
     * Generally used for detailed tracing/debugging.
     * Enable only when debugging — noisy in normal runs.
     */
    // Uncomment below for step-level tracing during debugging:
    // @BeforeStep
    // public void beforeStep(Scenario scenario) {
    //     LOGGER.debug("  → Executing step in: {}", scenario.getName());
    // }

    /**
     * Runs after every individual STEP.
     * Can be used to capture step-level screenshots for detailed reporting.
     */
    // Uncomment for step-level screenshots (very verbose!):
    // @AfterStep
    // public void afterStep(Scenario scenario) {
    //     LOGGER.debug("  ← Step completed. Status so far: {}", scenario.getStatus());
    // }
}
