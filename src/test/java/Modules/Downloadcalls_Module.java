package Modules;

import Base.TestSetup;
import Listeners.FrameX_Listeners;
import org.testng.Assert;

import java.util.List;

import static Listeners.FrameX_Listeners.*;
import static Pages.Downloadcalls_page.*;
import static Pages.HomePage_page.*;
import static Pages.Login_Page.project;
import static Utilities.Actions.*;
import static Utilities.Utils.*;

/**
 * Downloadcalls_Module class.
 */
public class Downloadcalls_Module extends TestSetup {

	private static boolean duplicateCallsTest;
	public static boolean storesForVisitorLogin;

	/**
	 * Validates the download calls functionality.
	 *
	 * @throws Exception if an error occurs while verifying calls download functionality.
	 */
	public static void validateDownloadCalls()  {

		try {
			if (sourceExists(DownloadCalls)) {
				click("ACCESSIBILITYID", DownloadCalls);
			}
			List<String> unplannedCalls = targets;
			boolean isAllDownloaded = true;
			for (String upcalls : unplannedCalls) {
				click("ACCESSIBILITYID", addtarget);
				enter("classname", targetidtxtbox, upcalls);
				click("ACCESSIBILITYID", addbtn);
			}
			click("ACCESSIBILITYID", submit);
			for (String upcalls : unplannedCalls) {
				if (waitForMessage("Downloaded Successfully for target " + upcalls)) {
					logAndinfo("Downloaded Successfully for target " + upcalls);
				} else {
					isAllDownloaded = false;
				}
			}
			if (isAllDownloaded) {
				duplicateCallsTest = true;
				storesForVisitorLogin = true;
				logAndReportSuccess("TestCase Passed : All targets downloaded successfully.");
				Assert.assertTrue(true);
			} else {
				logAndReportFailure("TestCase Failed : Some targets failed to download.");
				Assert.assertTrue(false,"TestCase Failed : Some targets failed to download");
			}
		} catch (Exception e) {
			logAndReportFailure("TestCase Failed : Failed to verify calls download functionality: " + e.getMessage());
			Assert.fail("TestCase Failed : Failed to verify calls download functionality: " + e.getMessage());
		}
	}

	/**
	 * Validates if a duplicate target is being downloaded.
	 *
	 * @throws Exception if there is an error while verifying the duplicate call download.
	 */
	public static void validateDuplicateTarget() throws Exception {

		String orginalcall = targets.get(0);
		String duplicatecall = orginalcall;

		try {
			String alreadyDownloadedMsg = "Info&#10;Store " + duplicatecall + " already downloaded.";
			if (sourceExists(DownloadCalls)) {
				click("ACCESSIBILITYID", DownloadCalls);
			}
			boolean orginalcallExists = false;
			if(!duplicateCallsTest){
				click("ACCESSIBILITYID", addtarget);
				enter("classname", targetidtxtbox,orginalcall);
				click("ACCESSIBILITYID", addbtn);
				click("ACCESSIBILITYID", submit);
				waitForMessage("Downloaded Successfully for target " + orginalcall);
			}

			if (sourceExists(DownloadCalls)) {
				click("ACCESSIBILITYID", DownloadCalls);
			}
			click("ACCESSIBILITYID", addtarget);
			enter("classname", targetidtxtbox,duplicatecall);
			click("ACCESSIBILITYID", addbtn);
			click("ACCESSIBILITYID", submit);


			Thread.sleep(1000);
			if(sourceExists(alreadyDownloadedMsg)){
				logAndReportSuccess("TestCase Passed : "+alreadyDownloadedMsg+"  is Showing");
				Assert.assertTrue(true);
			}else{
				logAndReportFailure("TestCase Failed : "+alreadyDownloadedMsg+"  is not Showing");
				Assert.assertTrue(false);
			}
		} catch (Exception e) {
			logAndReportFailure("TestCase Failed : Failed to verify duplicate call download: " + e.getMessage());
			Assert.fail("TestCase Failed : Failed to verify duplicate call download: " + e.getMessage());
		}
	}

	/**
	 * Validates if the given target ID is invalid.
	 *
	 * @param trgid the target ID to be validated
	 * @throws Exception if an error occurs during the validation process
	 */
	public static void validateInvalidTarget(String trgid) throws Exception {

		String invalididmsg = "Target ID "+trgid+" is invalid. Please enter a valid Target Id and try again.";

		try {
			if (sourceExists(DownloadCalls)) {
				click("ACCESSIBILITYID", DownloadCalls);
			}
			click("ACCESSIBILITYID", addtarget);
			enter("classname", targetidtxtbox, trgid);
			click("ACCESSIBILITYID", addbtn);
			click("ACCESSIBILITYID", submit);
			if(waitForMessage(invalididmsg)){
				logAndReportSuccess("TestCase Passed : "+invalididmsg+"  is Showing");
				Assert.assertTrue(true);
			}else{
				logAndReportFailure("TestCase Failed : "+invalididmsg+"  is not Showing");
				Assert.assertTrue(false);
			}
		} catch (Exception e) {
			logAndReportFailure("TestCase Failed : Failed to verify invalid target download: " + e.getMessage());
			Assert.fail("TestCase Failed : Failed to verify invalid target download: " + e.getMessage());
		}
	}

	/**
	 * Validates the functionality of the remove button.
	 *
	 * @throws Exception if an error occurs while verifying the remove button functionality.
	 */
	public static void validateremovebutton() throws Exception {

		String target = "31233";

		try {
			if (sourceExists(DownloadCalls)) {
				click("ACCESSIBILITYID", DownloadCalls);
			}
			click("ACCESSIBILITYID", addtarget);
			enter("classname", targetidtxtbox,target );
			click("ACCESSIBILITYID", addbtn);
			click("ACCESSIBILITYID", target);
			click("ACCESSIBILITYID", "Yes");
			if(sourceExists(target)){
				logAndReportFailure("TestCase Failed : "+target+"  is Showing, Remove Target is not working");
				Assert.assertTrue(false);
			}else{
				logAndinfo("TestCase Passed : "+target+"  is not  Showing, Remove Target is Working");
				Assert.assertTrue(true);
			}

		} catch (Exception e) {
			logAndReportFailure("TestCase Failed : An error occurred while verifying remove button functionality: " + e.getMessage());
			Assert.fail("TestCase Failed :  An error occurred while verifying remove button functionality: " + e.getMessage());
		}
	}

	/**
	 * Validates the submit button and checks if the error message is displayed.
	 *
	 * @param errmsg The error message to be checked.
	 * @throws Exception If an error occurs while verifying the submit button without adding target.
	 */
	public static void validatesubmitbutton(String errmsg) throws Exception {
		try {
			if (sourceExists(DownloadCalls)) {
				click("ACCESSIBILITYID", DownloadCalls);
			}
			click("ACCESSIBILITYID", submit);
			waitForMessage(errmsg);
			if(sourceExists(errmsg)){
				logAndReportSuccess("TestCase Passed : Error message : "+errmsg+"  is Showing");
				Assert.assertTrue(true);
			}else{
				logAndReportSuccess("TestCase Failed :  Error message : "+errmsg+" is Not Showing");
				Assert.assertTrue(false);
			}
		} catch (Exception e) {
			logAndReportFailure("TestCase Failed : An error occurred while verifying submit button without adding target: " + e.getMessage());
			Assert.fail("TestCase Failed : An error occurred while verifying submit button without adding target: " + e.getMessage());
		}
	}

	public static void validateMaxTextLimitIntargetIDTextbox(String testtargetid,String limit){
		if (sourceExists(DownloadCalls)) {
			click("ACCESSIBILITYID", DownloadCalls);
		}
		click("ACCESSIBILITYID", addtarget);
		boolean isLimitEnforced = checkTextboxCharacterLimit("classname",targetidtxtbox,testtargetid,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + testtargetid);
			click("ACCESSIBILITYID",cancelbtn);
			driver.navigate().back();
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + testtargetid);
			click("ACCESSIBILITYID",cancelbtn);
			driver.navigate().back();
			Assert.assertTrue(false);
		}

	}

}
