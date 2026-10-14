package com.carina.demo.gui.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * LoginPage — Page Object for the SauceDemo Login Page
 * =============================================================================
 *
 * WHAT IS A PAGE OBJECT?
 * -----------------------
 * A Page Object is a Design Pattern used in test automation.
 * Each web page (or significant section of a page) gets its own Java class.
 * The class contains:
 *   1. LOCATORS   → Identify the UI elements (@FindBy annotations)
 *   2. ACTIONS    → Methods that perform user interactions (click, type, etc.)
 *   3. ASSERTIONS → Methods that verify the page state (isLoaded, getTitle, etc.)
 *
 * WHY PAGE OBJECTS?
 * -----------------
 * Without Page Objects, if a locator changes, you'd update it in 50 test files.
 * With Page Objects, you update it in ONE place — much easier to maintain!
 *
 * HOW CARINA ENHANCES PAGE OBJECTS:
 * ----------------------------------
 * Carina wraps standard Selenium WebElements in its own `ExtendedWebElement`.
 * This gives you superpowers like:
 *   - Built-in explicit waits (no more Thread.sleep()!)
 *   - Auto-screenshots on failure
 *   - Logging of every action automatically
 *   - Retry logic for flaky elements
 *
 * @author  Senior Test Lead (Your Name)
 * @since   1.0.0
 * =============================================================================
 */
public class LoginPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);

    // =========================================================================
    // LOCATORS — @FindBy tells Selenium HOW to find the element on the page
    // =========================================================================
    // TIP: Prefer IDs > names > CSS selectors > XPath (in that order)
    //      IDs are fastest and most stable; XPath is slowest and most fragile.

    /** Username input field */
    @FindBy(id = "user-name")
    private ExtendedWebElement usernameInput;

    /** Password input field */
    @FindBy(id = "password")
    private ExtendedWebElement passwordInput;

    /** Login submit button */
    @FindBy(id = "login-button")
    private ExtendedWebElement loginButton;

    /** Error message container (shown on invalid login) */
    @FindBy(css = "[data-test='error']")
    private ExtendedWebElement errorMessage;

    /** The logo on the login page (used to verify page is loaded) */
    @FindBy(className = "login_logo")
    private ExtendedWebElement loginLogo;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    /**
     * Every Page Object in Carina must call super(driver).
     * This wires up Carina's element management and the @FindBy annotations.
     *
     * @param driver The active WebDriver instance (managed by Carina's IDriverPool)
     */
    public LoginPage(WebDriver driver) {
        super(driver);
        // The URL the open() method will navigate to (relative to config 'url')
        setPageURL("/");
    }

    // =========================================================================
    // PAGE VERIFICATION
    // =========================================================================
    /**
     * Checks if the login page is actually loaded and visible.
     * Carina's AbstractPage.isPageOpened() checks the page URL.
     * We add an extra element-visibility check for robustness.
     *
     * @return true if login page is fully loaded
     */
    @Override
    public boolean isPageOpened() {
        LOGGER.info("Verifying Login page is open...");
        // isElementPresent() waits up to 'explicit_timeout' seconds from config
        return loginLogo.isElementPresent(10);
    }

    // =========================================================================
    // PAGE ACTIONS — Methods that mimic real user interactions
    // =========================================================================

    /**
     * Types the username into the username field.
     * Carina's type() method:
     *   1. Waits for element to be visible
     *   2. Clears any existing text
     *   3. Types the new text
     *   4. Logs the action automatically
     *
     * @param username The username to enter
     */
    public void enterUsername(String username) {
        LOGGER.debug("Entering username: {}", username);
        usernameInput.type(username);
    }

    /**
     * Types the password into the password field.
     * Note: We mask the password in logs for security.
     *
     * @param password The password to enter
     */
    public void enterPassword(String password) {
        LOGGER.debug("Entering password: [MASKED]");
        passwordInput.type(password);
    }

    /**
     * Clicks the Login button.
     * Carina's click() waits for the element to be clickable first.
     */
    public void clickLoginButton() {
        LOGGER.info("Clicking Login button");
        loginButton.click();
    }

    /**
     * CONVENIENCE METHOD: Perform the complete login flow in one call.
     * This is a higher-level action that combines multiple steps.
     * Step Definitions will typically call this instead of individual methods.
     *
     * @param username The username credential
     * @param password The password credential
     * @return ProductsPage — the page we land on after successful login
     */
    public ProductsPage loginAs(String username, String password) {
        LOGGER.info("Performing login with username: {}", username);
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        // Return the next page (Carina's initPage creates a new page object)
        return new ProductsPage(getDriver());
    }

    // =========================================================================
    // GETTERS — Methods that read data FROM the page
    // =========================================================================

    /**
     * Returns the error message text displayed on login failure.
     *
     * @return The error message string, or empty string if not visible
     */
    public String getErrorMessage() {
        if (errorMessage.isElementPresent(5)) {
            return errorMessage.getText();
        }
        return "";
    }

    /**
     * Checks if an error message is currently visible on the page.
     *
     * @return true if error message is displayed
     */
    public boolean isErrorMessageDisplayed() {
        return errorMessage.isElementPresent(5);
    }
}
