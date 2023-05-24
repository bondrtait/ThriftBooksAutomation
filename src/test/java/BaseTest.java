import common.Config;
import common.DriverSingleton;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
//    protected WebDriver driver;
//    protected HomePage homePage;

    @BeforeMethod
    public void init() {
        WebDriver driver = DriverSingleton.getDriver();
//        System.out.println("Driver setup by Thread " + Thread.currentThread().getId() + " and Driver reference is: " + driver);
        driver.get(Config.THRIFTBOOKS_URL);
//        homePage = new HomePage(driver);
//        System.out.println("Before Method: Thread " + Thread.currentThread().getId() + " homePage = " + homePage);
    }

    @AfterMethod
    public void close() {
//        System.out.println("Browser closed by Thread "+Thread.currentThread().getId() + " and Closing driver reference is : " + DriverSingleton.getDriver());
//        System.out.println("After Method: Thread " + Thread.currentThread().getId() + " homePage = " + homePage);
        DriverSingleton.quit();
    }
}
