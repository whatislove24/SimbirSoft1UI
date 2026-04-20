package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pages.*;
import utils.DriverFactory;

import static org.junit.jupiter.api.Assertions.*;

public class CartTest {

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
    void testRemoveEvenItems() {

        HomePage home = new HomePage(driver);
        ProductPage product = new ProductPage(driver);
        CartPage cart = new CartPage(driver);

        home.open();

        for (int i = 0; i < 5; i++) {
            home.openRandomProduct();
            product.setRandomQuantity();
            product.addToCart();
            home.open();
        }

        cart.open();

        double before = cart.getTotal();
        cart.removeEvenItems();
        double after = cart.getTotal();

        assertTrue(after < before);
    }
}
