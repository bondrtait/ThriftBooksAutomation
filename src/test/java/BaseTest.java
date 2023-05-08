import common.Config;
import common.DriverSingleton;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import pages.homepage.HomePage;

public class BaseTest {
    protected WebDriver driver = DriverSingleton.getDriver();
    protected HomePage homePage;

    @BeforeMethod
    public void init() {
        driver = DriverSingleton.getDriver();
        driver.get(Config.THRIFTBOOKS_URL);
        homePage = new HomePage(driver);
    }

    @AfterMethod
    public void close() {
        System.out.println("calling close()");
//        DriverSingleton.quit();
    }
}
