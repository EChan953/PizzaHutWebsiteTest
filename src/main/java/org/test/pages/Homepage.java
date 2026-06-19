package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Homepage {
    WebDriver driver;

    public Homepage(WebDriver driver) {
        this.driver = driver;
    }

    private By registerPageBtn = By.cssSelector("[data-tag='register-lbl']");
    private By loginButton = By.linkText("Login");
    private By homeBanner = By.cssSelector("div[class='container-bg-top container-bg-top-mobile d-block d-lg-none wow fadeIn']");



    public void clickRegisterPageButton() {
        driver.findElement(registerPageBtn).click();
    }
    public void clickLoginPageButton() {
        driver.findElement(loginButton).click();
    }
}
