package org.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class EmailPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public EmailPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //locators
    private static final String SITE = "https://yopmail.com/en/";
    private final By emailInput = By.cssSelector("input[class='ycptinput']");
    private final By arrowButton = By.cssSelector("button[class='md']");
    private final By resetLink = By.xpath("//a[contains(@href, 'setnewpassword')]");
    private final By refreshButton = By.xpath("//button[@id='refresh']");

    //actions

    public void redirectToEmailPage(){
        driver.switchTo().newWindow(WindowType.TAB);
        driver.get(SITE);
    }
    public void enterEmail(String email) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        driver.findElement(emailInput).sendKeys(email);
    }
    public void clickArrowButton(){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0, 300)");
        wait.until(ExpectedConditions.visibilityOfElementLocated(arrowButton));
        driver.findElement(arrowButton).click();
    }
    public void clickResetLink() {
        wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt("ifmail"));
        wait.until(ExpectedConditions.visibilityOfElementLocated(resetLink));
        driver.findElement(resetLink).click();
    }
    public void swtichToNewestTab(){
        List<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(tabs.size() - 1));
    }
    public void refreshEmail() throws InterruptedException {
        Thread.sleep(20000);
        wait.until(ExpectedConditions.visibilityOfElementLocated(refreshButton));
        driver.findElement(refreshButton).click();
    }

}
