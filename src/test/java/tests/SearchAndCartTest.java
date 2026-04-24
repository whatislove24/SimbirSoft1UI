package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.SearchPage;

public class SearchAndCartTest extends BaseTest {

    private static final String SEARCH_QUERY = "shirt";
    private static final String SORT_OPTION = "Name A - Z";

    @Test
    void testSearchAndCart() {
        SearchPage searchPage = homePage.openSkincareCategory()
                .searchAndSort(SEARCH_QUERY, SORT_OPTION);

        for (int i = 2; i <= 3; i++) {
            homePage = searchPage.openProduct(i)
                    .setRandomQuantity()
                    .addToCart()
                    .returnToHome();

            searchPage = homePage.openSkincareCategory()
                    .searchAndSort(SEARCH_QUERY, SORT_OPTION);
        }

        CartPage cartPage = homePage.goToCart();

        double before = cartPage.getTotal();
        cartPage.updateCheapestItemQuantity(2);
        double after = cartPage.getTotal();

        Assertions.assertTrue(after > before, "Сумма корзины не увеличилась после изменения количества товара");
    }
}