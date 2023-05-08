import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pages.browsepage.BrowsePage;
import pages.browsepage.ProductTile;
import pages.ShoppingCartPage;

public class OneBookSearchAndCartTest extends BaseTest{
    BrowsePage browsePage;
    ProductTile productTile;
    String eyebrowProductURL;

    @Test
    @Parameters({"searchQuery", "bookTitle"})
    public void testSearchingBook(String searchQuery, String bookTitle) {
        browsePage = homePage.getHeader()
                .clickSearchBar()
                .inputSearchQuery(searchQuery)
                .clickSearchBtn();

        if (!browsePage.isFilterEnabled(BrowsePage.ProductTypes.BOOKS)) {
            browsePage.applyFilteringBy(BrowsePage.ProductTypes.BOOKS);
        }

        Assert.assertEquals(browsePage.getSearchQueryOnSortBar(), searchQuery);

        productTile = browsePage.getProductTile(bookTitle);
        Assert.assertEquals(productTile.getProductTileTitle(), bookTitle);
    }

    @Test(dependsOnMethods = {"testSearchingBook"})
    public void testAddBookToCart() {
        Assert.assertEquals(productTile.getTileAddToCartLabelText(), "Add To Cart");

        productTile.clickTileAddToCart();

        Assert.assertEquals(productTile.getTileAddToCartLabelText(), "Added to Cart");

        Assert.assertEquals(productTile.getTileProductPrice(),
                browsePage.getEyebrowCartTotal());

        eyebrowProductURL = browsePage.getEyebrowProductURL();

        //assert product link from the card contains product link from the eyebrow
        //meaning that the same product which was added to cart is shown on the eyebrow panel
        Assert.assertTrue(productTile.getProductTileURL().contains(eyebrowProductURL));
    }

    @Test(dependsOnMethods = "testAddBookToCart")
    public void testCartPageCalculations() {
        ShoppingCartPage shoppingCartPage = browsePage.clickViewCartEyebrow();

        //check book is present in the cart
        Assert.assertTrue(shoppingCartPage.getCartItemURL(eyebrowProductURL).contains(eyebrowProductURL));

        //check that quantity is 1
        int productQuantity = shoppingCartPage.getCartItemQuantityDropdownValue(eyebrowProductURL);
        Assert.assertEquals(productQuantity, 1);

        //Check that subtotal = quantity * each
        Assert.assertEquals(shoppingCartPage.getCartItemSubtotal(eyebrowProductURL),
                shoppingCartPage.getCartItemEachPrice(eyebrowProductURL));

        //check subtotal on order summary = subtotal on each item in the cart
        Assert.assertEquals(shoppingCartPage.getCartSummarySubtotal(),
                shoppingCartPage.getCartItemSubtotal(eyebrowProductURL));
    }
}
