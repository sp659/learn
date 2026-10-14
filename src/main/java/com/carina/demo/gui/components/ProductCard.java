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
 * ProductCard — UI Component representing a single product on the inventory page
 * =============================================================================
 *
 * KEY LEARNING: WHAT IS A UI COMPONENT?
 * ---------------------------------------
 * A Component (or "Page Component") is a reusable section of the UI that
 * appears multiple times on a page (like a product card in a grid).
 *
 * In Carina, components extend `AbstractUIObject` instead of `AbstractPage`.
 * The difference:
 *   AbstractPage   → Represents a full page (has a URL)
 *   AbstractUIObject → Represents a section/widget WITHIN a page (no URL)
 *
 * HOW IT WORKS WITH @FindBy:
 * ---------------------------
 * When a component is instantiated with a `rootElement`, all @FindBy locators
 * inside it are searched RELATIVE to that root element.
 * This means "find .btn within THIS product card", not across the whole page.
 *
 * USAGE IN ProductsPage:
 * -----------------------
 * List<ProductCard> cards = initElementList(productItems, ProductCard.class);
 * cards.get(0).addToCart();  // Add first product to cart
 *
 * =============================================================================
 */
public class ProductCard extends AbstractUIObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCard.class);

    // All @FindBy locators below are searched WITHIN the product card container
    // (not the entire page), because this class extends AbstractUIObject

    @FindBy(className = "inventory_item_name")
    private ExtendedWebElement productName;

    @FindBy(className = "inventory_item_desc")
    private ExtendedWebElement productDescription;

    @FindBy(className = "inventory_item_price")
    private ExtendedWebElement productPrice;

    @FindBy(css = "button.btn_inventory")
    private ExtendedWebElement addToCartButton;

    @FindBy(className = "item_pricebar")
    private ExtendedWebElement priceBar;

    // =========================================================================
    // CONSTRUCTOR — Required for AbstractUIObject
    // =========================================================================

    /**
     * @param driver        The WebDriver instance
     * @param searchContext The root element (the product card container element)
     */
    public ProductCard(WebDriver driver, SearchContext searchContext) {
        super(driver, searchContext);
    }

    // =========================================================================
    // COMPONENT ACTIONS
    // =========================================================================

    /**
     * Gets the product name from this card.
     *
     * @return Product name string
     */
    public String getName() {
        String name = productName.getText();
        LOGGER.debug("Product card name: {}", name);
        return name;
    }

    /**
     * Gets the product description from this card.
     *
     * @return Product description string
     */
    public String getDescription() {
        return productDescription.getText();
    }

    /**
     * Gets the displayed price as a string (e.g., "$29.99").
     *
     * @return Price string with currency symbol
     */
    public String getPriceText() {
        return productPrice.getText();
    }

    /**
     * Gets the price as a numeric double for calculations.
     * Strips the "$" symbol before parsing.
     *
     * @return Price as double
     */
    public double getPriceNumeric() {
        String priceText = getPriceText().replace("$", "").trim();
        return Double.parseDouble(priceText);
    }

    /**
     * Clicks "Add to cart" for this specific product card.
     */
    public void addToCart() {
        LOGGER.info("Adding '{}' to cart", getName());
        addToCartButton.click();
    }

    /**
     * Checks if the button currently shows "Add to cart" (not "Remove").
     *
     * @return true if the product is not yet in the cart
     */
    public boolean isAddToCartButtonVisible() {
        return addToCartButton.isElementPresent(3) &&
               addToCartButton.getText().equalsIgnoreCase("Add to cart");
    }
}
