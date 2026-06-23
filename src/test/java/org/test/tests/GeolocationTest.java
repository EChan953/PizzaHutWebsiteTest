package org.test.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.test.base.BaseTest;
import org.test.pages.Homepage;
import org.test.utils.DriverFactory;
import org.test.utils.ExcelReader;
import org.test.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.*;

@Listeners(TestListener.class)
public class GeolocationTest extends BaseTest {
    private Homepage homepage;

    private static final String SITE = "https://www.pizzahut.com.ph/";
    private static final String ORDER = SITE + "order/deal";
    private static final String excelFilePath = "src/test/resources/testdata/TestData.xlsx";

    @BeforeTest(groups = {"regression"})
    public void initPage() {
        homepage = new Homepage(driver);
    }

    // Test Data
    @DataProvider(name="geolocationTestData")
    public Iterator<Object[]> getGeolocationTestData(Method method) {

        String rawTestCaseID = method.getName();

        //Update Test Case Identifier
        String testCaseID = rawTestCaseID.split("_")[0].replace("GTSTC","GTS-TC-");

        //Update Sheet Name
        List<Map<String, String>> allData =
                ExcelReader.readExcelData(excelFilePath, "Geolocation_Tracking_Data");

        List<Map<String, String>> filtered =
                ExcelReader.filterByTestCase(allData, testCaseID);

        List<Object[]> result = new ArrayList<>();

        for (Map<String, String> map : filtered) {
            result.add(new Object[]{map});
        }

        return result.iterator();

    }

    public void loadHomepage() {
        // Load Homepage
        driver.get(SITE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[role='tabpanel']")
        ));
    }

    // Verify Homepage is Accessible
    @Test(groups = {"smoke", "regression", "e2e"})
    public void GTSTC001_verifyHomepageAccessibility() {
        // Access Homepage
        loadHomepage();

        // Assert URL
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, SITE);

        // Check if Geolocation div is present
        Assert.assertTrue(homepage.isGeolocationDivDisplayed(), "User is not redirected to Homepage.");
    }

    // Verify if user can toggle between Delivery and Pickup Tab
    @Test(groups = {"smoke", "regression", "e2e"})
    public void GTSTC002_verifySwitchBetweenDeliveryPickup() {
        // Access Homepage
        loadHomepage();

        // Check if Delivery tab is the default selection
        Assert.assertTrue(homepage.isDeliverySelected(),"Delivery Tab is not the default option.");

        // Navigate to Pickup tab and assert
        homepage.clickPickupTab();
        Assert.assertTrue(homepage.isPickupSelected(), "Pickup Tab hasn't been selected.");

        // Navigate back to Delivery tab and assert
        homepage.clickDeliveryTab();
        Assert.assertTrue(homepage.isDeliverySelected(), "Delivery Tab hasn't been selected.");
    }

    @Test(groups = {"regression"})
    public void GTSTC003_noDeliveryAddress() {
        // Access Homepage
        loadHomepage();

        // Click Go button w/o inputting address
        homepage.clickGoButton();
        Assert.assertTrue(homepage.isAddressErrorDisplayed(), "Address error message should be displayed.");
    }

    @Test(dataProvider = "geolocationTestData", groups = {"regression"})
    public void GTSTC004_invalidDeliveryAddress(Map<String, String> data) {
        // Access Homepage
        loadHomepage();

        // Input invalid address
        homepage.enterAddress(data.get("Address"));

        // Assert that there are no address options displayed
        Assert.assertFalse(homepage.isAddressOptionDisplayed(), "No address options should be displayed.");
    }

    @Test(dataProvider = "geolocationTestData", groups = {"smoke", "regression", "e2e"})
    public void GTSTC005_validDeliveryAddress(Map<String, String> data) {
        // Access Homepage
        loadHomepage();

        // Input valid address
        String expectedAddress = data.get("Address");
        homepage.enterAddress(expectedAddress);

        // Click on closest-matching address option
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption(expectedAddress);

        // Navigate to Order Page based on business hours
        homepage.checkTime();

        // Check if on Order Page
        Assert.assertEquals(driver.getCurrentUrl(), ORDER, "Not redirected to Order page.");

        // Check if Address Input is Correct
        WebElement orderAddress = driver.findElement(By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']"));
        Assert.assertTrue(orderAddress.getText().contains(data.get("Address")));

        // Revert back to default homepage for other test cases
        driver.get(SITE);
        homepage.clearAddress();
    }

    @Test(groups = {"regression"})
    public void GTSTC006_denyDeliveryLocationPermission() {
        WebDriver geoDriver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME, 2); // deny
        WebDriverWait geoWait = new WebDriverWait(geoDriver, Duration.ofSeconds(10));
        Homepage geoHomepage = new Homepage(geoDriver);

        geoDriver.get(SITE);
        geoHomepage.clickCurrentLocation();

        geoWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//small[@class='text-danger'])[1]")
        ));

        Assert.assertTrue(geoHomepage.isCurrentLocationErrorDisplayed(),
                "Current Location error message is not displayed.");

        geoDriver.quit();
    }

    @Test(groups = {"smoke", "regression", "e2e"})
    public void GTSTC007_allowDeliveryLocationPermission() {
        // Access Homepage
        loadHomepage();

        // Click on "Or use my current location"
        homepage.clickCurrentLocation();

        // Assert that Delivery Map and Address is Displayed
        Assert.assertTrue(homepage.isDeliveryMapDisplayed(), "Delivery Map is not displayed.");
    }

    @Test(groups = {"regression"})
    public void GTSTC009_noPickupAddress() {
        // Access Homepage
        loadHomepage();

        // Click on Pickup Tab
        homepage.clickPickupTab();

        // Click Go button w/o inputting address
        homepage.clickGoButton();
        Assert.assertTrue(homepage.isAddressErrorDisplayed(), "Address error message should be displayed.");
    }

    @Test(dataProvider = "geolocationTestData", groups = {"regression"})
    public void GTSTC010_invalidPickupAddress(Map<String, String> data) {
        // Access Homepage
        loadHomepage();

        // Click on Pickup Tab
        homepage.clickPickupTab();

        // Input invalid address
        homepage.enterAddress(data.get("Address"));

        // Assert that there are no address options displayed
        Assert.assertFalse(homepage.isAddressOptionDisplayed(), "No address options should be displayed.");
    }

    @Test(dataProvider = "geolocationTestData", groups = {"smoke", "regression", "e2e"})
    public void GTSTC011_validPickupAddress(Map<String, String> data) {
        // Access Homepage
        loadHomepage();

        // Click on Pickup Tab
        homepage.clickPickupTab();

        // Input valid address
        String expectedAddress = data.get("Address");
        homepage.enterAddress(expectedAddress);

        // Click on closest-matching address option
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption(expectedAddress);

        // Explicit wait until nearest huts with map appear
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".container-store-content.w-100.p-3")
        ));

        Assert.assertTrue(check.isDisplayed(), "List of Pizza Hut Establishments are not listed.");
    }

    @Test(groups = {"regression"})
    public void GTSTC012_denyPickupLocationPermission() {
        WebDriver geoDriver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME, 2); // deny
        WebDriverWait geoWait = new WebDriverWait(geoDriver, Duration.ofSeconds(10));
        Homepage geoHomepage = new Homepage(geoDriver);

        geoDriver.get(SITE);
        geoHomepage.clickPickupTab();
        geoHomepage.clickNearestHut();

        geoWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//small[@class='text-danger'])[1]")
        ));

        Assert.assertTrue(geoHomepage.isCurrentLocationErrorDisplayed(),
                "Current Location error message is not displayed.");

        geoDriver.quit();
    }

    @Test(groups = {"smoke", "regression", "e2e"})
    public void GTSTC013_acceptPickupLocationPermission() {
        // Access Homepage
        loadHomepage();

        // Click on Pickup Tab
        homepage.clickPickupTab();

        // Click on "Find my nearest Hut"
        homepage.clickNearestHut();

        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".container-store-content.w-100.p-3")
        ));

        Assert.assertTrue(check.isDisplayed(), "List of Pizza Hut Establishments are not listed.");
    }

    @Test(groups = {"regression"})
    public void GTSTC015_cancelLocationUpdate() {
        // Pre-requisite: Needs an address
        loadHomepage();
        homepage.enterAddress("123 Shaw Boulevard");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption("123 Shaw Boulevard");
        homepage.checkTime(); // comment out for flow of testing in regression and e2e?

        driver.get(ORDER);

        String pickupLocation = driver.findElement(By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']")).getText();

        homepage.clickChangeAddressOrderPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'bg-white p-4')]")
        ));

        homepage.clickChangeAddressModal();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'container-content')]")
        ));

        homepage.clickChangeAddressCancel();

        String modalLocation = driver.findElement(By.cssSelector("p[class='font-weight-bold mb-0']")).getText();
        Assert.assertEquals(modalLocation, pickupLocation, "Locations are not matching.");
    }

    @Test(groups = {"regression", "e2e"})
    public void GTSTC016_clearLocationUpdate() {
        driver.get(ORDER);

        homepage.clickChangeAddressOrderPage();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'bg-white p-4')]")
        ));

        homepage.clickChangeAddressModal();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'container-content')]")
        ));

        homepage.clickChangeAddressYes();

        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#address-autocomplete")
        ));

        Assert.assertTrue(check.isDisplayed(), "Location has not been changed.");
    }
}
