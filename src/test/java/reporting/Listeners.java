package reporting;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
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

    WebDriver driver = DriverSingleton.getDriver();
    ExtentReports extent = ExtentReporterNG.getReportObject();
    ExtentTest test;
    @Override
    public void onTestStart(ITestResult result) {
        test = extent.createTest(result.getMethod().getMethodName());
        test.info(Arrays.toString(result.getParameters()));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        test.log(Status.PASS, "Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        test.fail(result.getThrowable(), MediaEntityBuilder.createScreenCaptureFromPath(getScreenshot(result.getMethod().getMethodName(), driver)).build());

//        test.addScreenCaptureFromPath(getScreenshot(result.getMethod().getMethodName(), driver),
//                result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }

    private String getScreenshot(String testCaseName, WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;
        File source = ts.getScreenshotAs(OutputType.FILE);
        String path = System.getProperty("user.dir") + "//reports//" + testCaseName + ".png";
        File file = new File(path);

        try {
            FileUtils.copyFile(source, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return path;
    }
}
