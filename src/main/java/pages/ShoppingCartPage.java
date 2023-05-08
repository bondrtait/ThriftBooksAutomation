package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

public class ShoppingCartPage extends BasePage{

    //returns multiple elements
    private final By CART_ITEMS = By.xpath("//div[@class='ShoppingCartItem']");

    //relative to a CART_ITEM
    private final By CART_ITEM_TITLE = By.xpath(".//a[@class='ShoppingCartItem-title']");

    //relative to a CART_ITEM
    private final By CART_ITEM_QUANTITY_DROPDOWN = By.xpath(".//select[@aria-label='Select Quantity']");

    //relative to a CART_ITEM
    private final By CART_ITEM_PRICE_EACH = By.xpath(".//span[contains(@class,'ShoppingCartItem-priceEach')]");

    //relative to a CART_ITEM
    private final By CART_ITEM_PRICE_SUBTOTAL = By.xpath(".//span[@class = 'ShoppingCartItem-price']");
    private final By CART_SUMMARY_SUBTOTAL = By.xpath("//div[@class='OrderSummary-Item'][1]/div[2]");

    public ShoppingCartPage(WebDriver driver) {
        super(driver);
    }

    private List<WebElement> getCartItems() {
        return driver.findElements(CART_ITEMS);
    }

    private WebElement getCartItem(String bookURL) {
        return getCartItems()
                .stream()
                .filter(w -> w.findElement(CART_ITEM_TITLE).getAttribute("href").contains(bookURL))
                .findFirst()
                .orElse(null);
    }

    public String getCartItemURL(String bookURL) {
        return getCartItem(bookURL).findElement(CART_ITEM_TITLE).getAttribute("href");
    }

    public int getCartItemQuantityDropdownValue(String bookURL) {
        Select select = new Select(getCartItem(bookURL).findElement(CART_ITEM_QUANTITY_DROPDOWN));
        return Integer.parseInt(select.getFirstSelectedOption().getText());
    }

    public double getCartItemEachPrice(String bookURL) {
        return Double.parseDouble(getCartItem(bookURL)
                .findElement(CART_ITEM_PRICE_EACH)
                .getText()
                .replace("$", ""));
    }

    public double getCartItemSubtotal(String bookURL) {
        return Double.parseDouble(getCartItem(bookURL)
                .findElement(CART_ITEM_PRICE_SUBTOTAL)
                .getText()
                .replace("$", ""));
    }

    public double getCartSummarySubtotal() {
        return Double.parseDouble(driver
                .findElement(CART_SUMMARY_SUBTOTAL)
                .getText()
                .replace("$", ""));
    }
}