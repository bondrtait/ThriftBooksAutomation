package pages.browsepage;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.BasePage;

public class ProductTile extends BasePage {
    private final WebElement productTile;

    //a title of a product card, relative to a single product card
    private final By PRODUCT_TILE_TITLE = By.xpath(".//div[@class='AllEditionsItem-tileTitle']/a");

    //"Add to Cart" button of a product card, relative to a single product card
    private final By PRODUCT_TILE_ADD_TO_CART_BTN = By.xpath(".//div[contains(@class, 'AddToCartButton-Desktop')]");

    //product price on the product card, relative to a single product card
    private final By PRODUCT_TILE_PRICE = By.xpath(".//div[@class='SearchResultListItem-dollarAmount']");

    public ProductTile(WebDriver driver, WebElement productTile) {
        super(driver);
        this.productTile = productTile;
    }

    public String getProductTileTitle() {
        return productTile.findElement(PRODUCT_TILE_TITLE).getText();
    }

    public String getProductTileURL() {
        return productTile.findElement(PRODUCT_TILE_TITLE).getAttribute("href");
    }

    public void clickTileAddToCart() {
        productTile.findElement(PRODUCT_TILE_ADD_TO_CART_BTN).click();

        //trying to wait for the label text to change, but letting the execution go further if, for some reason, it has not
        //the validation of the label text is to be performed inside the tests
        try {
            wait.until(ExpectedConditions.textToBePresentInElement(
                            productTile.findElement(PRODUCT_TILE_ADD_TO_CART_BTN), "Added to Cart"));
        } catch (TimeoutException e) {
            System.out.println(e.getMessage());
        }

        cartSubtotal += getTileProductPrice();
    }

    public String getTileAddToCartLabelText() {
        return productTile.findElement(PRODUCT_TILE_ADD_TO_CART_BTN).getText();
    }

    public double getTileProductPrice() {
        return Double.parseDouble(productTile.findElement(PRODUCT_TILE_PRICE).getText());
    }
}
