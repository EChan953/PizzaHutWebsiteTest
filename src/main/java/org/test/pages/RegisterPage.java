package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Objects;

public class RegisterPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // locators
    private final By firstName = By.cssSelector("[data-tag='first-name-txt']");
    private final By lastName = By.cssSelector("[data-tag='last-name-txt']");
    private final By gender = By.cssSelector("[data-tag='gender-drp']");
    private final By email = By.cssSelector("[data-tag='email-txt']");
    private final By phone_number = By.cssSelector("[data-tag='phone-txt']");
    private final By password = By.cssSelector("[data-tag='password-lbl']");
    private final By retypePassword = By.cssSelector("[data-tag='retype-password-txt']");
    private final By birthdayDay = By.cssSelector("[data-tag='day-drp']");
    private final By birthdayMonth = By.cssSelector("[data-tag='month-drp']");
    private final By birthdayYear = By.cssSelector("[data-tag='year-drp']");
    private final By termsOfUse = By.cssSelector("[data-tag='term-privacy-chk']");
    private final By promotional = By.cssSelector("[data-tag='receive-promotional-chk']");

    private final By registerBtn = By.cssSelector("[data-tag='register-btn']");
    private final By header = By.xpath("//h4[contains(text(), 'Register')]");
    private final By backBtn = By.cssSelector("[data-tag='back-btn']");
    private final By pizzaHutLogo = By.cssSelector("[data-tag='logo']");
    private final By passwordMaskButton = By.cssSelector(".input-group-text.cursor-pointer");

    private final By uppercase =
            By.xpath("//p[contains(.,'Uppercase')]");

    private final By lowercase =
            By.xpath("//p[contains(.,'Lowercase')]");

    private final By numbers =
            By.xpath("//p[contains(.,'Numbers')]");

    private final By non_alphanumeric =
            By.xpath("//p[contains(.,'Non-alphanumeric')]");

    private final By passwordMismatchError = By.cssSelector(".invalid-feedback.d-block");
    private final By phoneNumberError = By.xpath("//div[contains(text(),'Please follow format 09xxxxxxxxx')]");
    private final By toastError = By.cssSelector("div[data-tag='toast-error']");

    // actions
    // finds the web element; uses explicit wait for reliability
    public WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // input in first name textbox
    public void enterFirstName(String name) {
        find(firstName).sendKeys(name);
    }

    // input last name
    public void enterLastName(String name) {
        find(lastName).sendKeys(name);
    }

    // input gender
    public void selectGender(String genderInput) {
        Select dropdown = new Select(find(gender));
        dropdown.selectByVisibleText(genderInput);
    }

    // input email
    public void enterEmail(String emailInput) {
        find(email).sendKeys(emailInput);
    }

    // input phone number
    public void enterPhoneNumber(String phoneNumber) {
        find(phone_number).sendKeys(phoneNumber);
    }

    // input password
    public void enterPassword(String passwordInput) {
        find(password).sendKeys(passwordInput);
    }

    // input retype-password
    public void enterRetypePassword(String retypePasswordInput) {
        find(retypePassword).sendKeys(retypePasswordInput);
    }

    // input birth day
    public void selectBirthDay(String day) {
        Select dropdown = new Select(find(birthdayDay));
        dropdown.selectByVisibleText(day);
    }

    // input birth month
    public void selectBirthMonth(String month) {
        Select dropdown = new Select(find(birthdayMonth));
        dropdown.selectByVisibleText(month);
    }

    // input birth year
    public void selectBirthYear(String year) {
        Select dropdown = new Select(find(birthdayYear));
        dropdown.selectByVisibleText(year);
    }

    // input whole birthday
    public void selectCompleteBirthday(String day, String month, String year) {
        selectBirthDay(day);
        selectBirthMonth(month);
        selectBirthYear(year);
    }

    // click terms&conditions checkbox
    public void clickTermsCheckbox() {
        find(termsOfUse).click();
    }

    // click promotional Checkbox
    public void clickPromotionalCheckbox() {
        find(promotional).click();
    }

    // click register button
    public void clickRegisterButton() {
        find(registerBtn).click();
    }

    // check if register header is displayed
    public boolean isHeaderDisplayed() {
        return find(header).isDisplayed();
    }

    // click back button
    public void clickBackButton() {
        find(backBtn).click();
    }

    // click pizza hut logo
    public void clickPizzaHutLogo() {
        find(pizzaHutLogo).click();
    }

    // check validity of input
    public boolean checkValidity(By locator) {
        Object result = js.executeScript(
                "return arguments[0].checkValidity();", find(locator)
        );

        return Boolean.TRUE.equals(result);
    }

    // check if first name is valid
    public boolean isFirstNameValid() {
        return checkValidity(firstName);
    }

    // check if last name is valid
    public boolean isLastNameValid() {
        return checkValidity(lastName);
    }

    // check if gender is valid
    public boolean isGenderValid() {
        return checkValidity(gender);
    }

    // check if email is valid
    public boolean isEmailValid() {
        return checkValidity(email);
    }

    // check if phone number is valid
    public boolean isPhoneNumberValid() {
        return checkValidity(phone_number);
    }

    // check if phone number is valid
    public boolean isPhoneNumberErrorDisplayed() {
        return !driver.findElements(phoneNumberError).isEmpty();
    }

    // CHECK PASSWORD POLICIES
    // Check if uppercase is missing
    public boolean hasUppercase() {
        return Objects.requireNonNull(find(uppercase).getAttribute("class")).contains("text-success");
    }

    // Check if lowercase is missing
    public boolean hasLowercase() {
        return Objects.requireNonNull(find(lowercase).getAttribute("class")).contains("text-success");
    }

    // Check if number is missing
    public boolean hasNumber() {
        return Objects.requireNonNull(find(numbers).getAttribute("class")).contains("text-success");
    }

    // Check if non-alphanumeric is missing
    public boolean hasNonAlphanumeric() {
        return Objects.requireNonNull(find(non_alphanumeric).getAttribute("class")).contains("text-success");
    }

    // Count total password policies met
    public int getPasswordPoliciesMet() {
        int met = 0;

        if(hasUppercase()) {
            met++;
        }
        if(hasLowercase()) {
            met++;
        }
        if(hasNumber()) {
            met++;
        }
        if(hasNonAlphanumeric()) {
            met++;
        }

        return met;
    }

    // check if password is valid
    public boolean isPasswordValid() {
        return checkValidity(password);
    }

    // check if retyped password matches password
    public boolean isRetypePasswordValid() {
        return checkValidity(retypePassword);
    }

    // check if birth day is valid
    public boolean isBirthDayValid() {
        return checkValidity(birthdayDay);
    }

    // check if birth month is valid
    public boolean isBirthMonthValid() {
        return checkValidity(birthdayMonth);
    }

    // check if birth year is valid
    public boolean isBirthYearValid() {
        return checkValidity(birthdayYear);
    }

    // check if whole birthday input is valid
    public boolean isBirthdayInputValid() {
        return isBirthDayValid() && isBirthMonthValid() && isBirthYearValid();
    }

    // check if terms is checked
    public boolean isTermsChecked() {
        return Objects.requireNonNull(find(termsOfUse).getAttribute("class")).contains("checked");
    }

    // check if promos is checked
    public boolean isPromotionalChecked() {
        return Objects.requireNonNull(find(promotional).getAttribute("class")).contains("checked");
    }

    // check if Terms of Use error msg is displayed
    public boolean isTermsOfUseErrorDisplayed() {
        return find(By.xpath("//small[@class='text-danger mb-1']")).isDisplayed();
    }

    // main method to scroll to a specific element
    public void scrollToView(By locator) {
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", driver.findElement(locator));
    }

    // scroll to register button to see it
    public void scrollToRegisterButton() {
        scrollToView(registerBtn);
    }

    // get year
    public String getBirthYear() {
        return find(birthdayYear).getAttribute("value");
    }

    // get password type
    public String getPasswordType() {
        return find(password).getAttribute("type");
    }

    // get toast error
    public WebElement getToastError() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(
                toastError
        ));
    }

    // Click password mask/unmask button
    public void clickPasswordMaskButton() {
        find(passwordMaskButton).click();
    }

    public boolean isPasswordMismatchErrorPresent() {
        return find(passwordMismatchError).isDisplayed();
    }
}
