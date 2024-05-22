package Listeners;

import Base.TestSetup;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.FileNotFoundException;

import static Base.TestSetup.log;
import static Base.TestSetup.properties;


public class ExtentManager {

    private static ExtentReports extent;


    public static ExtentReports createInstance(String fileName) throws FileNotFoundException {
        try {
            log.info("Creating ExtentReports instance...");
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

            htmlReporter.config().setTheme(Theme.valueOf(properties.get("Reporttheme")));
            htmlReporter.config().setDocumentTitle(properties.get("Reporttitle"));
            htmlReporter.config().setEncoding("utf-8");
            htmlReporter.config().setReportName(properties.get("Reportname"));

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo(properties.get("Role"), properties.get("Roleuser"));
            extent.setSystemInfo("Organization", properties.get("Organization"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", properties.get("Environment"));
            extent.setSystemInfo("Device", TestSetup.deviceModel);

            log.info("ExtentReports instance created successfully.");
        } catch (Exception e) {
            log.error("Error creating ExtentReports instance:", e);
            throw e;
        }

        return extent;
    }
}
