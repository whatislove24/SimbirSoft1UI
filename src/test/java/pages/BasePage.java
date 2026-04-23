package pages;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.TestConfig;

public abstract class BasePage {

    protected final WebDriver driver;
    protected final WebDriverWait wait;

    @FindBy(css = "a.logo")
    private WebElement homeLink;

    @FindBy(css = "a[href*='rt=checkout/cart']")
    private WebElement cartLink;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, TestConfig.DEFAULT_TIMEOUT);
        PageFactory.initElements(driver, this);
    }

    protected void openStartPage() {
        driver.get(TestConfig.BASE_URL);
    }

    protected String getTitle() {
        return driver.getTitle();
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void clearAndType(WebElement element, String value) {
        wait.ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOf(element));
        element.clear();
        element.sendKeys(value);
    }

    protected void click(WebElement element) {
        wait.ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.elementToBeClickable(element))
                .click();
    }

    protected void selectByText(WebElement element, String text) {
        wait.ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOf(element));
        new Select(element).selectByVisibleText(text);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({block:'center'});", element);
    }

    protected void jsClick(WebElement element) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].click();", element);
    }

    protected void refreshPage() {
        driver.navigate().refresh();
    }

    public HomePage goHome() {
        click(homeLink);
        return new HomePage(driver);
    }

    public CartPage goToCart() {
        click(cartLink);
        return new CartPage(driver).waitUntilOpened();
    }
}