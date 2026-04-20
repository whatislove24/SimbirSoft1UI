package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import utils.PriceUtils;

import java.util.List;

public class SearchPage extends BasePage {

    @FindBy(id = "filter_keyword")
    private WebElement searchInput;

    @FindBy(id = "sort")
    private WebElement sortDropdown;

    public SearchPage(WebDriver driver) {
        super(driver);
    }


    public void search(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        searchInput.clear();
        searchInput.sendKeys(keyword);

        try { Thread.sleep(500); } catch (InterruptedException ignored) {}

        searchInput.sendKeys(Keys.ENTER);
    }


    public void reopenSearch() {
        driver.get("https://automationteststore.com/index.php?rt=product/search");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("maintext")));
    }

    public void sortBy(String value) {
        wait.until(ExpectedConditions.visibilityOf(sortDropdown));
        new Select(sortDropdown).selectByVisibleText(value);
        wait.until(ExpectedConditions.urlContains("sort="));
    }


    public void openProduct(int index) {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("keyword="),
                ExpectedConditions.urlContains("product/search"),
                ExpectedConditions.visibilityOfElementLocated(By.className("maintext"))
        ));


        By productLocator = By.cssSelector("a.prdocutname");

        try {

            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(productLocator));
        } catch (TimeoutException e) {

            driver.navigate().refresh();

            wait.until(ExpectedConditions.visibilityOfElementLocated(productLocator));
        }

        List<WebElement> products = driver.findElements(productLocator);

        if (products.size() >= index) {
            WebElement target = products.get(index - 1);


            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", target);


            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", target);
        } else {
            throw new NoSuchElementException("Товар №" + index + " не найден. Найдено всего: " + products.size());
        }
    }

    public List<String> getProductNames() {
        List<WebElement> names = driver.findElements(By.cssSelector("a.prdocutname"));
        return names.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    public List<Double> getProductPrices() {
        List<WebElement> priceElements = driver.findElements(By.cssSelector(".oneprice, .pricenew"));
        return priceElements.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .map(PriceUtils::parsePrice)
                .filter(price -> price > 0.0)
                .toList();
    }
}
