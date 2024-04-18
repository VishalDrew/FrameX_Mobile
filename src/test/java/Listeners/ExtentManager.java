package Listeners;

import Base.TestSetup;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.FileNotFoundException;

import static Base.TestSetup.log;
import static Base.TestSetup.props;


public class ExtentManager {

    private static ExtentReports extent;


    public static ExtentReports createInstance(String fileName) throws FileNotFoundException {
        try {
            log.info("Creating ExtentReports instance...");
            ExtentSparkReporter htmlReporter = new ExtentSparkReporter(fileName);

            htmlReporter.config().setTheme(Theme.valueOf(props.get("Reporttheme")));
            htmlReporter.config().setDocumentTitle(props.get("Reporttitle"));
            htmlReporter.config().setEncoding("utf-8");
            htmlReporter.config().setReportName(props.get("Reportname"));

            extent = new ExtentReports();
            extent.attachReporter(htmlReporter);
            extent.setSystemInfo(props.get("Role"), props.get("Roleuser"));
            extent.setSystemInfo("Organization", props.get("Organization"));
            extent.setSystemInfo("User", System.getProperty("user.name"));
            extent.setSystemInfo("Environment", props.get("Environment"));
            extent.setSystemInfo("Device", TestSetup.devicemodel );

            log.info("ExtentReports instance created successfully.");
        } catch (Exception e) {
            log.error("Error creating ExtentReports instance:", e);
            throw e;
        }

        return extent;
    }
}
