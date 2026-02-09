package tests;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pages.MainPage;
import pages.OrderPage;
import utils.BaseTest;
import utils.OrderData;

@RunWith(Parameterized.class)
public class OrderTest extends BaseTest {

    private final boolean topButton;
    private final OrderData data;

    public OrderTest(boolean topButton, OrderData data) {
        this.topButton = topButton;
        this.data = data;
    }

    @Parameterized.Parameters(name = "Order entry={0}")
    public static Object[][] params() {
        return new Object[][]{
                {true, new OrderData(
                        "Иван", "Иванов", "Москва, Тверская 1", "Тверская", "89990000001",
                        "10.10.2026", "сутки",
                        "black", // <-- ДОБАВИЛИ ЦВЕТ
                        "Тестовый заказ 1"
                )},
                {false, new OrderData(
                        "Анна", "Петрова", "Москва, Арбат 10", "Арбатская", "89990000002",
                        "12.10.2026", "двое суток",
                        "grey", // <-- ДОБАВИЛИ ЦВЕТ
                        "Тестовый заказ 2"
                )}
        };
    }

    @Test
    public void orderShouldBeCreatedFromBothEntryPoints() {
        MainPage main = new MainPage(driver).acceptCookiesIfVisible();

        if (topButton) {
            main.clickOrderTop();
        } else {
            main.clickOrderBottom();
        }

        OrderPage order = new OrderPage(driver);
        order.waitForLoad();

        order.fillStepOne(data);
        order.fillStepTwoAndSubmit(data);

        Assert.assertTrue("Не появилось сообщение об успешном создании заказа", order.isOrderCreated());
    }
}
