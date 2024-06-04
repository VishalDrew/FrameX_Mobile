package Utilities;


import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import Base.TestSetup;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.qameta.allure.Allure;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import static Base.FrameX_Listeners.*;
import static Pages.CallPlan_page.*;
import static Pages.Login_Page.lgpage;
import static Pages.Login_Page.menubutton;
import static Utilities.Actions.*;
import static Utilities.Constants.*;

/**
 * Utils class.
 */
public class AppUtils extends TestSetup {

    public static int totalimagescaptured;
    public  static boolean capturedDone;

    /**
     * Retrieves the name of the connected device.
     *
     * @return The name of the connected device, or null if no device is connected.
     * @throws IOException If an I/O error occurs while executing the adb command.
     */
    public static String getDeviceName() {
        try {
            Process process = Runtime.getRuntime().exec("adb devices");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.endsWith("device")) {
                    String[] parts = line.split("\\t");
                    if (parts.length > 1) {
                        String deviceName = parts[0].trim();
                        log.info("Device name retrieved successfully: " + deviceName);
                        return deviceName;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            log.error("Error retrieving device name: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Generates a formatted date string based on the given format.
     *
     * @param format the format string specifying the desired date format
     * @return the formatted date string
     * @throws DateTimeException if an error occurs while formatting the date
     */
    public static String generateFormattedDate(String format) {
        // Get the current date
        LocalDate currentDate = LocalDate.now();

        // Extract the last two digits of the year
        int lastTwoDigitsOfYear = currentDate.getYear() % 100;

        // Format the date in the required format "dd-MM-yy"
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern(format));

        return formattedDate;
    }

    /**
     * Generates yesterday's date in the specified format.
     *
     * @param format the format of the date to be generated
     * @return the formatted yesterday's date
     * @throws DateTimeException if an error occurs while formatting the date
     */
    public static String generateyesterdaydate(String format) {

        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        String formattedYesterday = yesterday.format(formatter);

        return formattedYesterday;
    }

    /**
     * Generates a formatted date and time string.
     *
     * @return The formatted date and time string in the format "yyyy-MM-dd_hh:mm:ss_a".
     */
    public static String generatedateandtime() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_hh:mm:ss_a");
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    public static void captureScreenshot(String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            byte[] screenshot = ts.getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment(screenshotName, new ByteArrayInputStream(screenshot));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Returns the current time of the device.
     *
     * @return the current time of the device in HH:mm format.
     * @throws NullPointerException if the driver is null.
     */
    public static String getdevicetime() {
        String time  =  driver.getDeviceTime();
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(time);
        String devicetime = offsetDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        return devicetime;
    }

    /**
     * Returns the formatted device time when the driver visited.
     *
     * @return The formatted device time in the format "yyyy-MM-dd HH:mm".
     * @throws DateTimeParseException if the device time cannot be parsed into an OffsetDateTime.
     */
    public static String datevisitedtime() {
        String time  =  driver.getDeviceTime();
        OffsetDateTime offsetDateTime = OffsetDateTime.parse(time);
        String devicetime = offsetDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return devicetime;
    }

    /**
     * Loads properties from a configuration file and converts them into a HashMap.
     *
     * @return a HashMap containing the properties loaded from the configuration file
     * @throws FileNotFoundException if the configuration file is not found
     */
    public static HashMap<String,String> loadProperties() throws FileNotFoundException {

        Properties properties = new Properties();
        FileInputStream fileInputStream = null;

        Map<String, String> propertiesMap = null;
        try {
            fileInputStream = new FileInputStream(Constants.configfilepath);
            properties.load(fileInputStream);

            String value = "";

            // Convert properties to HashMap
            propertiesMap = new HashMap<>();
            for (String key : properties.stringPropertyNames()) {
                value = properties.getProperty(key);
                if (key.contains("path")) {
                    value = System.getProperty("user.dir") + properties.getProperty(key);
                }
                propertiesMap.put(key, value);
            }

            log.info("Properties loaded successfully.");
        } catch (IOException e) {
            log.error("Error loading properties: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Error closing FileInputStream: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return (HashMap<String, String>) propertiesMap;
    }

    /**
     * Loads queries from a SQL file and returns them as a map.
     *
     * @return a map containing the loaded queries
     * @throws IOException if an I/O error occurs while reading the properties file
     */
    public static Map<String, String> loadQueries() throws IOException {

        Properties properties = new Properties();
        FileInputStream fileInputStream = null;

        Map<String, String> Queries = new HashMap<>();

        try {
            log.info("Loading queries from file: " + queryfilepath);
            fileInputStream = new FileInputStream(queryfilepath);
            properties.load(fileInputStream);

            String[] querykeys = {"Categorymasterquery", "FormFieldsquery", "QuestionFormFieldsquery",
                    "ProductColumnquery", "FormMasterquery", "Productquery", "EnumFieldquery", "EnumQuestionFieldquery"};

            for (String key : querykeys) {
                Queries.put(key, properties.getProperty(key));
            }

            log.info("Queries loaded successfully.");
        } catch (IOException e) {
            log.error("Error loading queries: " + e.getMessage());
            throw e; // Rethrow the exception to indicate failure
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    log.error("Error closing file input stream: " + e.getMessage());
                    throw e; // Rethrow the exception to indicate failure
                }
            }
        }

        return Queries;
    }

    /**
     * Replaces special characters in a given string with their corresponding HTML entities.
     *
     * @param value the string to be modified
     * @return the modified string with HTML entities
     * @throws NullPointerException if the input value is null
     */
    public static String SetSpecialCharacter(String value) {

        // Map of special characters and their HTML entities
        Map<String, String> specialCharacters = new HashMap<>();
        specialCharacters.put("&", "&amp;");
        specialCharacters.put("<", "&lt;");
        specialCharacters.put(">", "&gt;");
        specialCharacters.put("\"", "&quot;");
        specialCharacters.put("'", "&apos;");
        specialCharacters.put("©", "&copy;");
        specialCharacters.put("®", "&reg;");
        specialCharacters.put("™", "&trade;");

        // Check if the input value contains any special characters
        boolean containsSpecialCharacter = false;
        for (String specialChar : specialCharacters.keySet()) {
            if (value.contains(specialChar)) {
                containsSpecialCharacter = true;
                break;
            }
        }
        // If the input value contains special characters, search and replace them with their HTML entities
        if (containsSpecialCharacter) {
            for (Map.Entry<String, String> entry : specialCharacters.entrySet()) {
                value = value.replaceAll(Pattern.quote(entry.getKey()), entry.getValue());
            }
        }
        // Return the modified value with HTML entities
        return value;
    }


    /**
     * Checks if the given value exists in the page source.
     *
     * @param value the value to search for in the page source
     * @return true if the value exists in the page source, false otherwise
     * @throws NullPointerException if the page source is null
     */
    public static boolean sourceExists(String value) {

        return driver.getPageSource().contains(value);
    }

    /**
     * Assertion method to check if the source exists.
     *
     * @param expected the expected source
     * @param message the message to be displayed if the assertion fails
     * @throws AssertionError if the assertion fails
     * @returns void
     */
    public static void Assertion(String expected, String message) {
        try {
            if (sourceExists(expected)) {
                logAndReportSuccess("TestCase Passed : " + expected + " is Displayed");
            } else {
                logAndReportFailure("TestCase Failed : " + message);
                if (sourceExists("Ok")) {
                    click("ACCESSIBILITYID", "Ok");
                }
                Assert.fail("TestCase Failed : " + message);
            }
        } catch (AssertionError e) {
            logAndReportFailure("Assertion Error: " + e.getMessage());
            Assert.fail("TestCase Failed due to Assertion Error: " + e.getMessage());
        }
    }


    public static void verifyModuleDisplayedStatus(String module) throws InterruptedException {
        Thread.sleep(900);
        applogin(module);
        if(!sourceExists(module)){
            logAndReportFailure("TestCase Failed : "+module+" module is not displayed");
            Assert.fail("TestCase Failed : "+module+" module is not displayed");
        }else{
            logAndReportSuccess("TestCase Passed : "+module+" module is Displayed");
            Assert.assertTrue(true);
        }

    }



    /**
     * Logs into the application.
     *
     * @param module the module to log into
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public static void applogin(String module) throws InterruptedException {
        if(!sourceExists(module)){
            if(sourceExists("Username")){
                lgpage();
            }else{
                boolean back = sourceExists(backbutton);
                if(back){
                    while(back){
                        click("ACCESSIBILITYID",backbutton);
                        back = sourceExists(backbutton);
                    }
                }else{
                    if(isElementDisplayed("xpath",menubutton)){
                        click("Xpath", menubutton);
                    }
                }
            }

        }
    }

    /**
     * Navigates to the specified module.
     *
     * @param module the name of the module to navigate to
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public static void navigateto(String module) throws InterruptedException {
        if(sourceExists("Username")){
            lgpage();
        }
        if (!sourceExists(module)) {
            if(isElementDisplayed("xpath",menubutton)){
                click("Xpath", menubutton);
                click("ACCESSIBILITYID", module);
            }
        }else {
            click("ACCESSIBILITYID", module);
        }
    }


    public static void scroll(AndroidDriver driver, int distance) throws InterruptedException {
        String scrollScript = String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollForward(%d)", distance);
        driver.findElement(AppiumBy.androidUIAutomator(scrollScript));

    }

    public static String removeUnderscores(String input) {
        return input.replace("_", " ");
    }


    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_+[]{}|;:',.<>?/~`";

    public static String generateRandomSpecialCharacters(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(SPECIAL_CHARACTERS.length());
            sb.append(SPECIAL_CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

    public static String generateRandomNumber() {
        Random random = new Random();
        int min = 10000000;
        int max = 99999999;
        int randnum =  random.nextInt(max - min + 1) + min;
        return String.valueOf(randnum);
    }

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static String generateRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}





