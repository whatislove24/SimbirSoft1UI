package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    @FindBy(css = ".thumbnail")
    private List<WebElement> products;

    public HomePage(WebDriver driver) {
        super(driver);
    }


    public void open() {
        String url = "https://automationteststore.com/";
        if (!driver.getCurrentUrl().equals(url)) {
            driver.get(url);
        }

        wait.until(ExpectedConditions.titleContains("A place to practice"));
    }
    public void openRandomProduct() {

        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".thumbnail")));

        if (products.isEmpty()) {
            throw new RuntimeException("Товары на главной странице не найдены!");
        }

        int index = new Random().nextInt(products.size());
        products.get(index).findElement(By.cssSelector("a")).click();
    }
}
