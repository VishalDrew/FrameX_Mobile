package Modules.CallPlan;

import Pages.CallPlan_page;
import Utilities.Utils;
import com.aventstack.extentreports.ExtentTest;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static Listeners.FrameX_Listeners.*;
import static Modules.CallPlanModule.driver;
import static Modules.CallPlanModule.log;
import static Modules.CallPlanModule.*;
import static Pages.ActivityLog_Page.*;
import static Pages.CallPlan_page.*;
import static Pages.HomePage_page.ActivityLog;
import static Pages.HomePage_page.Callplan;
import static Utilities.Actions.*;
import static Utilities.DatabaseUtility.*;
import static Utilities.Utils.*;

public class CallPlanActionHandler {

    public static boolean Uploadcallfunction(String starttime, String networkmode , String networkoffduration) throws Exception {

        ExtentTest Uploadtest = extent.createTest("Target ID : " + targetid+" , Call Type : Upload");

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
                Uploadtest.pass(formatData(targetid +" Call Uploaded Successfully , Image Count : "+totalimagescaptured) );
                driver.navigate().back();
                return true;
            }else{
                driver.navigate().back();
                handleUploadFailure(Uploadtest);
                return false;
            }
        } else {
            handleUploadFailure(Uploadtest);
            log.warn("Timeout reached while waiting for message: '" + "Target "+ targetid +" successfully uploaded" + "'");
            return false;
        }
    }

    public static boolean closecallfunction(String networkmode, String networkoffduration) throws Exception {

        ExtentTest closetest = extent.createTest("Target ID : " + targetid+" , Call Type : Close");

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
                    closetest.pass(formatData(targetid + " Close Call Uploaded Successfully"));
                    driver.navigate().back();
                    return true;
                } else {
                    handleUploadFailure(closetest);
                    return false;
                }
            } else {
                handleUploadFailure(closetest);
                return false;
            }
        }
        return false;
    }


    private static void handleUploadFailure(ExtentTest test) throws InterruptedException {
        test.fail(formatData("Failed to Upload call " + targetid + ", Image Count: " + totalimagescaptured));
        getactivitylogpageScreenshot(test);
        sendDB();
        logAndReportFailure("Failed to Upload call " + targetid + ", Image Count: " + totalimagescaptured);
    }


    static void captureAndAttachScreenshot(ExtentTest test) {
        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            test.addScreenCaptureFromBase64String(Base64.getEncoder().encodeToString(screenshot), "Screenshot");
        }
    }

    // Helper method to capture close call image
    static void captureCloseCallImage() {
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

    static void clickAndWait(String locatorType, String locator, int waitTime) {
        webdriverWait(locatorType, locator, waitTime);
        click(locatorType, locator);
    }


    static boolean handleUploadSuccess() throws Exception {

        String  isexitstoreRequired = getProjectDataFromDatabase(generateStoreRequiredQuery(),"ExitButtonRequired");
        String uploadedtargetxpath = "//android.widget.ImageView[contains(@content-desc, 'Target ID: " + targetid + "')]";
        String Exitstoreuploadedtargetxpath = "//android.view.View[contains(@content-desc, 'Target ID: " + targetid + "')]";

        clickAndWait("ACCESSIBILITYID", Callplan, 15);
        Thread.sleep(1000);
        if (!sourceExists(targetid)) {
            Utils.scroll(driver, 600);
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

    static void getactivitylogpageScreenshot(ExtentTest type) throws InterruptedException {
        click("ACCESSIBILITYID", ActivityLog);
        Thread.sleep(1000);
        captureAndAttachScreenshot(type);
    }

    static void sendDB(){
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

}

