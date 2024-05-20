package Pages;

import org.testng.Assert;

import static Base.TestSetup.driver;
import static Base.TestSetup.log;
import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Listeners.FrameX_Listeners.logAndReportSuccess;
import static Modules.Downloadcalls_Module.storesForVisitorLogin;
import static Modules.VisitorLogin_Module.downloadstores;
import static Pages.HomePage_page.Callplan;
import static Pages.HomePage_page.DownloadCalls;
import static Pages.Login_Page.menubutton;
import static Utilities.Actions.*;
import static Utilities.Utils.*;

/**
 * CallPlan_page class.
 */
public class CallPlan_page {
	public static  String currentdate = generateFormattedDate("yyyy-MM-dd");
	public static String sync = "Sync";
	public static String targetdownloadnotification = "//*[@text='Downloaded Successfully for target " + 38356+"']";
	public static String TodayCalls = "//android.view.View[contains(@content-desc, 'Today Calls')]";
	public static String fiveminituesSyncmsg = "You can click after 5 minutes, since it is in Downloading or Uploading Call process.";
	public static String startworkvalidation = "You cannot view this target because another target  38353 is already in process. Please complete it first";
	public static String formcompleted = "Store Front Photo Completed";
	public static String UploadcallButton = "Upload Call";
	public static String backbutton = "Back";
	public static String CloseCallButton = "Close Call";
	//public static String targetUploadnotification =  "//*[@text='Uploading process for "+tid+" is Completed']";  ////android.view.View[@content-desc="You have already uploaded 14906 target"]
	public static String Startworkbutton = "//android.view.View[@content-desc='Start Work']";
	public static String samplephotobtn = "Sample Photo";
	public static String samplephotonotavailable_msg = "Sample Photo not available";
	public static String imagemandatory_msg = "Please, Take  Photo";
	public static String fieldsmandatory_msg = "Fields listed below are Required and cannot be left blank";
	public static String categorycompleted_msg = "Store Front Photo Completed";
	public static String mandatorydropdown_msg = "Enter DropDownList";
	public static String Uploadcallconfirmpopup = "Are you sure you want to upload Call ?";
	public static String Viewimagesbutton = "View Images";
	public static String Uploadcallsbutton = "Upload Calls";
	public static String uploadsuccessActivitylog_msg = "Target 38353 successfully uploaded";
	public static String stattinguploadprocessActivitylog_msg = "Starting the Upload process for Target . 38353 08/01/24 19:34:23";
	public static String sospopup_msg = "Industry Facing should be greater than Our Brand Facing";
	public static String enterourbrandfacing_msg = "Enter Our Brand Facing";
	public static String enterIndustryfacing_msg = "Enter Industry Facing";
	public static String NextButton = "Next";
	public static String Donebutton = "Done";
	public static String Camerabutton = "//android.view.View[@content-desc='Photo']/android.view.View[3]";
	public static String Shutterbutton = "//android.view.View[3]";
	public static String Perfectstorescorepopup = "//android.view.View[@content-desc='Perfect Store Score']";
	public static String Okbutton = "ok";
	public static String psssuccs = "Pss Upload Success";
	public static String Yesbutton = "Yes";
	public static String Nobutton = "No";
	public static String Attendancepopup = "Please also mark your Attendance for today!";
	public static String uploadcall_errmsg = "First fill all categories data to upload";
	public static String Underprocesspopup = "Attendance is under process  please Proceed to next step ?";
	public static String Camerabutton_M = "//android.view.View[@content-desc='Photo *']/android.view.View[2]";
	public static String Camerabutton_NM= "//android.view.View[@content-desc='Photo']/android.view.View[2]";

	public static String uploadcallerr_msg= "Internal server error, upload could not completed. Contact support 500";


	public static String generatecategorylocator(String cate) {
		String categorylocator = "//android.view.View[contains(@content-desc, '"+cate+"')]";
		log.info("Generated category locator for category '" + cate + "': " + categorylocator);
		return categorylocator;
	}

	public static String generateproductlocator(String prodname) {
		String productnamelocator = "//android.view.View[@content-desc=\"" + prodname + "\"]";
		log.info("Generated product locator for product '" + prodname + "': " + productnamelocator);
		return productnamelocator;
	}

	public static String generatetextfieldlocator(String field) {
		String FormslistsLocator = " //*[@hint='"+field+"']";
		log.info("Generated text field locator for field '" + field + "': " + FormslistsLocator);
		return FormslistsLocator;
	}

	public static String gettargetxpath(String trgid) {
		String targetXpath = "//android.view.View[contains(@content-desc, 'Target ID: "+trgid+"')]";
		log.info("Generated target XPath for target ID '" + trgid + "': " + targetXpath);
		return targetXpath;
	}

	public static void sync() {
		if(!sourceExists(sync)){
			click("ACCESSIBILITYID",Callplan);
			log.info("Clicked on Call Plan");
		}
		click("ACCESSIBILITYID",sync);
		log.info("Clicked on Sync");
	}

	public static void fiveminssync(String expected) {
		sync(); // Perform sync
		if(sourceExists(expected)) {
			click("ACCESSIBILITYID","Ok");
			logAndReportSuccess("TestCase Passed : 5 Minutes Sync is working");
			Assert.assertTrue(true);
		} else {
			logAndReportFailure("TestCase Failed : 5 Minutes Sync is not working");
			Assert.assertTrue(false,"TestCase Failed : 5 Minutes Sync is not working");
		}
	}

	public static void navigateToCallplanPage() {
		if(sourceExists("Username")) {
			lgpage();
		}
		if (!sourceExists(Callplan)) {
			if(isElementDisplayed("xpath",menubutton)) {
				click("Xpath", menubutton);
			} else {
				lgpage();
				click("ACCESSIBILITYID", Callplan);
			}
			log.info("Navigated to Call Plan page");
		}
	}

	public static void formcompletingvalidation(String errmsg) throws Exception {
		if(!sourceExists((targets.get(0)))){
			if (!storesForVisitorLogin == true) {
				navigateto(DownloadCalls);
				downloadstores();
			}
		}
		navigateToCallplanPage();
		click("ACCESSIBILITYID",Callplan);
		click("xpath",gettargetxpath(targets.get(0)));
		click("Xpath", Startworkbutton);
		shopfrontphotorequired ();
		webdriverWait("ACCESSIBILITYID", UploadcallButton, 20);
		click("ACCESSIBILITYID",UploadcallButton);
		if(sourceExists(errmsg)){
			logAndReportSuccess("TestCase Passed : First fill all categories data to upload is Displayed");
			Assert.assertTrue(true);
		} else {
			logAndReportFailure("TestCase Failed : First fill all categories data to upload is Not Displayed");
			Assert.assertTrue(false);
		}
	}

}
