package com.carina.demo.runners;

import io.cucumber.testng.CucumberOptions;
import com.zebrunner.carina.cucumber.CucumberRunner;

/**
 * =============================================================================
 * SmokeTestRunner — Cucumber TestNG Runner for SMOKE tests
 * =============================================================================
 *
 * WHAT IS A RUNNER CLASS?
 * ------------------------
 * A Runner class is the "entry point" for Cucumber test execution.
 * It bridges Cucumber (BDD framework) with TestNG (Java test framework).
 *
 * Carina provides `CucumberRunner` as the base class.
 * This handles:
 *   - WebDriver lifecycle (start/stop browser per scenario)
 *   - Carina configuration injection
 *   - Zebrunner reporting hooks
 *   - Parallel execution thread management
 *
 * @CucumberOptions EXPLAINED:
 * ---------------------------
 *
 *   features: Path to your .feature files (relative to project root or classpath)
 *
 *   glue:     Package(s) where Cucumber should look for:
 *               - Step Definition classes
 *               - Hook classes (@Before, @After)
 *             Multiple packages can be listed (comma-separated)
 *
 *   tags:     Cucumber Tag Expression to filter which scenarios run.
 *             Examples:
 *               "@smoke"                    → only smoke-tagged scenarios
 *               "@smoke and @login"         → both tags required
 *               "@regression and not @slow" → regression, excluding slow tests
 *               "@smoke or @sanity"         → either tag
 *
 *   plugin:   Report output formatters
 *               "pretty"       → Colorful console output
 *               "html:..."     → HTML report file
 *               "json:..."     → JSON report (used by reporting tools)
 *               "junit:..."    → JUnit XML (used by CI/CD systems like Jenkins)
 *
 *   publish:  Publishes results to Cucumber Reports (cloud service)
 *
 *   monochrome: true → removes color codes from console output (cleaner in CI logs)
 *
 * HOW TO USE MULTIPLE RUNNERS:
 * -----------------------------
 * Create separate runner classes for each test category:
 *   SmokeTestRunner.java     → @tags = "@smoke"
 *   RegressionTestRunner.java → @tags = "@regression"
 *   FullSuiteRunner.java     → @tags = "not @manual"
 *
 * Select which runner to use in your TestNG XML suite file.
 *
 * =============================================================================
 */
@CucumberOptions(
    // -------------------------------------------------------------------------
    // FEATURE FILES: Where Cucumber looks for .feature files
    // Using classpath: prefix is recommended for JAR-based deployments
    // -------------------------------------------------------------------------
    features = "src/test/resources/features",

    // -------------------------------------------------------------------------
    // GLUE: Where Cucumber looks for step definitions and hooks
    // -------------------------------------------------------------------------
    glue = "com.carina.demo.steps",

    // -------------------------------------------------------------------------
    // TAGS: Only run scenarios tagged with @smoke
    // -------------------------------------------------------------------------
    tags = "@smoke",

    // -------------------------------------------------------------------------
    // PLUGINS: Report generation
    // -------------------------------------------------------------------------
    plugin = {
        "pretty",                                            // Console output
        "html:target/cucumber-reports/smoke-report.html",   // HTML report
        "json:target/cucumber-reports/smoke.json",          // JSON for Masterthought
        "junit:target/cucumber-reports/smoke-junit.xml"     // JUnit XML for Jenkins
    },

    // -------------------------------------------------------------------------
    // MONOCHROME: Clean console output (no color escape codes)
    // -------------------------------------------------------------------------
    monochrome = true
)
public class SmokeTestRunner extends CucumberRunner {
    /*
     * This class intentionally left empty.
     * All configuration is done via @CucumberOptions above.
     * CucumberRunner (Carina's base class) handles test execution.
     *
     * When TestNG runs this class, it:
     *   1. Reads the @CucumberOptions
     *   2. Scans 'features' path for .feature files
     *   3. Filters by 'tags'
     *   4. For each matching scenario:
     *      a. Starts a browser (via Carina's driver pool)
     *      b. Runs @Before hooks
     *      c. Executes each step (matching via 'glue' package)
     *      d. Runs @After hooks
     *      e. Closes the browser
     *   5. Generates reports via 'plugin' formatters
     */
}
