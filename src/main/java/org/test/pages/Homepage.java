package org.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Homepage {
    WebDriver driver;

    public Homepage(WebDriver driver) {
        this.driver = driver;
    }

    private final By registerPageBtn = By.cssSelector("[data-tag='register-lbl']");

    public void clickRegisterPageButton() {
        driver.findElement(registerPageBtn).click();
    }
}
