package org.test.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.HashMap;

public class DriverFactory {
    public enum BrowserType {
        CHROME,
        FIREFOX,
        EDGE,
    }

    public static WebDriver createDriver(BrowserType browserType, int geoPermission, boolean headless) { // 1 = allow, 2 = deny
        WebDriver driver = null;

        switch (browserType) {
            case CHROME:
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--start-maximized", "incognito");
                if (headless) {
                    chromeOptions.addArguments("-headless=new");
                    chromeOptions.addArguments("--disable-gpu");
                }

                // Geolocation Toggle Code
                HashMap<String, Integer> contentSettings = new HashMap<>();
                HashMap<String, Object> profile = new HashMap<>();
                HashMap<String, Object> prefs = new HashMap<>();
                contentSettings.put("geolocation", geoPermission); // 1 = allow, 2 = deny
                profile.put("managed_default_content_settings", contentSettings);
                prefs.put("profile", profile);
                chromeOptions.setExperimentalOption("prefs", prefs);

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
