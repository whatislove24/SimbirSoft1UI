package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SearchAndCartTest extends BaseTest {

    @Test
    void testSearchAndCart() {
        searchPage.searchAndSort("shirt", "Name A - Z");

        for (int i = 2; i <= 3; i++) {
            searchPage.openProduct(i);
            productPage.setRandomQuantity();
            productPage.addToCart();
            productPage.returnToHome();

            searchPage.searchAndSort("shirt", "Name A - Z");
        }

        cartPage.open();

        double before = cartPage.getTotal();
        cartPage.updateCheapestItemQuantity(2);
        double after = cartPage.getTotal();

        Assertions.assertTrue(after > before, "Сумма не увеличилась");
    }
}