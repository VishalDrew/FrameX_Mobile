package Tests;

import Base.TestSetup;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Base.FrameX_Listeners.logAndReportFailure;
import static Base.FrameX_Listeners.logAndReportSuccess;
import static Modules.CallPlanModule.*;
import static Pages.HomePage_page.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;

/**
 * CallPlanTest class.
 */
public class CallPlanTest extends TestSetup {

    /**
     * Test case to verify if the Call Plan module is displayed.
     *
     * @throws Exception if an error occurs during the execution of the method
     */
    @Test(priority = 1, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify if the Call Plan module is displayed.")
    @Step("Check if the Call Plan module is displayed.")
    @Severity(SeverityLevel.BLOCKER)
    public static void TC_001_Verify_Call_Plan_Module_Displayed() throws Exception {
        verifyModuleDisplayedStatus(Callplan);
    }

    /**
     * Test case to verify the functionality of the Sync Button.
     *
     * @throws Exception if an error occurs during the execution of the test case.
     */
    @Test(priority = 2, dependsOnMethods = {"TC_001_Verify_Call_Plan_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify the functionality of the Sync Button.")
    @Step("Click on the Sync Button and verify if it is working.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC_002_Verify_Sync_Button_Functionality() throws Exception {
        navigateto(Callplan);
        sync();
        if(sourceExists(Callplan)){
            Assert.assertTrue(true);
            logAndReportSuccess("Sync Button is working correctly.");
        } else {
            logAndReportFailure("Sync Button is not working.");
            Assert.fail("TestCase Failed : Sync Button is not working");

        }
    }

    /**
     * Test case to verify the 5 Minute Sync Process.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(priority = 3, dependsOnMethods = {"TC_001_Verify_Call_Plan_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify the 5 Minute Sync Process.")
    @Step("Check if the 5-minute sync process completes successfully.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC_003_Verify_5_Minute_Sync_Process() throws Exception {
        JSONObject callplandata = gettestdata("Callplan", "5minutesSync");
        navigateto(Callplan);
        fiveminssync(callplandata.getString("expectedMessage"));
    }

    /**
     * Test case to verify the upload button functionality without completing the form.
     *
     * @throws Exception if an error occurs during the execution of the test case
     */
    @Test(priority = 4, dependsOnMethods = {"TC_001_Verify_Call_Plan_Module_Displayed"}, groups = {"negative","regression"}, enabled = true)
    @Description("Verify the upload button functionality without completing the form.")
    @Step("Attempt to upload a call plan without completing the form and verify the error message.")
    @Severity(SeverityLevel.CRITICAL)
    public static void TC_005_Verify_Upload_Button_Without_Completing_Form() throws Exception {
        JSONObject callplandata = gettestdata("Callplan", "FormFillingValidation");
        navigateto(Callplan);
        formcompletingvalidation(callplandata.getString("expectedMessage"));
    }

    /**
     * Test case to verify the call upload functionality.
     *
     * @throws Exception if an error occurs during the test execution.
     */
    @Test(priority = 5, dependsOnMethods = {"TC_001_Verify_Call_Plan_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify the call upload functionality.")
    @Step("Start the call upload process.")
    @Severity(SeverityLevel.BLOCKER)
    public static void TC_009_Verify_Call_Upload() throws Exception {
        startCallProcess();
    }

}
