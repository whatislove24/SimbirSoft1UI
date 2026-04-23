package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utils.PriceUtils;

import java.util.List;

public class SearchPage extends BasePage {

    private static final By PAGE_TITLE = By.className("maintext");
    private static final By PRODUCT_LINKS = By.cssSelector("a.prdocutname");
    private static final By PRICE_ELEMENTS = By.cssSelector(".oneprice, .pricenew");

    @FindBy(id = "filter_keyword")
    private WebElement searchInput;

    @FindBy(id = "sort")
    private WebElement sortDropdown;

    public SearchPage(WebDriver driver) {
        super(driver);
    }

    public SearchPage search(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchInput));
        type(searchInput, keyword);
        searchInput.sendKeys(Keys.ENTER);
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("keyword="),
                ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE)
        ));
        return this;
    }

    public SearchPage sortBy(String value) {
        selectByText(sortDropdown, value);
        wait.until(ExpectedConditions.urlContains("sort="));
        return this;
    }

    public ProductPage openProduct(int index) {
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("keyword="),
                ExpectedConditions.urlContains("product/search"),
                ExpectedConditions.visibilityOfElementLocated(PAGE_TITLE)
        ));

        try {
            wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(PRODUCT_LINKS));
        } catch (TimeoutException e) {
            refreshPage();
            wait.until(ExpectedConditions.visibilityOfElementLocated(PRODUCT_LINKS));
        }

        List<WebElement> products = driver.findElements(PRODUCT_LINKS);

        if (products.size() < index) {
            throw new NoSuchElementException(
                    "Товар №" + index + " не найден. Найдено всего: " + products.size()
            );
        }

        WebElement target = products.get(index - 1);
        scrollIntoView(target);
        jsClick(target);

        return new ProductPage(driver);
    }

    public List<String> getProductNames() {
        List<WebElement> names = driver.findElements(PRODUCT_LINKS);
        return names.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(name -> !name.isEmpty())
                .map(String::toLowerCase)
                .toList();
    }

    public List<Double> getProductPrices() {
        List<WebElement> priceElements = driver.findElements(PRICE_ELEMENTS);
        return priceElements.stream()
                .map(WebElement::getText)
                .map(String::trim)
                .filter(text -> !text.isEmpty())
                .map(PriceUtils::parsePrice)
                .filter(price -> price > 0.0)
                .toList();
    }
}