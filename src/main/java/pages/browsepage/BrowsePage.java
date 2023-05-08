package pages.browsepage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;
import pages.HeaderComponent;
import pages.ShoppingCartPage;

import java.util.List;

public class BrowsePage extends BasePage {
    //search query string displayed on the sort bar
    private final By SORT_BAR_SEARCH_QUERY = By.xpath("//div[@class='Search-sortBar-results']/span/strong[2]");
    private final String FILTER_CHECKBOX_LABEL = "//div[contains(@class, 'Checkbox-label') and starts-with(text(),'%s')]";

    //checkbox for every filter depending on the text value - %s
    private final String FILTER_CHECKBOX_INPUT = "//div[contains(@class, 'Checkbox-label') and starts-with(text(),'%s')]/preceding-sibling::input";

    //array of product cards for tile view
    private final By PRODUCT_TILES = By.xpath("//div[@class='SearchContentResults-tilesContainer']/div");

    //a title of a product card, relative to a single product card
    private final By PRODUCT_TILE_TITLE = By.xpath(".//div[@class='AllEditionsItem-tileTitle']/a");

    //total sum of products in the cart, displayed on the Eyebrow panel, which appears after clicking "Add to Cart" button
    private final By EYEBROW_CART_TOTAL = By.xpath("//span[@class='Eyebrow-cartTotal']");

    //title of the most recently added product, displayed on the Eyebrow panel
    private final By EYEBROW_PRODUCT_TITLE = By.xpath("//div[@class='Eyebrow-work']//a");

    //"View Cart" button on the Eyebrow pane;
    private final By EYEBROW_VIEW_CART_BTN = By.xpath("//a[contains(@class,'btn-ViewCart')]");
    private final By SORT_BY_SELECTOR = By.xpath("//select[@id='Search-sortBar-sortby-description']");
    private final String SORT_BY_SELECTOR_OPTION = "//option[@value='%s']";

    private final HeaderComponent header;

    public BrowsePage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    public HeaderComponent getHeader() {
        return header;
    }

    public String getSearchQueryOnSortBar() {
        return driver.findElement(SORT_BAR_SEARCH_QUERY).getText().replace("\"", "");
    }

    public BrowsePage applyFilteringBy(ProductTypes type) {
        driver.findElement(By.xpath(String.format(FILTER_CHECKBOX_INPUT, type.typeString))).click();
        waitForLoadingSpinnerToDisappear();
        return this;
    }

    public boolean isFilterEnabled(ProductTypes type) {
        return Boolean.parseBoolean(driver.findElement(By.xpath(String.format(FILTER_CHECKBOX_INPUT, type.typeString)))
                .getAttribute("value"));
    }

    private List<WebElement> getProductList() {
        return driver.findElements(PRODUCT_TILES);
    }

    public ProductTile getProductTile(String bookTitle) {
        WebElement webElement = getProductList()
                .stream()
                .filter(w -> w.findElement(PRODUCT_TILE_TITLE).getText().equals(bookTitle))
                .findFirst()
                .orElse(null);

        return new ProductTile(driver, webElement);
    }

    public double getEyebrowCartTotal() {
        return Double.parseDouble(driver.findElement(EYEBROW_CART_TOTAL).getText().replace("$", ""));
    }

    public String getEyebrowProductURL() {
        return driver.findElement(EYEBROW_PRODUCT_TITLE).getAttribute("href");
    }

    public ShoppingCartPage clickViewCartEyebrow() {
        driver.findElement(EYEBROW_VIEW_CART_BTN).click();
        wait.until(ExpectedConditions.urlContains("shopping-cart"));
        return new ShoppingCartPage(driver);
    }

    public BrowsePage clickSortBy() {
        driver.findElement(SORT_BY_SELECTOR).click();
        wait.until(ExpectedConditions
                .visibilityOfAllElements(driver.findElement(SORT_BY_SELECTOR)
                                .findElements(By.xpath("*"))));
        return this;
    }

    public BrowsePage clickSortByOption(String optionValue) {
        driver.findElement(SORT_BY_SELECTOR)
                .findElement(By.xpath(String.format(SORT_BY_SELECTOR_OPTION, optionValue))).click();
        waitForLoadingSpinnerToDisappear();
        return this;
    }

    public enum ProductTypes {
        BOOKS("Books"),
        MOVIES_AND_TV("Movies & TV"),
        VIDEO_GAMES("Video Games"),
        MUSIC("Music");

        private final String typeString;

        ProductTypes(String typeString) {
            this.typeString = typeString;
        }

        public String getTypeString() {
            return typeString;
        }
    }
}
