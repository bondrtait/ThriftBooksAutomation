import common.DriverSingleton;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.ShoppingCartPage;
import pages.browsepage.BrowsePage;
import pages.browsepage.ProductTile;
import pages.homepage.HomePage;

public class MultipleBooksSearchAndAddCartTest extends BaseTest{
    BrowsePage browsePage;
    ShoppingCartPage shoppingCartPage;
    ProductTile productTile;
    String eyebrowProductURL;
    String[][] testData = new String[][] {
            {"The Lord of the Rings", "The Lord of the Rings Trilogy"},
            {"451", "Fahrenheit 451"}
    };

    @Test
    public void testAssemblingCart() {
        HomePage homePage = new HomePage(DriverSingleton.getDriver());
        homePage.closeCookieConsent();
        for (String[] testDatum : testData) {
            browsePage = homePage.getHeader()
                    .clickSearchBar()
                    .inputSearchQuery(testDatum[0])
                    .clickSearchBtn()
                    .applyFilteringBy(BrowsePage.ProductTypes.BOOKS);

            Assert.assertEquals(browsePage.getSearchQueryOnSortBar(), testDatum[0]);

            productTile = browsePage.getProductTile(testDatum[1]);
            Assert.assertEquals(productTile.getProductTileTitle(), testDatum[1]);

            Assert.assertEquals(productTile.getTileAddToCartLabelText(), "Add To Cart");

            productTile.clickTileAddToCart();

            Assert.assertEquals(productTile.getTileAddToCartLabelText(), "Added to Cart");

            Assert.assertTrue(Math.abs(browsePage.getCartSubtotal() - browsePage.getEyebrowCartTotal()) < 0.0001);

            eyebrowProductURL = browsePage.getEyebrowProductURL();

            //assert product link from the card contains product link from the eyebrow
            //meaning that the same product which was added to cart is shown on the eyebrow panel
            Assert.assertTrue(productTile.getProductTileURL().contains(eyebrowProductURL));

            browsePage.getHeader().clickSearchResetBtn();
        }

        shoppingCartPage = browsePage.clickViewCartEyebrow();
        Assert.fail("Failing on purpose");
    }
}
