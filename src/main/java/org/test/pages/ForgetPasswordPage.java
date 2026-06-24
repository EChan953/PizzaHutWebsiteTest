package org.test.pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ForgetPasswordPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public ForgetPasswordPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //locators
    private final By passwordInput = By.cssSelector("input[data-tag='password-lbl']");
    private final By revealPasswordButton = By.cssSelector("span[class='input-group-text cursor-pointer']");
    private final By confirmPasswordInput = By.cssSelector("input[data-tag='confirm-password']");
    private final By resetPasswordButton = By.cssSelector("button[data-tag='reset-password-btn']");
    private final By passwordErrorMessage = By.cssSelector("div[class='invalid-feedback d-block']");

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

    public boolean isPasswordInputValid(){
        return checkValidity(passwordInput);
    }
    public boolean isConfirmPasswordInputValid(){
        return checkValidity(confirmPasswordInput);
    }
    public void clickRevealPasswordButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(revealPasswordButton));
        driver.findElement(revealPasswordButton).click();
    }

    public void clickResetPasswordButton() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(resetPasswordButton));
        driver.findElement(resetPasswordButton).click();
    }

    public void enterPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        driver.findElement(passwordInput).sendKeys(password);
    }

    public void enterConfirmPassword(String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordInput));
        driver.findElement(confirmPasswordInput).sendKeys(password);
    }

    public String getPlainTextPassword(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        String pass = driver.findElement(passwordInput).getAttribute("value");
        return pass;
    }
    public String getEmailErroMessage() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordErrorMessage));
        return driver.findElement(passwordErrorMessage).getText();
    }
    public void clearPasswordInput(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(passwordInput));
        driver.findElement(passwordInput).clear();
    }
    public void clearConfirmPasswordInput(){
        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmPasswordInput));
        driver.findElement(confirmPasswordInput).clear();
    }
}
