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

    @BeforeTest(groups = {"smoke", "regression", "e2e", "ordering"})
    public void initPage() {
        homepage = new Homepage(driver);
        orderingPage = new OrderingPage(driver);
    }

    @BeforeMethod(groups = {"smoke", "regression", "e2e", "ordering"})
    public void setupPreRequisite(Method method) {
        // FOR Pre-Requisite: User is on the Registration Page
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
        extentTest.log(Status.INFO, "Loading Homepage");
        driver.get(SITE);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div[role='tabpanel']")
        ));

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
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h4")));
    }

    // Verify Navigation to Order Menu
    @Test(dataProvider = "geolocationTestData", groups = {"smoke", "regression", "ordering"})
    public void OSTC001_shouldNavigateToOrderMenu(Map<String, String> data) {
        // 1. Navigate to the order page after choosing their mode of order collection.
        inputAddress(data);

        // 2. Verify that the order page has successfully loaded
        extentTest.log(Status.INFO, "Verifying that order page has successfully loaded");
        Assert.assertTrue(driver.getCurrentUrl().contains("order"), "Not redirected to Order page.");
    }

    // Verify Menu Area
    @Test(groups = {"smoke", "regression", "ordering"})
    public void OSTC002_verifyMenuAreaVisibility() {
        extentTest.log(Status.INFO, "Loading Order Page");
        driver.get(ORDER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".container-main-content")
        ));
        // 1. Verify that the menu area has successfully loaded and is on the left side of the website
        extentTest.log(Status.INFO, "Verifying that Menu area is visible");
        Assert.assertTrue(orderingPage.isMenuAreaPresent());
    }

    // Verify Your Cart Area
    @Test(groups = {"smoke", "regression",  "ordering"})
    public void OSTC003_verifyCartAreaVisibility() {
        // 1. Verify that the Your Cart area has successfully loaded and is on the right side of the website
        extentTest.log(Status.INFO, "Verifying that Cart area is visible");
        Assert.assertTrue(orderingPage.isCartAreaPresent());
    }

    // Verify that clicking the Pizza Hut logo redirects the user to the homepage
    @Test(groups = {"smoke", "ordering"})
    public void OSTC004_verifyLogoRedirection() {
        // 1. Click on the Pizza Hut Logo
        extentTest.log(Status.INFO, "Clicking Pizza Hut Logo");
        orderingPage.clickPizzaHutLogo();

        // 2. Verify that the user is redirected to the homepage
        extentTest.log(Status.INFO, "Verifying redirection to Homepage");
        Assert.assertEquals(driver.getCurrentUrl(), SITE, "Not redirected to Homepage");
    }

    // Verify DEALS Tab
    @Test(groups = {"regression", "ordering"})
    public void OSTC005_verifyDealsTab() {
        // 1. Click on the DEALS tab
        extentTest.log(Status.INFO, "Loading Order Page");
        driver.get(ORDER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".container-main-content")
        ));
        extentTest.log(Status.INFO, "Clicking DEALS Tab");
        orderingPage.clickDealsTab();

        // 2. Verify that the DEALS tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Deals tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "DEALS");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify DEALS Filter Functionality
    @Test(dataProvider = "orderingTestData", groups = {"regression", "ordering"})
    public void OSTC006_verifyDealsFilterFunctionality(Map<String, String> data) {
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
    @Test(groups = {"ordering"})
    public void OSTC007_verifyPizzaTab() {
        // 1. Click on the PIZZA tab
        extentTest.log(Status.INFO, "Clicking PIZZA Tab");
        orderingPage.clickPizzaTab();

        // 2. Verify that the PIZZA tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Pizza tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "PIZZA");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify PIZZA Filter Functionality
    @Test(dataProvider = "orderingTestData", groups = {"ordering"})
    public void OSTC008_verifyPizzaFilterFunctionality(Map<String, String> data) {
        // 1. Click on the PIZZA tab
        extentTest.log(Status.INFO, "Clicking PIZZA Tab");
        orderingPage.clickPizzaTab();

        // 2. Click on a filter
        extentTest.log(Status.INFO, "Clicking on filter: " + data.get("Filter"));
        orderingPage.clickFilter(data.get("Filter"));

        // 3. Verify that products under the selected filter option are the only ones displayed
        extentTest.log(Status.INFO, "Verifying that filter works");
        Assert.assertTrue(orderingPage.isPizzaFilterWorking(data.get("Filter")));

        extentTest.log(Status.INFO, "Resetting Filter");
        orderingPage.clickFilter(data.get("Filter"));
    }

    // Verify MELTS Tab
    @Test(groups = {"ordering"})
    public void OSTC009_verifyMeltsTab() {
        // 1. Click on the MELTS tab
        extentTest.log(Status.INFO, "Clicking MELTS Tab");
        orderingPage.clickMeltsTab();

        // 2. Verify that the MELTS tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Melts tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "MELTS");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify Pasta Tab
    @Test(groups = {"ordering"})
    public void OSTC010_verifyPastaTab() {
        // 1. Click on the PASTA tab
        extentTest.log(Status.INFO, "Clicking PASTA Tab");
        orderingPage.clickPastaTab();

        // 2. Verify that the PASTA tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Pasta tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "PASTA");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // Verify Pasta Filter Functionality
    @Test(dataProvider = "orderingTestData", groups = {"ordering"})
    public void OSTC011_verifyPastaFilterFunctionality(Map<String, String> data) {
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
    @Test(groups = {"ordering"})
    public void OSTC012_verifyWingstreetTab() {
        // 1. Click on the WINGSTREET tab
        extentTest.log(Status.INFO, "Clicking WINGSTREET Tab");
        orderingPage.clickWingstreetTab();

        // 2. Verify that the WINGSTREET tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Wingstreet tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "WINGSTREET®");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // verify chicken&sides tab
    @Test(groups = {"ordering"})
    public void OSTC013_verifyChickenAndSidesTab() {
        // 1. Click on the CHICKEN & SIDES tab
        extentTest.log(Status.INFO, "Clicking CHICKEN & SIDES Tab");
        orderingPage.clickChickenAndSidesTab();

        // 2. Verify that the CHICKEN & SIDES tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Chicken&Sides tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "CHICKEN & SIDES");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    // verify beverages tab
    @Test(groups = {"ordering"})
    public void OSTC014_verifyBeveragesTab() {
        // 1. Click on the BEVERAGES tab
        extentTest.log(Status.INFO, "Clicking BEVERAGES Tab");
        orderingPage.clickBeveragesTab();

        // 2. Verify that the BEVERAGES tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Beverages tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "BEVERAGES");
        Assert.assertTrue(orderingPage.areMenuItemsLoaded());
    }

    @Test(dataProvider = "orderingTestData", groups = {"regression", "ordering"})
    public void OSTC015_verifyProductListingNoCustomizationOrVariationAddToCart(Map<String, String> data) {
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

    // Verify Product Listing (with Variation)
    @Test(dataProvider = "orderingTestData", groups = {"smoke", "ordering"})
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

    @Test(dataProvider = "orderingTestData", groups = {"ordering"})
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

    // Verify Vouchers Tab
    @Test(groups = {"ordering"})
    public void OSTC018_verifyVouchersTab() {
        // 1. Click on the Vouchers tab
        extentTest.log(Status.INFO, "Clicking Vouchers Tab");
        orderingPage.clickVouchersTab();

        // 2. Verify that the Vouchers tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Vouchers tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "VOUCHERS");
        Assert.assertTrue(orderingPage.isVoucherAddBtnPresent());
    }

    // Verify "Choose from voucher list" button works
    @Test(groups = {"ordering"})
    public void OSTC019_verifyChooseFromVoucherListFunctionality() {
        // 1. Click on "Choose from voucher list" Button located in Your Cart section
        orderingPage.clickChooseFromVouchersListBtn();

        // 2. Verify that the VOUCHERS tab has successfully loaded
        extentTest.log(Status.INFO, "Verifying that Vouchers tab is loaded");
        Assert.assertEquals(orderingPage.getActiveTabName(), "VOUCHERS");
        Assert.assertTrue(orderingPage.isVoucherAddBtnPresent());
    }

    // Verify product qty increase functionality
    @Test(dataProvider = "orderingTestData", groups = {"regression", "ordering"})
    public void OSTC025_verifyIncreaseProductQuantity(Map<String, String> data) {
        // 1. Click on + button of an added product in Your Cart section
        extentTest.log(Status.INFO, "Increasing Quantity of " + data.get("Product"));
        int origQty = orderingPage.getItemQty(data.get("Product"));
        orderingPage.increaseItemQty(data.get("Product"));

        // 2. Verify that the product quantity has been incremented
        extentTest.log(Status.INFO, "Verifying that quantity increased");
        Assert.assertEquals(orderingPage.getItemQty(data.get("Product")), origQty + 1, "Product Quantity not increased");
    }

    // Verify product qty decrease functionality
    @Test(dataProvider = "orderingTestData", groups = {"regression", "ordering"})
    public void OSTC026_verifyDecreaseProductQuantity(Map<String, String> data) {
        // 1. Click on - button of an added product in Your Cart section
        extentTest.log(Status.INFO, "Decreasing Quantity of " + data.get("Product"));
        int origQty = orderingPage.getItemQty(data.get("Product"));
        orderingPage.decreaseItemQty(data.get("Product"));

        // 2. Verify that the product quantity has been incremented
        extentTest.log(Status.INFO, "Verifying that quantity decreased");
        Assert.assertEquals(orderingPage.getItemQty(data.get("Product")), origQty - 1, "Product Quantity not decreased");
    }

    // Verify product remove functionality
    @Test(dataProvider = "orderingTestData", groups = {"regression", "ordering"})
    public void OSTC027_verifyRemoveProduct(Map<String, String> data) {
        // 1. Click on + button of an added product in Your Cart section
        extentTest.log(Status.INFO, "Removing " + data.get("Product"));
        orderingPage.deleteItem(data.get("Product"));

        // 2. Verify that the product quantity has been incremented
        extentTest.log(Status.INFO, "Verifying that product is removed");
        Assert.assertFalse(orderingPage.isProductInsideCart(data.get("Product")), "Product not removed");
    }

    // Verify Senior Citizen/PWD/NAC Discount Checkbox
    @Test(groups = {"ordering"})
    public void OSTC028_verifySeniorCitizenDiscountChkbox() {
        // 1. Click on "I am a Senior Citizen/PWD/NAC" checkbox
        extentTest.log(Status.INFO, "Clicking Senior Citizen Discount Checkbox");
        orderingPage.clickSeniorCitizenChkbox();

        // 2. Verify that the checkbox is checked and an input area has been displayed
        extentTest.log(Status.INFO, "Verifying that Senior Citizen Discount checkbox is ticked");
        Assert.assertTrue(orderingPage.isSeniorCitizenChkboxTicked());
    }

    @Test(groups = {"regression", "ordering"})
    public void OSTC038_verifyMinimumPriceMsg() {
        // 1. Remove the products in the Your Cart section
        // Assume that this is already finished
        // 2. Verify that the minimum price message is displayed
        extentTest.log(Status.INFO, "Verifying that min. price msg is displayed");
        Assert.assertTrue(orderingPage.isMinDeliveryOrderWarningDisplayed());
    }

    @Test(groups = {"regression", "ordering"})
    public void OSTC039_verifyDisabledCheckoutBtn() {
        // Pre-Req: User is on the order page and has no added products to cart
        // 1. Click on Checkout button -> no code needed, cant click a disabled button
        // 2. Verify that the checkout button is gray and unclickable
        extentTest.log(Status.INFO, "Verifying that Checkout button is disabled");
        Assert.assertFalse(orderingPage.isCheckoutAvailable());
    }

    @Test(dataProvider = "orderingTestData", groups = {"smoke", "regression", "e2e", "ordering"})
    public void OSTC040_verifyDealsProductListing(Map<String, String> data) {
        // Pre-req: user should be on the order page
        extentTest.log(Status.INFO, "Loading Order Page");
        driver.get(ORDER);
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".container-main-content")
        ));

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

    // verify checkout button works
    @Test(groups = {"smoke", "regression", "e2e", "ordering"})
    public void OSTC041_verifyCheckoutButton() {
        // 1. Click on Checkout button
        extentTest.log(Status.INFO, "Verifying Checkout Button Availability");
        Assert.assertTrue(orderingPage.isCheckoutAvailable());

        extentTest.log(Status.INFO, "Clicking Checkout Button");
        orderingPage.clickCheckout();

        // 2. Verify that the user is redirected to the Checkout page
        waitForCheckout();
        extentTest.log(Status.INFO, "Verifying that user is redirected to Checkout page");
        Assert.assertEquals(driver.getCurrentUrl(), CHECKOUT, "User is not redirected to Checkout page.");
    }
}
