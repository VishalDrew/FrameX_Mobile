package Tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static Modules.VisitorLogin_Module.*;
import static Pages.HomePage_page.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;

/**
 * VisitorLoginTest class.
 */
public class VisitorLoginTest {

    /**
     * This method is used to verify the display of VisitorLogin module.
     *
     * @throws InterruptedException if the thread is interrupted while waiting for the module to be displayed.
     */
    @Test(priority = 1, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify the display of the Visitor Login module.")
    @Step("Check if the Visitor Login module is displayed.")
    @Severity(SeverityLevel.BLOCKER)
    public void TC_001_VerifyVisitorLoginModuleDisplayed() throws InterruptedException {
        verifyModuleDisplayedStatus(visitorlogin);
    }

    /**
     * This test case verifies the behavior of the Submit button when no fields are entered.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test(priority = 2, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"negative", "regression"}, enabled = true)
    @Description("Verify the behavior of the Submit button when no fields are entered in the Visitor Login module.")
    @Step("Check the error message when the Submit button is clicked without entering any fields.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_002_VerifySubmitButtonWithoutEnteringFields() throws InterruptedException {
        validateErrorFields();
    }

    /**
     * This test case verifies the character limit of the Employee ID textbox in the Visitor Login module.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test(priority = 3, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify the character limit of the Employee ID textbox in the Visitor Login module.")
    @Step("Enter the maximum allowed characters in the Employee ID textbox and verify the limit.")
    @Severity(SeverityLevel.MINOR)
    public void TC_003_VerifyVisitorEmpIDTextboxCharacterLimit() throws InterruptedException {
        JSONObject charclimit = gettestdata("Visitor Login", "EmpID CharacterLimit");
        validateMaxTextLimitInEmpIDTextbox(charclimit.getString("EmpID"), charclimit.getString("Limit"));
    }

    /**
     * This test case verifies if the Employee ID textbox accepts special characters.
     */
    @Test(priority = 4, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Employee ID textbox in the Visitor Login module accepts special characters.")
    @Step("Enter special characters in the Employee ID textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_004_VerifyVisitorEmpIDTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInEmpIDTextbox(generateRandomSpecialCharacters(10));
    }

    /**
     * This test case verifies if the Employee ID textbox accepts numeric values.
     */
    @Test(priority = 5, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Employee ID textbox in the Visitor Login module accepts numeric values.")
    @Step("Enter numeric values in the Employee ID textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_005_VerifyVisitorEmpIDTextboxAcceptsNumbers() {
        validateNumbersInEmpIDTextbox(generateRandomNumber());
    }

    /**
     * This test case verifies the character limit of the Name textbox in the Visitor Login module.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test(priority = 6, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify the character limit of the Name textbox in the Visitor Login module.")
    @Step("Enter the maximum allowed characters in the Name textbox and verify the limit.")
    @Severity(SeverityLevel.MINOR)
    public void TC_006_VerifyVisitorNameTextboxCharacterLimit() throws InterruptedException {
        JSONObject Namecharacterlimit = gettestdata("Visitor Login", "Name CharacterLimit");
        validateMaxTextLimitInNameTextbox(Namecharacterlimit.getString("Name"), Namecharacterlimit.getString("Limit"));
    }

    /**
     * This test case verifies if the Name textbox accepts special characters.
     */
    @Test(priority = 7, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Name textbox in the Visitor Login module accepts special characters.")
    @Step("Enter special characters in the Name textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_007_VerifyVisitorNameTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInNameTextbox(generateRandomSpecialCharacters(10));
    }

    /**
     * This test case verifies if the Name textbox accepts numeric values.
     */
    @Test(priority = 8, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Name textbox in the Visitor Login module accepts numeric values.")
    @Step("Enter numeric values in the Name textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_008_VerifyVisitorNameTextboxAcceptsNumbers() {
        validateNumbersInNameTextbox(generateRandomNumber());
    }

    /**
     * This test case verifies the character limit of the Designation textbox in the Visitor Login module.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test(priority = 9, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify the character limit of the Designation textbox in the Visitor Login module.")
    @Step("Enter the maximum allowed characters in the Designation textbox and verify the limit.")
    @Severity(SeverityLevel.MINOR)
    public void TC_009_VerifyDesignationTextboxCharacterLimit() throws InterruptedException {
        JSONObject desigcharacterlimit = gettestdata("Visitor Login", "Designation CharacterLimit");
        validateMaxTextLimitInDesignationTextbox(desigcharacterlimit.getString("Name"), desigcharacterlimit.getString("Limit"));
    }

    /**
     * This test case verifies if the Designation textbox accepts special characters.
     */
    @Test(priority = 10, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Designation textbox in the Visitor Login module accepts special characters.")
    @Step("Enter special characters in the Designation textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_010_VerifyDesignationTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInDesignationTextbox(generateRandomSpecialCharacters(10));
    }

    /**
     * This test case verifies if the Designation textbox accepts numeric values.
     */
    @Test(priority = 11, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Designation textbox in the Visitor Login module accepts numeric values.")
    @Step("Enter numeric values in the Designation textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_011_VerifyDesignationTextboxAcceptsNumbers() {
        validateNumbersInDesignationTextbox(generateRandomNumber());
    }

    /**
     * This test case verifies the character limit of the Remarks textbox in the Visitor Login module.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test(priority = 12, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify the character limit of the Remarks textbox in the Visitor Login module.")
    @Step("Enter the maximum allowed characters in the Remarks textbox and verify the limit.")
    @Severity(SeverityLevel.MINOR)
    public void TC_012_VerifyRemarksTextboxCharacterLimit() throws InterruptedException {
        JSONObject remarkscharlimit = gettestdata("Visitor Login", "Remarks CharacterLimit");
        validateMaxTextLimitInRemarksTextbox(remarkscharlimit.getString("Name"), remarkscharlimit.getString("Limit"));
    }

    /**
     * This test case verifies if the Remarks textbox accepts special characters.
     */
    @Test(priority = 13, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Remarks textbox in the Visitor Login module accepts special characters.")
    @Step("Enter special characters in the Remarks textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_013_VerifyRemarksTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInRemarksTextbox(generateRandomSpecialCharacters(10));
    }

    /**
     * This test case verifies if the Remarks textbox accepts numeric values.
     *
     * @throws InterruptedException if the thread is interrupted while waiting.
     */
    @Test(priority = 14, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"regression"}, enabled = true)
    @Description("Verify if the Remarks textbox in the Visitor Login module accepts numeric values.")
    @Step("Enter numeric values in the Remarks textbox and verify acceptance.")
    @Severity(SeverityLevel.MINOR)
    public void TC_014_Verify_RemarksTextboxAcceptsNumbers() throws InterruptedException {
        validateNumbersInRemarksTextbox(generateRandomNumber());
    }

    /**
     * This method is used to verify the visitor upload functionality.
     *
     * @throws Exception if an error occurs during the execution of the test case.
     */
    @Test(priority = 15, dependsOnMethods = { "TC_001_VerifyVisitorLoginModuleDisplayed" }, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify the visitor upload functionality in the Visitor Login module.")
    @Step("Test the functionality of uploading visitor details.")
    @Severity(SeverityLevel.CRITICAL)
    public void TC_015_Verify_Visitor_Upload() throws Exception {
        validatevisitorUpload();
    }


}