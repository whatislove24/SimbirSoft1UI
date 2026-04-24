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
            "'Name A - Z', 'name', false",
            "'Name Z - A', 'name', true",
            "'Price Low > High', 'price', false",
            "'Price High > Low', 'price', true"
    })
    void testSorting(String sortOption, String sortType, boolean reversed) {
        SearchPage searchPage = openSortedSkincarePage(sortOption);

        if ("name".equals(sortType)) {
            assertNamesSorted(searchPage, sortOption, reversed);
        } else {
            assertPricesSorted(searchPage, sortOption, reversed);
        }
    }

    private void assertNamesSorted(SearchPage searchPage, String sortOption, boolean reversed) {
        List<String> actualNames = searchPage.getProductNames();
        List<String> expectedNames = sortedCopy(actualNames, reversed);

        Assertions.assertEquals(
                expectedNames,
                actualNames,
                "Сортировка по имени работает неверно: " + sortOption
        );
    }

    private void assertPricesSorted(SearchPage searchPage, String sortOption, boolean reversed) {
        List<Double> actualPrices = searchPage.getProductPrices();
        List<Double> expectedPrices = sortedCopy(actualPrices, reversed);

        Assertions.assertEquals(
                expectedPrices,
                actualPrices,
                "Сортировка по цене работает неверно: " + sortOption
        );
    }

    private SearchPage openSortedSkincarePage(String sortOption) {
        SearchPage searchPage = homePage.openSkincareCategory();
        searchPage.sortBy(sortOption);
        return searchPage;
    }

    private <T extends Comparable<? super T>> List<T> sortedCopy(List<T> actual, boolean reversed) {
        List<T> expected = new ArrayList<>(actual);

        if (reversed) {
            expected.sort(Collections.reverseOrder());
        } else {
            Collections.sort(expected);
        }

        return expected;
    }
}