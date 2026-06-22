package org.test.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.test.utils.DriverFactory;
import org.test.utils.EmailUtil;
import org.test.utils.ExtentManager;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.time.Duration;

public class BaseTest {

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected JavascriptExecutor js;

    @BeforeSuite(alwaysRun = true)
    public void setup() {
        driver = DriverFactory.createDriver(DriverFactory.BrowserType.CHROME, 1);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        js = (JavascriptExecutor) driver;
        System.out.println("Yay!");
    }

    @AfterSuite(alwaysRun = true)
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }

        ExtentManager.get().flush(); // generate report
        EmailUtil.sendReport();      // send it
    }

    // delay for debugging
    public void delay() {
        try {
            Thread.sleep(2500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
