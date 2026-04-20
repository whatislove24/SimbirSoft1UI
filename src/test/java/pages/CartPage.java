package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import java.util.List;
import utils.PriceUtils;

public class CartPage extends BasePage {


    @FindBy(xpath = "//table//td[.//span[contains(text(), 'Total')]]/following-sibling::td/span")
    private WebElement totalAmount;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void open() {
        driver.get("https://automationteststore.com/index.php?rt=checkout/cart");

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//span[@class='maintext' and contains(text(), 'Shopping Cart')]")));
    }

    public double getTotal() {

        By totalLocator = By.xpath("//table//td[.//span[contains(text(), 'Total')]]/following-sibling::td/span");

        WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(totalLocator));

        return wait.ignoring(StaleElementReferenceException.class)
                .until(d -> {
                    String text = d.findElement(totalLocator).getText();
                    if (text.isEmpty()) return null;
                    return PriceUtils.parsePrice(text);
                });
    }

    public void removeEvenItems() {

        By removeBtnXpath = By.xpath("//a[contains(@href,'remove')]");
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(removeBtnXpath));

        List<WebElement> buttons = driver.findElements(removeBtnXpath);

        for (int i = buttons.size(); i >= 1; i--) {
            if (i % 2 == 0) {
                WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(
                        By.xpath("(//a[contains(@href,'remove')])[" + i + "]")));
                btn.click();

                wait.until(ExpectedConditions.stalenessOf(btn));
            }
        }
    }

    public WebElement getCheapestRow() {

        By rowLocator = By.xpath("//table//tr[descendant::input[contains(@id, 'cart_quantity')]]");

        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
        } catch (TimeoutException e) {

            driver.navigate().refresh();
            try {
                wait.until(ExpectedConditions.visibilityOfElementLocated(rowLocator));
            } catch (TimeoutException e2) {
                return null;
            }
        }

        List<WebElement> currentRows = driver.findElements(rowLocator);
        double min = Double.MAX_VALUE;
        WebElement cheapest = null;

        for (WebElement row : currentRows) {
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

    public void updateCheapestItemQuantity(int multiplier) {
        WebElement row = getCheapestRow();
        if (row == null) {
            throw new NoSuchElementException("Не удалось найти товар для обновления количества.");
        }

        WebElement input = row.findElement(By.xpath(".//input[contains(@id, 'cart_quantity')]"));
        int currentQty = Integer.parseInt(input.getAttribute("value"));

        input.clear();
        input.sendKeys(String.valueOf(currentQty * multiplier));

        WebElement updateBtn = driver.findElement(By.id("cart_update"));
        updateBtn.click();

        wait.until(ExpectedConditions.stalenessOf(updateBtn));
    }
}
