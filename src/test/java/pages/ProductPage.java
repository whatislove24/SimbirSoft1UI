package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;

import java.util.Random;

public class ProductPage extends BasePage {

    @FindBy(id = "product_quantity")
    private WebElement quantity;

    @FindBy(css = ".cart")
    private WebElement addToCart;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public void setRandomQuantity() {
        quantity.clear();
        quantity.sendKeys(String.valueOf(new Random().nextInt(3) + 1));
    }

    public void addToCart() {
        addToCart.click();
    }
    public void returnToHome() {
        driver.get("https://automationteststore.com/");
    }
}
