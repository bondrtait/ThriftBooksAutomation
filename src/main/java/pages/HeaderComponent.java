package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import pages.browsepage.BrowsePage;

import java.util.List;
import java.util.stream.Collectors;

public class HeaderComponent extends BasePage{
    private final By SEARCH_INPUT = By.xpath("//input[contains(@class, 'Search-input')]");
    //search button
    private final By SEARCH_SUBMIT_BTN = By.xpath("//div[contains(@class, 'Search-submit')]");
    private final By SEARCH_SUGGESTIONS = By.xpath("//li[@class='Search-result']");
    private final By SEARCH_RESET_BTN = By.xpath("//button[@class='Search-reset']");

    public HeaderComponent(WebDriver driver) {
        super(driver);
    }

    public HeaderComponent clickSearchBar() {
        driver.findElement(SEARCH_INPUT).clear();
        driver.findElement(SEARCH_INPUT).click();
        return this;
    }

    public HeaderComponent inputSearchQuery(String query) {
        WebElement searchInput = driver.findElement(SEARCH_INPUT);

        //ensure the search field is active
        if (!searchInput.equals(driver.switchTo().activeElement())) {
            throw new IllegalStateException("Search input is not active in order to accept any keys. " +
                    "Please call clickSearchBar() before attempting to provide a search query.");
        }

        searchInput.sendKeys(query);
        waitForLoadingSpinnerToDisappear();
        return this;
    }

    public String getSearchBarValue() {
        return driver.findElement(SEARCH_INPUT).getAttribute("value");
    }

    public HeaderComponent clickSearchResetBtn() {
        driver.findElement(SEARCH_RESET_BTN).click();
        return this;
    }

    public List<String> getSearchSuggestions() {
        return driver.findElements(SEARCH_SUGGESTIONS)
                .stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public BrowsePage clickSearchBtn() {
        driver.findElement(SEARCH_SUBMIT_BTN).click();
        String searchQuery = driver.findElement(SEARCH_INPUT).getAttribute("value").replace(" ", "%20");
        wait.until(ExpectedConditions.urlContains(searchQuery));
        return new BrowsePage(driver);
    }
}
