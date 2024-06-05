package Base;

import Utilities.Constants;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import io.qameta.allure.model.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import static Base.TestSetup.*;
import static Utilities.Constants.allureReportDir;
import static Utilities.Constants.command;

public class FrameX_Listeners implements ITestListener {

    static Date d = new Date();
    public static final String AllureReportfileName = properties.get("AllureReportFilename") + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".html";
    public static ThreadLocal<String> currentTest = new ThreadLocal<>();
    public static String screenshotName;

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getTestClass().getName() + " @TestCase: " + result.getMethod().getMethodName();
        currentTest.set(testName);
        Allure.step("Test started: " + testName);
        log.info("Test started: " + testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String logText = "TEST CASE: " + methodName.toUpperCase() + " PASSED";
        Allure.step(logText,Status.PASSED);
        log.info("Test passed: " + methodName);
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String exceptionMessage = Arrays.toString(result.getThrowable().getStackTrace());
        String logText = "TEST CASE: " + methodName.toUpperCase() + " FAILED";
        Allure.step(logText, Status.FAILED);
        takeScreenshot(result.getName());
        log.error("Test failed: " + methodName);
        log.error("Exception: ", result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String logText = "TEST CASE: " + methodName.toUpperCase() + " SKIPPED";
        Allure.step(logText,Status.SKIPPED);
        log.info("Test skipped: " + methodName);
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("Test suite finished.");
    }


    private void takeScreenshot(String testName) {
        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
             screenshotName = testName + "_" + new SimpleDateFormat("dd_MM_yyyy_HHmmss").format(new Date()) + ".jpg";
            File destFile = new File("screenshots/" + screenshotName);
            FileUtils.copyFile(srcFile, destFile);
            try (FileInputStream fis = new FileInputStream(destFile)) {
                Allure.addAttachment(testName, new ByteArrayInputStream(fis.readAllBytes()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void logAndReportSuccess(String message) {
        Allure.step(message, io.qameta.allure.model.Status.PASSED);
        log.info(message);
    }

    public static void logAndinfo(String message) {
        io.qameta.allure.Allure.step(message);
        log.info(message);
    }

    public static void logAndReportFailure(String message) {
        Allure.step(message, io.qameta.allure.model.Status.FAILED);
        log.error(message);
    }


    public static void generateAllureReport(){
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((line = errorReader.readLine()) != null) {
                System.err.println(line);
            }

            // Wait for the process to complete
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                TestSetup.log.info("Allure report generated successfully.");
                File oldFile = new File(allureReportDir + File.separator + "index.html");
                File newFile = new File(allureReportDir + File.separator + AllureReportfileName);
                if (oldFile.exists()) {
                    if (oldFile.renameTo(newFile)) {
                        TestSetup.log.info("Report renamed to " + AllureReportfileName);
                    } else {
                        TestSetup.log.error("Failed to rename report file.");
                    }
                } else {
                    TestSetup.log.error("index.html file not found.");
                }
            } else {
                TestSetup.log.error("Failed to generate Allure report. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    public static void clearAllureResultsDirectory() {
        File allureResultsDir = new File(Constants.allureResultsDir);
        if (allureResultsDir.exists()) {
            deleteDirectory(allureResultsDir);
        }
    }

    private static void deleteDirectory(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directory.delete();
    }


}