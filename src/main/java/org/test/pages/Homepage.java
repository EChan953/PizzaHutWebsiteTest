package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Homepage {
    WebDriver driver;
    WebDriverWait wait;

    public Homepage(WebDriver driver) {
        this.driver = driver;
    }

    //locators
    //private final By registerPageBtn = By.cssSelector("[data-tag='register-lbl']");
    private final By registerPageBtn = By.linkText("Register");
    private final By loginPageBtn = By.linkText("Login");

    //actions
    public void openWebsite(String SITE){
        driver.get(SITE);
    }
    public void clickRegisterPageButton() {
        driver.findElement(registerPageBtn).click();
    }
    public void clickLoginPageButton() {
        driver.findElement(loginPageBtn).click();
    }
    public String getHomePageUrl() {
        String url = driver.getCurrentUrl();
        return url;
    }


}
