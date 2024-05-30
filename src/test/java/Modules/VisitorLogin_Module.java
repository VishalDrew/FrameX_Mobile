package Modules;

import Listeners.FrameX_Listeners;
import Utilities.AppUtils;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.ArrayList;

import static Listeners.FrameX_Listeners.*;
import static Modules.Downloadcalls_Module.storesForVisitorLogin;
import static Pages.Downloadcalls_page.*;
import static Pages.HomePage_page.DownloadCalls;
import static Pages.HomePage_page.visitorlogin;
import static Pages.Visitorlogin_page.*;
import static Utilities.Actions.*;
import static Utilities.DatabaseUtility.getdatafromdatabase;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;
import static io.appium.java_client.touch.offset.PointOption.point;

/**
 * VisitorLogin_Module class.
 */
public class VisitorLogin_Module {

	private static JSONObject visitorlogindata = gettestdata("Visitor Login", "Visitor Upload");
	private static JSONObject visitorLoginNegativeData = gettestdata("Visitor Login", "Visitor Upload errmsg");
	private static String targetid = targets.get(0);
	private static String storename;

	static {
		try {
			storename = getStoreName(targetid) + "/" + targetid;
		} catch (Exception e) {
			throw new RuntimeException("Error initializing store name", e);
		}
	}


	/**
     * Validates the visitor upload process.
     * 
     * @throws Exception if an error occurs during the validation process.
     */
    public static void validatevisitorUpload() throws Exception {
		if (!storesForVisitorLogin == true) {
			navigateto(DownloadCalls);
			downloadstores();
		}

		navigateto(visitorlogin);
		if (sourceExists(visitorlogin)) {
			click("ACCESSIBILITYID", visitorlogin);
		}
		fillvisitordetails();
		if (sourceExists(visitorlogindata.getString("expected message"))) {
			click("ACCESSIBILITYID", visitorlogin);
			click("ACCESSIBILITYID", selectvisitordd);
			if(sourceExists("Visitor" + visitorlogindata.getString("Visitor"))){
				logAndReportSuccess("TestCase Failed : Visitor Upload Failed");
				Assert.fail();
			}else {
				logAndReportSuccess("TestCase Passed : "+visitorlogindata.getString("expected message"));
				driver.navigate().back();
				Assert.assertTrue(true);
			}
		}
	}



	/**
     * Validates the error fields in the visitor login page.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public static void validateErrorFields() throws InterruptedException {

		navigateto(visitorlogin);
		click("ACCESSIBILITYID", selectvisitordd);
		click("ACCESSIBILITYID", "Visitor" + visitorlogindata.getString("Visitor"));
		scrollE2E("up");
		click("ACCESSIBILITYID", submit_btn);
		scrollE2E("down");

		boolean allErrorsDisplayed = true;
		for (String key : visitornegativedata()) {
			String value = visitorLoginNegativeData.getString(key);
			boolean isDisplayed = sourceExists(value);
			if (!isDisplayed) {
				scroll("up");
				isDisplayed = sourceExists(value);
			}
			if (!isDisplayed) {
				allErrorsDisplayed = false;
				logAndinfo("TestCase Failed : "+value + " is not displayed");
			} else {
				log.info(value + " is displayed");
			}
		}
		if (!allErrorsDisplayed) {
			logAndReportFailure("TestCase Failed: Not all error messages are displayed in visitor login");
			scrollE2E("up");
			click("ACCESSIBILITYID", "Visitor" + visitorlogindata.getString("Visitor"));
			click("ACCESSIBILITYID", selectvisitordd);
			Assert.fail("TestCase Failed: Not all error messages are displayed in visitor login");
		} else {
			logAndReportSuccess("TestCase Passed : All error messages are displayed in visitor login");
			click("ACCESSIBILITYID", "Visitor" + visitorlogindata.getString("Visitor"));
			click("ACCESSIBILITYID", selectvisitordd);
			Assert.assertTrue(true);
		}
	}


	/**
     * Retrieves the store name based on the given store ID.
     * 
     * @param storeId the ID of the store
     * @return the name of the store
     * @throws Exception if there is an error retrieving the store name from the database
     */
     private static String getStoreName(String storeId) throws Exception {
		return getdatafromdatabase("select TargetName from TargetMaster where TargetID = " + storeId + "", "TargetName");
	}


	/**
     * Retrieves the negative data for visitor login.
     * 
     * @return An ArrayList of Strings containing the error messages for various fields in visitor login.
     * 
     * @throws JSONException if there is an error in parsing the test data.
     */
     private static ArrayList<String> visitornegativedata(){

		ArrayList<String>keys = new ArrayList<String>();
		keys.add("Visitor Type errmsg");
		keys.add("Visitor Empid errmsg");
		keys.add("Visitor Name errmsg");
		keys.add("Designation errmsg");
		keys.add("Remarks errmsg");
		keys.add("Storeinfo errmsg");
		keys.add("WellGroomed errmsg");
		keys.add("Maintained well all categories errmsg");
		keys.add("Aware about all the targets errmsg");
		keys.add("Selfie errmsg");
		return keys;
	}

	/**
	 * Downloads stores for a given target ID.
	 *
	 * @throws Exception if an error occurs during the download process.
	 */
	public static void downloadstores() throws Exception {

		click("ACCESSIBILITYID", addtarget);
		enter("classname", targetidtxtbox, targetid);
		click("ACCESSIBILITYID", addbtn);
		click("ACCESSIBILITYID", submit);
		if (waitForMessage("Downloaded Successfully for target " + targetid)) {
			logAndinfo("Downloaded Successfully for target " + targetid);
		}
	}


	/**
	 * Fills the visitor details in the Visitor Login module.
	 *
	 * @throws InterruptedException if the thread is interrupted while sleeping.
	 */
	 private static void fillvisitordetails() throws InterruptedException {
		Thread.sleep(1000);
		click("ACCESSIBILITYID", selectvisitordd);
		click("ACCESSIBILITYID", "Visitor" + visitorlogindata.getString("Visitor"));
		click("ACCESSIBILITYID", visitortype_dd);
		click("ACCESSIBILITYID", visitorlogindata.getString("Visitor Type"));
		enter("Xpath", visitorempid_txtbox, visitorlogindata.getString("Visitor Empid"));
		driver.hideKeyboard();
		enter("Xpath", visitorname_txtbox, visitorlogindata.getString("Visitor Name"));
		driver.hideKeyboard();
		enter("Xpath", designation_txtbox, visitorlogindata.getString("Designation"));
		driver.hideKeyboard();
		enter("Xpath", remarks_txtbox, visitorlogindata.getString("Remarks"));
		driver.hideKeyboard();
		 AppUtils.scroll(driver,600);
		click("ACCESSIBILITYID", storeinfo_dd);
		click("ACCESSIBILITYID", storename);
		click("ACCESSIBILITYID", promoterwellgroomed_dd);
		click("ACCESSIBILITYID", visitorlogindata.getString("WellGroomed"));
		click("ACCESSIBILITYID", maintainedwellallcat_dd);
		click("ACCESSIBILITYID", visitorlogindata.getString("Maintained well all categories"));
		click("ACCESSIBILITYID", awareallabttargts_dd);
		click("ACCESSIBILITYID", visitorlogindata.getString("Aware about all the targets"));
		AppUtils.scroll(driver,600);
		if (visitorlogindata.getString("Selfie").equalsIgnoreCase("True")) {
			click("xpath", selfie_img);
			click("id", shutter_btn);
			click("id", "com.android.camera:id/done_button");
		}
		click("ACCESSIBILITYID", submit_btn);
	}


	public static void validateMaxTextLimitInEmpIDTextbox(String Empid,String limit) throws InterruptedException {
		click("ACCESSIBILITYID", selectvisitordd);
		click("ACCESSIBILITYID", "Visitor1");
		boolean isLimitEnforced = validateTextLimit("Xpath",visitorempid_txtbox,Empid,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + Empid);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + Empid);
			Assert.fail("TestCase Failed : Character limit is not enforced. Entered text: " + Empid);
		}
	}

	public static void validateSpecialcharInEmpIDTextbox(String Empid){
		boolean ishandlespecialchar = validateSpecialCharactersandNumbers("Xpath",visitorempid_txtbox,Empid);
		if (ishandlespecialchar) {
			FrameX_Listeners.logAndReportSuccess("Special characters are allowed. Entered text: " + Empid);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Special characters are NOT allowed. Entered text: " + Empid);
			Assert.fail("TestCase Failed : Special characters are NOT allowed. Entered text: " + Empid);
		}
	}

	public static void validateNumbersInEmpIDTextbox(String Empid){
		boolean ishandlenumber = validateSpecialCharactersandNumbers("Xpath",visitorempid_txtbox,Empid);
		if (ishandlenumber) {
			FrameX_Listeners.logAndReportSuccess("Numbers are allowed. Entered text: " + Empid);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Numbers are NOT allowed. Entered text: " + Empid);
			Assert.fail("TestCase Failed : Numbers are NOT allowed. Entered text: " + Empid);
		}
	}

	public static void validateMaxTextLimitInNameTextbox(String visitorname,String limit){

		 if(driver.isKeyboardShown()){
			 driver.hideKeyboard();
		 }
		boolean isLimitEnforced = validateTextLimit("Xpath",visitorname_txtbox,visitorname,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + visitorname);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + visitorname);
			Assert.fail("TestCase Failed :  Character limit is NOT enforced. Entered text: " + visitorname);
		}
	}

	public static void validateSpecialcharInNameTextbox(String visitorname){
		boolean ishandlespecialchar = validateSpecialCharactersandNumbers("Xpath",visitorname_txtbox,visitorname);
		if (ishandlespecialchar) {
			FrameX_Listeners.logAndReportSuccess("Special characters are allowed. Entered text: " + visitorname);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Special characters are NOT allowed. Entered text: " + visitorname);
			Assert.fail("TestCase Failed : Special characters are NOT allowed. Entered text: " + visitorname);
		}
	}

	public static void validateNumbersInNameTextbox(String visitorname){
		boolean ishandlenumber = validateSpecialCharactersandNumbers("Xpath",visitorname_txtbox,visitorname);
		if (ishandlenumber) {
			FrameX_Listeners.logAndReportSuccess("Numbers are allowed. Entered text: " + visitorname);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Numbers are NOT allowed. Entered text: " + visitorname);
			Assert.fail("TestCase Failed : Numbers are NOT allowed. Entered text: " + visitorname);
		}
	}

	public static void validateMaxTextLimitInDesignationTextbox(String Designation,String limit){
		if(driver.isKeyboardShown()){
			driver.hideKeyboard();
		}
		boolean isLimitEnforced = validateTextLimit("Xpath",designation_txtbox,Designation,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + Designation);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + Designation);
			Assert.fail("TestCase Failed : Character limit is NOT enforced. Entered text: " + Designation);
		}
	}

	public static void validateSpecialcharInDesignationTextbox(String Designation){

		boolean ishandlespecialchar = validateSpecialCharactersandNumbers("Xpath",designation_txtbox,Designation);
		if (ishandlespecialchar) {
			FrameX_Listeners.logAndReportSuccess("Special characters are allowed. Entered text: " + Designation);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Special characters are NOT allowed. Entered text: " + Designation);
			Assert.fail("TestCase Failed :  Special characters are NOT allowed. Entered text: " + Designation);
		}
	}

	public static void validateNumbersInDesignationTextbox(String Designation){

		boolean ishandlenumber = validateSpecialCharactersandNumbers("Xpath",designation_txtbox,Designation);
		if (ishandlenumber) {
			FrameX_Listeners.logAndReportSuccess("Numbers are allowed. Entered text: " + Designation);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Numbers are NOT allowed. Entered text: " + Designation);
			Assert.fail("TestCase Failed : Numbers are NOT allowed. Entered text: " + Designation);
		}
	}

	public static void validateMaxTextLimitInRemarksTextbox(String remarks,String limit){
		if(driver.isKeyboardShown()){
			driver.hideKeyboard();
		}
		boolean isLimitEnforced = validateTextLimit("Xpath",remarks_txtbox,remarks,limit);
		if (isLimitEnforced) {
			FrameX_Listeners.logAndReportSuccess("Character limit is enforced. Entered text: " + remarks);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Character limit is NOT enforced. Entered text: " + remarks);
			Assert.fail("TestCase Failed :  Character limit is NOT enforced. Entered text: " + remarks);
		}
	}

	public static void validateSpecialcharInRemarksTextbox(String remarks){

		boolean ishandlespecialchar = validateSpecialCharactersandNumbers("Xpath",remarks_txtbox,remarks);
		if (ishandlespecialchar) {
			FrameX_Listeners.logAndReportSuccess("Special characters are allowed. Entered text: " + remarks);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Special characters are NOT allowed. Entered text: " + remarks);
			Assert.fail("TestCase Failed :  Special characters are NOT allowed. Entered text: " + remarks);
		}
	}

	public static void validateNumbersInRemarksTextbox(String remarks) throws InterruptedException {

		boolean ishandlenumber = validateSpecialCharactersandNumbers("Xpath",remarks_txtbox,remarks);
		if (ishandlenumber) {
			FrameX_Listeners.logAndReportSuccess("Numbers are allowed. Entered text: " + remarks);
			Assert.assertTrue(true);
		} else {
			FrameX_Listeners.logAndReportFailure("Numbers are NOT allowed. Entered text: " + remarks);
			Assert.fail("TestCase Failed : Numbers are NOT allowed. Entered text: " + remarks);
		}
		driver.hideKeyboard();
		scrollE2E("down");
		click("ACCESSIBILITYID", "Visitor1");
		click("ACCESSIBILITYID", selectvisitordd);
	}
}

