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

    public void clickNearestHut() {
        driver.findElement(nearestHutLink).click();
    }

    public void clickContinueOrderButton() {
        driver.findElement(continueOrderButton).click();
    }

    public void clickMyHutButton() {
        // driver.findElement(myHutButton).click();
        js.executeScript("arguments[0].click();", myHutButton);
    }

    public String chooseAnyNearHut() {
        List<WebElement> nearestHutsText = driver.findElements(nearHutText);
        List<WebElement> nearestHutsDivs = driver.findElements(By.cssSelector(".item.w-100.p-2.pl-3.pr-3"));
        String hutText;

        if (!nearestHutsText.isEmpty()) {
            // Pick a random index
            int randomIndex = new Random().nextInt(nearestHutsText.size());
            System.out.println(randomIndex);

            // Click the randomly chosen hut
            // Find the radio input to the left of the <p> text
            js.executeScript("arguments[0].click();", nearestHutsDivs.get(randomIndex));
            hutText = nearestHutsText.get(randomIndex).getText().replaceFirst("^\\d+\\.\\s*", "");
            System.out.println(hutText);
        } else {
            throw new NoSuchElementException("No huts found with locator: " + nearestHutsText.toString());
        }

        clickMyHutButton();

        return hutText;
    }

    public void clickChangeAddressOrderPage() {
        driver.findElement(changeAddressOrderPage).click();
    }

    public void clickChangeAddressModal() {
        driver.findElement(changeAddressButton).click();
    }

    public void clickChangeAddressCancel() {
        driver.findElement(changeAddressCancel).click();
    }

    public void clickChangeAddressYes() {
        driver.findElement(changeAddressYes).click();
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
