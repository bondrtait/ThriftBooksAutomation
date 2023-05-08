import data.DataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.homepage.RecSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class HomePageTest extends BaseTest{
    private final int CONTAINERS_PER_SECTION_NUM = 24;

    @Test
    public void testContentBlocksAreDisplayed() {
        homePage.closeCookieConsent();
        Assert.assertTrue(homePage.isMainContentBlockDisplayed());
        Assert.assertTrue(homePage.isSecondaryContentBlockDisplayed());
        Assert.assertTrue(homePage.isFirstTertiaryContentBlockDisplayed());
        Assert.assertTrue(homePage.isSecondTertiaryContentBlockDisplayed());
    }

    @Test(dataProvider = "data")
    public void testSectionScrollWithSliders(Map<String, String> dataMap) {
        homePage.closeCookieConsent();
        RecSection recSection = homePage
                .getRecSection(dataMap.get("sectionTitle"))
                .scrollToSection();

        Assert.assertTrue(recSection.isContainerVisible(0));
        Assert.assertFalse(recSection.isContainerVisible(CONTAINERS_PER_SECTION_NUM));

        while(true) {
            if (!recSection.clickBookSliderNext()) {
                break;
            }
        }

        Assert.assertFalse(recSection.isContainerVisible(0));
        Assert.assertTrue(recSection.isContainerVisible(CONTAINERS_PER_SECTION_NUM));
    }

    @Test
    public void scrollHorizontally() {
        homePage.closeCookieConsent();
       RecSection recSection = homePage
               .getRecSection("Bestsellers")
               .scrollToSection();

       Random r = new Random();
       int bookNumberOnDragscroll = r.nextInt(CONTAINERS_PER_SECTION_NUM) + 1;
       recSection.scrollToBookContainerHorizontally(bookNumberOnDragscroll);
    }

    @DataProvider(name = "data")
    public Object[][] getData() {
        DataReader dataReader = new DataReader();
        List<HashMap<String, String>> data = dataReader
                .getJsonDataToMap("C:\\Users\\ValeriiLevytskyi\\IdeaProjects\\CT\\ThriftBooksAutomation\\src\\test\\java\\data\\RecommendedSectionData.json");

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
