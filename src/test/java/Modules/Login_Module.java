package Modules;

import Base.TestSetup;
import static Pages.Login_Page.*;
import static Utilities.Actions.*;
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

	/**
	 * Validates if the specified application version is displayed.
	 *
	 * @param versionToCheck The version of the application to verify.
	 * @return True if the specified version is displayed, false otherwise.
	 */
	public static boolean checkVersion(String versionToCheck) {
		try {
			webdriverWait("Xpath", username, 15);
			boolean isVersionDisplayed = sourceExists(versionToCheck);
			if (isVersionDisplayed) {
				log.info("App version is matched");
				return true;
			} else {
				log.error("App version is not matched: " + versionToCheck);
				tearDownApp();
				return false;
			}
		} catch (Exception e) {
			log.error("Error occurred during version check:", e);
			return false;
		}
	}
}