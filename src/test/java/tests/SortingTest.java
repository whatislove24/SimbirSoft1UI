package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingTest extends BaseTest {

    @Test
    @DisplayName("Проверка сортировки по имени и цене")
    void testSorting() {

        driver.get("https://automationteststore.com/index.php?rt=product/category&path=68");

        searchPage.sortBy("Name A - Z");
        List<String> actualNamesAZ = searchPage.getProductNames();
        List<String> expectedNamesAZ = new ArrayList<>(actualNamesAZ);
        Collections.sort(expectedNamesAZ);
        Assertions.assertEquals(expectedNamesAZ, actualNamesAZ, "Сортировка по имени A-Z неверна");

        searchPage.sortBy("Price Low > High");
        List<Double> actualPricesLowHigh = searchPage.getProductPrices();
        List<Double> expectedPricesLowHigh = new ArrayList<>(actualPricesLowHigh);
        Collections.sort(expectedPricesLowHigh);
        Assertions.assertEquals(expectedPricesLowHigh, actualPricesLowHigh, "Сортировка по цене Low > High неверна");

        searchPage.sortBy("Price High > Low");
        List<Double> actualPricesHighLow = searchPage.getProductPrices();
        List<Double> expectedPricesHighLow = new ArrayList<>(actualPricesHighLow);
        expectedPricesHighLow.sort(Collections.reverseOrder());
        Assertions.assertEquals(expectedPricesHighLow, actualPricesHighLow, "Сортировка по цене High > Low неверна");
    }
}