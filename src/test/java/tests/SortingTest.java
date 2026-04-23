package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.SearchPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingTest extends BaseTest {

    @Test
    @DisplayName("Проверка сортировки по имени A-Z")
    void testSortByNameAZ() {
        SearchPage searchPage = homePage.openSkincareCategory();

        searchPage.sortBy("Name A - Z");

        List<String> actualNames = searchPage.getProductNames();
        List<String> expectedNames = new ArrayList<>(actualNames);
        Collections.sort(expectedNames);

        Assertions.assertEquals(expectedNames, actualNames, "Сортировка по имени A-Z неверна");
    }

    @ParameterizedTest(name = "Проверка сортировки по цене: {0}")
    @CsvSource({
            "'Price Low > High', 'asc'",
            "'Price High > Low', 'desc'"
    })
    void testSortByPrice(String sortOption, String direction) {
        SearchPage searchPage = homePage.openSkincareCategory();

        searchPage.sortBy(sortOption);

        List<Double> actualPrices = searchPage.getProductPrices();
        List<Double> expectedPrices = new ArrayList<>(actualPrices);

        if ("asc".equals(direction)) {
            Collections.sort(expectedPrices);
        } else {
            expectedPrices.sort(Collections.reverseOrder());
        }

        Assertions.assertEquals(
                expectedPrices,
                actualPrices,
                "Сортировка по цене работает неверно: " + sortOption
        );
    }
}