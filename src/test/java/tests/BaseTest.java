package tests;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;
import pages.SearchPage;
import utils.DriverFactory;

public abstract class BaseTest {

    protected WebDriver driver;
    protected HomePage homePage;
    protected ProductPage productPage;
    protected SearchPage searchPage;
    protected CartPage cartPage;

    @BeforeEach
    void setUp() {
        DriverFactory.createDriver();
        driver = DriverFactory.getDriver();

        homePage = new HomePage(driver);
        productPage = new ProductPage(driver);
        searchPage = new SearchPage(driver);
        cartPage = new CartPage(driver);

        homePage.open();
    }

    @AfterEach
    void tearDown() {
        DriverFactory.quitDriver();
    }
}