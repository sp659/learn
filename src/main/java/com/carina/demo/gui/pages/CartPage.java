package com.carina.demo.gui.pages;

import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * =============================================================================
 * CartPage — Page Object for the SauceDemo Shopping Cart Page
 * =============================================================================
 *
 * KEY LEARNING: RETURNING CORRECT PAGE OBJECTS
 * ----------------------------------------------
 * Notice how methods that cause navigation RETURN the new page object.
 * This makes step definitions read like natural user flows:
 *
 *   CartPage cart = productsPage.goToCart();
 *   CheckoutPage checkout = cart.proceedToCheckout();
 *
 * This is called the "Fluent Page Object" pattern.
 * =============================================================================
 */
public class CartPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(CartPage.class);

    // =========================================================================
    // LOCATORS
    // =========================================================================

    /** Page title (should show "Your Cart") */
    @FindBy(className = "title")
    private ExtendedWebElement pageTitle;

    /** All cart item rows */
    @FindBy(className = "cart_item")
    private List<ExtendedWebElement> cartItems;

    /** Each item's name within the cart */
    @FindBy(className = "inventory_item_name")
    private List<ExtendedWebElement> itemNames;

    /** Each item's price within the cart */
    @FindBy(className = "inventory_item_price")
    private List<ExtendedWebElement> itemPrices;

    /** "Continue Shopping" button — goes back to products */
    @FindBy(id = "continue-shopping")
    private ExtendedWebElement continueShoppingButton;

    /** "Checkout" button — proceeds to checkout */
    @FindBy(id = "checkout")
    private ExtendedWebElement checkoutButton;

    /** Remove button for the first item (for demonstration) */
    @FindBy(css = ".cart_item .btn_secondary")
    private ExtendedWebElement removeFirstItemButton;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public CartPage(WebDriver driver) {
        super(driver);
        setPageURL("/cart.html");
    }

    // =========================================================================
    // PAGE VERIFICATION
    // =========================================================================

    @Override
    public boolean isPageOpened() {
        LOGGER.info("Verifying Cart page is open...");
        return pageTitle.isElementPresent(10) &&
               pageTitle.getText().equalsIgnoreCase("Your Cart");
    }

    // =========================================================================
    // PAGE ACTIONS
    // =========================================================================

    /**
     * Returns the number of items in the cart.
     *
     * @return Cart item count
     */
    public int getCartItemCount() {
        int count = cartItems.size();
        LOGGER.debug("Cart has {} items", count);
        return count;
    }

    /**
     * Checks if a specific product is in the cart by name.
     *
     * @param productName Product name to look for
     * @return true if product found in cart
     */
    public boolean isProductInCart(String productName) {
        return itemNames.stream()
            .anyMatch(el -> el.getText().equalsIgnoreCase(productName));
    }

    /**
     * Proceeds to the checkout page.
     *
     * @return CheckoutPage instance
     */
    public CheckoutPage proceedToCheckout() {
        LOGGER.info("Proceeding to Checkout");
        checkoutButton.click();
        return new CheckoutPage(getDriver());
    }

    /**
     * Goes back to the Products page.
     *
     * @return ProductsPage instance
     */
    public ProductsPage continueShopping() {
        LOGGER.info("Continuing shopping — going back to Products page");
        continueShoppingButton.click();
        return new ProductsPage(getDriver());
    }

    /**
     * Gets the page title text.
     *
     * @return Title string
     */
    public String getPageTitle() {
        return pageTitle.getText();
    }
}
