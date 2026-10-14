package com.carina.demo.utils;

/**
 * =============================================================================
 * TestDataConstants — Centralized test data constants
 * =============================================================================
 *
 * KEY LEARNING: WHY CENTRALIZE TEST DATA?
 * -----------------------------------------
 * Hard-coding credentials in feature files or step definitions is BAD practice:
 *
 * BAD ❌:  When I enter "standard_user" and "secret_sauce"  (in 50 feature files)
 * GOOD ✅: Constants class → feature file uses the constant → one place to update
 *
 * For SENSITIVE data (production passwords, API keys):
 * → Use environment variables or a secrets manager (e.g., AWS Secrets Manager)
 * → NEVER commit real passwords to version control!
 *
 * For this demo, SauceDemo credentials are public knowledge, so we use constants.
 *
 * =============================================================================
 */
public final class TestDataConstants {

    // Private constructor — constants class, not meant to be instantiated
    private TestDataConstants() {}

    // =========================================================================
    // SAUCEDEMO USER CREDENTIALS
    // These are the officially documented test accounts on saucedemo.com
    // =========================================================================

    /** Standard user — can log in and browse products normally */
    public static final String STANDARD_USER = "standard_user";

    /** Locked out user — login is blocked (used to test error handling) */
    public static final String LOCKED_OUT_USER = "locked_out_user";

    /** Problem user — some UI elements are broken (for negative testing) */
    public static final String PROBLEM_USER = "problem_user";

    /** Performance glitch user — simulates slow network (for performance testing) */
    public static final String PERFORMANCE_GLITCH_USER = "performance_glitch_user";

    /** The universal password for ALL SauceDemo test accounts */
    public static final String VALID_PASSWORD = "secret_sauce";

    /** An intentionally wrong password for negative test cases */
    public static final String INVALID_PASSWORD = "wrong_password_123";


    // =========================================================================
    // SAUCEDEMO PRODUCT NAMES
    // =========================================================================

    public static final String PRODUCT_BACKPACK     = "Sauce Labs Backpack";
    public static final String PRODUCT_BIKE_LIGHT   = "Sauce Labs Bike Light";
    public static final String PRODUCT_BOLT_SHIRT   = "Sauce Labs Bolt T-Shirt";
    public static final String PRODUCT_FLEECE_JACKET = "Sauce Labs Fleece Jacket";
    public static final String PRODUCT_ONESIE       = "Sauce Labs Onesie";
    public static final String PRODUCT_RED_SHIRT    = "Test.allTheThings() T-Shirt (Red)";


    // =========================================================================
    // CHECKOUT TEST DATA
    // =========================================================================

    public static final String CHECKOUT_FIRST_NAME  = "John";
    public static final String CHECKOUT_LAST_NAME   = "Doe";
    public static final String CHECKOUT_ZIP_CODE    = "10001";


    // =========================================================================
    // EXPECTED ERROR MESSAGES
    // =========================================================================

    public static final String ERROR_INVALID_CREDENTIALS =
        "Epic sadface: Username and password do not match any user in this service";

    public static final String ERROR_LOCKED_OUT =
        "Epic sadface: Sorry, this user has been locked out.";

    public static final String ERROR_USERNAME_REQUIRED =
        "Epic sadface: Username is required";

    public static final String ERROR_PASSWORD_REQUIRED =
        "Epic sadface: Password is required";
}
