package com.carina.demo.gui.components;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractUIObject;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * CartIcon — UI Component for the Shopping Cart Icon in the header
 * =============================================================================
 * This component represents the cart icon that appears on every page header.
 * By making it a component, we can reuse it across all page objects.
 * =============================================================================
 */
public class CartIcon extends AbstractUIObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartIcon.class);

    @FindBy(className = "shopping_cart_link")
    private ExtendedWebElement cartLink;

    @FindBy(className = "shopping_cart_badge")
    private ExtendedWebElement itemCountBadge;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public CartIcon(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    // =========================================================================
    // COMPONENT ACTIONS
    // =========================================================================

    /**
     * Clicks the cart icon to navigate to the cart page.
     */
    public void click() {
        LOGGER.info("Clicking cart icon");
        cartLink.click();
    }

    /**
     * Gets the number shown on the cart badge.
     *
     * @return Item count, or 0 if cart is empty (no badge visible)
     */
    public int getItemCount() {
        if (itemCountBadge.isElementPresent(3)) {
            return Integer.parseInt(itemCountBadge.getText().trim());
        }
        return 0;
    }

    /**
     * Checks if the cart is empty (no badge number visible).
     *
     * @return true if cart is empty
     */
    public boolean isEmpty() {
        return !itemCountBadge.isElementPresent(2);
    }
}
