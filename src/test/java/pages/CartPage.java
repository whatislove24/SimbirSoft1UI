package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.PriceUtils;

import java.util.List;

public class CartPage extends BasePage {

    private static final By PAGE_TITLE =
            By.xpath("//span[@class='maintext' and contains(text(), 'Shopping Cart')]");
    private static final By TOTAL_AMOUNT =
            By.xpath("//table//td[.//span[contains(text(), 'Total')]]/following-sibling::td/span");
    private static final By REMOVE_BUTTONS = By.xpath("//a[contains(@href,'remove')]");
    private static final By PRODUCT_ROWS =
            By.xpath("//table//tr[descendant::input[contains(@id, 'cart_quantity')]]");
    private static final By QUANTITY_INPUT =
            By.xpath(".//input[contains(@id, 'cart_quantity')]");
    private static final By UPDATE_BUTTON = By.id("cart_update");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public CartPage waitUntilOpened() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE));
        return this;
    }

    public double getTotal() {
        wait.until(ExpectedConditions.presenceOfElementLocated(TOTAL_AMOUNT));

        return wait.ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    String text = d.findElement(TOTAL_AMOUNT).getText();
                    if (text.isEmpty()) {
                        return null;
                    }
                    return PriceUtils.parsePrice(text);
                });
    }

    public CartPage removeEvenItems() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(REMOVE_BUTTONS));

        List<WebElement> buttons = driver.findElements(REMOVE_BUTTONS);

        for (int i = buttons.size(); i >= 1; i--) {
            if (i % 2 == 0) {
                WebElement button = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//a[contains(@href,'remove')])[" + i + "]")
                ));
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

        List<WebElement> rows = driver.findElements(PRODUCT_ROWS);
        double min = Double.MAX_VALUE;
        WebElement cheapest = null;

        for (WebElement row : rows) {
            try {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 4) {
                    double price = PriceUtils.parsePrice(cells.get(3).getText());
                    if (price < min) {
                        min = price;
                        cheapest = row;
                    }
                }
            } catch (StaleElementReferenceException e) {
                return getCheapestRow();
            }
        }

        return cheapest;
    }

    public CartPage updateCheapestItemQuantity(int multiplier) {
        WebElement row = getCheapestRow();

        if (row == null) {
            throw new NoSuchElementException("Не удалось найти товар для обновления количества.");
        }

        WebElement input = row.findElement(QUANTITY_INPUT);
        int currentQty = Integer.parseInt(input.getAttribute("value"));

        type(input, String.valueOf(currentQty * multiplier));

        WebElement updateButton = driver.findElement(UPDATE_BUTTON);
        click(updateButton);
        wait.until(ExpectedConditions.stalenessOf(updateButton));

        return this;
    }
}