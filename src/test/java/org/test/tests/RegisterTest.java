package org.test.tests;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.test.base.BaseTest;
import org.test.pages.Homepage;
import org.test.pages.RegisterPage;
import org.test.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(TestListener.class)
public class RegisterTest extends BaseTest {
    private RegisterPage register;
    private Homepage homepage;

    private static final String SITE = "https://www.pizzahut.com.ph/";
    private static final String REGISTER = SITE + "register";

    @BeforeTest(groups = {"regression"})
    public void initPage() {
        register = new RegisterPage(driver);
        homepage = new Homepage(driver);
    }

    // Verify Registration Page is Accessible
    public void redirectToRegisterPage() {
        // load homepage
        driver.get(SITE);

        // click on register
        homepage.clickRegisterPageButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Register')]")
        ));
    }

    // load register page
    public void loadRegisterPage() {
        // load register page
        driver.get(REGISTER);

        // wait until its actually loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Register')]")
        ));
    }

    public void clickRegisterButton() {
        // Scroll to bottom of page, press Register
        register.scrollToRegisterButton();

        // click register button
        register.clickRegisterButton();
    }

    // Verify Registration Page is Accessible
    @Test(groups = {"smoke", "regression", "e2e"})
    public void RSTC001_verifyRegisterPageAccessibility() {
        // Access Register Page
        redirectToRegisterPage();

        // assert URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, REGISTER);

        // Check if header is actually Register
        Assert.assertTrue(register.isHeaderDisplayed(), "User is not redirected to Register Page.");
    }

    // Verify that clicking the Back button redirects the user back to the previous Pizza Hut webpage
    @Test(groups = {"regression"})
    public void RSTC002_backButtonInRegisterRedirectsToHomepage() {
        // Access Register Page
        redirectToRegisterPage();

        // assert URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, REGISTER);

        // Check if header is actually Register
        Assert.assertTrue(register.isHeaderDisplayed());

        // press back
        register.clickBackButton();
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("address-autocomplete")
        ));

        // check if correct link
        Assert.assertEquals(driver.getCurrentUrl(), SITE, "Assertion Failed: Not redirected to Homepage.");

        // check if address input is there, since its part of the homepage
        Assert.assertTrue(check.isDisplayed(), "Assertion Failed: Not redirected to Homepage.");
    }

    // Verify that clicking the Pizza Hut logo redirects the user to the homepage
    @Test(groups = {"regression"})
    public void RSTC003_logoInRegisterRedirectsToHomepage() {
        // Access Register Page
        redirectToRegisterPage();

        // assert URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, REGISTER);

        // Check if header is actually Register
        Assert.assertTrue(register.isHeaderDisplayed());

        // press pizza hut logo
        register.clickPizzaHutLogo();
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("address-autocomplete")
        ));

        // check if correct link
        Assert.assertEquals(driver.getCurrentUrl(), SITE, "Assertion Failed: Not redirected to Homepage.");

        // check if address input is there, since its part of the homepage
        Assert.assertTrue(check.isDisplayed(), "Assertion Failed: Not redirected to Homepage.");
    }

    // Verify that First Name field rejects non-letter input
    @Test(groups = {"regression"})
    public void RSTC004_shouldRejectFirstNameWithNonLetters() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing numbers or special characters in the First Name field
        register.enterFirstName("Jane123!");

        // 2. Press the Register button
        // Scroll to bottom of page, press Register
        clickRegisterButton();

        // Go back to see input
        register.scrollToFirstName();

        // 3. Verify that the First Name field rejects non-letter inputs.
        Assert.assertFalse(register.isFirstNameValid(), "System should reject First Name with Non-Letters.");
    }
//
//    // Verify that First Name field rejects no input
//    @Test(groups = {"regression"})
//    public void shouldRejectFirstNameWithNoInput() {
//        // Pre-Requisite: User is on the Registration Page
//        loadRegisterPage();
//
//        // 1. Do not enter anything
//        register.enterFirstName("");
//
//        // 2. Press the Register button
//        // Scroll to bottom of page, press Register
//        clickRegisterButton();
//
//        // Go back to see input
//        register.scrollToFirstName();
//
//        // 3. Verify that the First Name field rejects no inputs.
//        Assert.assertFalse(register.isFirstNameValid(), "System should reject Blank First Name inputs.");
//    }

    // Verify that First Name field accepts a valid name
    @Test(groups = {"regression"})
    public void RSTC005_shouldAcceptValidFirstName() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters only in the First Name field
        register.enterFirstName("Jane");

        // 2. Press the Register button
        // Scroll to bottom of page, press Register
        clickRegisterButton();

        // Go back to see input
        register.scrollToFirstName();

        // 3. Verify that First Name field accepts a valid name.
        Assert.assertTrue(register.isFirstNameValid(), "System should accept valid first name.");
    }

//    // Verify that Last Name field rejects no input
//    @Test(groups = {"regression"})
//    public void shouldRejectLastNameWithNoInput() {
//        // Pre-Requisite: User is on the Registration Page
//        loadRegisterPage();
//
//        // 1. Enter no value
//        register.enterLastName("");
//
//        // 2. Press the Register button
//        // Scroll to bottom of page, press Register
//        clickRegisterButton();
//
//        // Go back to Last Name to see input
//        register.scrollToLastName();
//
//        // 3. Verify that Last Name field rejects no input.
//        Assert.assertFalse(register.isLastNameValid(), "System should reject last name with no input.");
//    }

    // Verify that Last Name field rejects non-letter input
    @Test(groups = {"regression"})
    public void RSTC006_shouldRejectLastNameWithNonLetters() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter input w/ non-letter value
        register.enterLastName("Doe123!");

        // 2. Press the Register button
        // Scroll to bottom of page, press Register
        clickRegisterButton();

        // Go back to see input
        register.scrollToLastName();

        // 3. Verify that Last Name field rejects non-letter input.
        Assert.assertFalse(register.isLastNameValid(), "System should reject last name with non-letters.");
    }

    // Verify that Last Name field accepts a valid name
    @Test(groups = {"regression"})
    public void RSTC007_shouldAcceptValidLastName() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter no value
        register.enterLastName("Doe");

        // 2. Press the Register button
        // Scroll to bottom of page, press Register
        clickRegisterButton();

        // Go back to see input
        register.scrollToLastName();

        // 3. Verify that Last Name field accepts a valid name.
        Assert.assertTrue(register.isLastNameValid(), "System should accept valid last name.");
    }

    // Verify that Gender field is optional
    @Test(groups = {"regression"})
    public void RSTC008_validateGenderIsOptional() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Select "Please select gender" from Gender field
        // actually, no need to do anything since "Please select gender" is by default chosen
        // so, just click Register Btn

        // 2. Press the Register button
        clickRegisterButton();

        // go back to see input
        register.scrollToGender();

        // 3. Verify that Gender field is valid even without an input.
        Assert.assertTrue(register.isGenderValid(), "Gender should be valid even with no input.");
    }

    // Verify that user can select from the Gender field dropdown
    @Test(groups = {"regression"})
    public void RSTC009_validateMaleGenderInput() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. 1. Click on the Gender field
        // 2. Choose Male from dropdown
        register.selectGender("Male");

        // 3. Press the Register button
        clickRegisterButton();

        // go back to see input
        register.scrollToGender();

        // 4. Verify that user can select from the Gender field dropdown.
        Assert.assertTrue(register.isGenderValid(), "System should accept gender input regardless of choice.");
    }

    // Verify that user can select from the Gender field dropdown
    @Test(groups = {"regression"})
    public void RSTC009_validateFemaleGenderInput() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. 1. Click on the Gender field
        // 2. Choose Male from dropdown
        register.selectGender("Female");

        // 3. Press the Register button
        clickRegisterButton();

        // go back to see input
        register.scrollToGender();

        // Verify that user can select from the Gender field dropdown.
        Assert.assertTrue(register.isGenderValid(), "System should accept gender input regardless of choice.");
    }

    // Verify that Email Address field rejects an invalid email address
    @Test(groups = {"regression"})
    public void RSTC010_shouldRejectInvalidEmailAddress() {
        // Pre-Requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter an invalid email address in the Email Address field
        register.enterEmail("janedoe.com");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToEmail();

        // 3. Verify that user can input a valid email address.
        Assert.assertFalse(register.isEmailValid(), "System should reject invalid email address.");
    }

    // Verify that Email Address field accepts a valid email address
    @Test(groups = {"regression"})
    public void RSTC011_shouldAcceptValidEmailAddress() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a valid email address in the Email Address field
        register.enterEmail("janedoe@gmail.com");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToEmail();

        // 3. Verify that user can input a valid email address.
        Assert.assertTrue(register.isEmailValid(), "System should accept valid email address.");
    }

//    // Verify that Phone Number field rejects no input phone number
//    @Test(groups = {"regression"})
//    public void shouldRejectNoInputPhoneNumber() {
//        // Pre-requisite: User is on the Registration Page
//        loadRegisterPage();
//
//        // 1. Enter a value containing letters or special characters in the Phone Number field
//        register.scrollToPhoneNumber();
//        register.enterPhoneNumber("");
//
//        // 2. Press the Register button
//        clickRegisterButton();
//        register.scrollToPhoneNumber();
//
//        // 3. Verify that user cannot input non-numbers in Phone Number field.
//        Assert.assertFalse(register.isPhoneNumberValid(), "System should reject no input in phone number field.");
//    }

    // Verify that Phone Number field rejects non-number input
    @Test(groups = {"regression"})
    public void RSTC012_shouldRejectNonNumberInputPhoneNumber() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPhoneNumber();
        register.enterPhoneNumber("testing!");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToPhoneNumber();
        delay();

        // 3. Verify that user cannot input non-numbers in Phone Number field.
        Assert.assertFalse(register.isPhoneNumberValid(), "System should not let use input non number phone numbers.");
    }

    // Verify that Phone Number field rejects non-formatted phone number
    @Test(groups = {"regression"})
    public void RSTC013_shouldRejectNonFormattedPhoneNumber() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPhoneNumber();
        register.enterPhoneNumber("12345678900");

        // 2. Press the Register button
        clickRegisterButton();
        delay();

        // 3. Verify that user cannot input non-numbers in Phone Number field.
        Assert.assertTrue(register.isPhoneNumberErrorDisplayed(), "System should reject incorrectly formatted phone number.");
        Assert.assertFalse(register.isPhoneNumberValid(), "System should reject incorrectly formatted phone number.");
    }

    // Verify that Phone Number field rejects an incomplete Philippine phone number
    @Test(groups = {"regression"})
    public void RSTC014_shouldRejectIncompletePhilippinePhoneNumber() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPhoneNumber();
        register.enterPhoneNumber("091234567");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToPhoneNumber();

        // 3. Verify that user cannot input non-numbers in Phone Number field.
        Assert.assertTrue(register.isPhoneNumberErrorDisplayed(), "System should reject incomplete PH Phone number.");
        Assert.assertFalse(register.isPhoneNumberValid(), "System should reject incomplete PH Phone number.");
    }

    // Verify that Phone Number field accepts a valid Philippine phone number
    @Test(groups = {"regression"})
    public void RSTC015_shouldAcceptValidPhilippinePhoneNumber() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPhoneNumber();
        register.enterPhoneNumber("09123456789");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToPhoneNumber();

        // 3. Verify that user cannot input non-numbers in Phone Number field.
        Assert.assertFalse(register.isPhoneNumberErrorDisplayed(), "System should accept valid PH Phone number.");
        Assert.assertTrue(register.isPhoneNumberValid(), "System should accept valid PH Phone number.");
    }

    // Verify that Your Password field rejects a password with only one Password Policy Requirement met
    @Test(groups = {"regression"})
    public void RSTC016_shouldRejectPasswordWithOnlyOnePasswordPolicyMet() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPassword();
        register.enterPassword("password");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToPassword();

        // 3. Verify that system rejects password that only follows 1 Password Policy Requirement
        Assert.assertFalse(register.getPasswordPoliciesMet() >= 3, "Password must meet at least 3 of 4 requirements.");
        Assert.assertFalse(register.isPasswordValid(), "Password must meet at least 3 of 4 requirements.");
    }

    // Verify that Your Password field rejects a password with only two Password Policy Requirement met
    @Test(groups = {"regression"})
    public void RSTC017_shouldRejectPasswordWithOnlyTwoPasswordPolicyMet() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPassword();
        register.enterPassword("password123");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToPassword();

        // 3. Verify that system rejects password that only follows 2 Password Policy Requirement
        Assert.assertFalse(register.getPasswordPoliciesMet() >= 3, "Password must meet at least 3 of 4 requirements.");
        Assert.assertFalse(register.isPasswordValid(), "Password must meet at least 3 of 4 requirements.");
    }

    // Verify that Your Password field accepts a password with three Password Policy Requirement met
    @Test(groups = {"regression"})
    public void RSTC018_shouldAcceptPasswordWithThreePasswordPolicyMet() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPassword();
        register.enterPassword("Password123");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToPassword();

        // 3. Verify that system rejects password that only follows 2 Password Policy Requirement
        Assert.assertTrue(register.getPasswordPoliciesMet() >= 3, "Password must meet at least 3 of 4 requirements.");
        Assert.assertTrue(register.isPasswordValid(), "Password must meet at least 3 of 4 requirements.");
    }

    // Verify that Your Password field accepts a password with all 4 Password Policy Requirement met
    @Test(groups = {"regression"})
    public void RSTC019_shouldAcceptPasswordWithAllPasswordPolicyMet() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPassword();
        register.enterPassword("Password123!");

        // 2. Press the Register button
        clickRegisterButton();
        register.scrollToPassword();

        // 3. Verify that system rejects password that only follows 2 Password Policy Requirement
        Assert.assertTrue(register.getPasswordPoliciesMet() >= 3, "Password must meet at least 3 of 4 requirements.");
        Assert.assertTrue(register.isPasswordValid(), "Password must meet at least 3 of 4 requirements.");
    }

    @Test(groups = {"regression"})
    public void RSTC020_togglePasswordShowsAndHidesPasswordInput() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a value containing letters or special characters in the Phone Number field
        register.scrollToPassword();
        register.enterPassword("Password123!");

        // 2. Click on the eye icon with slash to show the password; Verify that password can be unmasked
        register.clickPasswordMaskButton();
        Assert.assertEquals(register.getPasswordType(), "text", "Password should be unmasked.");

        // 3. Click on the eye icon without slash to hide the password
        register.clickPasswordMaskButton();
        Assert.assertEquals(register.getPasswordType(), "password", "Password should be masked.");
    }

    @Test(groups = {"regression"})
    public void RSTC021_shouldRejectMismatchingPasswords() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a valid password in the Your Password field
        register.scrollToPassword();
        register.enterPassword("Password123!");

        // 2.Enter a different password in the Confirm Password field
        register.enterRetypePassword("Password");
        clickRegisterButton();

        // 3. Verify that system does not accept mismatching passwords.
        Assert.assertTrue(register.isPasswordMismatchErrorPresent(), "Mismatching Passwords should be rejected.");
    }

    @Test(groups = {"regression"})
    public void RSTC022_shouldAcceptMatchingPasswords() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a valid password in the Your Password field
        register.scrollToPassword();
        register.enterPassword("Password123!");

        // 2.Enter a different password in the Confirm Password field
        register.enterRetypePassword("Password123!");
        clickRegisterButton();

        // 3. Verify that system does not accept mismatching passwords.
        Assert.assertTrue(register.isRetypePasswordValid(), "Matching Passwords should be accepted.");
    }

    // Verify that the Birthday fields reject empty input
    @Test(groups = {"regression"})
    public void RSTC023_shouldRejectEmptyBirthdayField() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Leave Day field set to "Day"
        // 2. Leave Month field set to "Month"
        // Above 2 means that nothing happens
        register.scrollToBirthday();

        // 3. Press the Register button
        clickRegisterButton();
        register.scrollToBirthday();

        // 4. Verify that Birthday field rejects empty input.
        Assert.assertFalse(register.isBirthdayInputValid(), "System should reject empty birthday fields.");
    }

    // Verify that the Year field in Birthday is set to the year that is 20 years before
    @Test(groups = {"regression"})
    public void RSTC024_defaultBirthYearIs20YearsBefore() {
        // Setup expected value
        int current_yr = java.time.Year.now().getValue();
        int expected_yr = current_yr - 20;

        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Check default year in the Year field
        register.scrollToBirthday();
        String actual_yr = register.getBirthYear();

        // 2. Verify that the Year field in Birthday is set to the year that is 20 years before.
        Assert.assertEquals(actual_yr, String.valueOf(expected_yr), "Assertion Failed: Default Birth Year is not 20 years before.");
    }

    // Verify that the Birthday fields rejects future dates
    @Test(groups = {"regression"})
    public void RSTC025_shouldRejectFutureBirthdays() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a date of birth that is later than today’s date
        register.scrollToBirthday();
        register.selectCompleteBirthday("31", "12", "2026");

        // 3. Press the Register button
        clickRegisterButton();
        register.scrollToBirthday();

        // 4. Verify that Birthday field rejects empty input.
        Assert.assertFalse(register.isBirthdayInputValid(), "System should reject dates that do not exist yet.");
    }

    // Verify that the Birthday fields accepts past or present dates
    @Test(groups = {"regression"})
    public void RSTC026_shouldAcceptValidBirthday() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Enter a date of birth that is today or before today's date
        register.scrollToBirthday();
        register.selectCompleteBirthday("19", "9", "2002");

        // 3. Press the Register button
        clickRegisterButton();
        register.scrollToBirthday();

        // 4. Verify that Birthday field rejects empty input.
        Assert.assertTrue(register.isBirthdayInputValid(), "System should accept valid birthdays.");
    }

    // Verify that the Terms of Use and Promotional Offers checkboxes are clickable and toggleable
    @Test(groups = {"regression"})
    public void RSTC027_checkboxesShouldBeClickable() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Click on the Terms of Use checkbox
        register.scrollToTerms();
        register.clickTermsCheckbox();

        // 2. Click on the Promotional Offers checkbox
        register.clickPromotionalCheckbox();
        Assert.assertTrue(register.isTermsChecked(), "Checkbox should be clickable.");
        Assert.assertTrue(register.isPromotionalChecked(), "Checkbox should be clickable.");

        // 3. Click on the Terms of Use checkbox
        register.scrollToTerms();
        register.clickTermsCheckbox();

        // 4. Click on the Promotional Offers checkbox
        register.clickPromotionalCheckbox();
        Assert.assertFalse(register.isTermsChecked(), "Checkbox should be clickable.");
        Assert.assertFalse(register.isPromotionalChecked(), "Checkbox should be clickable.");
    }

    // Verify that the Terms of Use checkbox is a required field
    @Test(groups = {"regression"})
    public void RSTC028_termsShouldBeRequired() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Leave Terms of Use checkbox unchecked
        register.scrollToTerms();

        // 2. Press Register
        clickRegisterButton();
        register.scrollToTerms();

        // 3. Verify that Terms of Use checkbox is required.
        Assert.assertTrue(register.isTermsOfUseErrorDisplayed(), "Terms checkbox should be required.");
    }

//    // Verify that the Promotional Offers checkbox is an optional field
//    @Test(groups = {"regression"})
//    public void RSTC029_promotionalShouldBeOptional() {
//        // Pre-requisite: User is on the Registration Page
//        loadRegisterPage();
//
//        // 1. Make sure the register form fields are incomplete/empty
//        // 2. Leave Promotional Offers checkbox unchecked
//        register.scrollToPromotional();
//        clickRegisterButton();
//
//        // 3.
//    }

    // Verify that registering with an existing account's email prevents user from creating an account
    @Test(groups = {"regression"})
    public void RSTC030_existingEmailShouldBePreventedFromCreatingAccount() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Input all required fields
        register.enterFirstName("Jane");
        register.enterLastName("Doe");
        register.selectGender("Female");
        register.enterEmail("janedoe@gmail.com");   // Input an existing email in the Email Address field
        register.enterPhoneNumber("09123456789");
        register.enterPassword("Password123!");
        register.enterRetypePassword("Password123!");
        register.selectCompleteBirthday("19", "9", "2002");
        register.clickTermsCheckbox();

        // 3. Click the Register button
//        clickRegisterButton();

        // 4. Verify that system does not accept existing emails.
//
    }

    // Verify that registering with an existing account's email prevents user from creating an account
    @Test(groups = {"regression"})
    public void RSTC031_ShouldAcceptNewAccount() {
        // Pre-requisite: User is on the Registration Page
        loadRegisterPage();

        // 1. Input all required fields
        register.enterFirstName("John");
        register.enterLastName("Doe");
        register.selectGender("Male");
        register.enterEmail("johndoe@gmail.com");   // 2. Input a new email in the Email Address field
        register.enterPhoneNumber("09987654321");
        register.enterPassword("Password123!");
        register.enterRetypePassword("Password123!");
        register.selectCompleteBirthday("19", "9", "2002");
        register.clickTermsCheckbox();

        // 3. Click the Register button
//        clickRegisterButton();

        // 4. Verify that registering account is successful with all valid inputs.
//
    }
}
