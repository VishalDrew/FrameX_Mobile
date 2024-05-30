package Base;

import Utilities.Rough;
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

import static Listeners.FrameX_Listeners.*;
import static Modules.CallPlanModule.fetchTargetsFromDatabase;
import static Utilities.Constants.Devicename;
import static Utilities.Constants.allureDirpath;
import static Utilities.Mailconfig.sendMailReport;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;

/**
 * TestSetup class for initializing and tearing down the test environment.
 *
 */

public class TestSetup {
    public static AndroidDriver driver;
    private static AppiumDriverLocalService service;
    public static Logger log = Logger.getLogger(TestSetup.class);
    private static DesiredCapabilities capabilities;
    public static String deviceModel;
    public static HashMap<String, String> properties;
    public static HashMap<String, String> sqlQueries;

    public static JSONObject globalData = gettestdata("Login", "User1");

    static {
        initializeProperties();
        initializeQueries();
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

    /**
     * Starts the Appium service and initializes the AndroidDriver.
     *
     * @throws IOException      if an I/O error occurs while starting the app.
     * @throws RuntimeException if an error occurs while starting the app.
     */
    @BeforeSuite(alwaysRun = true)
    public static void StartApp() throws IOException {

        try {
            PropertyConfigurator.configure(properties.get("Logpropertiesfilepath"));
            clearAllureResultsDirectory();
            log.info("Starting the Appium service.");
            service = new AppiumServiceBuilder()
                    .withAppiumJS(new File(properties.get("Server")))
                    .withIPAddress("127.0.0.1")
                    .usingPort(4723)
                    .build();
            service.start();
            setDesiredCapabilities();
            driver = new AndroidDriver(new URL(properties.get("Serverurl")), capabilities);
            deviceModel = driver.getCapabilities().getCapability("deviceModel").toString();
            driver.manage().timeouts().implicitlyWait(Integer.parseInt(properties.get("Implicitywaittimeout")), TimeUnit.SECONDS);
            log.info("Appium server started successfully, and AndroidDriver initialized.");
        } catch (IOException e) {
            log.error("An error occurred while starting the app:", e);
            throw new RuntimeException("Error starting the app", e);
        }
    }

    /**
     * Tears down the test environment after test execution.
     */
    @AfterSuite(alwaysRun = true)
    public static void tearDownApp() {
        try {
            closeDriver();
            stopAppiumService();
            generateAllureReport();
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
     * @param props      the properties containing the desired capabilities values
     * @param Devicename the name of the device
     * @throws IllegalArgumentException if any of the desired capabilities values are invalid
     */
    private static void setDesiredCapabilities() {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("platformName", properties.get("platformName"));
        capabilities.setCapability("appPackage", properties.get("appPackage"));
        capabilities.setCapability("appActivity", properties.get("appActivity"));
        capabilities.setCapability("automationName", properties.get("automationName"));
        capabilities.setCapability("autoGrantPermissions", Boolean.parseBoolean(properties.get("Autograntpermissions")));
        capabilities.setCapability("skipDeviceInitialization", Boolean.parseBoolean(properties.get("skipDeviceInitialization")));
        capabilities.setCapability("skipServerInstallation", Boolean.parseBoolean(properties.get("skipServerInstallation")));
        capabilities.setCapability("ignoreUnimportantViews", Boolean.parseBoolean(properties.get("ignoreUnimportantViews")));
        capabilities.setCapability("skipUnlock", Boolean.parseBoolean(properties.get("skipUnlock")));
        capabilities.setCapability("app", properties.get("Apppath"));
        capabilities.setCapability("deviceName", Devicename);
        capabilities.setCapability("adbExecTimeout", "120000");
        capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "100");
    }


    /**
     * Closes the AndroidDriver if it is not null.
     *
     * @throws NullPointerException if the AndroidDriver is null.
     */
    private static void closeDriver() {
        if (driver != null) {
            log.info("Quitting AndroidDriver.");
            driver.quit();
            driver = null;
        }
    }


    /**
     * Stops the Appium service.
     *
     * @throws NullPointerException  if the service is null.
     * @throws IllegalStateException if the service is not running.
     */
    private static void stopAppiumService() {
        if (service != null && service.isRunning()) {
            log.info("Stopping Appium service.");
            service.stop();
            service = null;
        }
    }


    /**
     * Handles the email report.
     *
     * @throws MessagingException    if there is an error with the email messaging
     * @throws FileNotFoundException if the file for the email report is not found
     */
    private static void handleEmailReport() throws MessagingException, FileNotFoundException {
        if (Boolean.parseBoolean(properties.get("EmailMode"))) {
            sendMailReport();
        }
    }

    /**
     * Opens the report in the default web browser.
     *
     * @throws IOException if an error occurs while opening the report in the web browser.
     */
    private static void openReportInBrowser() {
        File allureReport = new File(allureDirpath+ AllureReportfileName);
        try {
            Desktop.getDesktop().browse(allureReport.toURI());
            log.info("Report opened in default web browser.");
        } catch (IOException e) {
            log.error("Error opening report in default web browser.", e);
            e.printStackTrace();
        }
    }

    private static void initializeProperties() {
        try {
            properties = loadProperties();
        } catch (Exception e) {
            log.error("Error loading properties file", e);
            throw new RuntimeException("Error loading properties file", e);
        }
    }

    private static void initializeQueries() {
        try {
            sqlQueries = (HashMap<String, String>) loadQueries();
        } catch (Exception e) {
            log.error("Error loading queries file", e);
            throw new RuntimeException("Error loading queries file", e);
        }
    }

}

