package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import pages.CartPage;
import pages.ProductPage;
import pages.SearchPage;
import utils.DriverFactory;
import utils.TestConfig;

public class SearchAndCartTest {

    private WebDriver driver;
    private SearchPage search;
    private ProductPage product;
    private CartPage cart;

    @BeforeEach
    void setUp() {
        DriverFactory.createDriver();
        driver = DriverFactory.getDriver();

        search = new SearchPage(driver);
        product = new ProductPage(driver);
        cart = new CartPage(driver);

        driver.get(TestConfig.BASE_URL);
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }

    @Test
    void testSearchAndCart() {
        search.search("shirt");
        search.sortBy("Name A - Z");

        for (int i = 2; i <= 3; i++) {
            search.openProduct(i);
            product.setRandomQuantity();
            product.addToCart();
            product.returnToHome();

            search.search("shirt");
            search.sortBy("Name A - Z");
        }

        cart.open();

        double before = cart.getTotal();
        cart.updateCheapestItemQuantity(2);
        double after = cart.getTotal();

        Assertions.assertTrue(after > before, "Сумма не увеличилась");
    }
}
