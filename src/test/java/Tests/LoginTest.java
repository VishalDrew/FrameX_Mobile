package Tests;

import Base.TestSetup;
import Modules.Login_Module;
import org.json.JSONObject;
import org.testng.annotations.Test;

import static Modules.Login_Module.*;
import static Utilities.Actions.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.*;

public class LoginTest extends TestSetup {

    /**
     * Test case to verify login with valid credentials.
     *
     * @throws JSONException if there is an error in parsing JSON data.
     */
    @Test(priority = 1, groups = {"smoke", "regression"},enabled = true)
    public void TC_001_Verify_Login_With_Valid_Credentials() throws InterruptedException {
        JSONObject user1 = gettestdata("Login","User1");
        performLogin(user1);
        Assertion(user1.getString("expected"), "Login Failed");
        logout();
    }

    /**
     * Test case to verify login with invalid username.
     *
     * @throws JSONException if there is an error in parsing the test data.
     */
    @Test(priority = 2, groups = {"negative", "regression"},enabled = true)
    public void TC_002_Verify_Login_With_Invalid_Username() {
        JSONObject user2 = gettestdata("Login","User2");
        performLogin(user2);
        Assertion(user2.getString("expectedErrorMessage"), "Invalid Username Error message is Not showing");
        click("ACCESSIBILITYID","Ok");
    }

    /**
     * Test case to verify login with invalid password.
     *
     * @throws JSONException if there is an error in parsing the test data.
     */
    @Test(priority = 3, groups = {"negative", "regression"},enabled = true)
    public void TC_003_Verify_Login_With_Invalid_Password() {
        JSONObject user3 = gettestdata("Login","User3");
        performLogin(user3);
        Assertion(user3.getString("expectedErrorMessage"), "Invalid password Error message is Not showing");
        click("ACCESSIBILITYID","Ok");
    }

    /**
     * Test case to verify login with invalid project.
     *
     * @throws JSONException if there is an error in parsing JSON data.
     */
    @Test(priority = 4, groups = {"negative", "regression"},enabled = true)
    public void TC_004_Verify_Login_With_Invalid_Project() {
        JSONObject user4 = gettestdata("Login","User4");
        performLogin(user4);
        Assertion(user4.getString("expectedErrorMessage"), "Invalid project Error message is Not showing");
        click("ACCESSIBILITYID","Ok");
    }

    /**
     * Test case to verify login with invalid mobile number.
     *
     * @throws JSONException if there is an error in parsing JSON data
     */
    @Test(priority = 5, groups = {"negative", "regression"},enabled = true)
    public void TC_005_Verify_Login_With_Invalid_Mobile_Number() {
        JSONObject user5 = gettestdata("Login","User5");
        performLogin(user5);
        Assertion(user5.getString("expectedErrorMessage"), "Invalid Mobilenumber Error message is Not showing");
    }

    /**
     * Test case to verify login without entering username.
     *
     * @throws JSONException if there is an error in parsing JSON data.
     */
    @Test(priority = 6, groups = {"negative", "regression"},enabled = true)
    public void TC_006_Verify_Login_Without_Entering_Username() {
        JSONObject user6 = gettestdata("Login","User6");
        performLogin(user6);
        Assertion(user6.getString("expectedErrorMessage"), "Please Enter username Error message is Not showing");
    }

    /**
     * Test case to verify login without entering password.
     *
     * @throws JSONException if there is an error in parsing JSON data.
     */
    @Test(priority = 7, groups = {"negative", "regression"},enabled = true)
    public void TC_007_Verify_Login_Without_Entering_Password() {
        JSONObject user7 = gettestdata("Login","User7");
        performLogin(user7);
        Assertion(user7.getString("expectedErrorMessage"), "Please Enter password Error message is Not showing");
    }

    /**
     * Test case to verify login without entering project.
     *
     * @throws JSONException if there is an error in parsing JSON data.
     */
    @Test(priority = 8, groups = {"negative", "regression"},enabled = true)
    public void TC_008_Verify_Login_Without_Entering_Project() {
        JSONObject user8 = gettestdata("Login","User8");
        performLogin(user8);
        Assertion(user8.getString("expectedErrorMessage"), "Please Enter project Error message is Not showing");
    }

    /**
     * Test case to verify login without entering mobile number.
     *
     * @throws JSONException if there is an error in parsing JSON data.
     *
     */
    @Test(priority = 9, groups = {"negative", "regression"},enabled = true)
    public void TC_009_Verify_Login_Without_Entering_Mobile_Number() {
        JSONObject user9 = gettestdata("Login","User9");
        performLogin(user9);
        Assertion(user9.getString("expectedErrorMessage"), "Please Enter mobilenumber Error message is Not showing");
    }

    @Test(priority = 10, groups = {"regression"},enabled = true)
    public void TC_010_Verify_UsernameTextboxCharacterLimit() {
        JSONObject user10 = gettestdata("Login", "User10");
        validateMaxTextLimitInUsernameTextbox(user10.getString("username"), user10.getString("Limit"));

    }

    @Test(priority = 11, groups = {"regression"},enabled = true)
    public void TC_011_Verify_UsernameTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInUsernameTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 12, groups = {"regression"},enabled = true)
    public void TC_012_Verify_UsernameTextboxAcceptsNumbers() {
        validateNumbersInUsernameTextbox(generateRandomNumber());
    }

    @Test(priority = 13, groups = {"regression"},enabled = true)
    public void TC_013_Verify_PasswordTextboxCharacterLimit() {
        JSONObject user13 = gettestdata("Login", "User13");
        validateMaxTextLimitInPasswordTextbox(user13.getString("password"),user13.getString("Limit"));
    }

    @Test(priority = 14, groups = {"regression"},enabled = true)
    public void TC_014_Verify_ProjectTextboxCharacterLimit() {
        JSONObject user14 = gettestdata("Login", "User14");
        validateMaxTextLimitInProjectTextbox( user14.getString("project"), user14.getString("Limit"));
    }

    @Test(priority = 15, groups = {"regression"},enabled = true)
    public void TC_015_Verify_ProjectTextboxAcceptsSpecialCharacters() {
        validateSpecialcharInProjectTextbox(generateRandomSpecialCharacters(10));
    }

    @Test(priority = 16, groups = {"regression"},enabled = true)
    public void TC_016_Verify_ProjectTextboxAcceptsNumbers() {
        validateNumbersInProjectTextbox(generateRandomNumber());
    }

    @Test(priority = 17, groups = {"regression"},enabled = true)
    public void TC_017_Verify_MobileNumberTextboxCharacterLimit() {
        JSONObject user15 = gettestdata("Login", "User15");
        validateMaxTextLimitInMobilenoTextbox(user15.getString("Mobileno"), user15.getString("Limit"));
    }

    private void performLogin(JSONObject user)  {
        Login_Module.performLogin(user.getString("username"), user.getString("password"), user.getString("project"), user.getString("mobileno"));
    }
}
