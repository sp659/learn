package com.carina.demo.steps;

import com.carina.demo.gui.pages.*;
import com.zebrunner.carina.webdriver.IDriverPool;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.api.Assertions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * =============================================================================
 * ShoppingCartSteps — Cucumber Step Definitions for Shopping Cart Feature
 * =============================================================================
 *
 * KEY LEARNING: MULTIPLE STEP DEFINITION CLASSES
 * ------------------------------------------------
 * You should split step definitions by FEATURE AREA (or "domain"):
 *   - LoginSteps      → All login/logout related steps
 *   - ShoppingCartSteps → All cart, products, checkout related steps
 *
 * WHY SPLIT THEM?
 * ----------------
 * 1. Easier to find and maintain
 * 2. Follows Single Responsibility Principle
 * 3. Smaller files are less intimidating for new team members
 *
 * CUCUMBER DISCOVERS ALL STEP DEFS AUTOMATICALLY:
 * -------------------------------------------------
 * As long as all step definition classes are in the `glue` path
 * configured in the @CucumberOptions (the runner class), Cucumber
 * will find them all. Steps from LoginSteps and ShoppingCartSteps
 * can be mixed freely in any .feature file.
 *
 * =============================================================================
 */
public class ShoppingCartSteps implements IDriverPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShoppingCartSteps.class);

    // =========================================================================
    // STATE — Page objects shared between step methods within a scenario
    // =========================================================================
    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutPage checkoutPage;
    private CheckoutOverviewPage overviewPage;
    private CheckoutCompletePage completePage;

    // =========================================================================
    // PRODUCTS PAGE STEPS
    // =========================================================================

    /**
     * WHEN step: Adds a named product to the cart from the Products page.
     *
     * Matches feature step:
     *   When I add "Sauce Labs Backpack" to the cart
     */
    @When("I add {string} to the cart")
    public void iAddProductToCart(String productName) {
        LOGGER.info("STEP: Adding '{}' to the cart", productName);
        productsPage = new ProductsPage(getDriver());
        boolean added = productsPage.addProductToCartByName(productName);
        Assertions.assertThat(added)
            .as("Product '%s' should be found and added to cart", productName)
            .isTrue();
    }

    /**
     * THEN step: Verifies the cart badge shows the correct count.
     *
     * Matches feature steps:
     *   Then the cart icon should show "1" item
     *   Then the cart icon should show "2" items
     *
     * NOTE: The step pattern handles both "item" and "items" naturally.
     */
    @Then("the cart icon should show {string} item(s)")
    public void theCartIconShouldShowItems(String expectedCount) {
        LOGGER.info("STEP: Verifying cart badge shows {} item(s)", expectedCount);
        productsPage = new ProductsPage(getDriver());
        int actualCount = productsPage.getCartItemCount();
        Assertions.assertThat(actualCount)
            .as("Cart badge item count mismatch!")
            .isEqualTo(Integer.parseInt(expectedCount));
    }

    /**
     * THEN step: Verifies a product is in the cart (checks from Products page badge).
     *
     * Matches feature step:
     *   And the product "Sauce Labs Backpack" should be in the cart
     */
    @Then("the product {string} should be in the cart")
    public void theProductShouldBeInTheCart(String productName) {
        LOGGER.info("STEP: Verifying '{}' is in the cart", productName);
        // Navigate to cart to check
        productsPage = new ProductsPage(getDriver());
        cartPage = productsPage.goToCart();
        boolean found = cartPage.isProductInCart(productName);
        Assertions.assertThat(found)
            .as("Product '%s' should be in the cart", productName)
            .isTrue();
        // Navigate back to Products page for subsequent steps
        productsPage = cartPage.continueShopping();
    }

    // =========================================================================
    // CART PAGE STEPS
    // =========================================================================

    /**
     * AND step: Navigates to the Cart page.
     *
     * Matches feature step:
     *   And I navigate to the Cart page
     */
    @And("I navigate to the Cart page")
    public void iNavigateToTheCartPage() {
        LOGGER.info("STEP: Navigating to Cart page");
        productsPage = new ProductsPage(getDriver());
        cartPage = productsPage.goToCart();
        Assertions.assertThat(cartPage.isPageOpened())
            .as("Cart page should be open")
            .isTrue();
    }

    /**
     * THEN step: Verifies the cart contains the expected number of items.
     *
     * Matches feature step:
     *   Then the cart should contain "2" items
     */
    @Then("the cart should contain {string} items")
    public void theCartShouldContainItems(String expectedCount) {
        LOGGER.info("STEP: Verifying cart contains {} items", expectedCount);
        int actualCount = cartPage.getCartItemCount();
        Assertions.assertThat(actualCount)
            .as("Cart item count should be %s but was %s", expectedCount, actualCount)
            .isEqualTo(Integer.parseInt(expectedCount));
    }

    /**
     * THEN step: Checks for a specific product in the cart.
     *
     * Matches feature step:
     *   Then I should see "Sauce Labs Fleece Jacket" in the cart
     */
    @Then("I should see {string} in the cart")
    public void iShouldSeeProductInCart(String productName) {
        LOGGER.info("STEP: Verifying '{}' is visible in the cart", productName);
        cartPage = new CartPage(getDriver());
        boolean found = cartPage.isProductInCart(productName);
        Assertions.assertThat(found)
            .as("Product '%s' should be visible in the cart", productName)
            .isTrue();
    }

    /**
     * AND step: Clicks "Continue Shopping" on the Cart page.
     *
     * Matches feature step:
     *   And I click "Continue Shopping"
     */
    @And("I click {string}")
    public void iClickButton(String buttonText) {
        LOGGER.info("STEP: Clicking button '{}'", buttonText);
        if ("Continue Shopping".equalsIgnoreCase(buttonText)) {
            cartPage = new CartPage(getDriver());
            productsPage = cartPage.continueShopping();
        }
        // Can extend this to handle other button names
    }

    /**
     * THEN step: Verifies we're on the Products page.
     *
     * Matches feature step:
     *   Then I should be on the Products page
     */
    @Then("I should be on the Products page")
    public void iShouldBeOnTheProductsPage() {
        LOGGER.info("STEP: Verifying we're on the Products page");
        productsPage = new ProductsPage(getDriver());
        Assertions.assertThat(productsPage.isPageOpened())
            .as("Should be on the Products page")
            .isTrue();
    }

    // =========================================================================
    // CHECKOUT STEPS
    // =========================================================================

    /**
     * AND step: Clicks checkout button on the Cart page.
     *
     * Matches feature step:
     *   And I proceed to checkout
     */
    @And("I proceed to checkout")
    public void iProceedToCheckout() {
        LOGGER.info("STEP: Proceeding to checkout");
        cartPage = new CartPage(getDriver());
        checkoutPage = cartPage.proceedToCheckout();
        Assertions.assertThat(checkoutPage.isPageOpened())
            .as("Checkout page should be open")
            .isTrue();
    }

    /**
     * AND step: Fills in checkout shipping information.
     *
     * Matches feature step:
     *   And I fill in shipping details with first name "John", last name "Doe", and zip code "10001"
     */
    @And("I fill in shipping details with first name {string}, last name {string}, and zip code {string}")
    public void iFillInShippingDetails(String firstName, String lastName, String zipCode) {
        LOGGER.info("STEP: Filling checkout info — {} {}, ZIP: {}", firstName, lastName, zipCode);
        checkoutPage = new CheckoutPage(getDriver());
        overviewPage = checkoutPage.fillCheckoutInfo(firstName, lastName, zipCode);
        Assertions.assertThat(overviewPage.isPageOpened())
            .as("Checkout Overview page should be open after filling info")
            .isTrue();
    }

    /**
     * AND step: Confirms the order overview (just verifies the page is shown).
     *
     * Matches feature step:
     *   And I confirm the order overview
     */
    @And("I confirm the order overview")
    public void iConfirmTheOrderOverview() {
        LOGGER.info("STEP: Confirming order overview");
        overviewPage = new CheckoutOverviewPage(getDriver());
        Assertions.assertThat(overviewPage.isPageOpened())
            .as("Checkout Overview page should be visible")
            .isTrue();
        LOGGER.info("Order overview page confirmed. Total: {}", overviewPage.getTotalPrice());
    }

    /**
     * AND step: Clicks the "Finish" button to complete the purchase.
     *
     * Matches feature step:
     *   And I finish the order
     */
    @And("I finish the order")
    public void iFinishTheOrder() {
        LOGGER.info("STEP: Finishing the order");
        overviewPage = new CheckoutOverviewPage(getDriver());
        completePage = overviewPage.finishOrder();
        Assertions.assertThat(completePage.isPageOpened())
            .as("Checkout Complete page should be open after finishing order")
            .isTrue();
    }

    /**
     * THEN step: Verifies the order confirmation message.
     *
     * Matches feature step:
     *   Then the order confirmation message should contain "Thank you for your order"
     */
    @Then("the order confirmation message should contain {string}")
    public void theOrderConfirmationMessageShouldContain(String expectedMessage) {
        LOGGER.info("STEP: Verifying confirmation message contains '{}'", expectedMessage);
        completePage = new CheckoutCompletePage(getDriver());
        String actualMessage = completePage.getConfirmationMessage();
        Assertions.assertThat(actualMessage)
            .as("Confirmation message should contain expected text")
            .containsIgnoringCase(expectedMessage);
        LOGGER.info("Order successfully confirmed! Message: '{}'", actualMessage);
    }
}
