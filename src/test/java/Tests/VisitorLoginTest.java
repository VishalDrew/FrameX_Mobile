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
    public  void TC_001_VerifyVisitorLoginModuleDisplayed() throws InterruptedException {
        verifyModuleDisplayedStatus(visitorlogin);
    }


    /**
     * This test case verifies the behavior of the Submit button when no fields are entered.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 2, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" },groups = {"negative", "regression"},enabled = true)
    public void TC_002_VerifySubmitButtonWithoutEnteringFields() throws InterruptedException {
        validateErrorFields();
    }


    @Test(priority = 3,dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"},enabled = true)
    public void TC_003_VerifyVisitorEmpIDTextboxCharacterLimit() throws InterruptedException {
        JSONObject charclimit = gettestdata("Visitor Login", "EmpID CharacterLimit");
        String empid = charclimit.getString("EmpID");
        String limit = charclimit.getString("Limit");
        validateMaxTextLimitInEmpIDTextbox(empid,limit);
    }

    @Test(priority = 4, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" },groups = {"regression"},enabled = true)
    public void TC_004_VerifyVisitorEmpIDTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInEmpIDTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 5, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" },groups = {"regression"},enabled = true)
    public void TC_005_VerifyVisitorEmpIDTextboxAcceptsNumbers() {
        validateNumbersInEmpIDTextbox(generateRandomNumber());
    }

    @Test(priority = 6,dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"},enabled = true)
    public void TC_006_VerifyVisitorNameTextboxCharacterLimit() {
        JSONObject Namecharacterlimit = gettestdata("Visitor Login", "Name CharacterLimit");
        String name = Namecharacterlimit.getString("Name");
        String limit = Namecharacterlimit.getString("Limit");
        validateMaxTextLimitInNameTextbox(name,limit);
    }

    @Test(priority = 7, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" },groups = {"regression"},enabled = true)
    public void TC_007_VerifyVisitorNameTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInNameTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 8,dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"},enabled = true)
    public void TC_008_VerifyVisitorNameTextboxAcceptsNumbers() {
        validateNumbersInNameTextbox(generateRandomNumber());
    }

    @Test(priority = 9, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" },groups = {"regression"},enabled = true)
    public void TC_009_VerifyDesignationTextboxCharacterLimit() {
        JSONObject desigcharacterlimit = gettestdata("Visitor Login", "Designation CharacterLimit");
        String name = desigcharacterlimit.getString("Name");
        String limit = desigcharacterlimit.getString("Limit");
        validateMaxTextLimitInDesignationTextbox(name,limit);
    }

    @Test(priority = 10,dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"},enabled = true)
    public void TC_010_VerifyDesignationTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInDesignationTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 11,dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"},enabled = true)
    public void TC_011_VerifyDesignationTextboxAcceptsNumbers() {
        validateNumbersInDesignationTextbox(generateRandomNumber());
    }

    @Test(priority = 12, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" },groups = {"regression"},enabled = true)
    public void TC_012_VerifyRemarksTextboxCharacterLimit() {
        JSONObject remarkscharlimit = gettestdata("Visitor Login", "Remarks CharacterLimit");
        String name = remarkscharlimit.getString("Name");
        String limit = remarkscharlimit.getString("Limit");
        validateMaxTextLimitInRemarksTextbox(name,limit);
    }

    @Test(priority = 13, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" },groups = {"regression"},enabled = true)
    public void TC_013_VerifyRemarksTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInRemarksTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 14,dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"},enabled = true)
    public void TC_014_Verify_RemarksTextboxAcceptsNumbers() throws InterruptedException {
        validateNumbersInRemarksTextbox(generateRandomNumber());
    }

    /**
     * This method is used to verify the visitor upload functionality.
     * 
     * @throws Exception if an error occurs during the execution of the test case
     */
    @Test(priority = 15,dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"smoke", "regression"},enabled = true)
    public   void TC_015_Verify_Visitor_Upload() throws Exception {
        //navigateto(visitorlogin);
        validatevisitorUpload();
    }

}