package Tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import Modules.VisitorLogin_Module;

import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Listeners.FrameX_Listeners.logAndReportSuccess;
import static Modules.VisitorLogin_Module.validateErrorFields;
import static Modules.VisitorLogin_Module.validatevisitorUpload;
import static Pages.HomePage_page.*;
import static Utilities.Utils.*;

/**
 * VisitorLoginTest class.
 */
public class VisitorLoginTest {

    /**
     * This method is used to verify the display of VisitorLogin module.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting for the module to be displayed.
     */
    @Test(priority = 1, groups = {"smoke", "regression"},enabled = true)
    private  void TC_001_Verify_VisitorLogin_Module_Displayed() throws InterruptedException {
        verifyModuleDisplayedStatus(visitorlogin);
    }



    /**
     * This test case verifies the behavior of the Submit button when no fields are entered.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 2, dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" },groups = {"negative", "regression"},enabled = true)
    private void TC_002_Verify_Submit_Button_Without_Enter_fields() throws InterruptedException {
        validateErrorFields();
    }



    /**
     * This method is used to verify the visitor upload functionality.
     * 
     * @throws Exception if an error occurs during the execution of the test case
     */
    @Test(priority = 3,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"smoke", "regression"},enabled = true)
    private  void TC_003_Verify_Visitor_Upload() throws Exception {
        //navigateto(visitorlogin);
        validatevisitorUpload();
    }

}