package com.carina.demo.gui.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * CheckoutPage — Page Object for the SauceDemo Checkout Information Page
 * =============================================================================
 * This page collects shipping information (first name, last name, zip code).
 * =============================================================================
 */
public class CheckoutPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutPage.class);

    // =========================================================================
    // LOCATORS
    // =========================================================================

    @FindBy(className = "title")
    private ExtendedWebElement pageTitle;

    @FindBy(id = "first-name")
    private ExtendedWebElement firstNameInput;

    @FindBy(id = "last-name")
    private ExtendedWebElement lastNameInput;

    @FindBy(id = "postal-code")
    private ExtendedWebElement postalCodeInput;

    @FindBy(id = "continue")
    private ExtendedWebElement continueButton;

    @FindBy(id = "cancel")
    private ExtendedWebElement cancelButton;

    @FindBy(css = "[data-test='error']")
    private ExtendedWebElement errorMessage;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public CheckoutPage(WebDriver driver) {
        super(driver);
        setPageURL("/checkout-step-one.html");
    }

    // =========================================================================
    // PAGE VERIFICATION
    // =========================================================================

    @Override
    public boolean isPageOpened() {
        LOGGER.info("Verifying Checkout Information page is open...");
        return pageTitle.isElementPresent(10) &&
               pageTitle.getText().equalsIgnoreCase("Checkout: Your Information");
    }

    // =========================================================================
    // PAGE ACTIONS
    // =========================================================================

    /**
     * Fills in checkout information and proceeds to order overview.
     *
     * @param firstName  Customer first name
     * @param lastName   Customer last name
     * @param postalCode Postal / ZIP code
     * @return CheckoutOverviewPage instance
     */
    public CheckoutOverviewPage fillCheckoutInfo(String firstName, String lastName, String postalCode) {
        LOGGER.info("Filling checkout info: {} {}, ZIP: {}", firstName, lastName, postalCode);
        firstNameInput.type(firstName);
        lastNameInput.type(lastName);
        postalCodeInput.type(postalCode);
        continueButton.click();
        return new CheckoutOverviewPage(getDriver());
    }

    /**
     * Gets the error message if checkout validation fails.
     *
     * @return Error message text, or empty string if none
     */
    public String getErrorMessage() {
        if (errorMessage.isElementPresent(5)) {
            return errorMessage.getText();
        }
        return "";
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }
}
