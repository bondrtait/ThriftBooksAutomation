package pages.homepage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pages.BasePage;
import pages.HeaderComponent;

public class HomePage extends BasePage {

    private final By CONTENT_BLOCK_MAIN = By.xpath("//a[@class='HomepageContentBlocks-horizontalHalf Main']");
    private final By CONTENT_BLOCK_SECONDARY = By.xpath("//a[@class='HomepageContentBlocks-verticalHalf Secondary']");
    private final By CONTENT_BLOCK_TERTIARY = By.xpath("//a[@class='HomepageContentBlocks-horizontalHalf Tertiary']");
    private final String REC_SECTION = "//div[contains(@class, 'BookSliderNav') and .//h2[text()='%s']]";
    private final By COOKIE_CONSENT_CLOSE = By.xpath("//button[@class=' osano-cm-dialog__close osano-cm-close ']");

    private final HeaderComponent header;

    public HomePage(WebDriver driver) {
        super(driver);
        this.header = new HeaderComponent(driver);
    }

    public HeaderComponent getHeader() {
        return header;
    }

    public boolean isMainContentBlockDisplayed() {
        return driver.findElement(CONTENT_BLOCK_MAIN).isDisplayed();
    }

    public boolean isSecondaryContentBlockDisplayed() {
        return driver.findElement(CONTENT_BLOCK_SECONDARY).isDisplayed();
    }

    public boolean isFirstTertiaryContentBlockDisplayed() {
        return driver.findElements(CONTENT_BLOCK_TERTIARY).get(0).isDisplayed();
    }

    public boolean isSecondTertiaryContentBlockDisplayed() {
        return driver.findElements(CONTENT_BLOCK_TERTIARY).get(1).isDisplayed();
    }

    public RecSection getRecSection(String sectionTitle) {
        WebElement webElement = driver.findElement(By.xpath(String.format(REC_SECTION, sectionTitle)));

        return new RecSection(driver, webElement);
    }
}
