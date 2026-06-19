package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.Objects;

public class RegisterPage {
    WebDriver driver;
    JavascriptExecutor js;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
    }

    // locators
    private final By firstName = By.cssSelector("[data-tag='first-name-txt']");
    private final By lastName = By.cssSelector("[data-tag='last-name-txt']");
    private final By gender = By.cssSelector("[data-tag='gender-drp']");
    private final By email = By.cssSelector("[data-tag='email-txt']");
    private final By phone_number = By.cssSelector("[data-tag='phone-txt']");
    private final By password = By.cssSelector("[data-tag='password-lbl']");
    private final By birthday = By.cssSelector("[data-tag='birthday-lbl']");
    private final By birthday_day = By.cssSelector("[data-tag='day-drp']");
    private final By birthday_month = By.cssSelector("[data-tag='month-drp']");
    private final By birthday_year = By.cssSelector("[data-tag='year-drp']");
    private final By terms_and_conditions = By.cssSelector("[data-tag='term-privacy-chk']");
    private final By promotionals = By.cssSelector("[data-tag='receive-promotional-chk']");

    private final By registerBtn = By.cssSelector("[data-tag='register-btn']");
    private final By header = By.xpath("//h4[contains(text(), 'Register')]");
    private final By backBtn = By.cssSelector("[data-tag='back-btn']");
    private final By pizzaHutLogo = By.cssSelector("[data-tag='logo']");

    private final By uppercase =
            By.xpath("//p[contains(.,'Uppercase')]");

    private final By lowercase =
            By.xpath("//p[contains(.,'Lowercase')]");

    private final By numbers =
            By.xpath("//p[contains(.,'Numbers')]");

    private final By non_alphanumeric =
            By.xpath("//p[contains(.,'Non-alphanumeric')]");


    // actions
    // input in first name textbox
    public void enterFirstName(String name) {
        driver.findElement(firstName).sendKeys(name);
    }

    // input last name
    public void enterLastName(String name) {
        driver.findElement(lastName).sendKeys(name);
    }

    // input gender
    public void selectGender(String genderInput) {
        Select dropdown = new Select(driver.findElement(gender));
        dropdown.selectByVisibleText(genderInput);
    }

    // input email
    public void enterEmailAddress(String emailInput) {
        driver.findElement(email).sendKeys(emailInput);
    }

    // input phone number
    public void enterPhoneNumber(String phoneNumber) {
        driver.findElement(phone_number).sendKeys(phoneNumber);
    }

    // input password
    public void enterPassword(String passwordInput) {
        driver.findElement(password).sendKeys(passwordInput);
    }

    // input birth day
    public void enterBirthDay(String day) {
        Select dropdown = new Select(driver.findElement(birthday_day));
        dropdown.selectByVisibleText(day);
    }

    // input birth month
    public void enterBirthMonth(String month) {
        Select dropdown = new Select(driver.findElement(birthday_month));
        dropdown.selectByVisibleText(month);
    }

    // input birth year
    public void enterBirthYear(String year) {
        Select dropdown = new Select(driver.findElement(birthday_year));
        dropdown.selectByVisibleText(year);
    }

    // click terms&conditions checkbox
    public void clickTermsCheckbox() {
        driver.findElement(terms_and_conditions).click();
    }

    // click promotionals Checkbox
    public void clickPromotionalsCheckbox() {
        driver.findElement(promotionals).click();
    }

    // click register button
    public void clickRegisterButton() {
        driver.findElement(registerBtn).click();
    }

    // check if register header is displayed
    public boolean isHeaderDisplayed() {
        return driver.findElement(header).isDisplayed();
    }

    // click back button
    public void clickBackButton() {
        driver.findElement(backBtn).click();
    }

    // click pizza hut logo
    public void clickPizzaHutLogo() {
        driver.findElement(pizzaHutLogo).click();
    }

    // check validity of input
    public boolean checkValidity(By locator) {
        js = (JavascriptExecutor) driver;
        Object result = js.executeScript(
                "return arguments[0].checkValidity();", driver.findElement(locator)
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
        return !driver.findElements(By.xpath("//div[contains(text(),'P')]")).isEmpty();
    }

    // CHECK PASSWORD POLICIES
    // Check if uppercase is missing
    public boolean isUppercaseMissing() {
        return Objects.requireNonNull(driver.findElement(uppercase).getAttribute("class")).contains("text-danger");
    }

    // Check if lowercase is missing
    public boolean isLowercaseMissing() {
        return Objects.requireNonNull(driver.findElement(lowercase).getAttribute("class")).contains("text-danger");
    }

    // Check if number is missing
    public boolean isNumberMissing() {
        return Objects.requireNonNull(driver.findElement(numbers).getAttribute("class")).contains("text-danger");
    }

    // Check if non-alphanumeric is missing
    public boolean isNonAlphanumericMissing() {
        return Objects.requireNonNull(driver.findElement(non_alphanumeric).getAttribute("class")).contains("text-danger");
    }

    // Count total password policies met
    public int getPasswordPoliciesMet() {
        int met = 0;

        if(!isUppercaseMissing()) {
            met++;
        }
        if(!isLowercaseMissing()) {
            met++;
        }
        if(!isNumberMissing()) {
            met++;
        }
        if(!isNonAlphanumericMissing()) {
            met++;
        }

        return met;
    }

    // check if password is valid
    public boolean isPasswordValid() {
        return checkValidity(password);
    }

    // check if birth day is valid
    public boolean isBirthDayValid() {
        return checkValidity(birthday_day);
    }

    // check if birth month is valid
    public boolean isBirthMonthValid() {
        return checkValidity(birthday_month);
    }

    // check if birth year is valid
    public boolean isBirthYearValid() {
        return checkValidity(birthday_year);
    }

    // check if whole birthday input is valid
    public boolean isBirthdayInputValid() {
        return isBirthDayValid() && isBirthMonthValid() && isBirthYearValid();
    }

    // check if terms is checked
    public boolean isTermsChecked() {
        return Objects.requireNonNull(driver.findElement(terms_and_conditions).getAttribute("class")).contains("checked");
    }

    // check if promos is checked
    public boolean isPromotionalsChecked() {
        return Objects.requireNonNull(driver.findElement(promotionals).getAttribute("class")).contains("checked");
    }

    // check if Terms of Use error msg is displayed
    public boolean isTermsOfUseErrorDisplayed() {
        return driver.findElement(By.xpath("//small[@class='text-danger mb-1']")).isDisplayed();
    }

    // main method to scroll to a specific element
    public void scrollToView(By locator) {
        js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView({block: 'center'});", driver.findElement(locator));
    }

    // scroll to register button to see it
    public void scrollToRegisterButton() {
        scrollToView(registerBtn);
    }

    // scroll to first name textbox to see it
    public void scrollToFirstName() {
        scrollToView(firstName);
    }

    // scroll to last name textbox to see it
    public void scrollToLastName() {
        scrollToView(lastName);
    }

    // scroll to gender
    public void scrollToGender() {
        scrollToView(gender);
    }

    // scroll to email
    public void scrollToEmail() {
        scrollToView(email);
    }

    // scroll to phone number
    public void scrollToPhoneNumber() {
        scrollToView(phone_number);
    }

    // scroll to password
    public void scrollToPassword() {
        scrollToView(password);
    }

    // scroll to birthday
    public void scrollToBirthday() {
        scrollToView(birthday);
    }

    // scroll to terms&conditions
    public void scrollToTerms() {
        scrollToView(terms_and_conditions);
    }

    // get year
    public String getBirthYear() {
        return driver.findElement(birthday_year).getAttribute("value");
    }


}
