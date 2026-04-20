package utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class DriverFactory {

    private static ThreadLocal<WebDriver> driverThread = new ThreadLocal<>();

    public static void createDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--headless=new");


        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        driverThread.set(driver);
    }

    public static WebDriver getDriver() {
        return driverThread.get();
    }

    public static void quitDriver() {
        WebDriver driver = driverThread.get();
        if (driver != null) {
            driver.quit();
            driverThread.remove();
        }
    }
}
