package pages;


import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.OrderData;

import java.time.Duration;

public class OrderPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    public OrderPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private final By stepOneHeader =
            By.xpath("//div[contains(@class,'Order_Header') and contains(.,'Для кого самокат')]");

    private final By nameInput = By.xpath("//input[@placeholder='* Имя']");
    private final By surnameInput = By.xpath("//input[@placeholder='* Фамилия']");
    private final By addressInput = By.xpath("//input[contains(@placeholder,'Адрес')]");
    private final By metroInput = By.xpath("//input[contains(@placeholder,'Станция метро')]");
    private final By phoneInput = By.cssSelector("input[placeholder*='Телефон']");
    private final By nextButton = By.xpath("//button[normalize-space()='Далее']");

    private final By dateInput = By.xpath("//input[contains(@placeholder,'Когда привезти')]");
    private final By rentDropdown = By.className("Dropdown-control");
    private final By commentInput = By.xpath("//input[contains(@placeholder,'Комментарий')]");

    private final By orderButton =
            By.xpath("//div[contains(@class,'Order_Buttons')]/button[normalize-space()='Заказать']");


    private final By colorBlack = By.id("black");
    private final By colorGrey = By.id("grey");

    private final By cookieButton = By.id("rcc-confirm-button");

    private final By confirmModal = By.xpath(
            "//*[contains(@class,'Order_Modal')][.//*[contains(normalize-space(.),'Хотите оформить заказ')]]"
    );

    private final By successHeader = By.xpath(
            "//div[contains(@class,'Order_ModalHeader') and contains(.,'Заказ оформлен')]"
    );

    private final By orderSuccess =
            By.xpath("//*[contains(@class,'Order_Modal')]//*[contains(normalize-space(.),'Заказ оформлен')]");

    public void waitForLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(stepOneHeader));
        wait.until(ExpectedConditions.visibilityOfElementLocated(nameInput));
        acceptCookiesIfPresent();
    }

    public void fillStepOne(OrderData d) {
        waitForLoad();

        clearAndType(nameInput, d.name);
        clearAndType(surnameInput, d.surname);
        clearAndType(addressInput, d.address);

        selectMetro(d.metro);

        clearAndType(phoneInput, d.phone);

        safeClick(nextButton);


        wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
    }

    public void fillStepTwoAndSubmit(OrderData d) {

        clearAndType(dateInput, d.date);
        driver.findElement(dateInput).sendKeys(Keys.ENTER);

        wait.until(dr -> {
            String v = dr.findElement(dateInput).getAttribute("value");
            return v != null && !v.trim().isEmpty();
        });

        clickBody();

        safeClick(rentDropdown);
        safeClick(By.xpath("//div[contains(@class,'Dropdown-option') and normalize-space()='" + d.rentPeriod + "']"));

        selectColorSmart(d.color);

        if (d.comment != null && !d.comment.isBlank()) {
            clearAndType(commentInput, d.comment);
        }

        clickBody();

        safeClick(orderButton);


        wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));

        new OrderConfirmPage(driver).clickYes();


        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(successHeader));
        } catch (TimeoutException e) {
            new WebDriverWait(driver, Duration.ofSeconds(25))
                    .until(ExpectedConditions.visibilityOfElementLocated(orderSuccess));
        }
    }

    public boolean isOrderCreated() {
        return new WebDriverWait(driver, Duration.ofSeconds(25))
                .until(ExpectedConditions.visibilityOfElementLocated(orderSuccess))
                .isDisplayed();
    }

    private void acceptCookiesIfPresent() {
        try {
            if (!driver.findElements(cookieButton).isEmpty()) {
                driver.findElement(cookieButton).click();
            }
        } catch (Exception ignored) {
        }
    }

    private void clickBody() {
        try {
            driver.findElement(By.tagName("body")).click();
        } catch (Exception ignored) {
            ((JavascriptExecutor) driver).executeScript("document.body.click();");
        }
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -120);");

        try {
            wait.until(ExpectedConditions.elementToBeClickable(locator));
            el.click();
        } catch (Exception e) {
            WebElement fresh = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fresh);
        }
    }

    private void clearAndType(By locator, String value) {
        WebElement el = wait.until(ExpectedConditions.elementToBeClickable(locator));

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", el);
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -120);");

        el.click();
        el.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        el.sendKeys(Keys.DELETE);
        el.sendKeys(value);
    }

    private void selectMetro(String station) {
        WebElement metro = wait.until(ExpectedConditions.elementToBeClickable(metroInput));
        metro.click();
        metro.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        metro.sendKeys(Keys.DELETE);
        metro.sendKeys(station);

        By option = By.xpath(
                "//div[contains(@class,'select-search__select')]//*[contains(@class,'select-search__option')]" +
                        "[contains(normalize-space(.),'" + station + "')]"
        );

        try {
            wait.until(ExpectedConditions.elementToBeClickable(option)).click();
        } catch (TimeoutException e) {
            metro.sendKeys(Keys.ARROW_DOWN, Keys.ENTER);
        }

        wait.until(d -> {
            String value = d.findElement(metroInput).getAttribute("value");
            return value != null && !value.trim().isEmpty();
        });
    }

    private void selectColorSmart(String color) {
        if (color != null && !color.isBlank()) {
            String c = color.toLowerCase().trim();
            if (c.equals("black") && !driver.findElements(colorBlack).isEmpty()) {
                safeClick(colorBlack);
                return;
            }
            if ((c.equals("grey") || c.equals("gray")) && !driver.findElements(colorGrey).isEmpty()) {
                safeClick(colorGrey);
                return;
            }
        }

        if (!driver.findElements(colorBlack).isEmpty()) {
            safeClick(colorBlack);
        } else if (!driver.findElements(colorGrey).isEmpty()) {
            safeClick(colorGrey);
        }
    }
}
