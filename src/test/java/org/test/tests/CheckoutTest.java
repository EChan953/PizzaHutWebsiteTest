package org.test.tests;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.SourceType;
import org.test.base.BaseTest;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.test.pages.*;
import org.test.utils.ExcelReader;
import org.test.utils.ExtentManager;
import org.test.utils.TestListener;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Listeners(TestListener.class)
public class CheckoutTest extends BaseTest {
    private CheckoutPage checkout;
    private LoginPage login;
    private Homepage homepage;
    private Actions actions;

    private static final String SITE = "https://www.pizzahut.com.ph/";
    private static final String CHECKOUT = SITE + "checkout";
    private static final String excelFilePath = "src/test/resources/testdata/TestData.xlsx";

    @BeforeTest(groups = {"smoke", "regression", "login", "e2e", "checkoutTest"})
    public void initPage() {
        checkout = new CheckoutPage(driver);
        login = new LoginPage(driver);
        homepage = new Homepage(driver);
        actions = new Actions(driver);
    }
    //Test Data
    //Update Object and DataProvider Name
    @DataProvider(name="checkoutTestData")
    public Iterator<Object[]> getCheckoutTestData(Method method){
        String rawTestCaseID = method.getName();
        //Update Test Case Identifier
        String testCaseID = rawTestCaseID.split("_")[0].replace("CSTC","CS-TC-");
        //Update Sheet Name
        List<Map<String, String>> allData =
                ExcelReader.readExcelData(excelFilePath, "Checkout_System_Data");
        List<Map<String, String>> filtered =
                ExcelReader.filterByTestCase(allData, testCaseID);
        List<Object[]> result = new ArrayList<>();
        for (Map<String, String> map : filtered) {
            result.add(new Object[]{map});
        }
        return result.iterator();
    }

    public void CreateOrder() throws InterruptedException {
        //Redirection Code to Order Page / TEMP
        driver.get(SITE);
        driver.findElement(By.id("address-autocomplete")).sendKeys("1");
        Thread.sleep(1000);
        actions.sendKeys(Keys.DOWN).perform();
        Thread.sleep(1000);
        actions.sendKeys(Keys.ENTER).perform();
        Thread.sleep(1000);
//        WebElement preorderBtn = driver.findElement(By.cssSelector("button[data-tag='pre-order-btn']"));
//        wait.until(ExpectedConditions.elementToBeClickable(preorderBtn));
//       preorderBtn.click();
        Thread.sleep(5000);
        driver.findElement(By.cssSelector("ul.navbar-nav li.nav-item.item-category:nth-of-type(2)")).click();
        Thread.sleep(1000);
        List<WebElement> addButtons = driver.findElements(By.cssSelector("[data-tag='item-btn']"));
        addButtons.get(0).click();
    }

    //Tests
    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC001_verifyCheckoutPageAccessibility() throws InterruptedException {
        //Redirect to Order Page - Replace once merged
        CreateOrder();

        //1. Click the "Checkout" button
        extentTest.info("Click the \"Checkout\" button");
        checkout.clickCheckoutButton();

        //2. Verify that the Checkout Page is displayed
        extentTest.info("Verify that the Checkout Page is displayed");
        String actual = checkout.getTitle();
        Assert.assertEquals(actual, "Secure Checkout", "Checkout Page Not Found");

    }
    @Test(groups = {"smoke", "regression", "checkoutTest"})
    public void CSTC002_verifyBackButton(){
        //Continue from previous test "CSTC001"
        //2. Click on the "< Back" button located on the upper left corner
        extentTest.info("Click on the \"< Back\" button located on the upper left corner");
        checkout.clickBackButton();

        //3. Verify that the previous webpage is displayed
        //Check how to verify order page, maybe check if checkout button is present again
        extentTest.info("Verify that the previous webpage is displayed");
        Boolean actual = checkout.isCheckoutButtonVisible();
        Assert.assertTrue(actual, "Redirection Error");

    }
    @Test(groups = {"smoke", "regression", "checkoutTest"})
    public void CSTC003_verifyLogoRedirection(){
        //1. Click the "Checkout" button
        extentTest.info("Click the \"Checkout\" button");
        checkout.clickCheckoutButton();

        //2. Click on the Pizza Hut logo located on the top of the Login page
        extentTest.info("Click on the \"< Back\" button located on the upper left corner");
        checkout.clickHomeButton();

        //3. Verify that the homepage is displayed
        extentTest.info("Verify that the previous webpage is displayed");
        String actual = homepage.getHomePageUrl();
        Assert.assertEquals(actual,"https://www.pizzahut.com.ph/", "Homepage not Found");

    }
    @Test(groups = {"smoke", "regression", "checkoutTest"})
    public void CSTC004_verifySignInLink() throws InterruptedException {
        //Redirection Code to Order Page
        checkout.clickContinueToOrderButton();

        //1. Click the "Checkout" button
        extentTest.info("Click the \"Checkout\" button");
        checkout.clickCheckoutButton();

        //2. Click on the Sign In link
        extentTest.info("Click on the Sign In link");
        checkout.clickSignInLink();

        //3. Verify that the Login Page is displayed
        Thread.sleep(5000);
        extentTest.info("Verify that the Login Page is displayed");
        String actual = checkout.getTitle();
        Assert.assertEquals(actual,"Login", "Login Page not Found");

        //Redirection Code to Order Page
        checkout.clickHomeButton();
        checkout.clickContinueToOrderButton();
        checkout.clickCheckoutButton();
        Thread.sleep(2000);
    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC005_verifyDateTimePopup() throws InterruptedException {
        //Continue from previous test "CSTC004"
        //2. Click "Change" link besides date/time delivery
        extentTest.info("Click \"Change\" link besides date/time delivery");
        checkout.clickChangeLink();

        //3. Verify that the Schedule Update Pop-up is displayed
        extentTest.info("Verify that the Schedule Update Pop-up is displayed");
        checkout.isDateTimeScheduleVisible();

    }
    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC006_verifyDateTimeCancel(Map<String, String> data){
        //Continue from previous test "CSTC005"
        //3. Enter new order date and/or time
        extentTest.info("Enter new order date and/or time ");
        checkout.selectDate(data.get("Date"));
        checkout.selectTime(data.get("Time"));

        //4. Click "Cancel"
        extentTest.info("Click \"Cancel\"");
        checkout.clickCancelButton();

        //5. Verify that the Schedule Update Pop-up closes
        extentTest.info("Verify that the Schedule Update Pop-up closes");
        String actual = checkout.displayedSchedule();
        Assert.assertEquals(actual, "ASAP", "Invalid Schedule Update");
    }

    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC007_verifyDateTimeContinue(Map<String, String> data){
        //Continue from previous test "CSTC006"
        //2. Click "Change" link besides date/time delivery
        extentTest.info("Click \"Change\" link besides date/time delivery");
        checkout.clickChangeLink();

        //3. Enter new order date and/or time
        extentTest.info("Enter new order date and/or time ");
        checkout.selectDate(data.get("Date"));
        checkout.selectTime(data.get("Time"));

        //4. Click "Continue to order"
        extentTest.info("Click \"Continue to order\"");
        checkout.clickContinueButton();

        //5. Verify date and time is updated
        extentTest.info("Verify date and time is updated");
        String actual = checkout.displayedSchedule();
        String expected = data.get("Date") +" "+ data.get("Time");
        Assert.assertEquals(actual, expected, "Invalid Schedule Update");


    }
    @Test(dataProvider = "checkoutTestData",groups = {"regression", "checkoutTest"})
    public void CSTC008_verifyEmptyCustomerInformation(Map<String, String> data){
        //Continue from previous test "CSTC007"
        //2. Click "Go to payment" button
        extentTest.info("Click \"Go to payment\" button");
        checkout.clickGoToPaymentButton();

        //3. Verify Customer Information fields displays an error
        extentTest.info("Verify Customer Information fields displays an error");
        Assert.assertFalse(checkout.isFirstNameValid(), "Validation Error");
        Assert.assertFalse(checkout.isLastNameValid(), "Validation Error");
        Assert.assertFalse(checkout.isPhoneNumberValid(), "Validation Error");
        Assert.assertFalse(checkout.isEmailAddressValid(), "Validation Error");
        Assert.assertTrue(checkout.isSpecialRemarksValid(), "Validation Error");
    }
    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC009_verifyInvalidPhoneNumber(Map<String, String> data){
        //Continue from previous test "CSTC008"
        //2. Enter invalid Phone Number format under "Who's the order for"
        extentTest.info("Enter invalid Phone Number format under \"Who's the order for\"");
        checkout.enterPhoneNumber(data.get("MobileNumber"));

        //3. Verify "Phone Number" displays an error message
        extentTest.info("Verify \"Phone Number\" displays an error message");
        String actual = checkout.getErrorMessage();
        Assert.assertEquals(actual, "Please follow format 09xxxxxxxxx", "Incorrect Error Message");

    }
    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC010_verifyIncompletePhoneNumber(Map<String, String> data){
        //Continue from previous test "CSTC009"
        //2. Enter incomplete for Phone Number under "Who's the order for"
        extentTest.info("Enter incomplete for Phone Number under \"Who's the order for\"");
        checkout.clearPhoneNumber();
        checkout.enterPhoneNumber(data.get("MobileNumber"));

        //3. Verify "Phone Number" displays an error message
        extentTest.info("Verify \"Phone Number\" displays an error message");
        String actual = checkout.getErrorMessage();
        Assert.assertEquals(actual, "Phone number must be 11 digits", "Incorrect Error Message");

    }
    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC011_verifyInvalidEmailFormat(Map<String, String> data){
        //Continue from previous test "CSTC010"
        //2. Enter invalid Email format under "Who's the order for"
        extentTest.info("Enter invalid Email format under \"Who's the order for\"");
        checkout.clearPhoneNumber();
        checkout.enterPhoneNumber(data.get("MobileNumber"));
        checkout.enterEmail(data.get("Email"));

        //3. Verify "Email" displays an error message
        extentTest.info("Verify \"Email\" displays an error message");
        String actual = checkout.getErrorMessage();
        Assert.assertEquals(actual, "Please input valid email.", "Incorrect Error Message");

    }
    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC012_verifyValidCustomerInformation(Map<String, String> data){
        //Continue from previous test "CSTC011"
        //2. Enter valid details under "Who's the order for" (First Name, Last Name, Phone Number, Email Address, Special Remarks)
        extentTest.info("Enter valid details under \"Who's the order for\" (First Name, Last Name, Phone Number, Email Address, Special Remarks)");
        checkout.clearEmail();
        checkout.enterCustomerInformation(data.get("First Name"),data.get("Last Name"),data.get("MobileNumber")
        ,data.get("Email"),data.get("Special Remarks"));

        //3. Click "Go to payment" option
        extentTest.info("Click \"Go to payment\" option");
        checkout.clickGoToPaymentButton();

        //4. Verify that the inputted customer information is valid
        extentTest.info("Verify that the inputted customer information is valid");
        Assert.assertTrue(checkout.isFirstNameValid(), "Validation Error");
        Assert.assertTrue(checkout.isLastNameValid(), "Validation Error");
        Assert.assertTrue(checkout.isPhoneNumberValid(), "Validation Error");
        Assert.assertTrue(checkout.isEmailAddressValid(), "Validation Error");
        Assert.assertTrue(checkout.isSpecialRemarksValid(), "Validation Error");

    }
    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC013_verifyEmptyDeliveryInformation(){
        //Continue from previous test "CSTC012"
        //2. Leave "House Number / Unit Number / Floor " and "Barangay" blank
        extentTest.info("Leave \"House Number / Unit Number / Floor \" and \"Barangay\" blank");

        //3. Click "Go to payment" option
        extentTest.info("Click \"Go to payment\" option");
        //Already clicked previously

        //4. Verify "House Number / Unit Number / Floor " and "Barangay" displays an error
        extentTest.info("Verify \"House Number / Unit Number / Floor \" and \"Barangay\" displays an error");
        Assert.assertFalse(checkout.isHouseAddressValid(), "Validation Error");
        Assert.assertFalse(checkout.isBarangayValid(), "Validation Error");
        Assert.assertTrue(checkout.isLandmarkValid(), "Validation Error");
    }

    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC014_verifyDeliveryInformation(Map<String, String> data){
        //Continue from previous test "CSTC013"
        //2. Enter details under "Where should we deliver it?" (House Number /
        //Unit Number / Floor , Street Name - Subdivision / Barangay - Municipality, Barangay, Landmark or Nearest Corner Street)
        extentTest.info("Enter details under \"Where should we deliver it?\" (House Number / Unit Number / Floor , " +
                "Street Name - Subdivision / Barangay - Municipality, Barangay, Landmark or Nearest Corner Street)");
        checkout.enterDeliveryAddress(data.get("House Number/ Unit Number/ Floor"),
                data.get("Barangay"),
                data.get("Landmark or Nearest Cornet Steet"));

        //3. Click "Go to payment" option
        extentTest.info("Click \"Go to payment\" option");
        checkout.clickGoToPaymentButton();

        //4. Verify that the inputted delivery information is valid
        extentTest.info("Verify that the inputted delivery information is valid");
        Assert.assertTrue(checkout.isHouseAddressValid(), "Validation Error");
        Assert.assertTrue(checkout.isBarangayValid(), "Validation Error");
        Assert.assertTrue(checkout.isLandmarkValid(), "Validation Error");
    }

// Validation accepts no input - Skip for now
//    @Test(groups = {"regression", "checkout"})
//    public void CSTC015_verifyPaymentMethodCashNoInput(){
//        //Continue from previous test "CSTC014"
//        //2. Select "Cash" option under "How would you like to pay?"
//        extentTest.info("Select \"Cash\" option under \"How would you like to pay?\"");
//        checkout.clickCashOption();
//
//        //3. Click "Go to payment" option
//        extentTest.info("Click \"Go to payment\" option");
//        checkout.clickGoToPaymentButton();
//
//        //4. Verify Payment Method displays an error message
//        extentTest.info("Verify Payment Method displays an error message");
//        String actual = checkout.getErrorMessage();
//        Assert.assertEquals(actual, "Total cash must be equal or greater than ₱ 504.00", "Invalid Error Message");
//
//    }

    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC016_verifyPaymentMethodCashInvalidInput(Map<String, String> data) throws InterruptedException {
        //Continue from previous test "CSTC015"
        //3. Input invalid "change for" input
        extentTest.info("Input invalid \"change for\" input");
        checkout.clearCashOptions();
        checkout.inputCashCahnge(data.get("Change"));

        //4. Verify Payment Method displays an error message
        extentTest.info("Verify Payment Method displays an error message");
        String actual = checkout.isCashSelected();
        Assert.assertTrue(actual.contains("md-checked"), "Cash not Selected");
        String actual2 = checkout.getCashErrorMessage();
        Assert.assertEquals(actual2, "Total cash must be equal or greater than ₱ 504.00", "Invalid Error Message");


    }

    @Test(dataProvider = "checkoutTestData", groups = {"regression", "checkoutTest"})
    public void CSTC017_verifyPaymentMethodCashValidInput(Map<String, String> data){
        //Continue from previous test "CSTC016"
        //3. Input valid "change for" input
        extentTest.info("Input valid \"change for\" input");
        checkout.clearCashOptions();
        checkout.inputCashCahnge(data.get("Change"));

        //4. Click "Go to payment" option
        extentTest.info("Click \"Go to payment\" option");
        checkout.clickGoToPaymentButton();

        //5. Verify Payment Method is Validated
        extentTest.info("Verify Payment Method is Validated");
        String actual = checkout.isCashSelected();
        Assert.assertTrue(actual.contains("md-checked"), "Cash not Selected");
        Assert.assertTrue(checkout.isCashChangeValid(), "Valid Input Not Accepted");

    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC018_verifyPaymentMethodGCash(){
        //Continue from previous test "CSTC017"
        //2. Select "GCash QR Scan Upon Delivery"
        extentTest.info("Select \"GCash QR Scan Upon Delivery\"");
        checkout.clickGCashOption();

        //3. Verify Payment Method is Validated
        extentTest.info("Verify Payment Method is Validated");
        String actual = checkout.isGCashSelected();
        Assert.assertTrue(actual.contains("md-checked"), "GCash not Selected");

    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC019_verifyPaymentMethodMaya(){
        //Continue from previous test "CSTC018"
        //2. Select "Maya QR Scan Upon Delivery"
        extentTest.info("Select \"Maya QR Scan Upon Delivery\"");
        checkout.clickMayaOption();

        //3. Verify Payment Method is Validated
        extentTest.info("Verify Payment Method is Validated");
        String actual = checkout.isMayaSelected();
        Assert.assertTrue(actual.contains("md-checked"), "Maya not Selected");

    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC020_verifyPaymentMethodCreditCard(){
        //Continue from previous test "CSTC019"
        //2. Select "Credit Card Terminal"
        extentTest.info("Select \"Credit Card Terminal\"");
        checkout.clickCreditCardOption();

        //3. Verify Payment Method is Validated
        extentTest.info("Verify Payment Method is Validated");
        String actual = checkout.isCreditCardSelected();
        Assert.assertTrue(actual.contains("md-checked"), "Credit Card not Selected");

    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC021_verifyPaymentMethodsDelivery(){
        //Continue from previous test "CSTC020"
        //2. Verify Payment Methods
        extentTest.info("Verify Payment Methods List");
        List<WebElement> paymentOptions = checkout.paymentOptionsList();

        List<String> text = paymentOptions.stream()
                .map(WebElement::getText)
                .toList();

        Assert.assertTrue(text.contains("Cash"));
        Assert.assertTrue(text.contains("Credit Card Terminal"));
        Assert.assertTrue(text.contains("GCash QR Scan Upon Delivery"));
        Assert.assertTrue(text.contains("Maya QR Scan Upon Delivery"));
    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC023_verifyContactlessDeliveryDisabled(){
        //Continue from previous test "CSTC021"
        //2. Select "Credit Card Terminal"
        extentTest.info("Select \"Credit Card Terminal\"");
        checkout.clickCreditCardOption();

        //3. Verify Contactless Delivery Option is disabled
        extentTest.info("Verify Contactless Delivery Option is disabled");
        String containerAttribute = checkout.isContactlessDeliveryEnabled();
        Assert.assertTrue(containerAttribute.contains("container-disabled"),
                "Contactless should be disabled for credit card payment");

}

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC024_verifyContactlessDeliveryEnabled(){
        //Continue from previous test "CSTC023"
        //2. Select "GCash QR Scan Upon Delivery"
        extentTest.info("Select \"GCash QR Scan Upon Delivery\"");
        checkout.clickGCashOption();

        //3. Verify Contactless Delivery Option is enabled
        extentTest.info("Verify Contactless Delivery Option is enabled");
        checkout.isContactlessDeliveryEnabled();
        String containerAttribute = checkout.isContactlessDeliveryEnabled();
        Assert.assertFalse(containerAttribute.contains("container-disabled"),
                "Contactless should be enabled for GCash payment");

    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC026_verifyTermsOfUsePage() throws InterruptedException {
        //Continue from previous test "CSTC024"
        //2. Click on the "Terms of Use" link
        extentTest.info("Click on the \"Terms of Use\" link");
        checkout.clickTermsLink();

        //3. Verify User is redirected to the "Terms of Use" page
        extentTest.info("Verify User is redirected to the \"Terms of Use\" page");
        String actual = checkout.switchWindowTabs();
        Assert.assertEquals(actual, "Terms & Conditions", "Terms and Conditions Page Not Found");
        Thread.sleep(1000);

    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC027_verifyPrivacyPolicyPage() throws InterruptedException {
        //Continue from previous test "CSTC026"
        //2. Click on the "Privacy Policy" link
        extentTest.info("Click on the \"Privacy Policy\" link");
        checkout.clickPrivacyLink();

        //3. Verify User is redirected to the "Privacy Policy" page
        extentTest.info("Verify User is redirected to the \"Privacy Policy\" page");
        String actual = checkout.switchWindowTabs();
        Assert.assertEquals(actual, "Privacy Policy", "Privacy Policy Page Not Found");
        Thread.sleep(1000);

    }

    @Test(groups = {"regression", "checkoutTest"})
    public void CSTC028_verifyTermsAndPrivacyCheckbox(){
        //Continue from previous test "CSTC027"
        //5. Click "Go to payment" option
        extentTest.info("Click \"Go to payment\" option");
        checkout.clickGoToPaymentButton();

        //6. Verify User does not proceed to payment
        extentTest.info("Verify User does not proceed to payment");
        String actual = checkout.getSmallErrorMessage();
        Assert.assertEquals(actual, "Please agree to Terms of Use and Privacy Policy before proceeding.", "Invalid Error Message");

    }

    @Test(dataProvider = "checkoutTestData", groups = {"smoke", "regression", "checkoutTest", "e2e"})
    public void CSTC029_verifyPayment(Map<String, String> data) {
        //Recode later to fit with E2E
        //refresh
        checkout.refresh();

        //2. Input Customer Information
        extentTest.info("Input Customer Information");
        checkout.enterCustomerInformation(data.get("First Name"),
                                          data.get("Last Name"),
                                          data.get("MobileNumber"),
                                          data.get("Email"),
                                          data.get("Special Remarks"));

        //3. Input Delivery Information
        extentTest.info("Input Delivery Information");
        checkout.enterDeliveryAddress(data.get("House Number/ Unit Number/ Floor"),
                data.get("Barangay"),
                data.get("Landmark or Nearest Cornet Steet"));

        //4. Select Payment Method (Cash)
        extentTest.info("Select Payment Method (Cash)");
        checkout.clearCashOptions();
        checkout.inputCashCahnge(data.get("Change"));


        //5. Check "Terms of Use" "Privacy Policy" Checkbox
        extentTest.info("Check \"Terms of Use\" \"Privacy Policy\" Checkbox");
        checkout.clickTermsPrivacyCheckbox();

        //6. Click "Go to payment" option
        extentTest.info("Click \"Go to payment\" option");
        //Not coded

        //7. Verify User proceeds to payment
        //Cannot verify as we will not press the payment button, Instead, Verify if information is valid
        extentTest.info("Verify User does not proceed to payment");

        //validate customer info
        Assert.assertTrue(checkout.isFirstNameValid(), "Validation Error");
        Assert.assertTrue(checkout.isLastNameValid(), "Validation Error");
        Assert.assertTrue(checkout.isPhoneNumberValid(), "Validation Error");
        Assert.assertTrue(checkout.isEmailAddressValid(), "Validation Error");
        Assert.assertTrue(checkout.isSpecialRemarksValid(), "Validation Error");

        //validate delivery address
        Assert.assertTrue(checkout.isHouseAddressValid(), "Validation Error");
        Assert.assertTrue(checkout.isBarangayValid(), "Validation Error");
        Assert.assertTrue(checkout.isLandmarkValid(), "Validation Error");

        //validate payment option
        String actualRadioSelection = checkout.isCashSelected();
        Assert.assertTrue(actualRadioSelection.contains("md-checked"), "Credit Card not Selected");
        Assert.assertTrue(checkout.isCashChangeValid(), "Valid Input Not Accepted");

        //validate contactless delivery is disabled
        String containerAttribute = checkout.isContactlessDeliveryEnabled();
        Assert.assertTrue(containerAttribute.contains("container-disabled"),
                "Contactless should be disabled for credit card payment");

        //validate terms and conditions
        String actualChecboxSelection = checkout.isTermsPrivacyChecboxSelected();
        Assert.assertTrue(actualChecboxSelection.contains("is-valid"), "Checkbox not Selected");

    }
}
