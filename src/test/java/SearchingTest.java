import data.DataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.browsepage.BrowsePage;
import pages.browsepage.ProductTile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchingTest extends BaseTest{
//    private final String SEARCH_FIELD_PLACEHOLDER = "Search 13 million titles by title, author, or ISBN";
    private final int MAX_SUGGESTIONS_NUM = 13;

    @Test(dataProvider = "data")
    public void testSearchInputAndSuggestions(Map<String, String> dataMap) {
        Assert.assertTrue(homePage.getHeader().getSearchBarValue().isEmpty());
        homePage.getHeader().clickSearchBar().inputSearchQuery(dataMap.get("query"));
        Assert.assertEquals(homePage.getHeader().getSearchBarValue(), dataMap.get("query"));


        List<String> searchSuggestions = homePage.getHeader().getSearchSuggestions();

        Assert.assertEquals(searchSuggestions.get(0), "Search for \"" + dataMap.get("query") + "\"");
        Assert.assertTrue(searchSuggestions.size() <= MAX_SUGGESTIONS_NUM);
        Assert.assertTrue(searchSuggestions.size() > 0);
        Assert.assertTrue(searchSuggestions.stream().allMatch(s -> s.toLowerCase().contains(dataMap.get("query"))));

        homePage.getHeader().clickSearchResetBtn();
        Assert.assertTrue(homePage.getHeader().getSearchBarValue().isEmpty());
    }

    @Test(dataProvider = "data")
    public void testSearchResultsByQuery(Map<String, String> dataMap) {
        BrowsePage browsePage = homePage.getHeader()
                .clickSearchBar()
                .inputSearchQuery(dataMap.get("query"))
                .clickSearchBtn();

        ProductTile targetProduct = browsePage
                .clickSortBy()
                .clickSortByOption("topMatches-desc")
                .getProductTile(dataMap.get("book-title"));

        Assert.assertNotNull(targetProduct);
    }

    @DataProvider(name = "data")
    public Object[][] getData() {
        DataReader dataReader = new DataReader();
        List<HashMap<String, String>> data = dataReader
                .getJsonDataToMap("C:\\Users\\ValeriiLevytskyi\\IdeaProjects\\CT\\ThriftBooksAutomation\\src\\test\\java\\data\\BookSearchData.json");

        if (!data.isEmpty()) {
            Object[][] obj = new Object[data.size()][1];

            for(int i = 0; i < obj.length; i++){
                obj[i][0] = data.get(i);
            }

            return obj;
        } else {
            return null;
        }
    }
}
