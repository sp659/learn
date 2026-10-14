# =============================================================================
# Feature: SauceDemo Login Functionality
# =============================================================================
#
# WHAT IS A FEATURE FILE?
# -----------------------
# Feature files are written in Gherkin — a plain-English language designed so
# that non-technical stakeholders (business analysts, product owners, QA leads)
# can READ and WRITE test scenarios.
#
# The format follows the Given-When-Then structure:
#   GIVEN  → Pre-conditions (the starting state)
#   WHEN   → The action the user performs
#   THEN   → The expected outcome / assertion
#   AND    → Continuation of Given/When/Then (same keyword, just readable)
#   BUT    → Negative continuation
#
# TAGS (@smoke, @regression, @login):
#   Tags allow you to selectively run subsets of tests.
#   Run only smoke tests: mvn test -Dcucumber.filter.tags="@smoke"
#   Skip slow tests:      mvn test -Dcucumber.filter.tags="not @slow"
#
# BACKGROUND:
#   Steps that run BEFORE EVERY scenario in this feature file.
#   Used for common setup like navigating to the starting page.
#
# =============================================================================

Feature: SauceDemo User Login
  As a registered user
  I want to be able to log into SauceDemo
  So that I can browse and purchase products

  # Background runs before EVERY scenario below
  Background:
    Given I navigate to the SauceDemo login page

  # ---------------------------------------------------------------------------
  # SCENARIO 1: Happy path — valid credentials
  # ---------------------------------------------------------------------------
  @smoke @login @positive
  Scenario: Successful login with valid credentials
    When I enter username "standard_user" and password "secret_sauce"
    And I click the Login button
    Then I should be redirected to the Products page
    And the page title should be "Products"

  # ---------------------------------------------------------------------------
  # SCENARIO 2: Invalid credentials — should show error message
  # ---------------------------------------------------------------------------
  @regression @login @negative
  Scenario: Login with invalid credentials shows error message
    When I enter username "wrong_user" and password "wrong_password"
    And I click the Login button
    Then I should see an error message "Epic sadface: Username and password do not match"

  # ---------------------------------------------------------------------------
  # SCENARIO 3: Empty username — form validation
  # ---------------------------------------------------------------------------
  @regression @login @negative
  Scenario: Login with empty username shows validation error
    When I enter username "" and password "secret_sauce"
    And I click the Login button
    Then I should see an error message "Epic sadface: Username is required"

  # ---------------------------------------------------------------------------
  # SCENARIO 4: Scenario Outline — runs the same test with multiple data sets
  # This is a powerful BDD feature for data-driven testing!
  # The <placeholders> are filled from the Examples table below.
  # ---------------------------------------------------------------------------
  @regression @login @data-driven
  Scenario Outline: Login with different user roles
    When I enter username "<username>" and password "<password>"
    And I click the Login button
    Then I should be redirected to the Products page

    Examples:
      # Each row in the table = one test execution
      | username                | password     |
      | standard_user           | secret_sauce |
      | performance_glitch_user | secret_sauce |

  # ---------------------------------------------------------------------------
  # SCENARIO 5: Locked out user
  # ---------------------------------------------------------------------------
  @regression @login @negative
  Scenario: Locked out user cannot login
    When I enter username "locked_out_user" and password "secret_sauce"
    And I click the Login button
    Then I should see an error message "Epic sadface: Sorry, this user has been locked out"

  # ---------------------------------------------------------------------------
  # SCENARIO 6: Successful logout after login
  # ---------------------------------------------------------------------------
  @smoke @login @logout
  Scenario: User can successfully logout
    When I enter username "standard_user" and password "secret_sauce"
    And I click the Login button
    Then I should be redirected to the Products page
    When I logout from the application
    Then I should be redirected back to the Login page
