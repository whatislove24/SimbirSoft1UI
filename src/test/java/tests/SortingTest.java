package tests;

import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import pages.SearchPage;
import utils.DriverFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class SortingTest {
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
    @DisplayName("Проверка сортировки по имени и цене")
    void testSorting() {
        SearchPage page = new SearchPage(driver);
        // Переходим в категорию "Skincare", где достаточно товаров
        driver.get("https://automationteststore.com/index.php?rt=product/category&path=68");


        page.sortBy("Name A - Z");
        List<String> actualNamesAZ = page.getProductNames();
        List<String> expectedNamesAZ = new ArrayList<>(actualNamesAZ);
        Collections.sort(expectedNamesAZ);
        assertEquals(expectedNamesAZ, actualNamesAZ, "Сортировка по имени A-Z неверна");

        page.sortBy("Price Low > High");
        List<Double> actualPricesLowHigh = page.getProductPrices();
        List<Double> expectedPricesLowHigh = new ArrayList<>(actualPricesLowHigh);
        Collections.sort(expectedPricesLowHigh);
        assertEquals(expectedPricesLowHigh, actualPricesLowHigh, "Сортировка по цене Low > High неверна");

        page.sortBy("Price High > Low");
        List<Double> actualPricesHighLow = page.getProductPrices();
        List<Double> expectedPricesHighLow = new ArrayList<>(actualPricesHighLow);
        expectedPricesHighLow.sort(Collections.reverseOrder());
        assertEquals(expectedPricesHighLow, actualPricesHighLow, "Сортировка по цене High > Low неверна");
    }
}