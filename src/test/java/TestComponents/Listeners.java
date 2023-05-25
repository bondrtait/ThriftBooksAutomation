package TestComponents;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import common.DriverSingleton;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Listeners implements ITestListener {

    ExtentReports extent = ExtentReporterNG.getReportObject();
    ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    @Override
    public void onTestStart(ITestResult result) {
        test.set(extent.createTest(result.getMethod().getMethodName()));
        test.get().info(Arrays.toString(result.getParameters()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.get().log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();

        test.get().fail(result.getThrowable());
        test.get().addScreenCaptureFromPath(getScreenshot(methodName, DriverSingleton.getDriver()), methodName);
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private String getScreenshot(String testCaseName, WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "\\reports\\" + testCaseName + ".png";
        File file = new File(path);

        try {
            FileUtils.copyFile(source, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return testCaseName + ".png";
    }
}
