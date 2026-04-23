package tests;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CartTest extends BaseTest {

    @Test
    void testRemoveEvenItems() {
        for (int i = 0; i < 5; i++) {
            homePage.open();
            homePage.openRandomProduct();
            productPage.setRandomQuantity();
            productPage.addToCart();
            productPage.returnToHome();
        }

        cartPage.open();

        double before = cartPage.getTotal();
        cartPage.removeEvenItems();
        double after = cartPage.getTotal();

        Assertions.assertTrue(after < before);
    }
}