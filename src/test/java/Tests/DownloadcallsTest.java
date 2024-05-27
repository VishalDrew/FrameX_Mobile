package Tests;

import Modules.Downloadcalls_Module;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static Modules.Downloadcalls_Module.*;
import static Pages.HomePage_page.DownloadCalls;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;

/**
 * DownloadcallsTest class.
 */
public class DownloadcallsTest {

    /**
     * This method is used to verify if the DownloadCalls module is displayed.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 1, groups = {"smoke", "regression"}, enabled = true)
    public void TC_001_VerifyDownloadCallsModuleIsDisplayed() throws InterruptedException {
        verifyModuleDisplayedStatus(DownloadCalls);
    }

    /**
     * This method is used to verify that calls are downloaded.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 2, dependsOnMethods = {"TC_001_VerifyDownloadCallsModuleIsDisplayed"}, groups = {"smoke", "regression"}, enabled = true)
    public void TC_002_VerifyCallsAreDownloaded() throws InterruptedException {
        validateDownloadCalls();
    }

    /**
     * This method is used to verify the duplicate call download functionality.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test(priority = 3, dependsOnMethods = {"TC_001_VerifyDownloadCallsModuleIsDisplayed"}, groups = {"negative", "regression"}, enabled = true)
    public void TC_003_VerifyDuplicateCallDownload() throws Exception {
        validateDuplicateTarget();
    }

    /**
     * This test case verifies the functionality of invalid target download in the Downloadcalls module.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test(priority = 4, dependsOnMethods = {"TC_001_VerifyDownloadCallsModuleIsDisplayed"}, groups = {"negative", "regression"}, enabled = true)
    public void TC_004_VerifyInvalidTargetDownload() throws Exception {
        JSONObject downloadcallsdata = gettestdata("Downloadcalls", "InvalidTargetID");
        validateInvalidTarget(downloadcallsdata.getString("targetid"));
    }

    /**
     * This method is used to verify the functionality of the Remove button.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test(priority = 5, dependsOnMethods = {"TC_001_VerifyDownloadCallsModuleIsDisplayed"}, groups = {"regression"}, enabled = true)
    public void TC_005_VerifyRemoveButtonFunctionality() throws Exception {
        validateremovebutton();
    }

    /**
     * This method is used to verify the Submit button functionality without adding a target in the Download Calls module.
     *
     * @throws Exception if an error occurs during the test execution
     */
    @Test(priority = 6, dependsOnMethods = {"TC_001_VerifyDownloadCallsModuleIsDisplayed"}, groups = {"negative", "regression"}, enabled = true)
    public void TC_006_VerifySubmitButtonWithoutAddingTarget() throws Exception {
        JSONObject downloadcallsdata = gettestdata("Downloadcalls", "SubmitButtonValidation");
        validatesubmitbutton(downloadcallsdata.getString("ErrorMessage"));
    }

    /**
     * This method is used to verify the character limit of the Target ID textbox.
     */
    @Test(priority = 7, dependsOnMethods = {"TC_001_VerifyDownloadCallsModuleIsDisplayed"}, groups = {"regression"}, enabled = true)
    public void TC_007_VerifyTargetIDTextboxCharacterLimit() {
        JSONObject downloadcallsdata = gettestdata("Downloadcalls", "CharacterLimit");
        String testTrgId = downloadcallsdata.getString("TargetID");
        String limit = downloadcallsdata.getString("Limit");
        Downloadcalls_Module.validateMaxTextLimitIntargetIDTextbox(testTrgId, limit);
    }

}


