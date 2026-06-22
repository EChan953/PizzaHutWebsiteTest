package org.test.base;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.test.pages.Homepage;
import org.test.utils.DriverFactory;
import org.test.utils.EmailUtil;
import org.test.utils.ExtentManager;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;
import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;
    protected static ExtentReports extent;
    protected ExtentTest extentTest;

    @BeforeSuite(alwaysRun = true)
    public void setup() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME, 1);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        extent = ExtentManager.getReports();
        System.out.println("Yay!");
    }

    @AfterSuite(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }

        ExtentManager.getReports().flush(); // generate report
        EmailUtil.sendReport();      // send it
    }
    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method) {
        extentTest = extent.createTest(method.getName());  // auto-names node after test method
    }

    @AfterMethod(alwaysRun = true)
    public void endTest(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS ->
                    extentTest.log(Status.PASS, "Test passed");
            case ITestResult.FAILURE ->
                    extentTest.log(Status.FAIL, result.getThrowable());
            case ITestResult.SKIP ->
                    extentTest.log(Status.SKIP, "Test skipped");
        }
    }

    @BeforeMethod(alwaysRun = true)
    public void startTest(Method method) {
        extentTest = extent.createTest(method.getName());  // auto-names node after test method
    }

    @AfterMethod(alwaysRun = true)
    public void endTest(ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.SUCCESS ->
                    extentTest.log(Status.PASS, "Test passed");
            case ITestResult.FAILURE ->
                    extentTest.log(Status.FAIL, result.getThrowable());
            case ITestResult.SKIP ->
                    extentTest.log(Status.SKIP, "Test skipped");
        }
    }

    // delay in case for debugging
    public void delay() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
