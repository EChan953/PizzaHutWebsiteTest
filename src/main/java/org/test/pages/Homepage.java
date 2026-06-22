package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Homepage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public Homepage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //locators
    private final By registerPageBtn = By.linkText("Register");
    private final By loginPageBtn = By.linkText("Login");
    private final By geolocationDiv = By.cssSelector("div[role='tabpanel']");
    private final By addressDiv = By.xpath("//div[contains(@class,'bg-white p-4')]");
    private final By deliveryTab = By.cssSelector("div[title='Delivery']");
    private final By pickupTab = By.cssSelector("div[title='Pickup']");
    private final By goButton = By.cssSelector("button[aria-label='Go']");
    private final By addressTextbox = By.id("address-autocomplete");
    private final By addressError = By.cssSelector(".alert-warning.border-radius.p-2");
    private final By addressOptions = By.cssSelector(".pac-item .pac-matched");
    private final By currentLocation = By.xpath("//span[normalize-space()='Or use my current location']");
    private final By currentLocationError = By.xpath("(//small[@class='text-danger'])[1]");
    private final By changeAddressButton = By.xpath("//button[normalize-space()='Or change address']");
    private final By changeAddressYes = By.xpath("//div[@class='col p-3 cursor-pointer text-center']");
    private final By mapAddressBox = By.cssSelector(".container-address-info.p-3");
    private final By mapCursor = By.cssSelector("div[tabindex='-1']");

    //actions
    public void openWebsite(String SITE){
        driver.get(SITE);
    }
    // finds the web element; uses explicit wait for reliability
    public WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // click on register page button
    public void clickRegisterPageButton() {
        driver.findElement(registerPageBtn).click();
    }
    public void clickLoginPageButton() {
        driver.findElement(loginPageBtn).click();
    }
    public String getHomePageUrl() {
        String url = driver.getCurrentUrl();
        return url;
    }

    public void clickDeliveryTab() {
        driver.findElement(deliveryTab).click();
    }

    public void clickPickupTab() {
        driver.findElement(pickupTab).click();
    }

    // input address
    public void enterAddress(String addressInput) {
        find(addressTextbox).sendKeys(addressInput);
    }

    // find and select the best matching address option
    public void selectBestAddressOption(String expectedAddress) {
        List<WebElement> items = driver.findElements(By.cssSelector(".pac-item"));

        WebElement bestMatch = null;
        for (WebElement item : items) {
            // collect all pac-matched spans inside this item
            List<WebElement> matched = item.findElements(By.cssSelector(".pac-matched"));
            StringBuilder sb = new StringBuilder();
            for (WebElement el : matched) {
                sb.append(el.getText()).append(" ");
            }
            String concatenated = sb.toString().trim();

            // compare against expected input
            if (concatenated.contains(expectedAddress)) {
                bestMatch = item;
                break; // stop once we find the exact match
            }
        }

        // fallback: if no exact match, just click the first suggestion
        if (bestMatch == null && !items.isEmpty()) {
            bestMatch = items.get(0);
        }

        if (bestMatch != null) {
            bestMatch.click();
        } else {
            throw new RuntimeException("No address options available to select.");
        }
    }

    public void clickGoButton() {
        driver.findElement(goButton).click();
    }

    public void clickCurrentLocation() {
        driver.findElement(currentLocation).click();
    }

    // validation methods
    public boolean isGeolocationDivDisplayed() {
        return find(geolocationDiv).isDisplayed();
    }

    public boolean isAddressDivDisplayed() {
        return find(addressDiv).isDisplayed();
    }

    public void clearAddress() {
        if (isAddressDivDisplayed()) {
            driver.findElement(changeAddressButton).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    changeAddressYes
            ));
            driver.findElement(changeAddressYes).click();
        }
    }

    // check if delivery tab is selected
    public boolean isDeliverySelected() {
        return Objects.requireNonNull(find(deliveryTab).getAttribute("class")).contains("active");
    }

    // check if pickup tab is selected
    public boolean isPickupSelected() {
        return Objects.requireNonNull(find(pickupTab).getAttribute("class")).contains("active");
    }

    // check if address error message is displayed
    public boolean isAddressErrorDisplayed() {
        return find(addressError).isDisplayed();
    }

    // check if address dropdown items are available
    public boolean isAddressOptionDisplayed() {
        List<WebElement> options = driver.findElements(addressOptions);
        return !options.isEmpty();
    }

    // check if current location error message is displayed
    public boolean isCurrentLocationErrorDisplayed() {
        return find(currentLocationError).isDisplayed();
    }

    // check if map for delivery address is displayed
    public boolean isDeliveryMapDisplayed() {
        return find(mapAddressBox).isDisplayed();
    }

}
