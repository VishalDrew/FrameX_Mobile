package Base;

import Utilities.ExcelReader;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONObject;
import org.openqa.selenium.html5.Location;
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
import static Utilities.Constants.*;
import static Utilities.DBConfig.getprojectdatafromdatabase;
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

    public static JSONObject globaldata = gettestdata("Login","User1");
    public static  String isexitstoreRequired;

    static {
        try {
            // Load properties from the property file
            props = propertyloader();
            queries = (HashMap<String, String>) queryloader();
            isexitstoreRequired = getprojectdatafromdatabase("select ExitButtonRequired, * from Projectmaster where ProjectName = '"+globaldata.getString("project")+"'","ExitButtonRequired");
            log.info("Properties and queries loaded successfully.");
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
            targets = fetchTargetsFromDatabase(globaldata.getString("username"));
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

            String[]  capabs = {"platformName","appPackage","appActivity","autoGrantPermissions","automationName","skipDeviceInitialization","skipServerInstallation","ignoreUnimportantViews","skipUnlock"};
            // Set desired capabilities for Android driver
            capabilities = new DesiredCapabilities();
            for(String capkey : capabs){
                if(capkey.equalsIgnoreCase("autoGrantPermissions")||
                        capkey.equalsIgnoreCase("skipDeviceInitialization")||
                        capkey.equalsIgnoreCase("skipServerInstallation")||capkey.equalsIgnoreCase("ignoreUnimportantViews")||capkey.equalsIgnoreCase("skipUnlock")){
                    capabilities.setCapability(capkey,Boolean.parseBoolean(props.get(capkey)));
                }else{
                    capabilities.setCapability(capkey,props.get(capkey));
                }
            }
            capabilities.setCapability("app", props.get("Apppath"));
            capabilities.setCapability("deviceName", Devicename);
            capabilities.setCapability("adbExecTimeout", "120000");
            capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, "100");
            // Specify the URL with the correct IP address and port for the Appium server
            driver = new AndroidDriver(new URL(props.get("Serverurl")), capabilities);
            devicemodel = driver.getCapabilities().getCapability("deviceModel").toString();
            driver.manage().timeouts().implicitlyWait(Integer.parseInt(props.get("Implicitywaittimeout")),TimeUnit.SECONDS);
            log.info("Appium server started successfully, and AndroidDriver initialized.");
        } catch (IOException e) {
            log.error("An error occurred while starting the app:", e);
            throw new RuntimeException("Error starting the app", e);
        } catch (Exception e) {
            log.error("An error occurred:", e);
            throw new RuntimeException(e);
        }
    }

    // Method to tear down the test environment after test execution
    @AfterSuite(alwaysRun=true)
    public static void tearDownApp() throws InterruptedException, MessagingException, FileNotFoundException {
        try {

            // Close the AndroidDriver instance if it exists
            if (driver != null) {
                log.info("AndroidDriver is Quited");
                driver.quit();
            }
            // Stop the Appium service if running
            if (service != null && service.isRunning()) {
                log.info("Appium service is Stopped");
                service.stop();
            }

            // Send mail report and open the generated report file in the default web browser
            if(props.get("EmailMode").equalsIgnoreCase("true")){
                sendMailReport();
            }

            // Open the generated report file in the default web browser
            File extentReport = new File(props.get("TestReportspath")+fileName);
            try {
                Desktop.getDesktop().browse(extentReport.toURI());
                log.info("Report opened in default web browser.");
            } catch (IOException e) {
                log.error("Error opening report in default web browser.", e);
                e.printStackTrace();
            }

            // Log completion message
            log.info("Test Execution Completed");

        } catch (Exception e) {
            log.error("An error occurred during test teardown:", e);
            throw new RuntimeException("Error during test teardown", e);
        }
    }

}
