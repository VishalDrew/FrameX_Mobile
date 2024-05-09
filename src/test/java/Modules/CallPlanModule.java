package Modules;

import Base.TestSetup;
import Modules.CallPlan.CallPlanActionHandler;
import Utilities.Utils;
import com.aventstack.extentreports.ExtentTest;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import static Listeners.FrameX_Listeners.formatData;
import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Modules.CallPlan.CallPlanActionHandler.Uploadcallfunction;
import static Modules.CallPlan.CallPlanActionHandler.closecallfunction;
import static Modules.CallPlan.CallPlanData.generateCallPlanScenarios;
import static Modules.CallPlan.CallPlanData.targetSize;
import static Modules.CallPlan.DataBinder.dataBinder;
import static Pages.CallPlan_page.*;
import static Pages.HomePage_page.Callplan;
import static Utilities.Actions.click;
import static Utilities.Actions.webdriverWait;
import static Utilities.Constants.Categorymasterquerygenerator;
import static Utilities.DBConfig.getColumnNamesFromDatabase;
import static Utilities.DBConfig.getdatafromdatabase;
import static Utilities.TestDataUtil.getCallPlanTestData;
import static Utilities.Utils.*;

/**
 * Callplan_Module class.
 */
public class CallPlanModule extends TestSetup {

    public static String targetid;

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
    public static boolean validateUploadCall(String uploadType,String networkMode, String networkDuration, String ...fieldType) throws Exception {

        List<String> categories = getColumnNamesFromDatabase(Categorymasterquerygenerator(targetid), "name");
        String starttargetXPath = "//android.view.View[contains(@content-desc, 'Target ID: " + targetid + "')]";
        click("ACCESSIBILITYID", Callplan);
        searchtarget();
        if (sourceExists(targetid)) {
            click("Xpath", starttargetXPath);
            String startTime = CallPlanActionHandler.datevisitedvalidation();
            click("Xpath", Startworkbutton);
            shopfrontphotorequired ();
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


    static void searchtarget() throws InterruptedException {
        if (!sourceExists(targetid)) {
            log.info("Target id not found. Navigating back and trying again.");
            driver.navigate().back();
            Thread.sleep(3000);
            click("ACCESSIBILITYID", Callplan);
            log.info("Clicked on Call plan again");
        }
        if (!sourceExists(targetid)) {
            Utils.scroll(driver, 600);
        }
    }
}


