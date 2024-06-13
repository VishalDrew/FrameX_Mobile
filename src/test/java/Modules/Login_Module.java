package Modules;

import Base.TestSetup;
import Base.FrameX_Listeners;
import org.apache.log4j.Logger;
import org.testng.Assert;

import static Pages.Login_Page.*;
import static Utilities.Actions.*;

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
	public static void performLogin(String Username, String Password, String Project, String Mobileno)  {
		try {
			enter("Xpath", username, Username);
			enter("Xpath", password, Password);
			enter("Xpath", project, Project);
			driver.hideKeyboard();
			enter("Xpath", Mobiileno, Mobileno);
			if(driver.isKeyboardShown()){
				driver.hideKeyboard();
			}
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

	private static void validateMaxTextLimit(String fieldType, String fieldXpath, String inputText, String limit) {
		boolean isLimitEnforced = validateTextLimit("Xpath", fieldXpath, inputText, limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + inputText);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + inputText);
			Assert.fail("TestCase Failed : Character limit is NOT enforced. Entered text: " + inputText);
		}
	}

	private static void validateSpecialCharacters(String fieldType, String fieldXpath, String inputText) {
		boolean isHandled = validateSpecialCharactersandNumbers("Xpath", fieldXpath, inputText);
		if (isHandled) {
			FrameX_Listeners.logAndReportSuccess("Special characters are allowed. Entered text: " + inputText);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Special characters are NOT allowed. Entered text: " + inputText);
			Assert.fail("TestCase Failed : Special characters are NOT allowed. Entered text: " + inputText);
		}
	}

	private static void validateNumbers(String fieldType, String fieldXpath, String inputText) {
		boolean isHandled = validateSpecialCharactersandNumbers("Xpath", fieldXpath, inputText);
		if (isHandled) {
			FrameX_Listeners.logAndReportSuccess("Numbers are allowed. Entered text: " + inputText);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Numbers are NOT allowed. Entered text: " + inputText);
			Assert.fail("TestCase Failed : Numbers are not allowed. Entered text: " + inputText);
		}
	}

	public static void validateMaxTextLimitInMobilenoTextbox(String testmobno, String limit) {
		if(driver.isKeyboardShown()){
			driver.hideKeyboard();
		}
		validateMaxTextLimit("Mobileno", Mobiileno, testmobno, limit);
	}

	public static void validateMaxTextLimitInUsernameTextbox(String testusername, String limit) {
		validateMaxTextLimit("Username", username, testusername, limit);
	}

	public static void validateSpecialcharInUsernameTextbox(String testusername) {
		validateSpecialCharacters("Username", username, testusername);
	}

	public static void validateNumbersInUsernameTextbox(String testusername) {
		validateNumbers("Username", username, testusername);
	}

	public static void validateMaxTextLimitInPasswordTextbox(String testpassword, String limit) {
		validateMaxTextLimit("Password", password, testpassword, limit);
	}

	public static void validateMaxTextLimitInProjectTextbox(String testproject, String limit) {
		validateMaxTextLimit("Project", project, testproject, limit);
	}

	public static void validateSpecialcharInProjectTextbox(String testproject) {
		validateSpecialCharacters("Project", project, testproject);
	}

	public static void validateNumbersInProjectTextbox(String testproject) {
		validateNumbers("Project", project, testproject);
	}


}