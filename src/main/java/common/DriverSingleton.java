package common;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public class DriverSingleton {
    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private DriverSingleton() {
    }

    public static WebDriver getDriver() {
        if (driver.get() == null) {
            driver.set(new ChromeDriver());

            driver.get().manage().window().maximize();
            driver.get().manage().timeouts().implicitlyWait(Duration.ofSeconds(Config.IMPLICIT_WAIT));
        }

//        System.out.println("getDriver() was called by Thread " + Thread.currentThread().getId() + " and Driver instance returned is " + driver.get());
        return driver.get();
    }

    public static void quit() {
//        System.out.println("quit() was called by Thread " + Thread.currentThread().getId() + " and Driver instance is " + driver.get());
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
        }
    }
}
