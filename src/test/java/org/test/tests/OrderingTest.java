package org.test.tests;

import com.aventstack.extentreports.Status;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.test.base.BaseTest;
import org.test.pages.Homepage;
import org.test.pages.OrderingPage;
import org.test.pages.RegisterPage;
import org.test.utils.ExcelReader;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OrderingTest extends BaseTest {
    private Homepage homepage;
    private OrderingPage orderingPage;

    private static final String SITE = "https://www.pizzahut.com.ph/";
    private static final String ORDER = SITE + "order";
    private static final String CHECKOUT = SITE + "checkout";
    private static final String excelFilePath = "src/test/resources/testdata/TestData.xlsx";

    @BeforeTest(alwaysRun = true)
    public void initPage() {
        homepage = new Homepage(driver);
        orderingPage = new OrderingPage(driver);
    }

    @BeforeMethod(alwaysRun = true)
    public void setupPreRequisite(Method method) {
        // FOR Pre-Requisite: User is on the Registration Page
        if(method.getName().contains("OSTC001")) {
            extentTest.log(Status.INFO, "Loading Homepage");
            driver.get(SITE);
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector("div[role='tabpanel']")
            ));
        }
        else {
            extentTest.log(Status.INFO, "Loading Order Page");
            driver.get(ORDER);
            wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.cssSelector(".container-main-content")
            ));
        }
    }

    //Test Data for ordering
    @DataProvider(name="orderingTestData")
    public Iterator<Object[]> getOrderTestData(Method method){

        String rawTestCaseID = method.getName();
        String testCaseID = rawTestCaseID.split("_")[0].replace("OSTC","OS-TC-");

        List<Map<String, String>> allData =
                ExcelReader.readExcelData(excelFilePath, "Ordering_Data");

        List<Map<String, String>> filtered =
                ExcelReader.filterByTestCase(allData, testCaseID);

        List<Object[]> result = new ArrayList<>();

        for (Map<String, String> map : filtered) {
            result.add(new Object[]{map});
        }

        return result.iterator();
    }

    // Test Data for address input
    @DataProvider(name="geolocationTestData")
    public Iterator<Object[]> getGeolocationTestData(Method method) {
        String rawTestCaseID = method.getName();

        //Update Test Case Identifier
        String testCaseID = rawTestCaseID.split("_")[0].replace("OSTC","OS-TC-");

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

    // input address for ordering page
    public void inputAddress(Map<String, String> data) {
        // Input valid address
        String expectedAddress = data.get("Address");
        homepage.enterAddress(expectedAddress);

        // Click on closest-matching address option
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("pac-item")));
        homepage.selectBestAddressOption(expectedAddress);

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[class='container-localization-info media ml-4 cursor-pointer'] span[class='font-weight-bold']")
        ));
    }

    // wait for checkout
    public void waitForCheckout() {
        wait.until(ExpectedConditions.urlToBe(CHECKOUT));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("body")));
    }

    // Verify Navigation to Order Menu
    @Test(dataProvider = "geolocationTestData", groups = {"smoke", "regression", "e2e"})
    public void OSTC001_shouldNavigateToOrderMenu(Map<String, String> data) {
        // 1. Navigate to the order page after choosing their mode of order collection.
        inputAddress(data);

        // 2. Verify that the order page has successfully loaded
        extentTest.log(Status.INFO, "Verifying that order page has successfully loaded");
        Assert.assertTrue(driver.getCurrentUrl().contains("order"), "Not redirected to Order page.");
    }

    // Verify Menu Area
    @Test(groups = {"smoke", "regression"})
    public void OSTC002_verifyMenuAreaVisibility() {
        // 1. Verify that the menu area has successfully loaded and is on the left side of the website
        Assert.assertTrue(orderingPage.isMenuAreaPresent());
    }

    // Verify Your Cart Area
    @Test(groups = {"smoke", "regression"})
    public void OSTC003_verifyCartAreaVisibility() {
        // 1. Verify that the Your Cart area has successfully loaded and is on the right side of the website
        Assert.assertTrue(orderingPage.isCartAreaPresent());
    }

    // Verify DEALS Tab
    @Test(groups = {"regression"})
    public void OSTC004_verifyDealsTab() {
        // 1. Click on the DEALS tab
        extentTest.log(Status.INFO, "Clicking DEALS Tab");
        orderingPage.clickDealsTab();

        // 2. Verify that the DEALS tab has successfully loaded
        Assert.assertEquals(orderingPage.getActiveTabName(), "DEALS");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify DEALS Filter Functionality
    @Test(dataProvider = "orderingTestData", groups = {"regression"})
    public void OSTC005_verifyDealsFilterFunctionality(Map<String, String> data) {
        // 1. Click on the DEALS tab
        extentTest.log(Status.INFO, "Clicking DEALS Tab");
        orderingPage.clickDealsTab();

        // 2. Click on a filter
        extentTest.log(Status.INFO, "Clicking on filter: " + data.get("Filter"));
        orderingPage.clickFilter(data.get("Filter"));

        // 3. Verify that products under the selected filter option are the only ones displayed
        extentTest.log(Status.INFO, "Verifying that filter works");
        Assert.assertTrue(orderingPage.isDealsFilterWorking(data.get("Filter")));
    }

    // Verify PIZZA Tab
    @Test(groups = {"regression"})
    public void OSTC006_verifyPizzaTab() {
        // 1. Click on the PIZZA tab
        extentTest.log(Status.INFO, "Clicking PIZZA Tab");
        orderingPage.clickPizzaTab();

        // 2. Verify that the PIZZA tab has successfully loaded
        Assert.assertEquals(orderingPage.getActiveTabName(), "PIZZA");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify PIZZA Filter Functionality
    @Test(dataProvider = "orderingTestData", groups = {"regression"})
    public void OSTC007_verifyPizzaFilterFunctionality(Map<String, String> data) {
        // 1. Click on the PIZZA tab
        extentTest.log(Status.INFO, "Clicking PIZZA Tab");
        orderingPage.clickPizzaTab();

        // 2. Click on a filter
        extentTest.log(Status.INFO, "Clicking on filter: " + data.get("Filter"));
        orderingPage.clickFilter(data.get("Filter"));

        // 3. Verify that products under the selected filter option are the only ones displayed
        extentTest.log(Status.INFO, "Verifying that filter works");
        Assert.assertTrue(orderingPage.isPizzaFilterWorking(data.get("Filter")));
    }

    // Verify MELTS Tab
    @Test(groups = {"regression"})
    public void OSTC008_verifyMeltsTab() {
        // 1. Click on the MELTS tab
        extentTest.log(Status.INFO, "Clicking MELTS Tab");
        orderingPage.clickMeltsTab();

        // 2. Verify that the MELTS tab has successfully loaded
        Assert.assertEquals(orderingPage.getActiveTabName(), "MELTS");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify Pasta Tab
    @Test(groups = {"regression"})
    public void OSTC009_verifyPastaTab() {
        // 1. Click on the PASTA tab
        extentTest.log(Status.INFO, "Clicking PASTA Tab");
        orderingPage.clickPastaTab();

        // 2. Verify that the PASTA tab has successfully loaded
        Assert.assertEquals(orderingPage.getActiveTabName(), "PASTA");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify Pasta Filter Functionality
    @Test(dataProvider = "orderingTestData", groups = {"regression"})
    public void OSTC010_verifyPastaFilterFunctionality(Map<String, String> data) {
        // 1. Click on the PASTA tab
        extentTest.log(Status.INFO, "Clicking PASTA Tab");
        orderingPage.clickPastaTab();

        // 2. Click on a filter
        extentTest.log(Status.INFO, "Clicking on filter: " + data.get("Filter"));
        orderingPage.clickFilter(data.get("Filter"));

        // 3. Verify that products under the selected filter option are the only ones displayed
        extentTest.log(Status.INFO, "Verifying that filter works");
        Assert.assertTrue(orderingPage.isPastaFilterWorking(data.get("Filter")));
    }

    // Verify Wingstreet Tab
    @Test(groups = {"regression"})
    public void OSTC011_verifyWingstreetTab() {
        // 1. Click on the WINGSTREET tab
        extentTest.log(Status.INFO, "Clicking WINGSTREET Tab");
        orderingPage.clickWingstreetTab();

        // 2. Verify that the WINGSTREET tab has successfully loaded
        Assert.assertEquals(orderingPage.getActiveTabName(), "WINGSTREET®");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // verify chicken&sides tab
    @Test(groups = {"regression"})
    public void OSTC012_verifyChickenAndSidesTab() {
        // 1. Click on the CHICKEN & SIDES tab
        extentTest.log(Status.INFO, "Clicking CHICKEN & SIDES Tab");
        orderingPage.clickChickenAndSidesTab();

        // 2. Verify that the CHICKEN & SIDES tab has successfully loaded
        Assert.assertEquals(orderingPage.getActiveTabName(), "CHICKEN & SIDES");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // verify beverages tab
    @Test(groups = {"regression"})
    public void OSTC013_verifyBeveragesTab() {
        // 1. Click on the BEVERAGES tab
        extentTest.log(Status.INFO, "Clicking BEVERAGES Tab");
        orderingPage.clickBeveragesTab();

        // 2. Verify that the BEVERAGES tab has successfully loaded
        Assert.assertEquals(orderingPage.getActiveTabName(), "BEVERAGES");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    @Test(dataProvider = "orderingTestData", groups = {"regression"})
    public void OSTC014_verifyProductListingNoCustomizationOrVariationAddToCart(Map<String, String> data) {
        // 1. Click on the BEVERAGES tab
        extentTest.log(Status.INFO, "Clicking BEVERAGES Tab");
        orderingPage.clickBeveragesTab();

        // 2. Click on Add button of a product with no customization/variation
        extentTest.log(Status.INFO, "Adding Product: " + data.get("Product"));
        orderingPage.addToCartNoCustomizationOrVariation(data.get("Product"));

        // 3. Verify that the product is successfully added to cart
        extentTest.log(Status.INFO, "Verifying that product is successfully added to cart");
        Assert.assertTrue(orderingPage.isProductInsideCart(data.get("Product")));
    }

    @Test(dataProvider = "orderingTestData", groups = {"regression"})
    public void OSTC015_verifyProductListingWithCustomizationAddToCart(Map<String, String> data) {
        // 1. Click on the DEALS tab
        extentTest.log(Status.INFO, "Clicking DEALS Tab");
        orderingPage.clickDealsTab();

        // 2. Select a product
        // 3. Select customizations to the product by clicking on possibles option
        // 4. Click "Add deal to my basket" button
        extentTest.log(Status.INFO, "Adding Deal to Cart");
        orderingPage.addDealsToCart(data.get("Product"));

        // 5. Verify that the product is successfully added to cart
        extentTest.log(Status.INFO, "Verifying that product is successfully added to cart");
        Assert.assertTrue(orderingPage.isProductInsideCart(data.get("Product")));
    }

    // Verify Product Listing (with Variation)
    @Test(dataProvider = "orderingTestData", groups = {"smoke", "regression"})
    public void OSTC016_verifyProductListingWithVariationDropdownAddToCart(Map<String, String> data) {
        // 1. Click on the PIZZA tab
        extentTest.log(Status.INFO, "Clicking PIZZA Tab");
        orderingPage.clickPizzaTab();

        // 2. Select a variation option on a dropdown of a product
        // 3. Click on Add button
        extentTest.log(Status.INFO, "Adding Pizza to Cart: " + data.get("Product") + " - " + data.get("Variation"));
        orderingPage.addPizzaToCartWithVariation(data.get("Product"), data.get("Variation"));

        // 5. Verify that the product is successfully added to cart
        extentTest.log(Status.INFO, "Verifying that product is successfully added to cart");
        Assert.assertTrue(orderingPage.isProductInsideCart(data.get("Product")));
    }

    @Test(dataProvider = "orderingTestData", groups = {"regression"})
    public void OSTC017_verifyProductListingWithVariationPopupAddToCart(Map<String, String> data) {
        // 1. Click on a product listing with a variation (do not click on dropdown area)
        // Click on the PIZZA tab
        extentTest.log(Status.INFO, "Clicking PIZZA Tab");
        orderingPage.clickPizzaTab();

        // 2. Select variation of the product
        // 3. Click "Add deal to my basket" button
        extentTest.log(Status.INFO, "Adding Pizza to Cart");
        orderingPage.addPizzaToCartWithCustomization(data.get("Product"), data.get("Customization1"), data.get("Customization2"));

        // 5. Verify that the product is successfully added to cart
        extentTest.log(Status.INFO, "Verifying that product is successfully added to cart");
        Assert.assertTrue(orderingPage.isProductInsideCart(data.get("Product")));
    }



    // verify checkout button works
    @Test(dataProvider = "orderingTestData", groups = {"smoke", "regression", "e2e"})
    public void OSTC041_verifyCheckoutButton(Map<String, String> data) {
        // 1. Add products to cart with a total minimum of PHP 299
        extentTest.log(Status.INFO, "Clicking PIZZA Tab");
        orderingPage.clickPizzaTab();

        extentTest.log(Status.INFO, "Adding Pizza to Cart: " + data.get("Product") + " - " + data.get("Variation"));
        orderingPage.addPizzaToCartWithVariation(data.get("Product"), data.get("Variation"));

        // 2. Click on Checkout button
        extentTest.log(Status.INFO, "Verifying Checkout Button Availability");
        Assert.assertTrue(orderingPage.isCheckoutAvailable());

        extentTest.log(Status.INFO, "Clicking Checkout Button");
        orderingPage.clickCheckout();

        // 3. Verify that the user is redirected to the Checkout page
        waitForCheckout();
        extentTest.log(Status.INFO, "Verifying that user is redirected to Checkout page");
        Assert.assertEquals(driver.getCurrentUrl(), CHECKOUT, "User is not redirected to Checkout page.");
    }
}
