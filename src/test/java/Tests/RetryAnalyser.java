package Tests;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import static Base.TestSetup.props;

public class RetryAnalyser implements IRetryAnalyzer {
    private static final int MAX_RETRY_COUNT= Integer.parseInt(props.get("Testretrycount"));

    private int retryCount = 0;
    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            return true;
        }
        return false;
    }
}



