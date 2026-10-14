# Carina Framework + Cucumber - Complete Learning Guide
## A Sample BDD Test Automation Project for Freshers

---

## Welcome, Fresher!

This project is your hands-on introduction to professional test automation using:
- **Zebrunner Carina Framework** - enterprise-grade test automation framework
- **Cucumber (BDD)** - write tests in plain English that everyone can read
- **Selenium WebDriver** - control real browsers programmatically
- **TestNG** - Java test framework for execution and parallelism
- **Maven** - build and dependency management tool

We are testing **SauceDemo** (https://www.saucedemo.com) - a publicly available
e-commerce demo app purpose-built for test automation practice.

---

## Project Architecture

```
carina-cucumber-demo/
|-- pom.xml                           Maven config (dependencies, plugins)
|-- src/
|   |-- main/
|   |   |-- java/com/carina/demo/
|   |   |   |-- gui/
|   |   |   |   |-- pages/            PAGE OBJECTS (one class per page)
|   |   |   |   |   |-- LoginPage.java
|   |   |   |   |   |-- ProductsPage.java
|   |   |   |   |   |-- CartPage.java
|   |   |   |   |   |-- CheckoutPage.java
|   |   |   |   |   |-- CheckoutOverviewPage.java
|   |   |   |   |   `-- CheckoutCompletePage.java
|   |   |   |   `-- components/       REUSABLE UI COMPONENTS
|   |   |   |       |-- ProductCard.java
|   |   |   |       `-- CartIcon.java
|   |   |   `-- utils/
|   |   |       |-- WaitUtils.java
|   |   |       `-- TestDataConstants.java
|   |   `-- resources/
|   |       |-- _config.properties    MASTER Carina config file
|   |       `-- logback.xml
|   `-- test/
|       |-- java/com/carina/demo/
|       |   |-- runners/
|       |   |   |-- SmokeTestRunner.java
|       |   |   `-- RegressionTestRunner.java
|       |   `-- steps/
|       |       |-- LoginSteps.java
|       |       |-- ShoppingCartSteps.java
|       |       `-- Hooks.java
|       `-- resources/
|           |-- features/
|           |   |-- login.feature
|           |   `-- shopping_cart.feature
|           `-- testng_suites/
|               |-- smoke.xml
|               `-- regression.xml
```

---

## Core Concepts Explained

### 1. _config.properties - The Control Centre

The `_config.properties` file in `src/main/resources/` is the brain of Carina.
It controls everything: browser type, timeouts, URLs, threads.

```properties
browser=chrome
url=https://www.saucedemo.com
explicit_timeout=20
thread_count=1
```

**Override from command line** (highest priority):
```bash
mvn clean test -Dbrowser=firefox -Dheadless_mode=true
```

---

### 2. Feature Files - Gherkin BDD Syntax

Feature files use **Given-When-Then** format:

```gherkin
@smoke @login
Scenario: Successful login with valid credentials
  Given I navigate to the SauceDemo login page
  When I enter username "standard_user" and password "secret_sauce"
  And I click the Login button
  Then I should be redirected to the Products page
```

Tags allow selective execution:
```bash
mvn clean test -Dsuite=smoke.xml -Dcucumber.filter.tags="@smoke"
```

---

### 3. Page Object Pattern - AbstractPage

Each web page = 1 Java class with locators, actions, and verifications.

```java
public class LoginPage extends AbstractPage {
    @FindBy(id = "user-name")
    private ExtendedWebElement usernameInput;

    public void enterUsername(String username) {
        usernameInput.type(username); // Carina handles waits automatically!
    }
}
```

**ExtendedWebElement advantages over regular WebElement:**

| Feature          | WebElement         | ExtendedWebElement    |
|------------------|--------------------|-----------------------|
| Auto wait        | Manual Thread.sleep| Built-in waits        |
| Auto screenshot  | No                 | On failure            |
| Auto logging     | No                 | Every action logged   |
| Retry on stale   | Manual try/catch   | Built-in retry        |

---

### 4. UI Components - AbstractUIObject

When the same UI block repeats on a page (product cards, table rows), use a Component.
All @FindBy locators are searched WITHIN the component root element.

```java
public class ProductCard extends AbstractUIObject {
    @FindBy(className = "inventory_item_name")
    private ExtendedWebElement name;

    public void addToCart() { addToCartButton.click(); }
}
```

---

### 5. Step Definitions - Linking Gherkin to Java

```java
public class LoginSteps implements IDriverPool {

    @When("I enter username {string} and password {string}")
    public void enterCredentials(String username, String password) {
        loginPage.enterUsername(username);
        loginPage.enterPassword(password);
    }
}
```

**Key rule**: `IDriverPool.getDriver()` returns the thread-safe WebDriver.
Always use this instead of storing WebDriver as a field!

---

### 6. Runner Classes - CucumberOptions

```java
@CucumberOptions(
    features = "src/test/resources/features",
    glue = "com.carina.demo.steps",
    tags = "@smoke",
    plugin = {"pretty", "html:target/report.html", "json:target/out.json"}
)
public class SmokeTestRunner extends CucumberRunner { }
```

---

### 7. Hooks - Lifecycle Management

```java
public class Hooks implements IDriverPool {

    @Before
    public void beforeScenario(Scenario scenario) {
        LOGGER.info("Starting: {}", scenario.getName());
    }

    @After
    public void afterScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            // Take screenshot and attach to report
        }
    }
}
```

---

## How to Run the Tests

### Prerequisites
- Java 11+ installed (`java -version`)
- Maven 3.6+ installed (`mvn -version`)
- Chrome browser installed
- Internet connection (SauceDemo is a live website)

> **No ChromeDriver download needed!**
> Carina uses WebDriverManager to auto-download the correct driver version.

---

### Run Commands

**Run Smoke Tests** (quick, approx 2 minutes):
```bash
mvn clean test -Dsuite=src/test/resources/testng_suites/smoke.xml
```

**Run Full Regression Suite:**
```bash
mvn clean test -Dsuite=src/test/resources/testng_suites/regression.xml
```

**Run in Headless Mode** (no browser window, great for CI):
```bash
mvn clean test -Dsuite=src/test/resources/testng_suites/smoke.xml -Dheadless_mode=true
```

**Run with Firefox:**
```bash
mvn clean test -Dsuite=src/test/resources/testng_suites/smoke.xml -Dbrowser=firefox
```

**Run specific tag only:**
```bash
mvn clean test -Dsuite=src/test/resources/testng_suites/regression.xml -Dcucumber.filter.tags="@e2e"
```

---

## Test Reports

After running, open these files in your browser:

| Report           | Location                                              |
|------------------|-------------------------------------------------------|
| Cucumber HTML    | target/cucumber-reports/smoke-report.html             |
| Masterthought    | target/cucumber-html-reports/overview-features.html   |
| JUnit XML        | target/cucumber-reports/smoke-junit.xml               |
| Application Logs | target/logs/carina-test.log                           |

---

## Test Data and Credentials

SauceDemo pre-configured test users:

| Username                | Password     | Behavior               |
|-------------------------|--------------|------------------------|
| standard_user           | secret_sauce | Works normally         |
| locked_out_user         | secret_sauce | Login is blocked       |
| problem_user            | secret_sauce | Some UI elements broken|
| performance_glitch_user | secret_sauce | Slow page loads        |

All credentials are in: `src/main/java/com/carina/demo/utils/TestDataConstants.java`

---

## Test Scenarios Overview

| Feature | Scenario                           | Tags                      |
|---------|------------------------------------|---------------------------|
| Login   | Valid credentials -> Products page | @smoke @login @positive   |
| Login   | Invalid credentials -> Error shown | @regression @login @negative |
| Login   | Empty username -> Validation error | @regression @login @negative |
| Login   | Multiple user types (data-driven)  | @regression @data-driven  |
| Login   | Logout flows back to Login page    | @smoke @logout            |
| Cart    | Add single product to cart         | @smoke @cart              |
| Cart    | Add multiple products              | @regression @cart         |
| Cart    | Cart persists correct items        | @regression @cart         |
| Cart    | Continue shopping navigation       | @regression @cart         |
| E2E     | Complete purchase flow             | @smoke @e2e @purchase     |

---

## Troubleshooting Common Issues

**No such element exceptions:**
- Cause: Element locator is wrong OR page did not load
- Fix: Increase `explicit_timeout` in `_config.properties`

**StaleElementReferenceException:**
- Cause: DOM updated between finding and using an element
- Fix: Carina's ExtendedWebElement handles this automatically.

**ChromeDriver version mismatch:**
- Cause: Chrome browser version does not match cached ChromeDriver
- Fix: Delete `~/.wdm/` folder. WebDriverManager downloads the correct version.

**Tests pass locally but fail in CI:**
- Cause: Often a timing or headless issue
- Fix: Set `headless_mode=true` in CI configuration and increase timeouts.

**Cucumber step not found (Undefined step):**
- Cause: The glue path does not match the step definition package
- Fix: Verify `glue = "com.carina.demo.steps"` matches the actual package.

---

## Learning Path for Freshers

Follow this order to understand the project:

1. Read `_config.properties` - understand what is configurable
2. Read `login.feature` - understand Gherkin syntax
3. Read `LoginPage.java` - understand AbstractPage pattern
4. Read `LoginSteps.java` - understand how Gherkin maps to Java
5. Read `Hooks.java` - understand lifecycle management
6. Read `SmokeTestRunner.java` - understand how it all comes together
7. **RUN IT!** Execute the smoke suite and watch it in action
8. Read `ProductsPage.java` and `ProductCard.java` - learn component pattern

---

## Key Carina Concepts Cheat Sheet

```
IDriverPool          -> Interface providing getDriver() (always use this!)
AbstractPage         -> Base class for page objects (full pages)
AbstractUIObject     -> Base class for page components (sections of a page)
ExtendedWebElement   -> Enhanced web element with built-in waits and logging
CucumberRunner       -> Carina base class for Cucumber TestNG integration
_config.properties   -> Master config file read at startup
```

---

*Built by your Senior Test Lead*
*Questions? Talk to your mentor before Googling!*
#   l e a r n  
 