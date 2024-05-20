package Tests;

import org.testng.TestNG;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TestNG testng = new TestNG();
        List<String> suites = new ArrayList<>();
        suites.add("./src/test/resources/Runners/Smoke.xml");
        testng.setTestSuites(suites);
        testng.run();
    }
}
