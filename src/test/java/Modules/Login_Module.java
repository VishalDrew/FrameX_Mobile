package Modules;

import Base.TestSetup;
import Listeners.FrameX_Listeners;
import org.json.JSONObject;
import org.testng.Assert;

import static Pages.Login_Page.*;
import static Utilities.Actions.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.sourceExists;

/**
 * Login_Module class.
 */
public class Login_Module extends TestSetup {

	/**
	 * Logs in the user with the provided credentials and project information.
	 *
	 * @param Username the username of the user
	 * @param Password the password of the user
	 * @param Project the project information
	 * @param Mobileno the mobile number of the user
	 *
	 * @throws Exception if an error occurs during the login process
	 */
	public static void login(String Username,String Password,String Project,String Mobileno)  {
		try {
			enter("Xpath", username, Username);
			enter("Xpath", password, Password);
			enter("Xpath", project, Project);
			driver.hideKeyboard();
			enter("Xpath", Mobiileno, Mobileno);
			click("ACCESSIBILITYID", LoginButton);

			log.info("Username: " + Username);
			log.info("Password: " + Password);
			log.info("Project: " + Project);
			log.info("Mobile number: " + Mobileno);
		} catch (Exception e) {
			log.error("Error occurred during login process:", e);
			throw new RuntimeException("Error occurred during login process", e);
		}
	}

	/**
	 * Logs out the user from the application.
	 *
	 * @throws Exception if there is an error during the logout process.
	 */
	public static void logout() {
		try {
			click("Xpath", menubutton);
			click("ACCESSIBILITYID", Logoutbutton);
			click("ACCESSIBILITYID", yes);
		} catch (Exception e) {
			log.error("Error occurred during logout process:", e);
			throw new RuntimeException("Error occurred during logout process", e);
		}
	}

	public static void validateMaxTextLimitInUsernameTextbox(String testusername,String limit){
		boolean isLimitEnforced = checkTextboxCharacterLimit("Xpath",username,testusername,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + testusername);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + testusername);
			Assert.assertTrue(false);
		}
	}

	public static void validateSpecialcharInUsernameTextbox(String testusername){
		boolean ishandlespecialchar = verifyTextboxHandlesSpecialCharactersandNumbers("Xpath",username,testusername);
		if (ishandlespecialchar) {
			FrameX_Listeners.logAndReportSuccess("Special characters are allowed. Entered text: " + testusername);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Special characters are NOT allowed. Entered text: " + testusername);
			Assert.assertTrue(false);
		}
	}

	public static void validateNumbersInUsernameTextbox(String testusername){
		String testuser = String.valueOf(testusername);
		boolean ishandlenumber = verifyTextboxHandlesSpecialCharactersandNumbers("Xpath",username,testuser);
		if (ishandlenumber) {
			FrameX_Listeners.logAndReportSuccess("Numbers are allowed. Entered text: " + testuser);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Numbers are NOT allowed. Entered text: " + testuser);
			Assert.assertTrue(false);
		}
	}

	public static void validateMaxTextLimitInPasswordTextbox(String testpassword,String limit){
		boolean isLimitEnforced = checkTextboxCharacterLimit("Xpath",password,testpassword,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + testpassword);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + testpassword);
			Assert.assertTrue(false);
		}
	}

	public static void validateMaxTextLimitInProjectTextbox(String testproject,String limit){

		boolean isLimitEnforced = checkTextboxCharacterLimit("Xpath",project,testproject,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + testproject);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + testproject);
			Assert.assertTrue(false);
		}
	}

	public static void validateSpecialcharInProjectTextbox(String testproject){
		boolean ishandlespecialchar = verifyTextboxHandlesSpecialCharactersandNumbers("Xpath",project,testproject);
		if (ishandlespecialchar) {
			FrameX_Listeners.logAndReportSuccess("Special characters are allowed. Entered text: " + testproject);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Special characters are NOT allowed. Entered text: " + testproject);
			Assert.assertTrue(false);
		}
	}

	public static void validateNumbersInProjectTextbox(String testproject){
		boolean ishandlenumber = verifyTextboxHandlesSpecialCharactersandNumbers("Xpath",project,testproject);
		if (ishandlenumber) {
			FrameX_Listeners.logAndReportSuccess("Numbers are allowed. Entered text: " + testproject);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Numbers are NOT allowed. Entered text: " + testproject);
			Assert.assertTrue(false);
		}
	}

	public static void validateMaxTextLimitInMobilenoTextbox(String testmobno,String limit){

		if(driver.isKeyboardShown()){
			driver.hideKeyboard();
		}
		boolean isLimitEnforced = checkTextboxCharacterLimit("Xpath",Mobiileno,testmobno,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + testmobno);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + testmobno);
			Assert.assertTrue(false);
		}
	}


}