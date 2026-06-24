package org.test.tests;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.test.base.BaseTest;
import org.test.pages.*;
import org.test.utils.ExcelReader;
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
public class LoginTest extends BaseTest {
    private LoginPage login;
    private Homepage homepage;
    private EmailPage email;
    private ForgetPasswordPage forgetpassword;

    private static final String SITE = "https://www.pizzahut.com.ph/";
    private static final String LOGIN = SITE + "login";
    private static final String excelFilePath = "src/test/resources/testdata/TestData.xlsx";

    @BeforeTest(groups = {"smoke", "regression", "e2e", "login"})
    public void initPage() {
        System.out.println("hello");
        login = new LoginPage(driver);
        homepage = new Homepage(driver);
        email = new EmailPage(driver);
        forgetpassword = new ForgetPasswordPage(driver);
    }

    //Test Data
    //Update Object and DataProvider Name
    @DataProvider(name="loginTestData")
    public Iterator<Object[]> getLoginTestData(Method method){
        String rawTestCaseID = method.getName();
        //Update Test Case Identifier
        String testCaseID = rawTestCaseID.split("_")[0].replace("LSTC","LS-TC-");
        //Update Sheet Name
        List<Map<String, String>> allData =
                ExcelReader.readExcelData(excelFilePath, "Login_Credentials");
        List<Map<String, String>> filtered =
                ExcelReader.filterByTestCase(allData, testCaseID);
        List<Object[]> result = new ArrayList<>();
        for (Map<String, String> map : filtered) {
            result.add(new Object[]{map});
        }
        return result.iterator();
    }

    @DataProvider(name="forgetPasswordTestData")
    public Iterator<Object[]> getForgetPasswordTestData(Method method){
        String rawTestCaseID = method.getName();
        //Update Test Case Identifier
        String testCaseID = rawTestCaseID.split("_")[0].replace("LSTC","LS-TC-");
        //Update Sheet Name
        List<Map<String, String>> allData =
                ExcelReader.readExcelData(excelFilePath, "Forget_Password_Data");
        List<Map<String, String>> filtered =
                ExcelReader.filterByTestCase(allData, testCaseID);
        List<Object[]> result = new ArrayList<>();
        for (Map<String, String> map : filtered) {
            result.add(new Object[]{map});
        }
        return result.iterator();
    }

    // Verify Login Page is Accessible
    public void redirectToLoginPage() {
        // load homepage
        driver.get(SITE);

        // click on login button
        homepage.clickLoginPageButton();

        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Login')]")
        ));
    }

    // load login page
    public void loadLoginPage() {
        // load login page
        driver.get(LOGIN);

        // wait until its actually loaded
        wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//h4[contains(text(), 'Login')]")
        ));
    }

    @Test(groups = {"smoke", "regression", "e2e", "login"})
    public void LSTC001_verifyLoginPageAccessibility(){
        //1. Access Login Page
        extentTest.info("Access Login Page");
        driver.get(SITE);
        //Verify
        String actual = homepage.getHomePageUrl();
        Assert.assertEquals(actual,"https://www.pizzahut.com.ph/", "Homepage not Found");

        //2. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //3. Verify that the Login Page is displayed
        extentTest.info("Verify that the Login Page is displayed");
        String actualTitle = login.getLoginTitle();
        Assert.assertEquals(actualTitle, "Login", "Login Page not Found");
    }

    @Test(groups = {"smoke", "regression", "login"})
    public void LSTC002_verifyBackButton(){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Clicks on the "< Back" button located on the upper left corner
        extentTest.info("Clicks on the \"< Back\" button located on the upper left corner");
        login.clickBack();

        //3. Verify that the previous webpage is displayed
        extentTest.info("Verify that the previous webpage is displayed");
        String actual = homepage.getHomePageUrl();
        Assert.assertEquals(actual,"https://www.pizzahut.com.ph/", "Homepage not Found");
    }

    @Test(groups = {"smoke", "regression", "login"})
    public void LSTC003_verifyLogoRedirection(){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Click on the Pizza Hut logo located on the top of the Login page
        extentTest.info("Click on the Pizza Hut logo located on the top of the Login page");
        login.clickHomeButton();

        //3. Verify that the previous webpage is displayed
        extentTest.info("Verify that the previous webpage is displayed");
        String actual = homepage.getHomePageUrl();
        Assert.assertEquals(actual,"https://www.pizzahut.com.ph/", "Homepage not Found");
    }

    @Test(groups = {"regression", "login"})
    public void LSTC004_verifyEmptyLoginCredentials(){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Click the "Login" button
        extentTest.info("Click the \"Login\" button");
        login.clickLoginButton();

        //3. Verify that the user is not logged-in with no credentials
        extentTest.info("Verify that the user is not logged-in with no credentials");
        Boolean isValid = login.isEmailInputValid();
        Assert.assertFalse(isValid, "Invalid Email Accepted");
        isValid = login.isPasswordInputValid();
        Assert.assertFalse(isValid, "Invalid Password Accepted");
    }

    @Test(dataProvider = "loginTestData", groups = {"regression", "login"})
    public void LSTC005_verifyInvalidLoginCredentials(Map<String, String> data){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Enter invalid credentials
        extentTest.info("Enter invalid credentials");
        login.login(data.get("Email"),data.get("Password"));

        //3. Click the "Login" button
        extentTest.info("Click the \"Login\" button");
        login.clickLoginButton();

        //4. Verify that the user is not logged-in using invalid credentials
        extentTest.info("Verify that the user is not logged-in using invalid credentials");
        String actual = login.getErrorMessage();
        Assert.assertEquals(actual,"Authentication failed", "Error Message not Found");
    }

    @Test(dataProvider = "loginTestData", groups = {"regression", "login"})
    public void LSTC006_verifyInvalidEmail(Map<String, String> data){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Enter invalid email address
        extentTest.info("Enter invalid credentials");
        login.enterEmail(data.get("Email"));

        //3. Click the "Login" button
        extentTest.info("Click the \"Login\" button");
        login.clickLoginButton();

        //4. Verify that the user is not logged-in using invalid email
        extentTest.info("Verify that the user is not logged-in using invalid email");
        String actual = login.getEmailErroMessage().trim();
        Assert.assertEquals(actual,"Please input valid email.", "Error Message not Found");

    }

    @Test(dataProvider = "loginTestData", groups = {"regression", "login"})
    public void LSTC007_verifyInvalidPassword(Map<String, String> data){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Enter valid email
        extentTest.info("Enter valid email");
        login.enterEmail(data.get("Email"));

        //3. Enter invalid password
        extentTest.info("Enter invalid password");
        login.enterPassword(data.get("Password"));

        //4. Click the "Login" button
        extentTest.info("Click the \"Login\" button");
        login.clickLoginButton();

        //5. Verify that the user is not logged-in using invalid password
        extentTest.info("Verify that the user is not logged-in using invalid password");
        String actual = login.getErrorMessage();
        Assert.assertEquals(actual,"Authentication failed", "Error Message not Found");
    }

    @Test(dataProvider = "loginTestData", groups = {"smoke", "regression", "e2e", "login"})
    public void LSTC008_VerifyValidCredentials(Map<String, String> data){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Enter valid email address and password
        extentTest.info("valid email address and password");
        login.login(data.get("Email"),data.get("Password"));

        //3. Click the "Login" button
        extentTest.info("Click the \"Login\" button");
        login.clickLoginButton();

        //4. Verify that the user is logged in using valid credentials
        extentTest.info("Verify that the user is logged in using valid credentials");
        String actual = login.getSuccessMessage();
        Assert.assertEquals(actual,"Login successfully!", "Success Message not Found");
    }

    @Test(groups = {"smoke", "regression", "login"})
    public void LSTC009_VerifyLogoutButton() throws InterruptedException {
        //Continue from previous test "LSTC007"
        //4. Press Logout located at the navigation bar
        extentTest.info("Press Logout located at the navigation bar");
        login.clickLogoutButton();

        //5. Verify that the user is logged out of the system
        extentTest.info("Verify that the user is logged out of the system");
        Assert.assertFalse(login.isProfileButtonVisiblec(), "Logout not Successful");

    }

    @Test(groups = {"regression", "login"})
    public void LSTC010_VerifyForgetPasswordEmptyEmail(){
        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Click on "Forgot your password?" link
        extentTest.info("Click on \"Forgot your password?\" link");
        login.clickForgotYourPasswordLink();

        //3. Click "Send Password Reset Link"
        extentTest.info("Click \"Send Password Reset Link\"");
        login.clickResetPasswordButton();

        //4. Verify No Password Reset Link is sent
        extentTest.info("Verify No Password Reset Link is sent");
        Boolean isValid = login.isResetPasswordEmailInputValid();
        Assert.assertFalse(isValid, "Invalid Email Accepted");
    }

    @Test(dataProvider = "forgetPasswordTestData", groups = {"regression", "login"})
    public void LSTC011_VerifyForgetPasswordInvalidEmail(Map<String, String> data){
        //Refresh
        homepage.openWebsite(SITE);

        //1. Click on the Login Link located in the navigation bar
        extentTest.info("Click on the Login Link located in the navigation bar");
        homepage.clickLoginPageButton();

        //2. Click on "Forgot your password?" link
        extentTest.info("Click on \"Forgot your password?\" link");
        login.clickForgotYourPasswordLink();

        //3. Enter invalid email
        extentTest.info("Enter invalid email");
        login.enterForgetPasswordEmail(data.get("Email"));

        //4. Click "Send Password Reset Link"
        extentTest.info("Click \"Send Password Reset Link\"");
        login.clickResetPasswordButton();

        //5. Verify Password Reset Link is not sent to inputted Email
        extentTest.info("Verify Password Reset Link is not sent to inputted Email");
        String actual = login.getEmailErroMessage().trim();
        Assert.assertEquals(actual,"Please input valid email.", "Error Message not Found");
    }

    @Test(dataProvider = "forgetPasswordTestData", groups = {"regression", "login"})
    public void LSTC012_VerifyEmptyPassword(Map<String, String> data) throws InterruptedException {
        //Continue from previous test "LSTC009"
        //3. Enter valid email
        extentTest.info("Enter valid email");
        login.clearResetPasswordEmail();
        login.enterForgetPasswordEmail(data.get("Email"));

        //4. Click "Send Password Reset Link"
        extentTest.info("Click \"Send Password Reset Link\"");
        login.clickResetPasswordButton();
        String actual = login.getSuccessMessage();
        Assert.assertEquals(actual,"We have sent an email with instructions to reset your password.",
                "Password Reset Form not Sent");

        //5. Open email and click password reset link
        extentTest.info("Open email and click password reset link");
        email.redirectToEmailPage();
        email.enterEmail(data.get("Email"));
        email.clickArrowButton();
        email.refreshEmail();
        email.clickResetLink();
        email.swtichToNewestTab();

        //6. Click "Reset Password" Button
        extentTest.info("Click \"Reset Password\" Button");
        forgetpassword.clickResetPasswordButton();

        //7. Verify User does not proceed to next step
        extentTest.info("Verify User does not proceed to next step");
        boolean isValid = forgetpassword.isPasswordInputValid();
        Assert.assertFalse(isValid, "Invalid Password Accepted");
        isValid = forgetpassword.isConfirmPasswordInputValid();
        Assert.assertFalse(isValid, "Invalid Confirm Password Accepted");
    }
    @Test(dataProvider = "forgetPasswordTestData", groups = {"regression", "login"})
    public void LSTC013_VerifyInvalidPassword(Map<String, String> data){
        //Continue from previous test "LSTC012"
        //6. Enter invalid password conditions
        extentTest.info("Enter invalid password conditions");
        forgetpassword.enterPassword(data.get("Password"));
        forgetpassword.enterConfirmPassword(data.get("Confirm Password"));

        //7. Click "Reset Password" Button
        extentTest.info("Click \"Reset Password\" Button");
        forgetpassword.clickResetPasswordButton();

        //8. Verify Password is not accepted by the system
        extentTest.info("Verify Password is not accepted by the system");
        String actual = forgetpassword.getEmailErroMessage();
        Assert.assertEquals(actual, "Password policy requirements are not met", "Password was Accepted");
    }

    @Test(dataProvider = "forgetPasswordTestData", groups = {"regression", "login"})
    public void LSTC014_VerifyInvalidConfirmPassword(Map<String, String> data){
        //Continue from previous test "LSTC010"
        //6. Enter valid password
        extentTest.info("Enter valid password");
        forgetpassword.clearPasswordInput();
        forgetpassword.enterPassword(data.get("Password"));

        //7. Enter invalid confirm password
        extentTest.info("Enter invalid confirm password");
        forgetpassword.clearConfirmPasswordInput();
        forgetpassword.enterConfirmPassword(data.get("Confirm Password"));

        //8. Click "Reset Password" Button
        extentTest.info("Click \"Reset Password\" Button");
        forgetpassword.clickResetPasswordButton();

        //9. Verify Password is not accepted by the system
        extentTest.info("Verify Password is not accepted by the system");
        String actual = forgetpassword.getEmailErroMessage();
        Assert.assertEquals(actual, "Password and Retype password don't match", "Password was Accepted");
    }

    @Test(dataProvider = "forgetPasswordTestData", groups = {"regression", "login"})
    public void LSTC015_VerifyPasswordMasking(Map<String, String> data){
        //Continue from previous test "LSTC011"
        //7. Click the "eye" icon to turn off password masking
        extentTest.info("Click the \"eye\" icon to turn off password masking");
        forgetpassword.clickRevealPasswordButton();

        //8. Verify PlainText
        extentTest.info("Verify PlainText");
        String actual = forgetpassword.getPlainTextPassword();
        Assert.assertEquals(actual, "Password@1234", "Password not found");
    }

    @Test(dataProvider = "forgetPasswordTestData", groups = {"regression", "login"})
    public void LSTC016_VerifyValidPasswordChange(Map<String, String> data){
        //Continue from previous test "LSTC012"
        //7. Enter valid confirm password
        extentTest.info("Enter valid confirm password");
        forgetpassword.clearConfirmPasswordInput();
        forgetpassword.enterConfirmPassword(data.get("Confirm Password"));


        //8. Click "Reset Password" Button
        extentTest.info("Click \"Reset Password\" Button");
        forgetpassword.clickResetPasswordButton();

        //9. Verify System Accepts new Password Change
        extentTest.info("Verify System Accepts new Password Change");
        String currentPage = login.getLoginTitle();
        Assert.assertEquals(currentPage, "Login", "Valid Password not Accepted");
    }

    @Test(dataProvider = "loginTestData", groups = {"regression", "login"})
    public void LSTC017_VerifyUpdatedPassword(Map<String, String> data) throws InterruptedException {
        //Continue from previous test "LSTC013"
        //10. Enter valid email address and updated password
        extentTest.info("Enter valid email address and updated password");
        login.login(data.get("Email"),data.get("Password"));

        //11. Click the "Login" button
        extentTest.info("Click the \"Login\" button");
        Thread.sleep(1000);
        login.clickLoginButton();

        //12. Verify System has updated Account's password
        extentTest.info("Verify that the user is logged in using valid credentials");
        String actual = login.getSuccessMessage();
        Assert.assertEquals(actual,"Login successfully!", "Success Message not Found");
    }
}
