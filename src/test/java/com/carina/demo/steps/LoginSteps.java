package com.carina.demo.steps;

import com.carina.demo.gui.pages.LoginPage;
import com.carina.demo.gui.pages.ProductsPage;
import com.zebrunner.carina.webdriver.IDriverPool;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * LoginSteps — Cucumber Step Definitions for Login Feature
 * =============================================================================
 *
 * WHAT ARE STEP DEFINITIONS?
 * ---------------------------
 * Step definitions are Java methods that are "glued" to Gherkin steps.
 * When Cucumber runs a .feature file, it reads each step (Given/When/Then),
 * finds the matching Java method via a regex or string pattern,
 * and executes that method.
 *
 * ANNOTATIONS:
 *   @Given("pattern") → matches "Given <pattern>" steps
 *   @When("pattern")  → matches "When <pattern>" steps
 *   @Then("pattern")  → matches "Then <pattern>" steps
 *   @And("pattern")   → matches "And <pattern>" steps (same as any above)
 *
 * PARAMETER CAPTURE: {string}
 *   Cucumber Expression {string} captures quoted strings from feature files.
 *   Example: step "I enter username "standard_user""
 *            method: public void enterCredentials(String username) ...
 *
 * HOW CARINA PROVIDES WebDriver:
 * --------------------------------
 * Notice we implement `IDriverPool` — this is a Carina interface.
 * It gives us getDriver() which returns the WebDriver instance for
 * the current thread. This is THREAD-SAFE for parallel test execution.
 *
 * We do NOT use @Autowired, Spring DI, or any DI framework.
 * Carina manages the driver pool internally via ThreadLocal.
 *
 * STATE SHARING BETWEEN STEPS:
 * ------------------------------
 * Instance variables (like `loginPage`, `productsPage`) hold state
 * between step methods within the SAME scenario execution.
 * Cucumber creates a new instance of this class per scenario.
 *
 * =============================================================================
 */
public class LoginSteps implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSteps.class);

    // =========================================================================
    // STATE — Shared between step methods within a scenario
    // =========================================================================
    private LoginPage loginPage;
    private ProductsPage productsPage;

    // =========================================================================
    // STEP DEFINITIONS
    // =========================================================================

    /**
     * GIVEN step: Navigates to the SauceDemo login page.
     *
     * Matches feature step:
     *   Given I navigate to the SauceDemo login page
     */
    @Given("I navigate to the SauceDemo login page")
    public void iNavigateToTheSauceDemoLoginPage() {
        LOGGER.info("STEP: Navigating to SauceDemo login page");
        // Create a new LoginPage object — Carina's PageFactory initializes all @FindBy elements
        loginPage = new LoginPage(getDriver());
        // open() navigates to the URL configured in _config.properties + page URL
        loginPage.open();
        // Assertion: verify we actually landed on the login page
        Assertions.assertThat(loginPage.isPageOpened())
            .as("Login page should be open after navigation")
            .isTrue();
        LOGGER.info("Login page opened successfully");
    }

    /**
     * WHEN step: Types credentials into the login form.
     *
     * Matches feature steps:
     *   When I enter username "standard_user" and password "secret_sauce"
     *   When I enter username "wrong_user" and password "wrong_password"
     *   When I enter username "" and password "secret_sauce"
     *
     * {string} captures quoted values → they become the method parameters
     */
    @When("I enter username {string} and password {string}")
    public void iEnterUsernameAndPassword(String username, String password) {
        LOGGER.info("STEP: Entering credentials — username: [{}]", username);
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }

    /**
     * AND step: Clicks the Login button.
     *
     * Matches feature step:
     *   And I click the Login button
     *
     * NOTE: Step definitions don't care if the step says "And" or "When" or "Then"
     * in the feature file. The annotation decides which step text is matched.
     */
    @And("I click the Login button")
    public void iClickTheLoginButton() {
        LOGGER.info("STEP: Clicking the Login button");
        loginPage.clickLoginButton();
    }

    /**
     * THEN step: Asserts we're on the Products page after login.
     *
     * Matches feature step:
     *   Then I should be redirected to the Products page
     */
    @Then("I should be redirected to the Products page")
    public void iShouldBeRedirectedToTheProductsPage() {
        LOGGER.info("STEP: Verifying redirect to Products page");
        productsPage = new ProductsPage(getDriver());
        Assertions.assertThat(productsPage.isPageOpened())
            .as("Should be redirected to Products page after successful login")
            .isTrue();
        LOGGER.info("Successfully verified Products page is open");
    }

    /**
     * THEN step: Asserts the page title text.
     *
     * Matches feature step:
     *   And the page title should be "Products"
     */
    @Then("the page title should be {string}")
    public void thePageTitleShouldBe(String expectedTitle) {
        LOGGER.info("STEP: Verifying page title is '{}'", expectedTitle);
        productsPage = new ProductsPage(getDriver());
        String actualTitle = productsPage.getPageTitle();
        Assertions.assertThat(actualTitle)
            .as("Page title mismatch!")
            .isEqualToIgnoringCase(expectedTitle);
    }

    /**
     * THEN step: Asserts an error message is displayed.
     *
     * Matches feature steps:
     *   Then I should see an error message "Epic sadface: ..."
     */
    @Then("I should see an error message {string}")
    public void iShouldSeeAnErrorMessage(String expectedError) {
        LOGGER.info("STEP: Verifying error message is '{}'", expectedError);
        Assertions.assertThat(loginPage.isErrorMessageDisplayed())
            .as("Error message should be displayed")
            .isTrue();
        String actualError = loginPage.getErrorMessage();
        Assertions.assertThat(actualError)
            .as("Error message text mismatch!")
            .contains(expectedError);
    }

    /**
     * GIVEN step: Combined login shortcut (used in cart feature's Background).
     *
     * Matches feature step:
     *   And I login as "standard_user" with password "secret_sauce"
     *
     * NOTE: This step is shared across feature files.
     * Cucumber's glue path includes ALL step definition classes,
     * so steps from LoginSteps can be used in shopping_cart.feature too!
     */
    @And("I login as {string} with password {string}")
    public void iLoginAs(String username, String password) {
        LOGGER.info("STEP: Logging in as '{}'", username);
        loginPage = new LoginPage(getDriver());
        productsPage = loginPage.loginAs(username, password);
        Assertions.assertThat(productsPage.isPageOpened())
            .as("Should be on Products page after login")
            .isTrue();
    }

    /**
     * WHEN step: Performs logout from the Products page.
     *
     * Matches feature step:
     *   When I logout from the application
     */
    @When("I logout from the application")
    public void iLogoutFromTheApplication() {
        LOGGER.info("STEP: Logging out from the application");
        productsPage = new ProductsPage(getDriver());
        loginPage = productsPage.logout();
    }

    /**
     * THEN step: Verifies we're back on the Login page.
     *
     * Matches feature step:
     *   Then I should be redirected back to the Login page
     */
    @Then("I should be redirected back to the Login page")
    public void iShouldBeRedirectedBackToTheLoginPage() {
        LOGGER.info("STEP: Verifying redirect back to Login page");
        Assertions.assertThat(loginPage.isPageOpened())
            .as("Should be redirected to Login page after logout")
            .isTrue();
    }
}
