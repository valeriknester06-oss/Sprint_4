package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class OrderConfirmPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    public OrderConfirmPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // Модалка именно подтверждения (с текстом вопроса)
    private final By confirmModal = By.xpath(
            "//*[contains(@class,'Order_Modal')][.//*[contains(normalize-space(.),'Хотите оформить заказ')]]"
    );

    // Кнопка "Да" внутри этой модалки (привязываемся к контейнеру, не к классам кнопки)
    private final By yesButton = By.xpath(
            "//*[contains(@class,'Order_Modal')][.//*[contains(normalize-space(.),'Хотите оформить заказ')]]" +
                    "//button[normalize-space()='Да']"
    );

    public void clickYes() {
        WebElement modal = wait.until(ExpectedConditions.visibilityOfElementLocated(confirmModal));

        WebElement yes = wait.until(ExpectedConditions.presenceOfElementLocated(yesButton));

        // 1) скроллим к кнопке и даём ей стать кликабельной
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", yes);
        wait.until(ExpectedConditions.elementToBeClickable(yesButton));

        // 2) пробуем несколько стратегий клика
        boolean clicked = false;

        // обычный клик
        try {
            yes.click();
            clicked = true;
        } catch (Exception ignored) {}

        // Actions click (часто спасает, когда перекрывает оверлей)
        if (!clicked) {
            try {
                new Actions(driver).moveToElement(yes).pause(Duration.ofMillis(150)).click().perform();
                clicked = true;
            } catch (Exception ignored) {}
        }

        // JS click (последний шанс)
        if (!clicked) {
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", yes);
                clicked = true;
            } catch (Exception ignored) {}
        }

        if (!clicked) {
            String txt = "";
            try { txt = modal.getText(); } catch (Exception ignored) {}
            throw new TimeoutException("Не удалось нажать кнопку 'Да'. Текст модалки:\n" + txt);
        }

        // 3) после клика ждём, что модалка подтверждения исчезла
        // (НЕ ждём успех тут — это обязанность OrderPage)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(confirmModal));
    }
}
