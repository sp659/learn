package com.carina.demo.runners;

import io.cucumber.testng.CucumberOptions;
import com.zebrunner.carina.cucumber.CucumberRunner;

/**
 * =============================================================================
 * RegressionTestRunner — Runs ALL non-manual scenarios
 * =============================================================================
 *
 * This runner is used for the full regression cycle.
 * Typically run nightly via CI/CD pipeline.
 *
 * Command to run:
 *   mvn clean test -Dsuite=src/test/resources/testng_suites/regression.xml
 * =============================================================================
 */
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.carina.demo.steps",

    // Run all scenarios EXCEPT those tagged @manual or @wip
    tags = "not @manual and not @wip",

    plugin = {
        "pretty",
        "html:target/cucumber-reports/regression-report.html",
        "json:target/cucumber-reports/regression.json",
        "junit:target/cucumber-reports/regression-junit.xml"
    },

    monochrome = true
)
public class RegressionTestRunner extends CucumberRunner {
    /*
     * Intentionally empty — @CucumberOptions drives everything.
     *
     * PARALLEL EXECUTION:
     * -------------------
     * Parallel execution is controlled by the TestNG suite XML file.
     * Set parallel="methods" and thread-count in the XML to run scenarios
     * concurrently. Carina's ThreadLocal driver pool handles parallelism safely.
     */
}
