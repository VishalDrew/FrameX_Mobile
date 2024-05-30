package Listeners;

import Base.TestSetup;
import Utilities.AppUtils;
import Utilities.Constants;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.*;
import java.util.Arrays;
import java.util.Date;

import static Base.TestSetup.log;

import static Base.TestSetup.properties;
import static Utilities.AppUtils.screenshotName;
import static Utilities.Constants.allureReportDir;
import static Utilities.Constants.command;

public class FrameX_Listeners implements ITestListener, ISuiteListener {
    static Date d = new Date();
    public static final String ExtentReportfileName = properties.get("Reportfilename") + d.toString().replace(":", "_").replace(" ", "_") + ".html";
    public static final String AllureReportfileName = properties.get("AllureReportFilename") +"_"+ d.toString().replace(":", "_").replace(" ", "_") + ".html";
    public static boolean attachmentflag;
    public static ExtentReports extent;
    public static ExtentTest test;

    static {
        try {
            extent = ExtentManager.createInstance(properties.get("TestReportspath") + ExtentReportfileName);
        } catch (FileNotFoundException e) {
            log.error("Error creating ExtentReports instance:", e);
            throw new RuntimeException(e);
        }
    }

    public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<ExtentTest>();

    public void onTestStart(ITestResult result) {
        ExtentTest test = extent.createTest(result.getTestClass().getName() + "     @TestCase : " + result.getMethod().getMethodName());
        testReport.set(test);
        log.info("Test started: " + result.getTestClass().getName() + "     @TestCase : " + result.getMethod().getMethodName());

    }

    public void onTestSuccess(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String logText = "<b>" + "TEST CASE:- " + methodName.toUpperCase() + " PASSED" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
        testReport.get().pass(m);
        log.info("Test passed: " + methodName);
    }

    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        log.error("Test failed: " + methodName);
        String excepionMessage = Arrays.toString(result.getThrowable().getStackTrace());
        testReport.get().fail("<details>" + "<summary>" + "<b>" + "<font color=" + "red>" + "Exception Occurred: Click to see" + "</font>" + "</b >" + "</summary>" + excepionMessage.replaceAll(",", "<br>") + "</details>" + " \n");
        attachmentflag = true;
        try {
            AppUtils.captureScreenshot();
            testReport.get().fail("<b>" + "<font color=" + "red>" + "Screenshot of failure" + "</font>" + "</b>",
                    MediaEntityBuilder.createScreenCaptureFromPath(properties.get("Screenshotpath") + screenshotName)
                            .build());
        } catch (IOException e) {
            log.error("Error capturing screenshot:", e);
            e.printStackTrace();
        }
        String failureLog = "TEST CASE FAILED";
        Markup m = MarkupHelper.createLabel(failureLog, ExtentColor.RED);
        testReport.get().log(Status.FAIL, m);

    }

    public void onTestSkipped(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        String logText = "<b>" + "Test Case:- " + methodName + " Skipped" + "</b>";
        Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
        testReport.get().skip(m);
        log.info("Test skipped: " + methodName);
    }

    public void onFinish(ITestContext context) {
        if (extent != null) {
            extent.flush();
            log.info("ExtentReports flushed.");
        }
    }

    public static String formatData(String message, String... values) {
        String formattedData = "<span style=\"color: Black; font-weight: bold;\">" + message + " </span><span style=\"color: Black;\">";
        formattedData += String.join(" | ", values);
        formattedData += "</span>";
        return formattedData;
    }

    public static void logAndReportSuccess(String message) {
        io.qameta.allure.Allure.step(message);;
        log.info(message);
    }

    public static void logAndinfo(String message) {
        io.qameta.allure.Allure.step(message);
        log.info(message);
    }

    public static void logAndReportFailure(String message) {
        io.qameta.allure.Allure.step(message);
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