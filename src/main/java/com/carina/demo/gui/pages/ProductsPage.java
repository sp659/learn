package com.carina.demo.gui.pages;

import com.carina.demo.gui.components.CartIcon;
import com.carina.demo.gui.components.ProductCard;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.gui.AbstractPage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

/**
 * =============================================================================
 * ProductsPage — Page Object for the SauceDemo Products Inventory Page
 * =============================================================================
 *
 * KEY LEARNING: COMPONENT-BASED PAGE OBJECTS
 * -------------------------------------------
 * Notice how this page uses `ProductCard` components.
 * Instead of duplicating locators for every product, we extract
 * repeating sections into reusable "Component" classes.
 *
 * This follows the Composite Pattern:
 *   ProductsPage  →  contains many →  ProductCard (component)
 *
 * Each ProductCard manages its own elements (name, price, button).
 * The page only manages the "container" that holds all cards.
 *
 * =============================================================================
 */
public class ProductsPage extends AbstractPage {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductsPage.class);

    // =========================================================================
    // LOCATORS
    // =========================================================================

    /** Page title text element (shows "Products") */
    @FindBy(className = "title")
    private ExtendedWebElement pageTitle;

    /** All product item containers on the page */
    @FindBy(className = "inventory_item")
    private List<ExtendedWebElement> productItems;

    /** Shopping cart icon in the header */
    @FindBy(id = "shopping_cart_container")
    private ExtendedWebElement shoppingCartContainer;

    /** Cart badge showing item count */
    @FindBy(className = "shopping_cart_badge")
    private ExtendedWebElement cartBadge;

    /** Sort dropdown */
    @FindBy(className = "product_sort_container")
    private ExtendedWebElement sortDropdown;

    /** The hamburger menu button */
    @FindBy(id = "react-burger-menu-btn")
    private ExtendedWebElement burgerMenuButton;

    /** Logout link (inside the hamburger menu) */
    @FindBy(id = "logout_sidebar_link")
    private ExtendedWebElement logoutLink;

    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================

    public ProductsPage(WebDriver driver) {
        super(driver);
        setPageURL("/inventory.html");
    }

    // =========================================================================
    // PAGE VERIFICATION
    // =========================================================================

    @Override
    public boolean isPageOpened() {
        LOGGER.info("Verifying Products page is open...");
        return pageTitle.isElementPresent(10) &&
               pageTitle.getText().equalsIgnoreCase("Products");
    }

    // =========================================================================
    // PAGE ACTIONS
    // =========================================================================

    /**
     * Gets the count of products currently displayed on the page.
     *
     * @return Number of product cards visible
     */
    public int getProductCount() {
        int count = productItems.size();
        LOGGER.debug("Found {} products on the page", count);
        return count;
    }

    /**
     * Returns the page title text.
     * Useful for asserting we're on the right page.
     *
     * @return Page title string
     */
    public String getPageTitle() {
        return pageTitle.getText();
    }

    /**
     * Adds a product to the cart by its display name.
     * This demonstrates finding a specific item within a list.
     *
     * @param productName The exact product name as shown on the page
     * @return true if product was found and added, false otherwise
     */
    public boolean addProductToCartByName(String productName) {
        LOGGER.info("Adding product '{}' to cart", productName);

        // Find the "Add to cart" button next to the specific product name
        // We use XPath to navigate from the product name to its sibling button
        ExtendedWebElement addButton = findExtendedWebElement(
            org.openqa.selenium.By.xpath(
                "//div[contains(@class,'inventory_item_name') and text()='" + productName + "']" +
                "/ancestor::div[@class='inventory_item_description']" +
                "//button[contains(@class,'btn_inventory')]"
            )
        );

        if (addButton.isElementPresent(5)) {
            addButton.click();
            LOGGER.info("Successfully added '{}' to cart", productName);
            return true;
        }

        LOGGER.warn("Product '{}' not found on the page", productName);
        return false;
    }

    /**
     * Gets the current cart item count from the badge.
     *
     * @return Cart item count, or 0 if cart is empty (no badge shown)
     */
    public int getCartItemCount() {
        if (cartBadge.isElementPresent(3)) {
            int count = Integer.parseInt(cartBadge.getText().trim());
            LOGGER.debug("Cart badge shows {} items", count);
            return count;
        }
        LOGGER.debug("Cart badge not visible — cart is empty");
        return 0;
    }

    /**
     * Navigates to the Shopping Cart page.
     *
     * @return CartPage instance
     */
    public CartPage goToCart() {
        LOGGER.info("Navigating to Cart page");
        shoppingCartContainer.click();
        return new CartPage(getDriver());
    }

    /**
     * Performs logout via the hamburger menu.
     *
     * @return LoginPage — the page after logout
     */
    public LoginPage logout() {
        LOGGER.info("Logging out...");
        burgerMenuButton.click();
        // Wait for menu animation to complete
        logoutLink.waitUntilVisible(5);
        logoutLink.click();
        return new LoginPage(getDriver());
    }

    /**
     * Retrieves all product names currently displayed.
     * Useful for verifying sort order or product presence.
     *
     * @return List of product name strings
     */
    public List<String> getAllProductNames() {
        List<ExtendedWebElement> nameElements = findExtendedWebElements(
            org.openqa.selenium.By.className("inventory_item_name")
        );
        return nameElements.stream()
            .map(ExtendedWebElement::getText)
            .collect(Collectors.toList());
    }
}
