package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.Random;

public class HomePage extends BasePage {

    private static final By PRODUCTS = By.cssSelector(".thumbnail");
    private static final By SKINCARE_MENU_LINK = By.xpath(
            "//a[contains(translate(normalize-space(.), " +
                    "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'skincare')]"
    );

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        openStartPage();
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

        WebElement product = items.get(new Random().nextInt(items.size()));
        click(product);

        return new ProductPage(driver);
    }

    public SearchPage openSkincareCategory() {
        List<WebElement> links = driver.findElements(SKINCARE_MENU_LINK);

        WebElement target = links.stream()
                .filter(WebElement::isDisplayed)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Не удалось найти видимую ссылку Skincare"));

        scrollIntoView(target);
        click(target);

        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("product/category"),
                ExpectedConditions.visibilityOfElementLocated(By.id("sort"))
        ));

        return new SearchPage(driver);
    }
}