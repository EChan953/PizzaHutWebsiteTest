package org.test.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {
    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
    }

    public static WebDriver createDriver(BrowserType browserType) {
        WebDriver driver = null;

        switch (browserType) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized", "incognito");
                driver = new ChromeDriver(chromeOptions);
                break;

            case EDGE:
                EdgeOptions edgeOptions = new EdgeOptions();
                edgeOptions.addArguments("--start-maximized");
                driver = new EdgeDriver(edgeOptions);
                break;

            case FIREFOX:
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                driver = new FirefoxDriver(firefoxOptions);
                break;

            default:
                throw new IllegalArgumentException("Unsupported Browser Type: " + browserType);
        }

        return driver;
    }
}
