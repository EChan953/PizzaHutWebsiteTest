package org.test.tests;

import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.test.base.BaseTest;
import org.test.pages.Homepage;
import org.test.utils.DriverFactory;
import org.test.utils.ExcelReader;
import org.test.utils.ExtentManager;
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

    @BeforeTest(groups = {"smoke", "regression", "e2e", "geolocation"})
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
    @Test(groups = {"smoke", "regression", "geolocation"})
    public void GTSTC001_verifyHomepageAccessibility() {
        // 1. Access Homepage
        extentTest.info("Access Homepage");
        loadHomepage();

        // 2. Verify successful homepage access
        extentTest.info("Verify that Homepage is Accessed");
        String currentUrl = driver.getCurrentUrl();
        Assert.assertEquals(currentUrl, SITE);
        Assert.assertTrue(homepage.isGeolocationDivDisplayed(), "User is not redirected to Homepage."); // check if Geolocation div is present
    }

    // Verify if user can switch between Delivery and Pickup Tab
    @Test(groups = {"smoke", "regression", "geolocation"})
    public void GTSTC002_verifySwitchBetweenDeliveryPickup() {
        // 1. Navigate to Homepage
        extentTest.info("Navigate to Homepage");
        loadHomepage();

        // 2. Verify Delivery tab as default tab
        extentTest.info("Verify Delivery tab as default tab");
        Assert.assertTrue(homepage.isDeliverySelected(),"Delivery Tab is not the default option.");

        // 3. Navigate to Pickup tab
        extentTest.info("Navigate to Pickup tab");
        homepage.clickPickupTab();

        // 4. Verify that Pickup tab is selected
        extentTest.info("Verify that Pickup tab is selected");
        Assert.assertTrue(homepage.isPickupSelected(), "Pickup Tab hasn't been selected.");

        // 5. Navigate to Delivery tab
        extentTest.info("Navigate to Delivery tab");
        homepage.clickDeliveryTab();

        // 6. Verify that Delivery tab is selected
        extentTest.info("Verify that Delivery tab is selected");
        Assert.assertTrue(homepage.isDeliverySelected(), "Delivery Tab hasn't been selected.");
    }

    // Verify user cannot proceed without inputting address (Delivery)
    @Test(groups = {"regression", "geolocation"})
    public void GTSTC003_noDeliveryAddress() {
        // 1. Navigate to Delivery tab
        extentTest.info("Navigate to Delivery Tab");
        loadHomepage();

        // 2. Click Go button without inputting address
        extentTest.info("Click on Go button with no address");
        homepage.clickGoButton();

        // 3. Verify that user cannot proceed to Order page
        extentTest.info("Verify that user cannot proceed to order page");
        Assert.assertTrue(homepage.isAddressErrorDisplayed(), "Address error message should be displayed.");
    }

    // Verify user cannot select an invalid address from dropdown (Delivery)
    @Test(dataProvider = "geolocationTestData", groups = {"regression", "geolocation"})
    public void GTSTC004_invalidDeliveryAddress(Map<String, String> data) {
        // 1. Navigate to Delivery Tab
        extentTest.info("Navigate to Delivery Tab");
        loadHomepage();

        // 2. Input invalid address
        extentTest.info("Input invalid address");
        homepage.enterAddress(data.get("Address"));

        // 3. Verify that there are no address options displayed
        extentTest.info("Verify no address options are displayed");
        Assert.assertFalse(homepage.isAddressOptionDisplayed(), "No address options should be displayed.");
    }

    // Verify user can enter and select a valid address from dropdown (Delivery)
    @Test(dataProvider = "geolocationTestData", groups = {"smoke", "regression", "geolocation"})
    public void GTSTC005_validDeliveryAddress(Map<String, String> data) {
        // 1. Navigate to Delivery tab
        extentTest.info("Navigate to Delivery tab");
        loadHomepage();

        // 2. Input valid address
        extentTest.info("Input valid address");
        String expectedAddress = data.get("Address");
        homepage.enterAddress(expectedAddress);

        // 3. Select closest-matching address from dropdown options
        extentTest.info("Select address from dropdown options");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption(expectedAddress);

        // 4. Verify that user proceeds to order page
        extentTest.info("Verify user is redirected to order page");
        homepage.checkTime(); // navigate to order page based on business hours
        Assert.assertEquals(driver.getCurrentUrl(), ORDER, "Not redirected to Order page.");
        // Check if Address Input is Correct
        WebElement orderAddress = driver.findElement(By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']"));
        Assert.assertTrue(orderAddress.getText().contains(data.get("Address")));

        // Revert back to default homepage for other test cases
        driver.get(SITE);
        homepage.clearAddress();
    }

    // Verify user can enter and select a valid address from dropdown (Delivery)
    @Test(dataProvider = "geolocationTestData", groups = {"e2e", "geolocation"})
    public void GTSTC005_validDeliveryAddressE2E(Map<String, String> data) {
        // 1. Navigate to Delivery tab
        extentTest.info("Navigate to Delivery tab");
        loadHomepage();

        // 2. Input valid address
        extentTest.info("Input valid address");
        String expectedAddress = data.get("Address");
        homepage.enterAddress(expectedAddress);

        // 3. Select closest-matching address from dropdown options
        extentTest.info("Select address from dropdown options");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption(expectedAddress);

        // 4. Verify that user proceeds to order page
        extentTest.info("Verify user is redirected to order page");
        homepage.checkTime(); // navigate to order page based on business hours
        Assert.assertEquals(driver.getCurrentUrl(), ORDER, "Not redirected to Order page.");
        // Check if Address Input is Correct
        WebElement orderAddress = driver.findElement(By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']"));
        Assert.assertTrue(orderAddress.getText().contains(data.get("Address")));
    }

    // Verify denial of location permission (Delivery)
    @Test(groups = {"regression", "geolocation"})
    public void GTSTC006_denyDeliveryLocationPermission() {
        // 1. Navigate to Delivery tab
        extentTest.info("Navigate to Delivery tab");
        WebDriver geoDriver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME, 2); // deny
        WebDriverWait geoWait = new WebDriverWait(geoDriver, Duration.ofSeconds(10));
        Homepage geoHomepage = new Homepage(geoDriver);
        geoDriver.get(SITE);

        // 2. Click on "Use my current location"
        extentTest.info("Click on \"Use my current location\"");
        geoHomepage.clickCurrentLocation();

        // 3. Deny location permission
        // Done in browser configuration

        // 4. Verify unsuccessful retrieval of current location
        extentTest.info("Verify unsuccessful retrieval of current location");
        geoWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//small[@class='text-danger'])[1]")
        ));
        Assert.assertTrue(geoHomepage.isCurrentLocationErrorDisplayed(),
                "Current Location error message is not displayed.");

        // Quit driver for denying location permission
        geoDriver.quit();
    }

    // Verify accepting of location permission (Delivery)
    @Test(groups = {"smoke", "regression", "geolocation"})
    public void GTSTC007_allowDeliveryLocationPermission() {
        // 1. Navigate to Delivery tab
        extentTest.info("Navigate to Delivery tab");
        loadHomepage();

        // 2. Click on "Use my current location"
        extentTest.info("Click on \"Use my current location\"");
        homepage.clickCurrentLocation();

        // 3. Accept location permission
        // Done in browser configuration

        // 4. Verify map and address is displayed
        extentTest.info("Verify map and address is displayed");
        Assert.assertTrue(homepage.isDeliveryMapDisplayed(), "Delivery Map is not displayed.");
    }

    // Verify user cannot proceed without inputting address (Pickup)
    @Test(groups = {"regression", "geolocation"})
    public void GTSTC009_noPickupAddress() {
        // 1. Navigate to Pickup tab
        extentTest.info("Navigate to Pickup tab");
        loadHomepage();
        homepage.clickPickupTab();

        // 2. Click Go button without inputting address
        extentTest.info("Click on Go button with no address");
        homepage.clickGoButton();

        // 3. Verify that user cannot proceed to Order page
        extentTest.info("Verify user cannot proceed to Order page");
        Assert.assertTrue(homepage.isAddressErrorDisplayed(), "Address error message should be displayed.");
    }

    // Verify user cannot select an invalid address from dropdown (Pickup)
    @Test(dataProvider = "geolocationTestData", groups = {"regression", "geolocation"})
    public void GTSTC010_invalidPickupAddress(Map<String, String> data) {
        // 1. Navigate to Pickup tab
        extentTest.info("Navigate to Pickup tab");
        loadHomepage();
        homepage.clickPickupTab();

        // 2. Input invalid address
        extentTest.info("Input invalid address");
        homepage.enterAddress(data.get("Address"));

        // 3. Verify that there are no address options displayed
        extentTest.info("Verify no address options are displayed");
        Assert.assertFalse(homepage.isAddressOptionDisplayed(), "No address options should be displayed.");
    }

    // Verify user can enter and select a valid address from dropdown (Pickup)
    @Test(dataProvider = "geolocationTestData", groups = {"smoke", "regression", "geolocation"})
    public void GTSTC011_validPickupAddress(Map<String, String> data) {
        // 1. Navigate to Pickup tab
        extentTest.info("Navigate to Pickup tab");
        loadHomepage();
        homepage.clickPickupTab();

        // 2. Input valid address
        extentTest.info("Input valid address");
        String expectedAddress = data.get("Address");
        homepage.enterAddress(expectedAddress);

        // 3. Select closest-matching address from dropdown options
        extentTest.info("Select address from dropdown options");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption(expectedAddress);

        // 4. Verify user is redirected to Nearest Huts map
        extentTest.info("Verify user is redirected to Nearest Huts map");
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".container-store-content.w-100.p-3")
        ));
        Assert.assertTrue(check.isDisplayed(), "List of Pizza Hut Establishments are not listed.");
    }

    // Verify denial of location permission (Pickup)
    @Test(groups = {"regression", "geolocation"})
    public void GTSTC012_denyPickupLocationPermission() {
        // 1. Navigate to Pickup tab
        extentTest.info("Navigate to Pickup Tab");
        WebDriver geoDriver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME, 2); // deny
        WebDriverWait geoWait = new WebDriverWait(geoDriver, Duration.ofSeconds(10));
        Homepage geoHomepage = new Homepage(geoDriver);
        geoDriver.get(SITE);
        geoHomepage.clickPickupTab();

        // 2. Click on "Find nearest hut"
        extentTest.info("Click on \"Find nearest hut\"");
        geoHomepage.clickNearestHut();

        // 3. Deny location permission
        // Done in browser configuration

        // 4. Verify unsuccessful retrieval of current location
        geoWait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//small[@class='text-danger'])[1]")
        ));
        Assert.assertTrue(geoHomepage.isCurrentLocationErrorDisplayed(),
                "Current Location error message is not displayed.");

        // Quit driver for denying location permission
        geoDriver.quit();
    }

    // Verify Nearest Huts map is displayed through "Find my nearest hut" feature
    @Test(groups = {"smoke", "regression", "geolocation"})
    public void GTSTC013_allowPickupLocationPermission() {
        // 1. Navigate to Pickup tab
        extentTest.info("Navigate to Pickup tab");
        loadHomepage();
        homepage.clickPickupTab();

        // 2. Click on "Find my nearest Hut"
        extentTest.info("Click on \"Find my nearest Hut\"");
        homepage.clickNearestHut();

        // 3. Accept location permission
        // Done in browser configuration

        // Verify user is redirected to Nearest Huts map
        extentTest.info("Verify user is redirect to Nearest Huts map");
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".container-store-content.w-100.p-3")
        ));
        Assert.assertTrue(check.isDisplayed(), "List of Pizza Hut Establishments are not listed.");
    }

    // Verify system behavior when user cancels updating of location
    @Test(groups = {"regression", "geolocation"})
    public void GTSTC015_cancelLocationUpdate() {
        // Pre-requisite: Needs an address
        loadHomepage();
        homepage.enterAddress("123 Shaw Boulevard");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption("123 Shaw Boulevard");
        homepage.checkTime(); // comment out for flow of testing in regression and e2e
        driver.get(ORDER);

        // Initialization of Address
        String actualLocation = driver.findElement(By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']")).getText();

        // 1. Click "Change" beside the Delivery Address or Pickup Establishment
        extentTest.info("Click \"Change\" beside the Delivery Address or Pickup Establishment");
        homepage.clickChangeAddressOrderPage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'bg-white p-4')]")
        ));

        // 2. Click "Or change my address" from Delivery Details Modal
        extentTest.info("Click \"Or change my address\" from Delivery Details Modal");
        homepage.clickChangeAddressModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'container-content')]")
        ));

        // 3. Click "Cancel" from the Confirmation popup
        extentTest.info("Click \"Cancel\" from the Confirmation popup");
        homepage.clickChangeAddressCancel();

        // 4. Verify that the location does not change
        extentTest.info("Verify that the location does not change");
        String modalLocation = driver.findElement(By.cssSelector("p[class='font-weight-bold mb-0']")).getText();
        Assert.assertEquals(modalLocation, actualLocation, "Locations are not matching.");
    }

    // Verify system behavior when user updates location
    @Test(groups = {"regression", "geolocation"})
    public void GTSTC016_clearLocationUpdate() {
        // 1. Click "Change" beside the Delivery Address or Pickup Establishment
        extentTest.info("Click \"Change\" beside the Delivery Address or Pickup Establishment");
        driver.get(ORDER);
        homepage.clickChangeAddressOrderPage();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'bg-white p-4')]")
        ));

        // 2. Click "Or change my address" from Delivery Details Modal
        extentTest.info("Click \"Or change my address\" from Delivery Details Modal");
        homepage.clickChangeAddressModal();
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[contains(@class,'container-content')]")
        ));

        // 3. Click "Yes" from the Confirmation popup
        extentTest.info("Click \"Yes\" from the Confirmation popup");
        homepage.clickChangeAddressYes();

        // 4. Verify that the location is cleared
        extentTest.info("Verify that the location is cleared");
        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("#address-autocomplete")
        ));
        Assert.assertTrue(check.isDisplayed(), "Location has not been changed.");
    }
}
