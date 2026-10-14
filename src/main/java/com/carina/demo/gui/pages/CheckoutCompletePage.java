package com.carina.demo.gui.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * CheckoutCompletePage — Order Confirmation Page
 * =============================================================================
 * The final page after a successful order placement.
 * Shows "Thank you for your order!" message.
 * =============================================================================
 */
public class CheckoutCompletePage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutCompletePage.class);

    @FindBy(className = "complete-header")
    private ExtendedWebElement confirmationHeader;

    @FindBy(className = "complete-text")
    private ExtendedWebElement confirmationText;

    @FindBy(id = "back-to-products")
    private ExtendedWebElement backToProductsButton;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public CheckoutCompletePage(WebDriver driver) {
        super(driver);
        setPageURL("/checkout-complete.html");
    }

    // =========================================================================
    // PAGE VERIFICATION
    // =========================================================================

    @Override
    public boolean isPageOpened() {
        LOGGER.info("Verifying Checkout Complete page is open...");
        return confirmationHeader.isElementPresent(10);
    }

    // =========================================================================
    // PAGE ACTIONS
    // =========================================================================

    /**
     * Gets the order confirmation header text.
     * Expected: "Thank you for your order!"
     *
     * @return Confirmation header text
     */
    public String getConfirmationMessage() {
        return confirmationHeader.getText();
    }

    /**
     * Gets the confirmation sub-text.
     *
     * @return Sub-text below the header
     */
    public String getConfirmationText() {
        return confirmationText.getText();
    }

    /**
     * Checks if the order was successfully placed.
     * We verify by checking for the confirmation message.
     *
     * @return true if order confirmation is visible
     */
    public boolean isOrderSuccessful() {
        return confirmationHeader.isElementPresent(10);
    }

    /**
     * Goes back to the Products page to start shopping again.
     *
     * @return ProductsPage instance
     */
    public ProductsPage backToProducts() {
        LOGGER.info("Going back to Products page");
        backToProductsButton.click();
        return new ProductsPage(getDriver());
    }
}
