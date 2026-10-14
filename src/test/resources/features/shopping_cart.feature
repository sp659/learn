# =============================================================================
# Feature: SauceDemo Shopping Cart Functionality
# =============================================================================
#
# KEY LEARNING: SCENARIO CONTEXT (Sharing State Between Steps)
# -------------------------------------------------------------
# In Cucumber, each step method in a step definition class can share state
# through INSTANCE VARIABLES in the step definition class.
#
# For example, after clicking "Add to Cart", we store the product name,
# then later assert it's in the cart. The step def class holds this state.
#
# Also notice how steps from DIFFERENT step definition files can be used
# together in the same scenario. Cucumber auto-discovers and combines them!
# (e.g., login steps + product steps can appear in the same scenario)
# =============================================================================

Feature: Shopping Cart Management
  As a logged-in user
  I want to manage items in my shopping cart
  So that I can purchase the products I want

  Background:
    Given I navigate to the SauceDemo login page
    And I login as "standard_user" with password "secret_sauce"

  # ---------------------------------------------------------------------------
  # SCENARIO 1: Adding a product to cart
  # ---------------------------------------------------------------------------
  @smoke @cart @positive
  Scenario: Add a single product to the cart
    When I add "Sauce Labs Backpack" to the cart
    Then the cart icon should show "1" item
    And the product "Sauce Labs Backpack" should be in the cart

  # ---------------------------------------------------------------------------
  # SCENARIO 2: Adding multiple products
  # ---------------------------------------------------------------------------
  @regression @cart @positive
  Scenario: Add multiple products to the cart
    When I add "Sauce Labs Backpack" to the cart
    And I add "Sauce Labs Bike Light" to the cart
    Then the cart icon should show "2" items
    When I navigate to the Cart page
    Then the cart should contain "2" items

  # ---------------------------------------------------------------------------
  # SCENARIO 3: Cart persists the correct items
  # ---------------------------------------------------------------------------
  @regression @cart @positive
  Scenario: Cart contains the correct items after adding
    When I add "Sauce Labs Fleece Jacket" to the cart
    And I navigate to the Cart page
    Then I should see "Sauce Labs Fleece Jacket" in the cart

  # ---------------------------------------------------------------------------
  # SCENARIO 4: Continue shopping from cart page
  # ---------------------------------------------------------------------------
  @regression @cart
  Scenario: Continue shopping from cart page returns to products
    When I add "Sauce Labs Backpack" to the cart
    And I navigate to the Cart page
    And I click "Continue Shopping"
    Then I should be on the Products page

  # ---------------------------------------------------------------------------
  # SCENARIO 5: Complete end-to-end purchase flow
  # This is a full E2E scenario touching login → products → cart → checkout
  # ---------------------------------------------------------------------------
  @smoke @e2e @purchase
  Scenario: Complete a full purchase flow end-to-end
    When I add "Sauce Labs Backpack" to the cart
    And I navigate to the Cart page
    And I proceed to checkout
    And I fill in shipping details with first name "John", last name "Doe", and zip code "10001"
    And I confirm the order overview
    And I finish the order
    Then the order confirmation message should contain "Thank you for your order"
