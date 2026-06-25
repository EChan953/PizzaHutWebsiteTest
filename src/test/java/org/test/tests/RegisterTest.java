package org.test.tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.test.base.BaseTest;
import org.test.pages.Homepage;
import org.test.pages.RegisterPage;
import org.test.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class RegisterTest extends BaseTest {
    private RegisterPage register;
    private Homepage homepage;

    private static final String SITE = "https://www.pizzahut.com.ph/";
    private static final String REGISTER = SITE + "register";
    private static final String excelFilePath = "src/test/resources/testdata/TestData.xlsx";

    @BeforeTest(groups = {"smoke", "regression", "e2e", "register"})
    public void initPage() {
        register = new RegisterPage(driver);
        homepage = new Homepage(driver);
    }

    @BeforeMethod(groups = {"smoke", "regression", "e2e", "register"})
    public void setupPreRequisite(Method method) {
        // FOR Pre-Requisite: User is on the Registration Page
        if(!method.getName().contains("RSTC001")) {
            extentTest.log(Status.INFO, "Loading Register Page");
            driver.get(REGISTER);

            // wait until its loaded
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//h4[contains(text(), 'Register')]")
            ));
        }
    }

    //Test Data
    @DataProvider(name="registerTestData")
    public Iterator<Object[]> getRegisterTestData(Method method){
        String rawTestCaseID = method.getName();
        String testCaseID = rawTestCaseID.split("_")[0].replace("RSTC","RS-TC-");

        List<Map<String, String>> allData =
                ExcelReader.readExcelData(excelFilePath, "Registration_Data");

        List<Map<String, String>> filtered =
                ExcelReader.filterByTestCase(allData, testCaseID);

        List<Object[]> result = new ArrayList<>();

        for (Map<String, String> map : filtered) {
            result.add(new Object[]{map});
        }

        return result.iterator();
    }

    // Verify Registration Page is Accessible
    public void redirectFromHomepageToRegisterPage() {
        // load homepage
        extentTest.log(Status.INFO, "Loading Website Homepage");
        driver.get(SITE);

        // click on register
        homepage.clickRegisterPageButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Register')]")
        ));
    }

    // Click register button
    public void clickRegisterButton() {
        // Scroll to bottom of page, Click Register
        register.scrollToRegisterButton();

        // click register button
        extentTest.log(Status.INFO, "Clicking Register Button");
        register.clickRegisterButton();
    }

    // Fill form fields
    public void fillFields(Map<String, String> data) {
        extentTest.log(Status.INFO, "Filling Register inputs");

        register.enterFirstName(data.get("First Name"));
        register.enterLastName(data.get("Last Name"));
        register.selectGender(data.get("Gender"));
        register.enterEmail(data.get("Email"));   // Input an existing email in the Email Address field
        register.enterPhoneNumber(data.get("Mobile Number"));
        register.enterPassword(data.get("Password"));
        register.enterRetypePassword(data.get("ConfirmPassword"));
        register.selectCompleteBirthday(
                data.get("Day"),
                data.get("Month"),
                data.get("Year"));
        register.clickTermsCheckbox();
    }

    // Leave field blank/unselected
    public void leaveFieldBlank(Map<String, String> data, String fieldToLeaveBlank) {
        fillFields(data);

        switch (fieldToLeaveBlank) {
            case "firstName" -> register.enterFirstName("");
            case "lastName" -> register.enterLastName("");
            case "email" -> register.enterEmail("");
            case "password" -> register.enterPassword("");
            case "phoneNumber" -> register.enterPhoneNumber("");
            case "termsOfUse" -> {
                if (register.isTermsChecked()) {
                    register.clickTermsCheckbox();
                }

            }
        }
    }

    // Verify Registration Page is Accessible
    @Test(groups = {"smoke", "register"})
    public void RSTC001_verifyRegisterPageAccessibility() {
        // Access Register Page
        extentTest.log(Status.INFO, "Accessing Register Page from Homepage");
        redirectFromHomepageToRegisterPage();

        // assert URL
        extentTest.log(Status.INFO, "Validating Register Page URL");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, REGISTER);

        // Check if header is actually Register
        extentTest.log(Status.INFO, "Validating Register Page Header loaded");
        Assert.assertTrue(register.isHeaderDisplayed(), "User is not redirected to Register Page.");
    }

    // Verify that clicking the Back button redirects the user back to the previous Pizza Hut webpage
    @Test(groups = {"register"})
    public void RSTC002_shouldRedirectToHomepageWhenBackButtonClicked() {
        extentTest.log(Status.INFO, "Clicking Back Button");
        register.clickBackButton();

        extentTest.log(Status.INFO, "Waiting for redirect to Homepage");
        wait.until(ExpectedConditions.urlToBe(SITE));
        Assert.assertEquals(driver.getCurrentUrl(), SITE, "Not redirected to Homepage.");

        // Wait until homepage element is present
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("address-autocomplete")
        ));

        // check if address input is there, since its part of the homepage
        extentTest.log(Status.INFO, "Verifying Visibility of Homepage Element");
        Assert.assertTrue(check.isDisplayed(), "Homepage element not visible.");
    }

    // Verify that clicking the Pizza Hut logo redirects the user to the homepage
    @Test(groups = {"register"})
    public void RSTC003_shouldRedirectToHomepageWhenPizzaHutLogoClicked() {
        extentTest.log(Status.INFO, "Clicking Pizza Hut Logo");
        register.clickPizzaHutLogo();

        extentTest.log(Status.INFO, "Waiting for redirect to Homepage");
        wait.until(ExpectedConditions.urlToBe(SITE));
        Assert.assertEquals(driver.getCurrentUrl(), SITE, "Not redirected to Homepage.");

        // Wait until homepage element is present
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.id("address-autocomplete")
        ));

        // check if address input is there, since its part of the homepage
        extentTest.log(Status.INFO, "Verifying Visibility of Homepage Element");
        Assert.assertTrue(check.isDisplayed(), "Homepage element not visible.");
    }

    // Verify that First Name field rejects no input
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC004_shouldRejectFirstNameWithNoInput(Map<String, String> data) {
        // 1. Leave First Name field blank
        extentTest.log(Status.INFO, "Leaving First Name field blank");
        leaveFieldBlank(data, "firstName");

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that the First Name field rejects no inputs.
        extentTest.log(Status.INFO, "Verifying First Name field rejects no inputs");
        Assert.assertFalse(register.isFirstNameValid(), "System should reject Blank First Name inputs.");
    }

    // Verify that First Name field rejects non-letter input
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC005_shouldRejectFirstNameWithNonLetters(Map<String, String> data) {
        // 1. Enter a value containing numbers or special characters in the First Name field
        extentTest.log(Status.INFO, "Inputting invalid First Name: " + data.get("First Name"));
        register.enterFirstName(data.get("First Name"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that the First Name field rejects non-letter inputs.
        extentTest.log(Status.INFO, "Verifying First Name field rejects non-letter inputs");
        Assert.assertFalse(register.isFirstNameValid(), "System should reject First Name with Non-Letters.");
    }

    // Verify that First Name field accepts a valid name
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC006_shouldAcceptValidFirstName(Map<String, String> data) {
        // 1. Enter valid First Name input
        extentTest.log(Status.INFO, "Inputting valid First Name: " + data.get("First Name"));
        register.enterFirstName(data.get("First Name"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that First Name field accepts a valid name.
        extentTest.log(Status.INFO, "Verifying First Name field accepts valid name");
        Assert.assertTrue(register.isFirstNameValid(), "System should accept valid first name.");
    }

    // Verify that Last Name field rejects no input
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC007_shouldRejectLastNameWithNoInput(Map<String, String> data) {
        // 1. Enter no value in Last Name field
        extentTest.log(Status.INFO, "Leaving Last Name field blank");
        leaveFieldBlank(data, "lastName");

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that Last Name field rejects no input.
        extentTest.log(Status.INFO, "Verifying Last Name field rejects no input");
        Assert.assertFalse(register.isLastNameValid(), "System should reject last name with no input.");
    }

    // Verify that Last Name field rejects non-letter input
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC008_shouldRejectLastNameWithNonLetters(Map<String, String> data) {
        // 1. Enter input w/ non-letter value
        extentTest.log(Status.INFO, "Inputting invalid Last Name: " + data.get("Last Name"));
        register.enterLastName(data.get("Last Name"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that Last Name field rejects non-letter input.
        extentTest.log(Status.INFO, "Verifying Last Name field rejects non-letter inputs");
        Assert.assertFalse(register.isLastNameValid(), "System should reject last name with non-letters.");
    }

    // Verify that Last Name field accepts a valid name
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC009_shouldAcceptValidLastName(Map<String, String> data) {
        // 1. Enter valid last name input
        extentTest.log(Status.INFO, "Inputting valid Last Name: " + data.get("Last Name"));
        register.enterLastName(data.get("Last Name"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that Last Name field accepts a valid last name.
        extentTest.log(Status.INFO, "Verifying Last Name field accepts valid input");
        Assert.assertTrue(register.isLastNameValid(), "System should accept valid last name.");
    }

    // Verify that Gender field is optional
    @Test(groups = {"register"})
    public void RSTC010_validateGenderIsOptional() {
        // 1. Select "Please select gender" from Gender field
        // "Please select gender" is by default chosen, so just click Register Btn

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that Gender field is valid even without an input.
        extentTest.log(Status.INFO, "Verifying Gender field is valid regardless of input");
        Assert.assertTrue(register.isGenderValid(), "Gender should be valid even with no input.");
    }

    // Verify that user can select from the Gender field dropdown
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC011_validateGenderInput(Map<String, String> data) {
        // 1. Click on the Gender field
        // 2. Choose Male/Female from dropdown
        extentTest.log(Status.INFO, "Selecting Gender: " + data.get("Gender"));
        register.selectGender(data.get("Gender"));

        // 3. Click the Register button
        clickRegisterButton();

        // 4. Verify that user can select from the Gender field dropdown.
        extentTest.log(Status.INFO, "Verifying Gender field is valid regardless of input");
        Assert.assertTrue(register.isGenderValid(), "System should accept gender input regardless of choice.");
    }

    // Verify that Email Address field rejects no input
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC012_shouldRejectBlankEmailAddress(Map<String, String> data) {
        // 1. Leave Email Address field blank
        extentTest.log(Status.INFO, "Leaving Email field blank");
        leaveFieldBlank(data, "email");

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that user cannot proceed with a blank email address.
        extentTest.log(Status.INFO, "Verifying user cannot proceed with a blank email address");
        Assert.assertFalse(register.isEmailValid(), "System should reject blank email address.");
    }

    // Verify that Email Address field rejects an invalid email address
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC013_shouldRejectInvalidEmailAddress(Map<String, String> data) {
        // 1. Enter invalid email address in the Email Address field
        extentTest.log(Status.INFO, "Inputting invalid Email: " + data.get("Email"));
        register.enterEmail(data.get("Email"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that user cannot input an invalid email address.
        extentTest.log(Status.INFO, "Verifying Email field rejects invalid email");
        Assert.assertFalse(register.isEmailValid(), "System should reject invalid email address.");
    }

    // Verify that Email Address field accepts a valid email address
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC014_shouldAcceptValidEmailAddress(Map<String, String> data) {
        // 1. Enter a valid email address in the Email Address field
        extentTest.log(Status.INFO, "Inputting valid Email: " + data.get("Email"));
        register.enterEmail(data.get("Email"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that user can input a valid email address.
        extentTest.log(Status.INFO, "Verifying Email field accepts valid email");
        Assert.assertTrue(register.isEmailValid(), "System should accept valid email address.");
    }

    // Verify that Phone Number field rejects no input phone number
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC015_shouldRejectNoInputPhoneNumber(Map<String, String> data) {
        // 1. Leave Phone Number field blank.
        extentTest.log(Status.INFO, "Leaving Phone Number field blank");
        leaveFieldBlank(data, "phoneNumber");

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that user cannot input non-numbers in Phone Number field.
        extentTest.log(Status.INFO, "Verifying Phone Number field rejects no input");
        Assert.assertFalse(register.isPhoneNumberValid(), "System should reject no input in phone number field.");
    }

    // Verify that Phone Number field rejects non-number input
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC016_shouldRejectNonNumberInputPhoneNumber(Map<String, String> data) {
        // 1. Enter a value containing letters or special characters in the Phone Number field
        extentTest.log(Status.INFO, "Inputting invalid Phone Number: " + data.get("Mobile Number"));
        register.enterPhoneNumber(data.get("Mobile Number"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that user cannot input non-numbers in Phone Number field.
        extentTest.log(Status.INFO, "Verifying Phone Number field rejects non-number input");
        Assert.assertFalse(register.isPhoneNumberValid(), "System should not let use input non number phone numbers.");
    }

    // Verify that Phone Number field rejects non-formatted phone number
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC017_shouldRejectNonFormattedPhoneNumber(Map<String, String> data) {
        // 1. Enter a numerical value not following the phone number format 09xxxxxxxxx in the Phone Number field
        extentTest.log(Status.INFO, "Inputting invalid Phone Number: " + data.get("Mobile Number"));
        register.enterPhoneNumber(data.get("Mobile Number"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that user cannot input non-formatted phone numbers in Phone Number field.
        extentTest.log(Status.INFO, "Verifying Phone Number field rejects non-formatted mobile numbers");
        Assert.assertTrue(register.isPhoneNumberErrorDisplayed(), "System should reject incorrectly formatted phone number.");
        Assert.assertFalse(register.isPhoneNumberValid(), "System should reject incorrectly formatted phone number.");
    }

    // Verify that Phone Number field rejects an incomplete Philippine phone number
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC018_shouldRejectIncompletePhilippinePhoneNumber(Map<String, String> data) {
        // 1. Enter an incomplete Philippine phone number in the Phone Number field
        extentTest.log(Status.INFO, "Inputting invalid Phone Number: " + data.get("Mobile Number"));
        register.enterPhoneNumber(data.get("Mobile Number"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that user cannot input incomplete Philippine phone number in Phone Number field.
        extentTest.log(Status.INFO, "Verifying Phone Number field rejects non-Philippine mobile number");
        Assert.assertTrue(register.isPhoneNumberErrorDisplayed(), "System should reject incomplete PH Phone number.");
        Assert.assertFalse(register.isPhoneNumberValid(), "System should reject incomplete PH Phone number.");
    }

    // Verify that Phone Number field accepts a valid Philippine phone number
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC019_shouldAcceptValidPhilippinePhoneNumber(Map<String, String> data) {
        // 1. Enter a valid Philippine phone number in the Phone Number field
        extentTest.log(Status.INFO, "Inputting valid Phone Number: " + data.get("Mobile Number"));
        register.enterPhoneNumber(data.get("Mobile Number"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that system accepts valid PH phone number.
        extentTest.log(Status.INFO, "Verifying Phone Number field accepts valid Philippine mobile number");
        Assert.assertFalse(register.isPhoneNumberErrorDisplayed(), "System should accept valid PH Phone number.");
        Assert.assertTrue(register.isPhoneNumberValid(), "System should accept valid PH Phone number.");
    }

    // Verify that Your Password field rejects no input
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC020_shouldRejectBlankPasswordInput(Map<String, String> data) {
        // 1. Leave Your Password field blank
        extentTest.log(Status.INFO, "Leaving password field blank");
        leaveFieldBlank(data, "password");

        // 2. Click the Register button
        clickRegisterButton();
        delay();

        // 3. Verify that system rejects blank Your Password input.
        extentTest.log(Status.INFO, "Verifying Password field rejects blank password input");
        Assert.assertFalse(register.getPasswordPoliciesMet() >= 3, "Password must meet at least 3 of 4 requirements.");
        Assert.assertFalse(register.isPasswordValid(), "Password must meet at least 3 of 4 requirements.");
    }

    // Verify that Your Password field rejects a password with only one Password Policy Requirement met
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC021_shouldRejectPasswordWithOnlyOnePasswordPolicyMet(Map<String, String> data) {
        // 1. Enter an invalid password with only one Password Policy Requirement met in the Your Password field
        extentTest.log(Status.INFO, "Inputting invalid Password: " + data.get("Password"));
        fillFields(data);

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that system rejects password that only follows 1 Password Policy Requirement
        extentTest.log(Status.INFO, "Verifying system rejects password that only follows 1 password policy requirement");
        Assert.assertFalse(register.getPasswordPoliciesMet() >= 3, "Password must meet at least 3 of 4 requirements.");
        Assert.assertTrue(register.getToastError().getText().contains("Password"), "Password must meet at least 3 of 4 requirements.");
//        Assert.assertFalse(register.isPasswordValid(), "Password must meet at least 3 of 4 requirements.");
    }

    // Verify that Your Password field rejects a password with only two Password Policy Requirement met
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC022_shouldRejectPasswordWithOnlyTwoPasswordPolicyMet(Map<String, String> data) {
        // 1. Enter an invalid password with only two Password Policy Requirement met in the Your Password field
        extentTest.log(Status.INFO, "Inputting invalid Password: " + data.get("Password"));
        register.enterPassword(data.get("Password"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that system rejects password that only follows 2 Password Policy Requirement
        extentTest.log(Status.INFO, "Verifying system rejects password that only follows 2 password policy requirement");
        Assert.assertFalse(register.getPasswordPoliciesMet() >= 3, "Password must meet at least 3 of 4 requirements.");
//        Assert.assertTrue(register.getToastError().getText().contains("Password"), "Password must meet at least 3 of 4 requirements.");
        Assert.assertFalse(register.isPasswordValid(), "Password must meet at least 3 of 4 requirements.");
    }

    // Verify that Your Password field accepts a password with three Password Policy Requirement met
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC023_shouldAcceptPasswordWithThreePasswordPolicyMet(Map<String, String> data) {
        // 1. Enter a valid password with three Password Policy Requirement met in the Your Password field
        extentTest.log(Status.INFO, "Inputting valid Password: " + data.get("Password"));
        register.enterPassword(data.get("Password"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that system accepts password that follows 3 Password Policy Requirements
        extentTest.log(Status.INFO, "Verifying system accepts password that follow 3 password policy requirement");
        Assert.assertTrue(register.getPasswordPoliciesMet() >= 3, "Password meets at least 3 of 4 requirements, should be valid.");
        Assert.assertTrue(register.isPasswordValid(), "Password meets at least 3 of 4 requirements, should be valid.");
    }

    // Verify that Your Password field accepts a password with all 4 Password Policy Requirement met
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC024_shouldAcceptPasswordWithAllPasswordPolicyMet(Map<String, String> data) {
        // 1. Enter a valid password with all Password Policy Requirement met in the Your Password field
        extentTest.log(Status.INFO, "Inputting valid Password: " + data.get("Password"));
        register.enterPassword(data.get("Password"));

        // 2. Click the Register button
        clickRegisterButton();

        // 3. Verify that system accepts password that follows all 4 Password Policy Requirements
        extentTest.log(Status.INFO, "Verifying system accepts password that follow 4 password policy requirement");
        Assert.assertTrue(register.getPasswordPoliciesMet() >= 3, "Password meets at least 3 of 4 requirements, should be valid.");
        Assert.assertTrue(register.isPasswordValid(), "Password meets at least 3 of 4 requirements, should be valid.");
    }

    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC025_togglePasswordShowsAndHidesPasswordInput(Map<String, String> data) {
        // 1. Enter a valid password in the Your Password field
        extentTest.log(Status.INFO, "Inputting valid Password: " + data.get("Password"));
        register.enterPassword(data.get("Password"));

        // 2. Click on the eye icon with slash to show the password; Verify that password can be unmasked
        extentTest.log(Status.INFO, "Clicking password visibility toggle and verifying password is unmasked");
        register.clickPasswordMaskButton();
        Assert.assertEquals(register.getPasswordType(), "text", "Password should be unmasked.");

        // 3. Click on the eye icon without slash to hide the password; Verify that password can be unmasked
        extentTest.log(Status.INFO, "Clicking password visibility toggle and verifying password is masked");
        register.clickPasswordMaskButton();
        Assert.assertEquals(register.getPasswordType(), "password", "Password should be masked.");
    }

    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC026_shouldRejectMismatchingPasswords(Map<String, String> data) {
        // 1. Enter a valid password in the Your Password field
        extentTest.log(Status.INFO, "Inputting valid Password: " + data.get("Password"));
        register.enterPassword(data.get("Password"));

        // 2. Enter a different password in the Confirm Password field
        extentTest.log(Status.INFO, "Inputting mismatching Confirm Password: " + data.get("ConfirmPassword"));
        register.enterRetypePassword(data.get("ConfirmPassword"));

        // 3. Verify that system does not accept mismatching passwords.
        extentTest.log(Status.INFO, "Verifying system does not accept mismatching passwords");
        Assert.assertTrue(register.isPasswordMismatchErrorPresent(), "Mismatching Passwords should be rejected.");
    }

    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC027_shouldAcceptMatchingPasswords(Map<String, String> data) {
        // 1. Enter a valid password in the Your Password field
        extentTest.log(Status.INFO, "Inputting valid Password: " + data.get("Password"));
        register.enterPassword(data.get("Password"));

        // 2. Enter the same password in the Confirm Password field
        extentTest.log(Status.INFO, "Inputting matching Confirm Password: " + data.get("ConfirmPassword"));
        register.enterRetypePassword(data.get("ConfirmPassword"));

        // 3. Verify that system accepts matching passwords.
        extentTest.log(Status.INFO, "Verifying system accepts matching passwords");
        Assert.assertTrue(register.isRetypePasswordValid(), "Matching Passwords should be accepted.");
    }

    // Verify that the Birthday fields reject empty input
    @Test(groups = {"register"})
    public void RSTC028_shouldRejectEmptyBirthdayField() {
        // 1. Leave Day field set to "Day"
        // 2. Leave Month field set to "Month"
        // Nothing happens, since "Day" and "Month" are default values
        extentTest.log(Status.INFO, "Leaving Birthday values as default");

        // 3. Click the Register button
        clickRegisterButton();

        // 4. Verify that Birthday field rejects empty input.
        extentTest.log(Status.INFO, "Verifying Birthday field rejects empty input");
        Assert.assertFalse(register.isBirthdayInputValid(), "System should reject empty birthday fields.");
    }

    // Verify that the Year field in Birthday is set to the year that is 20 years before
    @Test(groups = {"register"})
    public void RSTC029_defaultBirthYearIs20YearsBefore() {
        // Setup expected value
        extentTest.log(Status.INFO, "Calculating expected birth year (current year - 20)");
        int current_yr = java.time.Year.now().getValue();
        int expected_yr = current_yr - 20;

        // 1. Check default year in the Year field
        extentTest.log(Status.INFO, "Retrieving Default year");
        String actual_yr = register.getBirthYear();

        // 2. Verify that the Year field in Birthday is set to the year that is 20 years before.
        extentTest.log(Status.INFO, "Verifying default birth year is set to " + expected_yr);
        Assert.assertEquals(actual_yr, String.valueOf(expected_yr), "Default Birth Year is not 20 years before.");
    }

    // Verify that the Birthday fields rejects future dates
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC030_shouldRejectFutureBirthdays(Map<String, String> data) {
        // 1. Enter a date of birth that is later than today’s date
        extentTest.log(Status.INFO, "Inputting future Birthday: " + data.get("Day") + "/" + data.get("Month") + "/" + data.get("Year"));
        fillFields(data);

        // 3. Click the Register button
        clickRegisterButton();

        // 4. Verify that system does not accept future dates.
        extentTest.log(Status.INFO, "Verifying that system does not accept future dates");
//        Assert.assertFalse(register.isBirthdayInputValid(), "System should reject dates that do not exist yet.");
        Assert.assertTrue(register.getToastError().getText().contains("invalid"));
    }

    // Verify that the Birthday fields accepts past or present dates
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC031_shouldAcceptValidBirthday(Map<String, String> data) {
        // 1. Enter a date of birth that is today or before today's date
        extentTest.log(Status.INFO, "Inputting valid Birthday: " + data.get("Day") + "/" + data.get("Month") + "/" + data.get("Year"));
        register.selectCompleteBirthday(data.get("Day"), data.get("Month"), data.get("Year"));

        // 3. Click the Register button
        clickRegisterButton();

        // 4. Verify that system accepts past or present dates.
        extentTest.log(Status.INFO, "Verifying that system accepts past or present dates");
        Assert.assertTrue(register.isBirthdayInputValid(), "System should accept valid birthdays.");
    }

    // Verify that the Terms of Use and Promotional Offers checkboxes are clickable and toggleable
    @Test(groups = {"register"})
    public void RSTC032_checkboxesShouldBeClickable() {
        // 1. Click on the Terms of Use checkbox
        extentTest.log(Status.INFO, "Clicking Terms of Use Checkbox");
        register.clickTermsCheckbox();

        // 2. Click on the Promotional Offers checkbox
        extentTest.log(Status.INFO, "Clicking Promotional Offers checkbox");
        register.clickPromotionalCheckbox();

        // 5. Verify that Terms of Use and Promotional Offers checkboxes are functional
        extentTest.log(Status.INFO, "Verifying that both checkboxes are selected and fully functional");
        Assert.assertTrue(register.isTermsChecked(), "Checkbox should be clickable.");
        Assert.assertTrue(register.isPromotionalChecked(), "Checkbox should be clickable.");

        // 3. Click on the Terms of Use checkbox
        extentTest.log(Status.INFO, "Unselecting Terms of Use checkbox");
        register.clickTermsCheckbox();

        // 4. Click on the Promotional Offers checkbox
        extentTest.log(Status.INFO, "Unselecting Promotional Offers checkbox");
        register.clickPromotionalCheckbox();

        // 5. Verify that Terms of Use and Promotional Offers checkboxes are functional
        extentTest.log(Status.INFO, "Verifying that both checkboxes are unselected and fully functional");
        Assert.assertFalse(register.isTermsChecked(), "Checkbox should be clickable.");
        Assert.assertFalse(register.isPromotionalChecked(), "Checkbox should be clickable.");
    }

    // Verify that the Terms of Use checkbox is a required field
    @Test(dataProvider = "registerTestData", groups = {"register"})
    public void RSTC033_termsShouldBeRequired(Map<String, String> data) {
        // 1. Leave Terms of Use checkbox unchecked; Do nothing
        extentTest.log(Status.INFO, "Leave Terms of Use checkbox unchecked");
        leaveFieldBlank(data, "termsOfUse");

        // 2. Click Register
        clickRegisterButton();

        // 3. Verify that Terms of Use checkbox is required.
        extentTest.log(Status.INFO, "Verifying that Terms of Use checkbox is required");
        Assert.assertTrue(register.isTermsOfUseErrorDisplayed(), "Terms checkbox should be required.");
    }

    // Verify that registering with an existing account's email prevents user from creating an account
    @Test(dataProvider = "registerTestData", groups = {"regression"})
    public void RSTC034_existingEmailShouldBePreventedFromCreatingAccount(Map<String, String> data) {
        // 1. Input all required fields
        fillFields(data);

        // 3. Click the Register button
        clickRegisterButton();

        // 4. Verify that system does not accept existing emails.
        extentTest.log(Status.INFO, "Verifying that register fails due to existing email");
        Assert.assertTrue(register.getToastError().getText().contains("Email is existed"), "Email existed error should pop-up");
    }

    // Verify that registering with an existing account's email prevents user from creating an account
    @Test(dataProvider = "registerTestData", groups = {"smoke", "register"})
    public void RSTC035_shouldAcceptNewAccount(Map<String, String> data) {
        // 1. Input all required fields
        fillFields(data);

        // 3. Click the Register button
//        clickRegisterButton();

        // 4. Verify that registering account is successful with all valid inputs.
        extentTest.log(Status.INFO, "Verifying all inputs valid, ready for Register");
        Assert.assertTrue(register.isFirstNameValid());
        Assert.assertTrue(register.isLastNameValid());
        Assert.assertTrue(register.isEmailValid());
        Assert.assertTrue(register.isPhoneNumberValid());
        Assert.assertTrue(register.isPasswordValid());
        Assert.assertTrue(register.isRetypePasswordValid());
        Assert.assertTrue(register.isBirthdayInputValid());
        Assert.assertTrue(register.isTermsChecked());
    }
}
