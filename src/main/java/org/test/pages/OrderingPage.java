package org.test.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class OrderingPage {
    WebDriver driver;
    WebDriverWait wait;
    JavascriptExecutor js;
    Actions a;

    public OrderingPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        this.js = (JavascriptExecutor) driver;
        this.a = new Actions(driver);
    }

    private final By deals = By.xpath("//p[normalize-space()='DEALS']");
    private final By pizza = By.xpath("//p[normalize-space()='PIZZA']");
    private final By melts = By.xpath("//p[normalize-space()='MELTS']");
    private final By pasta = By.xpath("//p[normalize-space()='PASTA']");
    private final By wingstreet = By.xpath("//p[normalize-space()='WINGSTREET®']");
    private final By chickenAndSides = By.xpath("//p[normalize-space()='CHICKEN & SIDES']");
    private final By beverages = By.xpath("//p[normalize-space()='BEVERAGES']");
    private final By vouchers = By.xpath("//p[normalize-space()='Vouchers']");

    private final By mainContentDiv = By.cssSelector(".container-main-content");
    private final By cartDiv = By.cssSelector(".container-layout-cart");
    private final By menuItems = By.cssSelector("[data-tag='item-menu']");
    private final By activeTab = By.cssSelector(".nav-link.active");
    private final By toastSuccess = By.cssSelector("div[data-tag='toast-success']");
    private final By checkoutBtn = By.cssSelector("[data-tag='checkout-btn']");

    private final By limitedTimeIcon = By.cssSelector("img[alt='Limited Time Offer']");

    private final By pizzaCustomizationWindow = By.id("container-modal-pizza");
    private final By pizzaCustomizationWindowSizes = By.cssSelector("[data-tag='item-size-modal']");
    private final By pizzaCustomizationWindowCrusts = By.cssSelector(".container-list-crust-size .item.cursor-pointer");
    private final By pizzaCustomizationWindowAddBtn = By.cssSelector("[data-tag='add-item-modal-btn']");
    private final By dealsAddToBasketBtn = By.cssSelector("[data-tag='add-deal-to-my-basket']");

    private final By cartItems = By.cssSelector(".container-item-cart");
    private final By cartItemName = By.cssSelector("[data-tag='item-cart-name'] span");

    // finds the web element; uses explicit wait for reliability
    public WebElement find(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    // checks if the element is present
    public boolean isElementPresent(By locator) {
        try {
            return find(locator).isDisplayed();
        }   catch (TimeoutException e) {
            return false;
        }
    }

    // check if menu area is present and visible
    public boolean isMenuAreaPresent() {
        return isElementPresent(mainContentDiv);
    }

    // check if cart area is present and visible
    public boolean isCartAreaPresent() {
        return isElementPresent(cartDiv);
    }

    // check if menu items are loaded
    public boolean areMenuItemsLoaded() {
        try {
            List<WebElement> items = driver.findElements(menuItems);
            return items.size() > 0;
        }   catch (TimeoutException e) {
            return false;
        }
    }

    // click a certain tab in the order navbar
    public void clickNavbarTab(By locator, String tabName) {
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();

        // wait for active tab
        wait.until(driver ->
                getActiveTabName().equalsIgnoreCase(tabName)
        );

        // wait for items to load
        wait.until(driver ->
                driver.findElements(menuItems).size() > 0
        );

    }

    // click the deals tab in the order navbar
    public void clickDealsTab() {
        clickNavbarTab(deals, "DEALS");
    }

    // click the pizza tab in the order navbar
    public void clickPizzaTab() {
        clickNavbarTab(pizza, "PIZZA");
    }

    // click the melts tab in the order navbar
    public void clickMeltsTab() {
        clickNavbarTab(melts, "MELTS");
    }

    // click the pasta tab in the order navbar
    public void clickPastaTab() {
        clickNavbarTab(pasta, "PASTA");
    }

    // click the wingstreet tab in the order navbar
    public void clickWingstreetTab() {
        clickNavbarTab(wingstreet, "WINGSTREET®");
    }

    // click the chicken&sides tab in the order navbar
    public void clickChickenAndSidesTab() {
        clickNavbarTab(chickenAndSides, "CHICKEN & SIDES");
    }

    // click the beverages tab in the order navbar
    public void clickBeveragesTab() {
        clickNavbarTab(beverages, "BEVERAGES");
    }

    // click the vouchers tab in the order navbar
    public void clickVouchersTab() {
        clickNavbarTab(vouchers, "Vouchers");
    }

    // click checkout button
    public void clickCheckout() {
        find(checkoutBtn).click();
    }

    // check current active tab
    public String getActiveTabName() {
        return find(activeTab).getText();
    }

    // get product listing
    public List<WebElement> getMenuItems() {
        return driver.findElements(menuItems);
    }

    // filter stuff
    public By filterByName(String filterName) {
        return By.xpath("//div[contains(@class,'item')]//span[normalize-space()='" + filterName + "']");
    }

    // click specific filter
    public void clickFilter(String filterName) {
        find(filterByName(filterName)).click();
        // wait for items to load
        wait.until(driver ->
                getMenuItems().size() > 0
        );
    }

    // filter functionality checking (Deals Tab)
    public boolean isDealsFilterWorking(String filterName) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(menuItems));
        List<WebElement> items = getMenuItems();

        if(filterName.equalsIgnoreCase("All")) {
            boolean foundLimited = false;
            boolean foundOthers = false;

            for(WebElement item : items) {
                boolean hasLimited = !item.findElements(limitedTimeIcon).isEmpty();
                if(hasLimited) {
                    foundLimited = true;
                }   else {
                    foundOthers = true;
                }

                if(foundLimited && foundOthers) {
                    return true;
                }
            }

            return false;
        } else {
            for(WebElement item : items) {
                if(item.findElements(By.cssSelector("img[alt='" + filterName + "']")).isEmpty()) {
                    return false;
                }
            }
            return true;
        }
    }

    // filter functionality checking (Pizza Tab)
    // first get src icon
    public String getPizzaFilterIconSrc(String filterName) {
        By locator = By.xpath(
                "//div[contains(@class,'item')]//span[normalize-space()='"
                        + filterName + "']/preceding-sibling::img"
        );

        WebElement icon = wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        return icon.getAttribute("src");
    }

    // then check if icon is present in the items
    public boolean isPizzaFilterWorking(String filterName) {
        String iconSrc = getPizzaFilterIconSrc(filterName);
        List<WebElement> items = getMenuItems();

        for(WebElement item : items) {
            if(item.findElements(By.cssSelector("img[src='" + iconSrc + "']")).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // filter functionality checking (pasta tab)
    public boolean isPastaFilterWorking(String filterName) {
        List<WebElement> items = getMenuItems();

        for(WebElement item : items) {
            List<WebElement> icons = item.findElements(By.cssSelector("img.item-menu-attribute"));
            if(icons.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // ADD Items to cart
    // No Variation/Customization Version
    public void addToCartNoCustomizationOrVariation(String productName) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(menuItems));

        List<WebElement> items = driver.findElements(menuItems);
        for (int i = 0; i < items.size(); i++) {
            // Re-fetch the list on each iteration to avoid stale refs
            items = driver.findElements(menuItems);
            WebElement item = items.get(i);

            String name = item.findElement(By.cssSelector("[data-tag='item-name']")).getText().trim();
            if (name.equalsIgnoreCase(productName)) {
                item.findElement(By.cssSelector("[data-tag='item-btn']")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess));
                return;
            }
        }
        throw new RuntimeException("Product not found: " + productName);
    }

    // With Customization
    public void selectPizzaSize(String size) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(pizzaCustomizationWindowSizes));
        List<WebElement> pizzaSizes = driver.findElements(pizzaCustomizationWindowSizes);
        for(WebElement pizzaSize : pizzaSizes) {
            if(pizzaSize.getAttribute("title").contains(size)) {
                pizzaSize.click();
                return;
            }
        }
        throw new RuntimeException("Size not found: " + size);
    }

    //
    public void selectPizzaCrust(String crust) {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(pizzaCustomizationWindowCrusts));
        List<WebElement> crustOptions = driver.findElements(pizzaCustomizationWindowCrusts);
        for(WebElement crustOption : crustOptions) {
            String name = crustOption.findElement(By.cssSelector("p.mb-0:not(.text-muted)")).getText().trim();
            if(name.equalsIgnoreCase(crust)) {
                crustOption.click();
                return;
            }
        }
        throw new RuntimeException("Crust not found: " + crust);
    }

    // Add Pizza to Cart With Customization
    public void addPizzaToCartWithCustomization(String pizzaName, String size, String crust) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(menuItems));
        List<WebElement> items = getMenuItems();

        for(WebElement item : items) {
            String name = item.findElement(By.cssSelector("[data-tag='item-name']")).getText().trim();
            if(name.equalsIgnoreCase(pizzaName)) {
                item.findElement(By.cssSelector("[data-tag='item-customize-btn']")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(pizzaCustomizationWindow));

                selectPizzaSize(size);
                selectPizzaCrust(crust);

                find(pizzaCustomizationWindowAddBtn).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(
                        toastSuccess
                ));
                return;
            }
        }
        throw new RuntimeException("Failed to Customize: " + pizzaName);
    }

    // Add Pizza to cart with Variation Dropdown
    public void addPizzaToCartWithVariation(String pizzaName, String variation) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(menuItems));
        List<WebElement> items = getMenuItems();

        for(WebElement item : items) {
            String name = item.findElement(By.cssSelector("[data-tag='item-name']")).getText().trim();
            if (name.equalsIgnoreCase(pizzaName)) {
                Select options = new Select(item.findElement(By.cssSelector("[data-tag='item-drp']")));
                options.selectByVisibleText(variation);
                item.findElement(By.cssSelector("[data-tag='item-btn']")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess));
                return;
            }
        }
        throw new RuntimeException("Failed to Set Variation: " + pizzaName);
    }

    // Add Deals to Cart
    public void addDealsToCart(String dealsName) {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(menuItems));
        List<WebElement> items = getMenuItems();

        for(WebElement item : items) {
            String name = item.findElement(By.cssSelector("[data-tag='item-name']")).getText().trim();
            if (name.equalsIgnoreCase(dealsName)) {
                item.findElement(By.cssSelector("[data-tag='item-btn']")).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(dealsAddToBasketBtn));
                find(dealsAddToBasketBtn).click();
                wait.until(ExpectedConditions.visibilityOfElementLocated(toastSuccess));
                wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(cartItems));
                return;
            }
        }
        throw new RuntimeException("Failed to Set Variation: " + dealsName);
    }

    // Check Cart Items
    public boolean isProductInsideCart(String productName) {
        List<WebElement> items = driver.findElements(cartItemName);
        System.out.println("Cart items found: " + items.size());
        for(WebElement item : items) {
            System.out.println(item.getText());
        }

        for(WebElement item : items) {
            if(item.getText().equalsIgnoreCase(productName)) {
                return true;
            }
        }
        return false;
    }

    // Check Checkout Button Availability
    public boolean isCheckoutAvailable() {
        return find(checkoutBtn).isEnabled();
    }

}
