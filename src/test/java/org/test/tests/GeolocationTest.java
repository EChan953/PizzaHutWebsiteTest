package org.test.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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

        WebElement check = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']")
        ));

        // Check if on Order Page
        Assert.assertEquals(driver.getCurrentUrl(), ORDER, "Not redirected to Order page.");

        // Check if Address Input is Correct
        Assert.assertTrue(check.getText().contains(data.get("Address")));

        // Revert back to default homepage for other test cases
        driver.get(SITE);
        homepage.clearAddress();
    }

    @Test(groups = {"regression"})
    public void GTSTC006_denyLocationPermission() {
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
    public void GTSTC007_allowLocationPermission() {
        // Access Homepage
        loadHomepage();

        // Click on "Or use my current location"
        homepage.clickCurrentLocation();

        // Assert that Delivery Map and Address is Displayed
        Assert.assertTrue(homepage.isDeliveryMapDisplayed(), "Delivery Map is not displayed.");
    }
}
