package Modules.CallPlan;

import Pages.CallPlan_page;
import Utilities.AppUtils;
import Utilities.Constants;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.Assert;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static Base.FrameX_Listeners.*;
import static Modules.CallPlanModule.driver;
import static Modules.CallPlanModule.log;
import static Modules.CallPlanModule.*;
import static Pages.ActivityLog_Page.*;
import static Pages.CallPlan_page.*;
import static Pages.HomePage_page.ActivityLog;
import static Pages.HomePage_page.Callplan;
import static Utilities.Actions.*;
import static Utilities.DatabaseUtility.*;
import static Utilities.AppUtils.*;

public class CallPlanActionHandler {

    public static boolean Uploadcallfunction(String starttime, String networkmode , String networkoffduration) throws Exception {

        click("ACCESSIBILITYID",UploadcallButton);
        log.info("Upload call button is clicked");
        if (isElementDisplayed("ACCESSIBILITYID", Uploadcallconfirmpopup)) {
            click("ACCESSIBILITYID", Yesbutton);
            click("ACCESSIBILITYID", Uploadcallsbutton);
            log.info("Uploadcalls button is clicked");
        } else if (isElementDisplayed("Xpath", Perfectstorescorepopup)) {
            log.info("Perfect Store popup is showing");
            click("ACCESSIBILITYID", Okbutton);
            if(sourceExists(starttime)){
                click("ACCESSIBILITYID", Uploadcallsbutton);
                log.info("Uploadcalls button is clicked");
            }
        }
        manageNetworkConditions(networkmode,networkoffduration);
        if (waitForCallupload( "Target "+ targetid +" successfully uploaded")) {
            if(handleUploadSuccess()){
                logAndReportSuccess(targetid +" Call Uploaded Successfully , Image Count : "+totalimagescaptured );
                driver.navigate().back();
                return true;
            }else{
                driver.navigate().back();
                handleUploadFailure();
                return false;
            }
        } else {
            handleUploadFailure();
            log.warn("Timeout reached while waiting for message: '" + "Target "+ targetid +" successfully uploaded" + "'");
            return false;
        }
    }

    public static boolean closecallfunction(String networkmode, String networkoffduration) throws Exception {

        click("ACCESSIBILITYID", CloseCallButton);
        log.info("Clicked on Close Call Button");
        click("ACCESSIBILITYID", "Yes");
        click("ACCESSIBILITYID", "Reason *");

        String closecallmasterquery = "select * from CloseCallReasonmaster where Status = '1'";
        List<String> reasondroplist = getColumnNamesFromDatabase(closecallmasterquery, "Name");
        Collections.shuffle(reasondroplist);

        Random random = new Random();
        for (String reasondrop : reasondroplist) {
            click("ACCESSIBILITYID", reasondrop);
            String imagerequiredquery = "select * from CloseCallReasonmaster where Name = '" + reasondrop + "'";
            String isimageneed = getdatafromdatabase(imagerequiredquery, "imagerequired");
            if (isimageneed.equals("1")) {
                log.info("Close Call image status : True");
                captureCloseCallImage();
            }
            click("ACCESSIBILITYID", "Done");
            log.info("Clicked on done button");
            manageNetworkConditions(networkmode, networkoffduration);
            if (waitForCallupload("Target " + targetid + "  Close Call  successfully uploaded")) {
                if (handleUploadSuccess()) {
                    logAndReportSuccess(targetid + " Close Call Uploaded Successfully ");
                    driver.navigate().back();
                    return true;
                } else {
                    handleUploadFailure();
                    return false;
                }
            } else {
                handleUploadFailure();
                return false;
            }
        }
        return false;
    }


    private static void handleUploadFailure() throws InterruptedException {
        sendDB();

        logAndReportFailure("Failed to Upload call " + targetid + ", Image Count: " + totalimagescaptured);
    }



    // Helper method to capture close call image
    private static void captureCloseCallImage() {
        click("Xpath", "//android.view.View[@content-desc=\" \"]/android.view.View[3]");
        click("Xpath", Shutterbutton);
        webdriverWait("ACCESSIBILITYID", "Done", 3);
        click("ACCESSIBILITYID", "Done");
    }


    public static String datevisitedvalidation()  {
        String devicetime = datevisitedtime();
        if(sourceExists(devicetime)){
            log.info("Target start time  :  "+devicetime);
            log.info("Visited date and time is Showing");
        }else{
            log.warn("Visited date and time is not Showing");
        }
        return devicetime;
    }

    public static void handleNoInternetConnection() throws InterruptedException {
        if (sourceExists("Please, check internet connection")) {
            click("ACCESSIBILITYID", "Ok");
        }
        click("ACCESSIBILITYID", Callplan);
        Thread.sleep(7000);
        click("ACCESSIBILITYID", CallPlan_page.sync);
        log.info("Clicked on Sync button");
        if(sourceExists("You can click after 5 minutes, since it is in Downloading or Uploading Call process.")){
            click("ACCESSIBILITYID", "Ok");
            driver.navigate().back();
        }
        if (sourceExists("Please, check internet connection")) {
            click("ACCESSIBILITYID", "Ok");
        }
    }

    private  static void clickAndWait(String locatorType, String locator, int waitTime) {
        webdriverWait(locatorType, locator, waitTime);
        click(locatorType, locator);
    }


    private static boolean handleUploadSuccess() throws Exception {

        String  isexitstoreRequired = getProjectDataFromDatabase(Constants.generateStoreRequiredQuery(),"ExitButtonRequired");
        String uploadedtargetxpath = "//android.widget.ImageView[contains(@content-desc, 'Target ID: " + targetid + "')]";
        String Exitstoreuploadedtargetxpath = "//android.view.View[contains(@content-desc, 'Target ID: " + targetid + "')]";

        clickAndWait("ACCESSIBILITYID", Callplan, 15);
        Thread.sleep(1000);
        if (!sourceExists(targetid)) {
            AppUtils.scroll(driver, 600);
        }
        if(isexitstoreRequired.equals("1")){
            click("xpath", Exitstoreuploadedtargetxpath);
        }else{
            click("xpath", uploadedtargetxpath);
        }
        if (isElementDisplayed("xpath","//android.view.View[@content-desc='You have already uploaded "+ targetid +" target']")) {
            click("ACCESSIBILITYID", "Ok");
            return true;
        }else{
            log.info("You have already uploaded " + targetid + " target is not showing");
            return false;
        }
    }


    private static void sendDB(){
        click("ACCESSIBILITYID", ActivityLog);
        click("Xpath", activitylog_menubtn);
        click("ACCESSIBILITYID", Senddb_btn);
        String dbsize = gettext("id", dbSize);
        log.info("Database size for "+targetid+" is "+dbsize);
        click("ACCESSIBILITYID", mailsend_btn);
        if(waitForMessage(mailsuccess_msg)){
            logAndinfo("Mobile DB Sent Successfully");
            driver.navigate().back();
        }
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
    private static void manageNetworkConditions(String mode, String duration) throws InterruptedException {

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
    private static void networkconnections(){
        driver.toggleWifi();
        driver.toggleData();
    }


    private static boolean waitForCallupload(String msg) throws InterruptedException {
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
}

