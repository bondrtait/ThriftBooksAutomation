package pages;

import common.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected final WebDriver driver;
    protected final WebDriverWait wait;

    private final By LOADING_SPINNER = By.xpath("//div[@class = 'Loading-spinner']");
    private final By COOKIE_CONSENT_CLOSE = By.xpath("//button[@class=' osano-cm-dialog__close osano-cm-close ']");

    protected static double cartSubtotal = 0.0;

    public BasePage (WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(Config.EXPLICIT_WAIT));
    }

    public double getCartSubtotal() {
        return cartSubtotal;
    }

    public void waitForLoadingSpinnerToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }

    protected long getScrollLeft(WebElement el) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (long) js.executeScript("return arguments[0].scrollLeft;", el);
    }

    protected long getOffsetLeft(WebElement el) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (long) js.executeScript("return arguments[0].offsetLeft;", el);
    }

    public void closeCookieConsent() {
        driver.findElement(COOKIE_CONSENT_CLOSE).click();
    }



/*    protected boolean isVisibleInViewport(WebElement element) {
        WebDriver wrappedDriver = ((RemoteWebElement)element).getWrappedDriver();

        return (Boolean)((JavascriptExecutor)wrappedDriver).executeScript(
                "var elem = arguments[0],                 " +
                        "  box = elem.getBoundingClientRect(),    " +
                        "  cx = box.left + box.width / 2,         " +
                        "  cy = box.top + box.height / 2,         " +
                        "  e = document.elementFromPoint(cx, cy); " +
                        "for (; e; e = e.parentElement) {         " +
                        "  if (e === elem)                        " +
                        "    return true;                         " +
                        "}                                        " +
                        "return false;                            "
                , element);
    }*/
}
