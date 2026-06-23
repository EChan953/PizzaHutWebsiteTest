package org.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.NoSuchElementException;

import static org.openqa.selenium.support.locators.RelativeLocator.with;

public class Homepage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;

    public Homepage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // locators
    private final By registerPageBtn = By.linkText("Register");
    private final By geolocationDiv = By.cssSelector("div[role='tabpanel']");
    private final By addressDiv = By.xpath("//div[contains(@class,'bg-white p-4')]");
    private final By deliveryTab = By.cssSelector("div[title='Delivery']");
    private final By pickupTab = By.cssSelector("div[title='Pickup']");
    private final By goButton = By.cssSelector("button[aria-label='Go']");
    private final By addressTextbox = By.id("address-autocomplete");
    private final By addressError = By.cssSelector(".alert-warning.border-radius.p-2");
    private final By addressOptions = By.cssSelector(".pac-item .pac-matched");
    private final By currentLocation = By.xpath("//span[normalize-space()='Or use my current location']");
    private final By nearestHutLink = By.xpath("//span[normalize-space()='Find my nearest Hut']");
    private final By currentLocationError = By.xpath("(//small[@class='text-danger'])[1]");
    private final By changeAddressButton = By.xpath("//button[normalize-space()='Or change address']");
    private final By changeAddressYes = By.xpath("//div[@class='col p-3 cursor-pointer text-center']");
    private final By changeAddressCancel = By.cssSelector(".col.p-3.cursor-pointer.text-center.border-right");
    private final By map = By.cssSelector(".container-map");
    private final By mapAddressBox = By.cssSelector(".container-address-info.p-3");
    private final By continueOrderButton = By.xpath("//button[normalize-space()='Continue to order']");
    private final By nearHutText = By.cssSelector(".font-weight-bold.mb-1");
    private final By myHutButton = By.className("btn-success");
    private final By changeAddressOrderPage = By.xpath("(//button[contains(@type,'button')][normalize-space()='Change'])[1]");

    // actions

    // finds the web element; uses explicit wait for reliability
    public WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // click on register page button
    public void clickRegisterPageButton() {
        driver.findElement(registerPageBtn).click();
    }

    // click on delivery tab in homepage
    public void clickDeliveryTab() {
        driver.findElement(deliveryTab).click();
    }

    // click on pickup tab in homepage
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

    // click on go button
    public void clickGoButton() {
        driver.findElement(goButton).click();
    }

    // click on "Or use my current location"
    public void clickCurrentLocation() {
        driver.findElement(currentLocation).click();
    }

    // click on "Find my nearest Hut"
    public void clickNearestHut() {
        driver.findElement(nearestHutLink).click();
    }

    // click on "Continue to order" button (Pre-order)
    public void clickContinueOrderButton() {
        driver.findElement(continueOrderButton).click();
    }

    // click on "Change" button in Order page
    public void clickChangeAddressOrderPage() {
        driver.findElement(changeAddressOrderPage).click();
    }

    // click on "Or change address" in modal
    public void clickChangeAddressModal() {
        driver.findElement(changeAddressButton).click();
    }

    // click on Cancel of confirmation popup
    public void clickChangeAddressCancel() {
        driver.findElement(changeAddressCancel).click();
    }

    // click on Yes of confirmation popup
    public void clickChangeAddressYes() {
        driver.findElement(changeAddressYes).click();
    }

    // validation methods
    // check if geolocation div in homepage is displayed
    public boolean isGeolocationDivDisplayed() {
        return find(geolocationDiv).isDisplayed();
    }

    // check if address div is displayed (when there is inputted location)
    public boolean isAddressDivDisplayed() {
        return find(addressDiv).isDisplayed();
    }

    // clears address in homepage
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
        return find(map).isDisplayed() && find(mapAddressBox).isDisplayed();
    }

    // check address in order page based on business hours
    public void checkTime() {
        // Get current local time
        LocalTime now = LocalTime.now();
        LocalTime startBusiness = LocalTime.of(9, 0);   // 10:00 AM
        LocalTime endBusiness   = LocalTime.of(23, 0);   // 11:00 PM

        if (!now.isBefore(startBusiness) && !now.isAfter(endBusiness)) {
            // Business Hours
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']")
            ));
        } else {
            // Out of Hours
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".p-2.alert-warning")
            ));
            clickContinueOrderButton();
        }
    }
}
