package Pages;

import static Base.TestSetup.log;
import static Pages.HomePage_page.Callplan;
import static Pages.Login_Page.lgpage;
import static Pages.Login_Page.menubutton;
import static Utilities.Actions.*;
import static Utilities.AppUtils.*;

/**
 * CallPlan_page class.
 */
public class CallPlan_page {
	public static  final String currentdate = generateFormattedDate("yyyy-MM-dd");
	public static final String sync = "Sync";
	public static final String UploadcallButton = "Upload Call";
	public static final String backbutton = "Back";
	public static final String CloseCallButton = "Close Call";
	public static final String Startworkbutton = "//android.view.View[@content-desc='Start Work']";
	public static final String Uploadcallconfirmpopup = "Are you sure you want to upload Call ?";
	public static final String Uploadcallsbutton = "Upload Calls";
	public static final String NextButton = "Next";
	public static final String Donebutton = "Done";
	public static final String Shutterbutton = "//android.view.View[3]";
	public static final String Perfectstorescorepopup = "//android.view.View[@content-desc='Perfect Store Score']";
	public static final String Okbutton = "ok";
	public static final String Yesbutton = "Yes";
	public static final String Camerabutton_M = "//android.view.View[@content-desc='Photo *']/android.view.View[2]";
	public static final String Camerabutton_NM= "//android.view.View[@content-desc='Photo']/android.view.View[2]";


	public static final  String generatecategorylocator(String cate) {
		String categorylocator = "//android.view.View[contains(@content-desc, '"+cate+"')]";
		return categorylocator;
	}

	public static final String generateproductlocator(String prodname) {
		String productnamelocator = "//android.view.View[@content-desc=\"" + prodname + "\"]";
		return productnamelocator;
	}

	public static final String generatetextfieldlocator(String field) {
		String FormslistsLocator = " //*[@hint='"+field+"']";
		return FormslistsLocator;
	}

	public static final String gettargetxpath(String trgid) {
		String targetXpath = "//android.view.View[contains(@content-desc, 'Target ID: "+trgid+"')]";
		return targetXpath;
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



}
