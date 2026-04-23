package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.CartPage;
import pages.SearchPage;

public class SearchAndCartTest extends BaseTest {

    @Test
    void testSearchAndCart() {
        SearchPage searchPage = homePage.openSkincareCategory()
                .searchAndSort("shirt", "Name A - Z");

        for (int i = 2; i <= 3; i++) {
            homePage = searchPage.openProduct(i)
                    .setRandomQuantity()
                    .addToCart()
                    .returnToHome();

            searchPage = homePage.openSkincareCategory()
                    .searchAndSort("shirt", "Name A - Z");
        }

        CartPage cartPage = homePage.goToCart();

        double before = cartPage.getTotal();
        cartPage.updateCheapestItemQuantity(2);
        double after = cartPage.getTotal();

        Assertions.assertTrue(after > before, "Сумма не увеличилась");
    }
}