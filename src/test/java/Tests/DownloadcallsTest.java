package Tests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Modules.Downloadcalls_Module.*;
import static Modules.Login_Module.*;
import static Pages.HomePage_page.Attendance;
import static Pages.HomePage_page.DownloadCalls;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.*;

/**
 * DownloadcallsTest class.
 */
public class DownloadcallsTest {

    /**
     * This method is used to verify if the DownloadCalls module is displayed.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     *
     */
    @Test(priority = 1, groups = {"smoke", "regression"},enabled = true)
    private  void TC_001_VerifyDownloadCallsModuleIsDisplayed() throws InterruptedException {
        verifyModuleDisplayedStatus(DownloadCalls);
    }

    /**
     * This method is used to verify that calls are downloaded.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     *
     */
    @Test(priority = 2, dependsOnMethods = { "TC_001_VerifyDownloadCallsModuleIsDisplayed" },groups = {"smoke", "regression"},enabled = true)
    private  void TC_002_VerifyCallsAreDownloaded() throws InterruptedException {
        validateDownloadCalls();
    }

    /**
     * This method is used to verify the duplicate call download functionality.
     */
    @Test(priority = 3, dependsOnMethods = { "TC_001_VerifyDownloadCallsModuleIsDisplayed" },groups = {"negative", "regression"},enabled = true)
    private  void TC_003_VerifyDuplicateCallDownload() throws Exception {
        validateDuplicateTarget();
    }

    /**
     * This test case verifies the functionality of invalid target download in the Downloadcalls module.
     *
     * @throws Exception if an error occurs during the test execution

     */
    @Test(priority = 4, dependsOnMethods = { "TC_001_VerifyDownloadCallsModuleIsDisplayed" }, groups = { "negative","regression"},enabled = true)
    private  void TC_004_VerifyInvalidTargetDownload() throws Exception {
        JSONObject downloadcallsdata = gettestdata("Downloadcalls", "InvalidTargetID");
        validateInvalidTarget(downloadcallsdata.getString("targetid"));
    }

    /**
     * This method is used to verify the functionality of the Remove button.
     */
    @Test(priority = 5, dependsOnMethods = { "TC_001_VerifyDownloadCallsModuleIsDisplayed" },groups = {"regression"},enabled = true)
    private  void TC_005_VerifyRemoveButtonFunctionality() throws Exception {
        validateremovebutton();
    }

    /**
     * This method is used to verify the Submit button functionality without adding a target in the Download Calls module.
     *
     */
    @Test(priority = 6, dependsOnMethods = { "TC_001_VerifyDownloadCallsModuleIsDisplayed" },groups = {"negative","regression"},enabled = true)
    private  void TC_006_VerifySubmitButtonWithoutAddingTarget() throws Exception {
        JSONObject downloadcallsdata = gettestdata("Downloadcalls", "SubmitButtonValidation");
        validatesubmitbutton(downloadcallsdata.getString("ErrorMessage"));
    }

    @Test(priority = 7, dependsOnMethods = { "TC_001_VerifyDownloadCallsModuleIsDisplayed" },groups = {"regression"},enabled = true)
    public void TC_007_Verify_TargetIDTextboxCharacterLimit() {
        JSONObject downloadcallsdata = gettestdata("Downloadcalls", "CharacterLimit");
        String testtrgid = downloadcallsdata.getString("TargetID");
        String limit = downloadcallsdata.getString("Limit");
        validateMaxTextLimitIntargetIDTextbox(testtrgid,limit);
    }
}


