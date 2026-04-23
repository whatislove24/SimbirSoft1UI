package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pages.SearchPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortingTest extends BaseTest {

    @ParameterizedTest(name = "Проверка сортировки: {0}")
    @CsvSource({
            "'Name A - Z', 'nameAsc'",
            "'Name Z - A', 'nameDesc'",
            "'Price Low > High', 'priceAsc'",
            "'Price High > Low', 'priceDesc'"
    })
    void testSorting(String sortOption, String sortType) {
        SearchPage searchPage = homePage.openSkincareCategory();

        searchPage.sortBy(sortOption);

        switch (sortType) {
            case "nameAsc" -> {
                List<String> actualNames = searchPage.getProductNames();
                List<String> expectedNames = new ArrayList<>(actualNames);
                Collections.sort(expectedNames);

                Assertions.assertEquals(
                        expectedNames,
                        actualNames,
                        "Сортировка по имени A-Z неверна"
                );
            }

            case "nameDesc" -> {
                List<String> actualNames = searchPage.getProductNames();
                List<String> expectedNames = new ArrayList<>(actualNames);
                expectedNames.sort(Collections.reverseOrder());

                Assertions.assertEquals(
                        expectedNames,
                        actualNames,
                        "Сортировка по имени Z-A неверна"
                );
            }

            case "priceAsc" -> {
                List<Double> actualPrices = searchPage.getProductPrices();
                List<Double> expectedPrices = new ArrayList<>(actualPrices);
                Collections.sort(expectedPrices);

                Assertions.assertEquals(
                        expectedPrices,
                        actualPrices,
                        "Сортировка по цене Low > High неверна"
                );
            }

            case "priceDesc" -> {
                List<Double> actualPrices = searchPage.getProductPrices();
                List<Double> expectedPrices = new ArrayList<>(actualPrices);
                expectedPrices.sort(Collections.reverseOrder());

                Assertions.assertEquals(
                        expectedPrices,
                        actualPrices,
                        "Сортировка по цене High > Low неверна"
                );
            }

            default -> throw new IllegalArgumentException("Неизвестный тип сортировки: " + sortType);
        }
    }
}