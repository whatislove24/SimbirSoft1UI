package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Random;

public class ProductPage extends BasePage {

    @FindBy(id = "product_quantity")
    private WebElement quantityInput;

    @FindBy(css = ".cart")
    private WebElement addToCartButton;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public ProductPage setRandomQuantity() {
        type(quantityInput, String.valueOf(new Random().nextInt(3) + 1));
        return this;
    }

    public ProductPage addToCart() {
        click(addToCartButton);
        return this;
    }

    public HomePage returnToHome() {
        return goHome();
    }
}