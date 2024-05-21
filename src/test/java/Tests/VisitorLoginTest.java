package Tests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import Modules.VisitorLogin_Module;

import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Listeners.FrameX_Listeners.logAndReportSuccess;
import static Modules.Login_Module.*;
import static Modules.VisitorLogin_Module.*;
import static Pages.HomePage_page.*;
import static Utilities.TestDataUtil.gettestdata;
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


    @Test(priority = 3,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"regression"},enabled = true)
    public void TC_003_Verify_VisitorEmpIDTextboxCharacterLimit() throws InterruptedException {
        JSONObject charclimit = gettestdata("Visitor Login", "EmpID CharacterLimit");
        String empid = charclimit.getString("EmpID");
        String limit = charclimit.getString("Limit");
        validateMaxTextLimitInEmpIDTextbox(empid,limit);
    }

    @Test(priority = 4, dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" },groups = {"regression"},enabled = true)
    public void TC_004_Verify_VisitorEmpIDTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInEmpIDTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 5, dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" },groups = {"regression"},enabled = true)
    public void TC_005_Verify_VisitorEmpIDTextboxAcceptsNumbers() {
        validateNumbersInEmpIDTextbox(generateRandomNumber());
    }

    @Test(priority = 6,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"regression"},enabled = true)
    public void TC_006_Verify_VisitorNameTextboxCharacterLimit() {
        JSONObject Namecharacterlimit = gettestdata("Visitor Login", "Name CharacterLimit");
        String name = Namecharacterlimit.getString("Name");
        String limit = Namecharacterlimit.getString("Limit");
        validateMaxTextLimitInNameTextbox(name,limit);
    }

    @Test(priority = 7, dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" },groups = {"regression"},enabled = true)
    public void TC_007_Verify_VisitorNameTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInNameTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 8,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"regression"},enabled = true)
    public void TC_008_Verify_VisitorNameTextboxAcceptsNumbers() {
        validateNumbersInNameTextbox(generateRandomNumber());
    }

    @Test(priority = 9, dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" },groups = {"regression"},enabled = true)
    public void TC_009_Verify_DesignationTextboxCharacterLimit() {
        JSONObject desigcharacterlimit = gettestdata("Visitor Login", "Designation CharacterLimit");
        String name = desigcharacterlimit.getString("Name");
        String limit = desigcharacterlimit.getString("Limit");
        validateMaxTextLimitInDesignationTextbox(name,limit);
    }

    @Test(priority = 10,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"regression"},enabled = true)
    public void TC_010_Verify_DesignationTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInDesignationTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 11,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"regression"},enabled = true)
    public void TC_011_Verify_DesignationTextboxAcceptsNumbers() {
        validateNumbersInDesignationTextbox(generateRandomNumber());
    }

    @Test(priority = 12, dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" },groups = {"regression"},enabled = true)
    public void TC_012_Verify_RemarksTextboxCharacterLimit() {
        JSONObject remarkscharlimit = gettestdata("Visitor Login", "Remarks CharacterLimit");
        String name = remarkscharlimit.getString("Name");
        String limit = remarkscharlimit.getString("Limit");
        validateMaxTextLimitInRemarksTextbox(name,limit);
    }

    @Test(priority = 13, dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" },groups = {"regression"},enabled = true)
    public void TC_013_Verify_RemarksTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInRemarksTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 14,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"regression"},enabled = true)
    public void TC_014_Verify_RemarksTextboxAcceptsNumbers() throws InterruptedException {
        validateNumbersInRemarksTextbox(generateRandomNumber());
    }

    /**
     * This method is used to verify the visitor upload functionality.
     * 
     * @throws Exception if an error occurs during the execution of the test case
     */
    @Test(priority = 15,dependsOnMethods = { "TC_001_Verify_VisitorLogin_Module_Displayed" }, groups = {"smoke", "regression"},enabled = true)
    private  void TC_015_Verify_Visitor_Upload() throws Exception {
        //navigateto(visitorlogin);
        validatevisitorUpload();
    }

}