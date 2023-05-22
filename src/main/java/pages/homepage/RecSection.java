package pages.homepage;

import common.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import pages.BasePage;
import pages.ProductPage;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class RecSection extends BasePage {

    private final WebElement recSection;

    private final By BOOK_SLIDER_PREVIOUS = By.xpath(".//div[contains(@class,'BookSlider-prev')]");
    private final By BOOK_SLIDER_NEXT = By.xpath(".//div[contains(@class,'BookSlider-next')]");
    private final By REC_SECTION_DRAGSCROLL = By.xpath(".//div[@class='dragscroll']");
    private final By BOOK_SLIDE_CONTAINERS = By.xpath(".//a[@class='BookSlideDesktop-Container']");

    public RecSection(WebDriver driver, WebElement recSection) {
        super(driver);
        this.recSection = recSection;

        //log
        System.out.println(recSection.findElements(BOOK_SLIDE_CONTAINERS).get(0).getText());
        System.out.println();
        List<WebElement> containersList = recSection.findElements(BOOK_SLIDE_CONTAINERS);
        System.out.println(containersList.get(containersList.size() - 1).getText());

    }

    public RecSection scrollToSection() {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", this.recSection);

        //lambda returns true if recSection bottom boundary is above window bottom boundary;
        //i.e. the section has been scrolled to completely
        Function<WebDriver, Boolean> recSectionHasScrolledIntoView = (driver) ->
                (long)((JavascriptExecutor)driver).executeScript("return window.pageYOffset")
                        + driver.manage().window().getSize().getHeight()
                >= recSection.getLocation().getY()
                        + recSection.getSize().getHeight();

        wait.until(recSectionHasScrolledIntoView);

        return this;
    }

    public boolean clickBookSliderPrevious() {
        if (recSection.findElement(BOOK_SLIDER_PREVIOUS).getAttribute("class").contains("u-fade")) {
            return false;
        }

        WebElement dragscroll = recSection.findElement(REC_SECTION_DRAGSCROLL);
        long scrollLeftBefore = getScrollLeft(dragscroll);

        recSection.findElement(BOOK_SLIDER_PREVIOUS).click();

        //client width + maxScrollLeft = scrollWidth
        //wait until scrollLeft is maxed OR
        //wait until scrollLeft changed by >= 960
        Function<WebDriver, Boolean> sliderScrollFinished = (driver) -> {
            long scrollLeftAfter = getScrollLeft(dragscroll);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            long clientWidth = (long) js.executeScript("return arguments[0].clientWidth;", dragscroll);
            long scrollWidth = (long) js.executeScript("return arguments[0].scrollWidth;", dragscroll);

            return Math.abs(scrollLeftAfter - scrollLeftBefore) >= 960 || clientWidth + scrollLeftAfter == scrollWidth;
        };

        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Config.EXPLICIT_WAIT))
                .pollingEvery(Duration.ofSeconds(1))
                .until(sliderScrollFinished);


        return true;
    }

    public boolean clickBookSliderNext() {
        if (recSection.findElement(BOOK_SLIDER_NEXT).getAttribute("class").contains("u-fade")) {
            return false;
        }

        WebElement dragscroll = recSection.findElement(REC_SECTION_DRAGSCROLL);
        long scrollLeftBefore = getScrollLeft(dragscroll);

        recSection.findElement(BOOK_SLIDER_NEXT).click();

        //client width + maxScrollLeft = scrollWidth
        //wait until scrollLeft is maxed OR
        //wait until scrollLeft changed by >= 960
        Function<WebDriver, Boolean> sliderScrollFinished = (driver) -> {
            long scrollLeftAfter = getScrollLeft(dragscroll);
            JavascriptExecutor js = (JavascriptExecutor) driver;
            long clientWidth = (long) js.executeScript("return arguments[0].clientWidth;", dragscroll);
            long scrollWidth = (long) js.executeScript("return arguments[0].scrollWidth;", dragscroll);

            return scrollLeftAfter - scrollLeftBefore >= 960 || clientWidth + scrollLeftAfter == scrollWidth;
        };

        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Config.EXPLICIT_WAIT))
                .pollingEvery(Duration.ofSeconds(1))
                .until(sliderScrollFinished);

        return true;
    }

    public boolean isContainerVisible(int containerNum) {
        List<WebElement> containersList = recSection.findElements(BOOK_SLIDE_CONTAINERS);
        long scrollLeft = getScrollLeft(recSection.findElement(REC_SECTION_DRAGSCROLL));
        return hOverflowElementIsInViewport(containersList.get(containerNum - 1), scrollLeft);
    }

    //determines if a recSection product container currently entirely in the viewport
    //based on elements bounds and window bounds;
    // if scrollLeft value is not 0, the method factors it in
    private boolean hOverflowElementIsInViewport(WebElement element, long scrollLeft) {
        //get element bounds
        int elLeftBound = element.getLocation().getX();
        int elUpperBound = element.getLocation().getY();
        int elRightBound = elLeftBound + element.getSize().getWidth();
        int elLowerBound = elUpperBound + element.getSize().getHeight();

        //translate the overflown element coordinates to the window coordinates if needed
        if (scrollLeft != 0) {
            elRightBound %= scrollLeft;
            elLeftBound %= scrollLeft;
        }

        JavascriptExecutor js = (JavascriptExecutor) driver;

        //get window bounds
        long winUpperBound = (long) js.executeScript("return window.pageYOffset");
        long winLeftBound = (long) js.executeScript("return window.pageXOffset");
        long winWidth = (long) js.executeScript("return document.documentElement.clientWidth");
        long winHeight = (long) js.executeScript("return document.documentElement.clientHeight");
        long winRightBound = winLeftBound + winWidth;
        long winLowerBound = winUpperBound + winHeight;

        return winLeftBound <= elLeftBound &&
                winRightBound >= elRightBound &&
                winUpperBound <= elUpperBound &&
                winLowerBound >= elLowerBound;
    }

    public void scrollToBookContainerHorizontally(int bookNumberOnDragscroll){
        List<WebElement> bookContainers = recSection.findElements(BOOK_SLIDE_CONTAINERS);

        if (bookNumberOnDragscroll < 1 || bookNumberOnDragscroll > bookContainers.size()) {
            throw new IllegalArgumentException("The provided bookNumberOnDragscroll is exceeding " +
                    "the actual number of books in the section dragscroll.");
        }

        WebElement targetContainer = bookContainers.get(bookNumberOnDragscroll - 1);
        WebElement dragscroll = driver.findElement(REC_SECTION_DRAGSCROLL);
        JavascriptExecutor js = (JavascriptExecutor) driver;

        long containerOffsetLeft = getOffsetLeft(targetContainer);
        long containerOffsetWidth = (long) js.executeScript("return arguments[0].offsetWidth;", targetContainer);
        long dragscrollClientWidth = (long) js.executeScript("return arguments[0].clientWidth;", dragscroll);

        js.executeScript(String.format("arguments[0].scrollLeft = %s - %s - %s;"
                        , containerOffsetLeft, containerOffsetWidth, dragscrollClientWidth)
                        , dragscroll);
    }

    public ProductPage clickBookContainer(int bookContainerNum) {
        driver.findElements(BOOK_SLIDE_CONTAINERS).get(bookContainerNum).click();
        wait.until(ExpectedConditions.urlMatches("thriftbooks.com/w/"));
        return new ProductPage(driver);
    }
}
