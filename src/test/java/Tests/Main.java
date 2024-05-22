package Tests;

import Utilities.Constants;
import Utilities.JsonConfigReader;
import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

import static Utilities.Constants.TESTSUITES_DATA_FILE;

public class Main {

    public static void main(String[] args) {
        String smokeSuite = "./src/test/resources/Runners/Smoke.xml";
        String regressionSuite = "./src/test/resources/Runners/Regression.xml";
        String negativeSuite = "./src/test/resources/Runners/Negative.xml";

        JsonConfigReader configReader = new JsonConfigReader(TESTSUITES_DATA_FILE);
        List<String> suites = new ArrayList<>();

        if (configReader.isSuiteRunnable("SMOKE")) {
            suites.add(smokeSuite);
        }
        if (configReader.isSuiteRunnable("REGRESSION")) {
            suites.add(regressionSuite);
        }
        if (configReader.isSuiteRunnable("NEGATIVE")) {
            suites.add(negativeSuite);
        }
        TestNG testng = new TestNG();

        testng.setTestSuites(suites);
        testng.run();
    }
}
