package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {

    public static WebDriver getDriver(String browser) {
        if (browser == null) browser = "chrome";

        switch (browser.toLowerCase()) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions ff = new FirefoxOptions();
                return new FirefoxDriver(ff);

            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                ChromeOptions ch = new ChromeOptions();

                ch.setPageLoadStrategy(PageLoadStrategy.EAGER);
                ch.addArguments("--disable-dev-shm-usage");
                ch.addArguments("--disable-gpu");
                ch.addArguments("--no-sandbox");
                ch.addArguments("--disable-extensions");
                ch.addArguments("--disable-notifications");

                return new ChromeDriver(ch);
        }
    }
}
