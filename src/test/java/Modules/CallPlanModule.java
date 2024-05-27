package Modules;

import Base.TestSetup;
import Modules.CallPlan.CallPlanActionHandler;
import Utilities.AppUtils;
import org.json.JSONObject;
import org.testng.Assert;

import java.text.MessageFormat;
import java.util.*;

import static Listeners.FrameX_Listeners.*;
import static Modules.CallPlan.CallPlanActionHandler.*;
import static Modules.CallPlan.CallPlanData.generateCallPlanScenarios;
import static Modules.CallPlan.CallPlanData.targetSize;
import static Modules.CallPlan.DataBinder.dataBinder;
import static Modules.CallPlan.DataBinder.fieldName;
import static Modules.Downloadcalls_Module.storesForVisitorLogin;
import static Modules.VisitorLogin_Module.downloadstores;
import static Pages.CallPlan_page.*;
import static Pages.HomePage_page.*;
import static Utilities.Actions.*;
import static Utilities.Constants.*;
import static Utilities.DatabaseUtility.*;
import static Utilities.TestDataUtil.getCallPlanTestData;
import static Utilities.AppUtils.*;

/**
 * Callplan_Module class.
 */
public class CallPlanModule extends TestSetup {

    public static String targetid;
    public static String fieldtypes;

    /**
     * Starts the call process by generating call plan scenarios for the given targets.
     *
     * @throws Exception if an error occurs during the call process
     */
    public static void startCallProcess() throws Exception {
        if (generateCallPlanScenarios(targets)) {
            JSONObject callPlanTestData;
            for (int i = 1; i <= targetSize; i++) {
                callPlanTestData = getCallPlanTestData("Call Plan", "Uploaddata" + i);
                targetid = callPlanTestData.getString("Targetid");
                if (callPlanTestData.getString("Call Type").equalsIgnoreCase("Upload")) {
                    fieldtypes = callPlanTestData.getString("Fields");
                    validateUploadCall(callPlanTestData.getString("Call Type") ,callPlanTestData.getString("Network Mode"),callPlanTestData.getString("Disable Duration"),callPlanTestData.getString("Fields"));
                } else {
                    validateUploadCall(callPlanTestData.getString("Call Type") ,callPlanTestData.getString("Network Mode"),callPlanTestData.getString("Disable Duration"));
                }

            }
        }
    }

    /**
     * Validates the upload call for a given target ID.
     *
     * @param uploadType The type of call, either "Upload" or "Close".
     * @param networkMode The network mode for the call.
     * @param networkDuration The network duration for the call.
     * @param fieldType The field types for the call.
     * @return True if the upload call is validated successfully, false otherwise.
     * @throws Exception If an error occurs during the validation process.
     */
    private static boolean validateUploadCall(String uploadType,String networkMode, String networkDuration, String ...fieldType) throws Exception {

        List<String> categories = getColumnNamesFromDatabase(Categorymasterquerygenerator(targetid), "name");
        String starttargetXPath = "//android.view.View[contains(@content-desc, 'Target ID: " + targetid + "')]";
        applogin(Callplan);
        click("ACCESSIBILITYID", Callplan);
        searchtarget();
        if (sourceExists(targetid)) {
            click("Xpath", starttargetXPath);
            String startTime = CallPlanActionHandler.datevisitedvalidation();
            click("Xpath", Startworkbutton);
            if(!capturedDone){
                shopfrontphotorequired ();
            }
            capturedDone = false;
            webdriverWait("ACCESSIBILITYID", UploadcallButton, 35);
            if (uploadType.equalsIgnoreCase("Upload")) {
                if (dataBinder(categories, Arrays.toString(fieldType))) {
                    return Uploadcallfunction(startTime, networkMode, networkDuration);
                }
            } else if (uploadType.equalsIgnoreCase("Close")) {
                return closecallfunction(networkMode, networkDuration);
            }
        } else {
            logAndReportFailure("Target ID " + targetid + " is not Displayed ");
        }
        return false;
    }


    private static void searchtarget() throws InterruptedException {
        if (!sourceExists(targetid)) {
            driver.navigate().back();
            Thread.sleep(3000);
            click("ACCESSIBILITYID", Callplan);
        }
        if (!sourceExists(targetid)) {
            AppUtils.scroll(driver, 600);
        }
    }

    public static void sync() {
        if(!sourceExists(sync)){
            click("ACCESSIBILITYID",Callplan);
            log.info("Clicked on Call Plan");
        }
        click("ACCESSIBILITYID",sync);
        log.info("Clicked on Sync");
    }

    public static void fiveminssync(String expected) {
        sync(); // Perform sync
        if(sourceExists(expected)) {
            click("ACCESSIBILITYID","Ok");
            logAndReportSuccess("TestCase Passed : 5 Minutes Sync is working");
            Assert.assertTrue(true);
        } else {
            logAndReportFailure("TestCase Failed : 5 Minutes Sync is not working");
            Assert.assertTrue(false,"TestCase Failed : 5 Minutes Sync is not working");
        }
    }

    public static void formcompletingvalidation(String errmsg) throws Exception {
        if(!sourceExists((targets.get(0)))){
            if (!storesForVisitorLogin == true) {
                navigateto(DownloadCalls);
                downloadstores();
            }
        }
        navigateToCallplanPage();
        click("ACCESSIBILITYID",Callplan);
        click("xpath",gettargetxpath(targets.get(0)));
        click("Xpath", Startworkbutton);
        shopfrontphotorequired ();
        webdriverWait("ACCESSIBILITYID", UploadcallButton, 20);
        click("ACCESSIBILITYID",UploadcallButton);
        if(sourceExists(errmsg)){
            logAndReportSuccess("TestCase Passed : First fill all categories data to upload is Displayed");
            Assert.assertTrue(true);
        } else {
            logAndReportFailure("TestCase Failed : First fill all categories data to upload is Not Displayed");
            Assert.assertTrue(false);
        }
    }

    private static void pssshopfrontimage() throws InterruptedException {

        webdriverWait("Xpath", Shutterbutton, 30);
        click("Xpath", Shutterbutton);
        webdriverWait("ACCESSIBILITYID", "Done", 3);
        click("ACCESSIBILITYID", "Done");

    }

    private static void shopfrontphotorequired () throws Exception {
        if (getProjectDataFromDatabase(isShopFrontPhotoRequired(), "IsShopFrontPhotoRequired").equals("1")) {
            log.info("ShopFrontPhotoRequired image is Required");
            pssshopfrontimage();
            capturedDone = true;
        }
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
            String checkTodayCalls = "select * from Pjpplan where username = '"+username+"'";
            String updateTodayCallstoUnplanned = "update Pjpplan set Status = 'A' where username = '"+username+"' ";
            fetchdatafromdb(updateTodayCallstoUnplanned);
            String getUnplannedCalls = "select top "+count+" * from Pjpplan where username = '"+username+"' ";
            String updateUnplannedCalls = ";WITH T AS (select top "+count+" * from PjpPlan where username = 'Abhisdel') update T set Pjpdate = '"+currentdate+"' ";
            fetchdatafromdb(updateUnplannedCalls);
            List<String> targetsforUnplannedCalls = getColumnNamesFromDatabase(getUnplannedCalls, "TargetId");
            log.info("Targets fetched successfully.");
            return targetsforUnplannedCalls;
        } catch (Exception e) {
            log.error("Error fetching targets from the database: " + e.getMessage());
            e.getMessage();
        }
        return null;
    }



}


