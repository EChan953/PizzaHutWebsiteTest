package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class LoginPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    //locators
    private final By pageTitle = By.cssSelector("h4[data-tag='login-title']");
    private final By backButton = By.cssSelector("div[data-tag='back-btn']");
    private final By emailInput = By.cssSelector("input[type='email']");
    private final By passwordInput = By.cssSelector("input[type='password']");
    private final By toastError = By.cssSelector("div[data-tag='toast-error']");
    private final By toastSuccess = By.cssSelector("div[data-tag='toast-success']");
    private final By homeButton = By.cssSelector("img[class='image-desktop']");
    private final By loginButton = By.cssSelector("button[data-tag='login-btn']");
    private final By emailErrorMessage = By.cssSelector("div[class='invalid-feedback d-block']");
    private final By logoutButton = By.cssSelector("a[data-tag='logout-lbl']");
    private final By profileButton = By.partialLinkText("Hello");
    private final By forgotYourPasswordLink = By.cssSelector("a[data-tag='forgot-password-btn']");
    private final By resetPasswordButton = By.cssSelector("button[data-tag='send-password-reset-btn']");
    private final By resetPasswordEmailInput = By.cssSelector("input[data-tag='email-txt']");

    //actions

    public String getLoginTitle() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(pageTitle));
        return driver.findElement(pageTitle).getText().trim();
    }

    public void clickBack() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(backButton));
        driver.findElement(backButton).click();
    }

    public void clickHomeButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(homeButton));
        driver.findElement(homeButton).click();
    }

    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        driver.findElement(emailInput).sendKeys(email);
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void login(String email, String password) {
        enterEmail(email);
        enterPassword(password);
    }

    public void clickLoginButton(){
        driver.findElement(loginButton).click();
    }

    public String getErrorMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(toastError));
        return driver.findElement(toastError).getText();
    }

    public String getSuccessMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess));
        return driver.findElement(toastSuccess).getText();
    }

    public String getEmailErroMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailErrorMessage));
        return driver.findElement(emailErrorMessage).getText();
    }
    public void clickLogoutButton(){
        wait.until(ExpectedConditions.invisibilityOfElementLocated(toastSuccess));
        wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton));
        wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        WebElement logoutbtn = driver.findElement(logoutButton);
        Actions actions = new Actions(driver);
        actions.moveToElement(logoutbtn).click().perform();

    }
    public boolean isProfileButtonVisiblec() throws InterruptedException {
        Thread.sleep(1000);
        List<WebElement> elements = driver.findElements(profileButton);
        return !elements.isEmpty();
    }
    public void clickForgotYourPasswordLink(){
        driver.findElement(forgotYourPasswordLink).click();
    }
    public void clickResetPasswordButton(){
        driver.findElement(resetPasswordButton).click();
    }
    public void enterForgetPasswordEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(resetPasswordEmailInput));
        driver.findElement(resetPasswordEmailInput).sendKeys(email);
    }
    public void clearResetPasswordEmail(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(resetPasswordEmailInput));
        driver.findElement(resetPasswordEmailInput).clear();
    }


}
