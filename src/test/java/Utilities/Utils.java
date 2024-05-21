package Utilities;


import java.io.*;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import Base.TestSetup;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.DataProvider;

import static Listeners.FrameX_Listeners.*;
import static Modules.CallPlan.CallPlanActionHandler.handleNoInternetConnection;
import static Modules.CallPlan.DataBinder.fieldName;
import static Modules.Login_Module.login;
import static Pages.CallPlan_page.*;
import static Pages.HomePage_page.ActivityLog;
import static Pages.Login_Page.menubutton;
import static Utilities.Actions.*;
import static Utilities.Constants.*;
import static Utilities.DatabaseUtility.*;
import static Utilities.TestDataUtil.gettestdata;

/**
 * Utils class.
 */
public class Utils extends TestSetup {

    public static String screenshotName;
    public static int totalimagescaptured;
    public  static boolean capturedDone;

    /**
     * Generates dataset based on the given type and facing type.
     *
     * @param type        the type of dataset (Int or Varchar)
     * @param facingtype  the facing type (Industry Facing * or Our Brand Facing *)
     * @return            the generated dataset
     * @throws            NullPointerException if type is null
     */

    public static String generateTestData(String dataType, String facingType) {
        final String INDUSTRY_FACINGS = "Industry Facings *";
        final String OUR_BRAND_FACINGS = "Our Brand Facings *";

        if ("Int".equals(dataType)) {
            if (facingType.contains(INDUSTRY_FACINGS)) {
                return "5";
            } else if (facingType.contains(OUR_BRAND_FACINGS)) {
                return "2";
            }
            return "3";
        } else if ("integer".equals(dataType)) {
            return "6";
        } else if (dataType.contains("Varchar") || dataType.contains("string")) {
            return "Testdata";
        } else {
            // Handle unknown data types
            throw new IllegalArgumentException("Unknown data type: " + dataType);
        }
    }


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
     * Captures a screenshot of the current screen.
     *
     * @throws IOException if an I/O error occurs while capturing the screenshot.
     */
    public static void captureScreenshot() throws IOException {

        File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

        Date d = new Date();
        screenshotName ="Screenshot_"+ d.toString().replace(":", "_").replace(" ", "_") + ".jpg";
        FileUtils.copyFile(scrFile, new File(props.get("Screenshotpath") + screenshotName));
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
            fileInputStream = new FileInputStream(configfilepath);
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
     * Manages network conditions based on the given mode and duration.
     *
     * @param mode     the network mode to be set (Wifi, MobileData, Disable)
     * @param duration the duration for which the network mode should be maintained (in seconds)
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     *
     * @throws NumberFormatException if the duration is not in a valid format
     *
     * @throws Exception             if there is an error while managing network conditions
     */
    public static void manageNetworkConditions(String mode, String duration) throws InterruptedException {

        try {
            int sleeptime = Integer.parseInt(duration+"000");
            log.info("Network Mode: "+mode+" Duration : "+duration);
            if(mode.equalsIgnoreCase("Wifi")){
                networkconnections();
                log.info("Wifi and Mobiledata is off");
                Thread.sleep(sleeptime);
                log.info("Thread is Waiting for "+sleeptime);
                driver.toggleWifi();
                log.info("Wifi is Turned On");
            } else if (mode.equalsIgnoreCase("MobileData")) {
                networkconnections();
                log.info("Wifi and Mobiledata is off");
                Thread.sleep(sleeptime);
                log.info("Thread is Waiting for "+sleeptime);
                driver.toggleData();
                log.info("Mobiledata is Turned On");
            }
            if (!mode.equalsIgnoreCase("Enable")) {
                handleNoInternetConnection();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupted status
            log.error("Thread interrupted while waiting: {}"+ e.getMessage(), e);
        } catch (NumberFormatException e) {
            log.error("Invalid duration format: {}"+ duration);
        } catch (Exception e) {
            log.error("Error while managing network conditions: {}"+ e.getMessage(), e);
        }

    }

    /**
     * Toggles the network connections.
     * This method toggles the wifi and data connections on the device.
     *
     * @throws UnsupportedOperationException if the device does not support toggling network connections.
     */
    public static void networkconnections(){
        driver.toggleWifi();
        driver.toggleData();
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
    public static void Assertion(String expected,String message){
        try {
            if(sourceExists(expected)){
                Assert.assertTrue(true);
                logAndReportSuccess("TestCase Passed : "+expected+" is Displayed");
            }else{
                Assert.assertTrue(false);
                logAndReportFailure("TestCase Failed : "+message);
            }
        } catch (AssertionError e) {
            logAndReportFailure(formatData("TestCase Failed : "+message));
        }
    }


    public static void verifyModuleDisplayedStatus(String module) throws InterruptedException {
        Thread.sleep(900);
        applogin(module);
        if(!sourceExists(module)){
            logAndReportFailure("TestCase Failed : "+module+" module is not displayed");
            Assert.assertTrue(false);
        }else{
            logAndReportSuccess("TestCase Passed : "+module+" module is Displayed");
            Assert.assertTrue(true);
        }

    }

    /**
     * Logs in the user using the provided login credentials.
     *
     * @throws JSONException if there is an error parsing the test data
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param project the project name
     * @param mobileno the mobile number of the user
     *
     * @returns void
     */
    public static void lgpage()  {
        JSONObject user1 = gettestdata("Login","User1");
        login(globalData.getString("username"), globalData.getString("password"),globalData.getString("project"),globalData.getString("mobileno"));
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


    public static void shopfrontphotorequired () throws Exception {
        if (getProjectDataFromDatabase(isShopFrontPhotoRequired(), "IsShopFrontPhotoRequired").equals("1")) {
            log.info("ShopFrontPhotoRequired image is Required");
            pssshopfrontimage();
            capturedDone = true;
        }
    }


    public static void pssshopfrontimage() throws InterruptedException {

        webdriverWait("Xpath", Shutterbutton, 30);
        click("Xpath", Shutterbutton);
        webdriverWait("ACCESSIBILITYID", "Done", 3);
        click("ACCESSIBILITYID", "Done");

    }

    public static void scroll(AndroidDriver driver, int distance) throws InterruptedException {
        String scrollScript = String.format("new UiScrollable(new UiSelector().scrollable(true)).scrollForward(%d)", distance);
        driver.findElement(AppiumBy.androidUIAutomator(scrollScript));

    }

    public static String removeUnderscores(String input) {
        // Replace underscores with an empty string
        return input.replace("_", " ");
    }


    public static void Dropdownsetter(String formName, String productName, String IsQuestionForm, String fieldName) throws Exception {
        try {
            String Enumquery;
            if (IsQuestionForm.equals("1")) {
                Enumquery = MessageFormat.format(queries.get("EnumQuestionFieldquery"), formName, "'" + productName + "'", "'" + formName + "'");
            } else {
                Enumquery = MessageFormat.format(queries.get("EnumFieldquery"), "'" + fieldName.replace(" *", "").replace(" ", "_") + "'", "'" + formName + "'");
            }
            log.info("EnumQuery " + Enumquery);
            List<String> dropList = getColumnNamesFromDatabase(Enumquery, "FieldOption");

            for (String drop : dropList) {
                if (sourceExists(fieldName)) {
                    dropdown(fieldName,drop);
                    break;
                } else{
                    if (sourceExists(drop)) {
                        click("ACCESSIBILITYID", drop);
                        break;
                    }
                    Utils.scroll(driver,600);
                    dropdown(fieldName,drop);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error setting dropdown for field " + fieldName + " in form " + formName);
            log.error(e.getMessage());
        }
    }

    public static void dropdown(String fieldName,String drop){

        if (sourceExists(fieldName)) {
            click("ACCESSIBILITYID", fieldName);
            click("ACCESSIBILITYID", drop);

        } else if (sourceExists(drop)) {
            click("ACCESSIBILITYID", drop);
        }
    }


    public static void imageCapture() throws InterruptedException {

        if(fieldName.contains("Photo *")){
            click("Xpath", Camerabutton_M);
        }else{
            click("Xpath", Camerabutton_NM);
        }
        webdriverWait("Xpath", Shutterbutton, 4);
        click("Xpath", Shutterbutton);
        webdriverWait("ACCESSIBILITYID", "Done", 3);
        click("ACCESSIBILITYID", "Done");
        Thread.sleep(700);
        totalimagescaptured++;

    }

    public static boolean waitForCallupload(String msg) throws InterruptedException {
        boolean displayed = sourceExists(msg);
        long startTime = System.currentTimeMillis();
        long timeout = 60000;
        try {
            while (!displayed && (System.currentTimeMillis() - startTime) < timeout) {
                log.info("Waiting for message: '" + msg + "'");
                Thread.sleep(500);
                click("ACCESSIBILITYID", ActivityLog);
                displayed = sourceExists(msg);
                driver.navigate().back();
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while waiting for message: '" + msg + "'");
            Thread.currentThread().interrupt(); // Reset interrupted status
        }
        return displayed;
    }

    /**
     * Fetches targets from the database for a given username.
     *
     * @param username the username for which targets are to be fetched
     * @return a list of target IDs for unplanned calls
     * @throws Exception if an error occurs while fetching targets from the database
     */


    public static List<String> fetchTargetsFromDatabase(String username) throws Exception {
        try {

            String count = globalData.getString("Targets");
            log.info("Fetching targets from the database for username: " + username);
            String checkTodayCalls = "select * from Pjpplan where username = '"+username+"'";
            String updateTodayCallstoUnplanned = "update Pjpplan set Status = 'A' where username = '"+username+"' ";
            fetchdatafromdb(updateTodayCallstoUnplanned);
            log.info("Updated today's calls to unplanned.");
            String getUnplannedCalls = "select top "+count+" * from Pjpplan where username = '"+username+"' ";
            String updateUnplannedCalls = ";WITH T AS (select top "+count+" * from PjpPlan where username = 'Abhisdel') update T set Pjpdate = '"+currentdate+"' ";
            fetchdatafromdb(updateUnplannedCalls);
            log.info("Updated unplanned calls.");
            List<String> targetsforUnplannedCalls = getColumnNamesFromDatabase(getUnplannedCalls, "TargetId");
            log.info("Targets fetched successfully.");
            return targetsforUnplannedCalls;
        } catch (Exception e) {
            log.error("Error fetching targets from the database: " + e.getMessage());
            e.getMessage();
        }
        return null;
    }

    public static String generateStoreRequiredQuery(){

        String storereqquery = "select ExitButtonRequired, * from Projectmaster where ProjectName = '"+ globalData.getString("project")+"'";
        return storereqquery;

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





