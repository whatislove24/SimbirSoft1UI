package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.PriceUtils;

import java.util.List;

public class CartPage extends BasePage {

    private static final By REMOVE_BUTTONS =
            By.xpath("//a[contains(@href,'remove')]");

    private static final By PRODUCT_ROWS =
            By.xpath("//table//tr[descendant::input[contains(@id, 'cart_quantity')]]");

    private static final By QUANTITY_INPUT =
            By.xpath(".//input[contains(@id, 'cart_quantity')]");

    @FindBy(xpath = "//span[@class='maintext' and contains(text(), 'Shopping Cart')]")
    private WebElement pageTitle;

    @FindBy(xpath = "//table//td[.//span[contains(text(), 'Total')]]/following-sibling::td/span")
    private WebElement totalAmount;

    @FindBy(id = "cart_update")
    private WebElement updateButton;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage waitUntilOpened() {
        wait.until(ExpectedConditions.visibilityOf(pageTitle));
        return this;
    }

    public double getTotal() {
        return wait.ignoring(StaleElementReferenceException.class)
                .until(driver -> {
                    String text = totalAmount.getText();

                    if (text == null || text.isBlank()) {
                        return null;
                    }

                    return PriceUtils.parsePrice(text);
                });
    }

    public CartPage removeEvenItems() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(REMOVE_BUTTONS));

        int count = driver.findElements(REMOVE_BUTTONS).size();

        for (int i = count; i >= 1; i--) {
            if (i % 2 == 0) {
                By indexedRemoveButton =
                        By.xpath("(//a[contains(@href,'remove')])[" + i + "]");

                WebElement button = wait.until(
                        ExpectedConditions.elementToBeClickable(indexedRemoveButton)
                );

                button.click();
                wait.until(ExpectedConditions.stalenessOf(button));
            }
        }

        return this;
    }

    public WebElement getCheapestRow() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_ROWS));
        } catch (TimeoutException e) {
            refreshPage();

            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_ROWS));
            } catch (TimeoutException ex) {
                return null;
            }
        }

        return findCheapestRow();
    }

    private WebElement findCheapestRow() {
        List<WebElement> rows = driver.findElements(PRODUCT_ROWS);

        double minPrice = Double.MAX_VALUE;
        WebElement cheapestRow = null;

        for (WebElement row : rows) {
            try {
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() < 4) {
                    continue;
                }

                double price = PriceUtils.parsePrice(cells.get(3).getText());

                if (price < minPrice) {
                    minPrice = price;
                    cheapestRow = row;
                }

            } catch (StaleElementReferenceException e) {
                return getCheapestRow();
            }
        }

        return cheapestRow;
    }

    public CartPage updateCheapestItemQuantity(int multiplier) {
        WebElement cheapestRow = getCheapestRow();

        if (cheapestRow == null) {
            throw new NoSuchElementException(
                    "Не удалось найти товар для обновления количества."
            );
        }

        double totalBeforeUpdate = getTotal();

        WebElement quantityInput = cheapestRow.findElement(QUANTITY_INPUT);

        int currentQuantity = Integer.parseInt(quantityInput.getAttribute("value"));
        int newQuantity = currentQuantity * multiplier;

        clearAndType(quantityInput, String.valueOf(newQuantity));

        click(updateButton);

        wait.until(driver -> getTotal() != totalBeforeUpdate);

        return this;
    }
}