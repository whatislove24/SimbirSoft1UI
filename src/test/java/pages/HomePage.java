package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    private static final By PRODUCTS = By.cssSelector(".thumbnail");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        openPath("");
        wait.until(ExpectedConditions.titleContains("practice your automation skills"));
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCTS));
        return this;
    }

    public ProductPage openRandomProduct() {
        List<WebElement> items = wait.until(
                ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCTS)
        );

        if (items.isEmpty()) {
            throw new RuntimeException("No products found on HomePage");
        }

        int index = new Random().nextInt(items.size());
        WebElement product = items.get(index);
        click(product);

        return new ProductPage(driver);
    }
}