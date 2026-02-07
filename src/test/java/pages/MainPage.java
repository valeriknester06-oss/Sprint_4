package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class MainPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    // Куки
    private final By cookieButton = By.id("rcc-confirm-button");

    // Кнопки "Заказать" (верхняя и нижняя)
    private final By orderButtonTop =
            By.xpath("//div[contains(@class,'Header_Nav')]//button[normalize-space()='Заказать']");
    private final By orderButtonBottom =
            By.xpath("//div[contains(@class,'Home_FinishButton')]//button[normalize-space()='Заказать']");

    public MainPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public MainPage open(String url) {
        driver.get(url);
        return this;
    }

    private By faqQuestion(int index) {
        return By.id("accordion__heading-" + index);
    }

    private By faqAnswer(int index) {
        return By.id("accordion__panel-" + index);
    }

    private void safeClick(By locator) {
        WebElement el = wait.until(ExpectedConditions.presenceOfElementLocated(locator));

        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView({block:'center'});", el
        );
        ((JavascriptExecutor) driver).executeScript("window.scrollBy(0, -120);");

        wait.until(ExpectedConditions.elementToBeClickable(el));

        try {
            el.click();
        } catch (ElementClickInterceptedException | StaleElementReferenceException e) {
            WebElement fresh = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", fresh);
        }
    }

    public MainPage acceptCookiesIfVisible() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(cookieButton));
            safeClick(cookieButton);
        } catch (TimeoutException ignored) {
        }
        return this;
    }

    public MainPage clickOrderTop() {
        acceptCookiesIfVisible();
        safeClick(orderButtonTop);
        return this;
    }

    public MainPage clickOrderBottom() {
        acceptCookiesIfVisible();
        safeClick(orderButtonBottom);
        return this;
    }

    public MainPage openFaqAnswer(int index) {
        acceptCookiesIfVisible();
        safeClick(faqQuestion(index));
        wait.until(ExpectedConditions.visibilityOfElementLocated(faqAnswer(index)));
        return this;
    }

    public String getFaqAnswerText(int index) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(faqAnswer(index))).getText();
    }
}
