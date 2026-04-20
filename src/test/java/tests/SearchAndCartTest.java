package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pages.*;
import utils.DriverFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.junit.jupiter.api.Assertions.*;

public class SearchAndCartTest {

    WebDriver driver;
    @BeforeEach
    void setUp() {
        DriverFactory.createDriver();
        driver = DriverFactory.getDriver();
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }
    @Test
    void testSearchAndCart() {

        SearchPage search = new SearchPage(driver);
        ProductPage product = new ProductPage(driver);
        CartPage cart = new CartPage(driver);

        driver.get("https://automationteststore.com/");

        search.search("shirt");
        search.sortBy("Name A - Z");

        for (int i = 2; i <= 3; i++) {
            search.openProduct(i);
            product.setRandomQuantity();
            product.addToCart();

            driver.navigate().back();
            search.search("shirt");
            search.sortBy("Name A - Z");
        }

        cart.open();

        WebElement cheapest = cart.getCheapestRow();

        WebElement qty = cheapest.findElement(By.cssSelector("input"));
        int value = Integer.parseInt(qty.getAttribute("value"));

        qty.clear();
        qty.sendKeys(String.valueOf(value * 2));

        driver.findElement(By.id("cart_update")).click();

        assertTrue(cart.getTotal() > 0);
    }
}
