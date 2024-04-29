package Tests;

import Base.TestSetup;
import dev.failsafe.Call;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Listeners.FrameX_Listeners.logAndReportSuccess;
import static Modules.Callplan_Module.validateUploadCall;
import static Pages.CallPlan_page.*;
import static Pages.HomePage_page.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.*;

/**
 * CallPlanTest class.
 */
public class CallPlanTest extends TestSetup {

    /**
     * This method is used to verify if the Call Plan module is displayed.
     * 
     * @throws Exception if an error occurs during the execution of the method
     */
    @Test(priority = 1, groups = {"smoke", "regression"},enabled = true)
    private static void TC_001_Verify_Call_Plan_Module_Displayed() throws Exception {

       verifyModuleDisplayedStatus(Callplan);
    }


    /**
     * This method is used to verify the functionality of the Sync Button.
     * It performs the following steps:
     * 1. Calls the sync() method.
     * 2. Asserts that the source exists for the Callplan.
     *
     * @throws Exception if an error occurs during the execution of the test case.
     */
    @Test(priority = 2,dependsOnMethods = { "TC_001_Verify_Call_Plan_Module_Displayed" },groups = {"smoke", "regression"},enabled = false)
    private static void TC_002_Verify_Sync_Button_Functionality() throws Exception {
        navigateto(Callplan);
        sync();
        if(sourceExists(Callplan)){
            Assert.assertTrue(true);
            logAndReportSuccess("TestCase Passed : Sync Button is working ");
        }else{
            Assert.assertTrue(false);
            logAndReportSuccess("TestCase Failed : Sync Button is not working ");
        }
    }



    /**
     * Test case to verify the 5 Minute Sync Process.
     * 
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(priority = 3,dependsOnMethods = { "TC_001_Verify_Call_Plan_Module_Displayed" },groups = {"smoke", "regression"},enabled = false)
    private static void TC_003_Verify_5_Minute_Sync_Process() throws Exception {
        JSONObject callplandata = gettestdata("Callplan", "5minutesSync");
        navigateto(Callplan);
        fiveminssync(callplandata.getString("expectedMessage"));
    }


    /**
     * This method is a test case for validating the error that occurs when starting a concurrent job.
     */
    @Test(priority = 4,dependsOnMethods = { "TC_001_Verify_Call_Plan_Module_Displayed"},groups = {"regression"},enabled = false)
    private static void TC_004_Concurrent_Job_Start_Error() throws Exception {
        navigateto(Callplan);
        validate_Concurrent_Job_Start(targets.get(0), targets.get(1));
    }


    /**
     * This method is used to verify the upload button functionality without completing the form.
     *
     * @throws Exception if an error occurs during the execution of the test case
     *
     */
    @Test(priority = 5,dependsOnMethods = { "TC_001_Verify_Call_Plan_Module_Displayed" },groups = {"negative","regression"},enabled = false)
    private static void TC_005_Verify_Upload_Button_Without_Completing_Form() throws Exception {
        navigateto(Callplan);
        formcompletingvalidation();
    }


    /**
     * Test case to verify that the mandatory fields error message is displayed.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(priority = 6, groups = {"negative", "regression"},enabled = false)
    private static void TC_006_Verify_Mandatory_Fields_Error_Message_Displayed() throws Exception {

    }

    /**
     * Test case to verify that the mandatory images error message is displayed.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(priority = 7, groups = {"negative", "regression"},enabled = false)
    private static void TC_007_Verify_Mandatory_Images_Error_Message_Displayed() throws Exception {

    }

    /**
     * Test case to verify the error messages for Industry and Brand facing fields.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(priority = 8, groups = {"negative", "regression"},enabled = false)
    private static void TC_008_Verify_Industry_And_Brand_Facing_Fields_Error_Messages() throws Exception {

    }

    /**
     * Test case to verify the call upload functionality.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(priority = 9, groups = {"smoke", "regression"},enabled = true)
    private static void TC_009_Verify_Call_Upload() throws Exception {
        validateUploadCall();
    }

}
