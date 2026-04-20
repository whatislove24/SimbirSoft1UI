package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    private final By products = By.cssSelector(".thumbnail");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://automationteststore.com/");

        wait.until(ExpectedConditions.titleContains(
                "practice your automation skills"
        ));

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(products));
    }

    public void openRandomProduct() {

        List<WebElement> items = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(products)
        );

        if (items.isEmpty()) {
            throw new RuntimeException("No products found on HomePage");
        }

        int index = new Random().nextInt(items.size());

        WebElement product = items.get(index);

        wait.until(ExpectedConditions.elementToBeClickable(product)).click();
    }
}