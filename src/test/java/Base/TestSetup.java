package Base;

import Utilities.ExcelReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import javax.mail.MessagingException;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static Listeners.FrameX_Listeners.fileName;
import static Utilities.Constants.Devicename;
import static Utilities.DatabaseUtility.getProjectDataFromDatabase;
import static Utilities.Mailconfig.sendMailReport;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.*;


/**
 * TestSetup class.
 */
public class TestSetup {
    public static AndroidDriver driver;
    private static AppiumDriverLocalService service;
    public static Logger log = Logger.getLogger(TestSetup.class);
    private static DesiredCapabilities capabilities ;
    public static String devicemodel;
    public static ExcelReader excel;
    public static HashMap<String,String>props;
    public static HashMap<String,String>queries;

    public static JSONObject globalData = gettestdata("Login","User1");

    static {
        try {
            props = loadProperties();
            queries = (HashMap<String, String>) loadQueries();
        } catch (Exception e) {
            log.error("Error loading file", e);
            throw new RuntimeException("Error loading file", e);
        }
        // Initialize ExcelReader with the specified data file path
        excel = new ExcelReader(props.get("Datafilepath"));
    }


    public static List<String> targets;
    static {
        try {
            targets = fetchTargetsFromDatabase(globalData.getString("username"));
        } catch (Exception e) {
            log.error("Error fetching targets from the database", e);
            throw new RuntimeException(e);
        }
    }

    // Method to start the app and set up the test environment
    @BeforeSuite(alwaysRun = true)
    public static void StartApp() throws IOException {

        try {
            PropertyConfigurator.configure(props.get("Logpropertiesfilepath"));
            log.info("Starting the Appium service.");
            // Start the Appium service
            service = new AppiumServiceBuilder()
                    .withAppiumJS(new File(props.get("Server")))
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .build();
            service.start();
            setDesiredCapabilities();
            // Specify the URL with the correct IP address and port for the Appium server
            driver = new AndroidDriver(new URL(props.get("Serverurl")), capabilities);
            devicemodel = driver.getCapabilities().getCapability("deviceModel").toString();
            driver.manage().timeouts().implicitlyWait(Integer.parseInt(props.get("Implicitywaittimeout")),TimeUnit.SECONDS);
            log.info("Appium server started successfully, and AndroidDriver initialized.");
        } catch (IOException e) {
            log.error("An error occurred while starting the app:", e);
            throw new RuntimeException("Error starting the app", e);
        }
    }

    // Method to tear down the test environment after test execution
    @AfterSuite(alwaysRun = true)
    public static void tearDownApp() {
        try {
            closeDriver();
            stopAppiumService();
            handleEmailReport();
            openReportInBrowser();
            log.info("Test Execution Completed");
        } catch (Exception e) {
            log.error("An error occurred during test teardown:", e);
            throw new RuntimeException("Error during test teardown", e);
        }
    }



    /**
     * Sets the desired capabilities for the test setup.
     * 
     * @param props the properties containing the desired capabilities values
     * @param Devicename the name of the device
     * @throws IllegalArgumentException if any of the desired capabilities values are invalid
     */
    static void setDesiredCapabilities() {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", props.get("platformName"));
        capabilities.setCapability("appPackage", props.get("appPackage"));
        capabilities.setCapability("appActivity",props.get("appActivity"));
        capabilities.setCapability("automationName", props.get("automationName"));
        capabilities.setCapability("autoGrantPermissions",Boolean.parseBoolean(props.get("Autograntpermissions")));
        capabilities.setCapability("skipDeviceInitialization",Boolean.parseBoolean(props.get("skipDeviceInitialization")) );
        capabilities.setCapability("skipServerInstallation", Boolean.parseBoolean(props.get("skipServerInstallation")));
        capabilities.setCapability("ignoreUnimportantViews", Boolean.parseBoolean(props.get("ignoreUnimportantViews")));
        capabilities.setCapability("skipUnlock",Boolean.parseBoolean(props.get("skipUnlock")));
        capabilities.setCapability("app", props.get("Apppath"));
        capabilities.setCapability("deviceName", Devicename);
        capabilities.setCapability("adbExecTimeout", "120000");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "100");
    }

    /**
     * Closes the AndroidDriver if it is not null.
     *
     * @throws NullPointerException if the AndroidDriver is null.
     */
    static void closeDriver() {
        if (driver != null) {
            log.info("AndroidDriver is Quited");
            driver.quit();
        }
    }

    /**
     * Stops the Appium service.
     *
     * @throws NullPointerException if the service is null.
     * @throws IllegalStateException if the service is not running.
     */
    static void stopAppiumService() {
        if (service != null && service.isRunning()) {
            log.info("Appium service is Stopped");
            service.stop();
        }
    }

    /**
     * Handles the email report.
     * 
     * @throws MessagingException if there is an error with the email messaging
     * @throws FileNotFoundException if the file for the email report is not found
     */
    static void handleEmailReport() throws MessagingException, FileNotFoundException {
        if (props.get("EmailMode").equalsIgnoreCase("true")) {
            sendMailReport();
        }
    }

    /**
     * Opens the report in the default web browser.
     * 
     * @throws IOException if an error occurs while opening the report in the web browser.
     */
    static void openReportInBrowser() {
        File extentReport = new File(props.get("TestReportspath") + fileName);
        try {
            Desktop.getDesktop().browse(extentReport.toURI());
            log.info("Report opened in default web browser.");
        } catch (IOException e) {
            log.error("Error opening report in default web browser.", e);
            e.printStackTrace();
        }
    }

}

