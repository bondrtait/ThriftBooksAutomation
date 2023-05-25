package TestComponents;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class Retry implements IRetryAnalyzer {
    private int retriesDoneCount = 0;
    private final int MAX_TRIES = 2;
    @Override
    public boolean retry(ITestResult iTestResult) {
        System.out.println("Entered retry()");
        if (retriesDoneCount < MAX_TRIES) {
            retriesDoneCount++;
            return true;
        }

        return false;
    }
}
