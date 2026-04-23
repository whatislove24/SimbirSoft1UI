package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pages.CartPage;

public class CartTest extends BaseTest {

    @Test
    void testRemoveEvenItems() {
        for (int i = 0; i < 5; i++) {
            homePage = homePage.openRandomProduct()
                    .setRandomQuantity()
                    .addToCart()
                    .returnToHome();
        }

        CartPage cartPage = homePage.goToCart().waitUntilOpened();

        double before = cartPage.getTotal();
        cartPage.removeEvenItems();
        double after = cartPage.getTotal();

        Assertions.assertTrue(after < before);
    }
}