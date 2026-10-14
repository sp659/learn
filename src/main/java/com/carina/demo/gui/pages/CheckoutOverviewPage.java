package com.carina.demo.gui.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * CheckoutOverviewPage — Order Summary Page
 * =============================================================================
 * Shows the order summary before final placement.
 * =============================================================================
 */
public class CheckoutOverviewPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutOverviewPage.class);

    @FindBy(className = "title")
    private ExtendedWebElement pageTitle;

    @FindBy(className = "summary_total_label")
    private ExtendedWebElement totalLabel;

    @FindBy(className = "summary_tax_label")
    private ExtendedWebElement taxLabel;

    @FindBy(id = "finish")
    private ExtendedWebElement finishButton;

    @FindBy(id = "cancel")
    private ExtendedWebElement cancelButton;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public CheckoutOverviewPage(WebDriver driver) {
        super(driver);
        setPageURL("/checkout-step-two.html");
    }

    // =========================================================================
    // PAGE VERIFICATION
    // =========================================================================

    @Override
    public boolean isPageOpened() {
        LOGGER.info("Verifying Checkout Overview page is open...");
        return pageTitle.isElementPresent(10) &&
               pageTitle.getText().equalsIgnoreCase("Checkout: Overview");
    }

    // =========================================================================
    // PAGE ACTIONS
    // =========================================================================

    /**
     * Gets the total price shown on the order summary.
     * Example return: "Total: $32.39"
     *
     * @return Total label text
     */
    public String getTotalPrice() {
        return totalLabel.getText();
    }

    /**
     * Clicks "Finish" to place the order.
     *
     * @return CheckoutCompletePage instance
     */
    public CheckoutCompletePage finishOrder() {
        LOGGER.info("Finishing the order...");
        finishButton.click();
        return new CheckoutCompletePage(getDriver());
    }

    public String getPageTitle() {
        return pageTitle.getText();
    }
}
