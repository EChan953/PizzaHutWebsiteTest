package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CheckoutPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //locators
    private final By homeButton = By.cssSelector("img[class='image-desktop']");
    private final By backButton = By.cssSelector("div[data-tag='back-btn']");
    private final By changeLink = By.cssSelector("button[data-tag='change-btn']");
    private final By dateTimeContainer = By.cssSelector("div[class='container-localization-body  bg-white p-4']");
    private final By scheduleDate = By.cssSelector("span[data-tag='timeslot-lbl']");
    private final By firstNameInput = By.cssSelector("input[data-tag='first-name-txt']");
    private final By lastNameInput = By.cssSelector("input[data-tag='last-name-txt']");
    private final By phoneNumber = By.cssSelector("input[data-tag='phone-txt']");
    private final By emailAddress = By.cssSelector("input[data-tag='email-txt']");
    private final By specialRemarks = By.cssSelector("textarea[data-tag='instruction-txt']");
    private final By houseAddress = By.cssSelector("input[data-tag='house-number-txt']");
    private final By barangay = By.cssSelector("input[data-tag='barangay-txt']");
    private final By landmark = By.cssSelector("input[data-tag='landmark-txt']");
    private final By radioCash = By.cssSelector("div[data-tag='cash-radio']");
    private final By cashChangeInput = By.cssSelector("input[data-tag='cash-txt']");
    private final By errorMessage = By.cssSelector("div[class='invalid-feedback d-block']");
    private final By cashErrorMessage = By.cssSelector("div[class='invalid-feedback d-block'] span");
    private final By smallErrorMessage = By.cssSelector("small[class='text-danger mb-1']");
    private final By radioCredit = By.cssSelector("div[data-tag='eftpos-radio']");
    private final By radioGCash = By.cssSelector("div[data-tag='gcash-radio']");
    private final By radioMaya = By.cssSelector("div[data-tag='paymaya-radio']");
    private final By termsOfUseLink = By.linkText("Terms of Use");
    private final By privacyPolicyLink = By.linkText("Privacy Policy");
    private final By checkboxTerms = By.cssSelector("div[data-tag='term-condition-chk']");
    private final By dropdownDate = By.cssSelector("select[data-tag='date-drp']");
    private final By dropdownTime = By.cssSelector("select[data-tag='time-drp']");
    private final By cancelButton = By.cssSelector("button[data-tag='cancel-btn']");
    private final By continueButton = By.cssSelector("button[data-tag='continue-order-btn']");
    private final By signInLink = By.xpath("//a[normalize-space()='Sign In']");
    private final By checkoutButton = By.cssSelector("button[data-tag='checkout-btn']");
    private final By contactlessContainer = By.cssSelector(".container-contactless");
    private final By curbsideContainer = By.cssSelector(".container-curbside");
    private final By h4Ttile = By.tagName("h4");
    private final By continueToOrderButton = By.cssSelector("button[data-tag='continue-order-btn']");
    private final By goToPaymentButton = By.cssSelector("button[data-tag='go-to-payment-btn']");

    //actions

    // check validity of input
    public boolean checkValidity(By locator) {
        Object result = js.executeScript(
                "return arguments[0].checkValidity();", find(locator)
        );

        return Boolean.TRUE.equals(result);
    }
    //find locator
    public WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitModal(){
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".modal-backdrop-customize")
        ));
    }
    public void refresh() {
        driver.navigate().refresh();
    }

    public boolean isFirstNameValid(){
        return checkValidity(firstNameInput);
    }
    public boolean isLastNameValid(){
        return checkValidity(lastNameInput);
    }
    public boolean isPhoneNumberValid(){
        return checkValidity(phoneNumber);
    }
    public boolean isEmailAddressValid(){
        return checkValidity(emailAddress);
    }
    public boolean isSpecialRemarksValid(){
        return checkValidity(specialRemarks);
    }
    public boolean isHouseAddressValid(){
        return checkValidity(houseAddress);
    }
    public boolean isBarangayValid(){
        return checkValidity(barangay);
    }
    public boolean isLandmarkValid(){
        return checkValidity(landmark);
    }
    public boolean isCashChangeValid(){
        return checkValidity(cashChangeInput);
    }

    public void clickCheckoutButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));
        driver.findElement(checkoutButton).click();
    }
    public void clickGoToPaymentButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(goToPaymentButton));
        driver.findElement(goToPaymentButton).click();
    }
    public void clickHomeButton(){
        waitModal();
        wait.until(ExpectedConditions.elementToBeClickable(homeButton));
        driver.findElement(homeButton).click();
    }
    public void clickBackButton(){
        wait.until(ExpectedConditions.elementToBeClickable(backButton));
        driver.findElement(backButton).click();
    }
    public boolean isCheckoutButtonVisible(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkoutButton));
        return driver.findElements(checkoutButton).size() > 0;
    }
    public void clickChangeLink(){
        waitModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(changeLink));
        driver.findElement(changeLink).click();
    }
    public boolean isDateTimeScheduleVisible(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(dateTimeContainer));
        return driver.findElements(dateTimeContainer).size() > 0;
    }
    public String displayedSchedule(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(scheduleDate));
        return driver.findElement(scheduleDate).getText();
    }
    public void clickContinueToOrderButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(continueToOrderButton));
        driver.findElement(continueToOrderButton).click();
    }
    public void clickSignInLink(){
        waitModal();
        wait.until(ExpectedConditions.elementToBeClickable(signInLink));
        driver.findElement(signInLink).click();
    }
    public void clickCancelButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(cancelButton));
        driver.findElement(cancelButton).click();
    }
    public void clickContinueButton(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(continueButton));
        driver.findElement(continueButton).click();
    }
    public void selectDate(String date){
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownDate));
        WebElement dateDropdown = driver.findElement(dropdownDate);
        Select dropdown = new Select(dateDropdown);
        dropdown.selectByVisibleText(date);
    }
    public void selectTime(String time){
        wait.until(ExpectedConditions.visibilityOfElementLocated(dropdownTime));
        WebElement dateDropdown = driver.findElement(dropdownTime);
        Select dropdown = new Select(dateDropdown);
        dropdown.selectByVisibleText(time);
    }
    public void enterCustomerInformation(String firstName,
                                         String lastName,
                                         String phoneNumber,
                                         String email,
                                         String specialRemarks) {
        clearFirstName();
        clearLastName();
        clearPhoneNumber();
        clearEmail();
        clearSpecialRemarks();
        enterFirstName(firstName);
        enterLastName(lastName);
        enterPhoneNumber(phoneNumber);
        enterEmail(email);
        enterSpecialRemarks(specialRemarks);
    }
    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        driver.findElement(firstNameInput).sendKeys(firstName);
    }
    public void clearFirstName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(firstNameInput));
        driver.findElement(firstNameInput).clear();
    }
    public void enterLastName(String lastName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameInput));
        driver.findElement(lastNameInput).sendKeys(lastName);
    }
    public void clearLastName() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(lastNameInput));
        driver.findElement(lastNameInput).clear();
    }
    public void enterPhoneNumber(String mobileNumber) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneNumber));
        driver.findElement(phoneNumber).sendKeys(mobileNumber);
    }
    public void clearPhoneNumber() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(phoneNumber));
        driver.findElement(phoneNumber).clear();
    }
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailAddress));
        driver.findElement(emailAddress).sendKeys(email);
    }
    public void clearEmail() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailAddress));
        driver.findElement(emailAddress).clear();
    }
    public void enterSpecialRemarks(String remarks) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(specialRemarks));
        driver.findElement(specialRemarks).sendKeys(remarks);
    }
    public void clearSpecialRemarks() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(specialRemarks));
        driver.findElement(specialRemarks).clear();
    }

    public void enterDeliveryAddress(String houseAddress,
                                     String barangay,
                                     String landmark) {
        clearHouseAddress();
        clearBarangay();
        clearLandmark();
        enterHouseAddress(houseAddress);
        enterBarangay(barangay);
        enterLandmark(landmark);
    }
    public void enterHouseAddress(String address) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(houseAddress));
        driver.findElement(houseAddress).sendKeys(address);
    }
    public void clearHouseAddress() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(houseAddress));
        driver.findElement(houseAddress).clear();
    }
    public void enterBarangay(String inputBarangay) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(barangay));
        driver.findElement(barangay).sendKeys(inputBarangay);
    }
    public void clearBarangay() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(barangay));
        driver.findElement(barangay).clear();
    }
    public void enterLandmark(String inputLandmark) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(landmark));
        driver.findElement(landmark).sendKeys(inputLandmark);
    }
    public void clearLandmark() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(landmark));
        driver.findElement(landmark).clear();
    }

    public void clickCashOption (){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioCash));
        driver.findElement(radioCash).click();
    }
    public void inputCashCahnge(String change){
        clickCashOption();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cashChangeInput));
        driver.findElement(cashChangeInput).click();
        driver.findElement(cashChangeInput).sendKeys(change);
    }
    public void clearCashOptions() {
        clickCashOption();
        wait.until(ExpectedConditions.visibilityOfElementLocated(cashChangeInput));
        driver.findElement(cashChangeInput).click();
        driver.findElement(cashChangeInput).clear();
    }
    public String getErrorMessage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(errorMessage));
        return driver.findElement(errorMessage).getText();
    }
    public String getCashErrorMessage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(cashErrorMessage));
        return driver.findElement(cashErrorMessage).getText();
    }
    public String getSmallErrorMessage(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(smallErrorMessage));
        return driver.findElement(smallErrorMessage).getText();
    }
    public void clickCreditCardOption (){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioCredit));
        driver.findElement(radioCredit).click();
    }
    public void clickGCashOption (){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioGCash));
        driver.findElement(radioGCash).click();
    }
    public void clickMayaOption (){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioMaya));
        driver.findElement(radioMaya).click();
    }
    public String isCashSelected(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioCash));
        WebElement radio = driver.findElement(radioCash);
        return radio.getAttribute("class");
    }
    public String isCreditCardSelected(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioCredit));
        WebElement radio = driver.findElement(radioCredit);
        return radio.getAttribute("class");
    }
    public String isGCashSelected(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioGCash));
        WebElement radio = driver.findElement(radioGCash);
        return radio.getAttribute("class");
    }
    public String isMayaSelected(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(radioMaya));
        WebElement radio = driver.findElement(radioMaya);
        return radio.getAttribute("class");
    }
    public String isContactlessDeliveryEnabled() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(contactlessContainer));
        WebElement container = driver.findElement(contactlessContainer);
        String attribute = container.getAttribute("class");
        return attribute;
    }
    public String isCurbsideOptionEnabled() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(curbsideContainer));
        WebElement container = driver.findElement(curbsideContainer);
        String attribute = container.getAttribute("class");
        return attribute;
    }
    public List<WebElement> paymentOptionsList(){
        List<WebElement> paymentOptions = driver.findElements(
                By.cssSelector("[data-tag$='-name']")
        );
        return paymentOptions;
    }

    public void clickTermsLink(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(termsOfUseLink));
        driver.findElement(termsOfUseLink).click();
    }
    public void clickPrivacyLink(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(privacyPolicyLink));
        driver.findElement(privacyPolicyLink).click();
    }
    public String getTitle(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(h4Ttile));
        return driver.findElement(h4Ttile).getText();
    }
    public void closeTab(){
        driver.close();
    }

    public void clickTermsPrivacyCheckbox(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkboxTerms));
        driver.findElement(checkboxTerms).click();
    }

    public String isTermsPrivacyChecboxSelected(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkboxTerms));
        WebElement radio = driver.findElement(checkboxTerms);
        return radio.getAttribute("class");
    }

    public String switchWindowTabs(){
        String parent = driver.getWindowHandle();
        Set<String> windows = driver.getWindowHandles();
        String child = null;

        for (String window : windows) {
            if (!window.equals(parent)) {
                child = window;
                break;
            }
        }

        driver.switchTo().window(child);
        String Title = getTitle();
        driver.close();
        driver.switchTo().window(parent);
        return Title;
    }

    //Update Test Cases
    // -> CS-TC-21 disabled enabled depending on payment method
    // -> CS-TC-22 always disabled, unsure how to enable
    // -> Add Test Case to check what available Payment methods depending on if for Pickup or Delivery

}
