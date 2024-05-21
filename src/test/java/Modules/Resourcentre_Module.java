package Modules;

import Base.TestSetup;

import java.util.Set;

import static Listeners.FrameX_Listeners.*;
import static Pages.HomePage_page.Callplan;
import static Pages.HomePage_page.ResourceCentre;
import static Pages.Login_Page.menubutton;
import static Utilities.Actions.*;
import static Utilities.Utils.*;

/**
 * Resourcentre_Module class.
 */
public class Resourcentre_Module extends TestSetup {

    /**
     * Validates the download of a file.
     *
     * @param filename the name of the file to be validated
     * @return true if the file is downloaded successfully, false otherwise
     * @throws Exception if an exception occurs during the validation process
     */
    public static boolean validateFiles(String filename) {
        try {
            if(sourceExists(ResourceCentre)){
                click("ACCESSIBILITYID", ResourceCentre);
            }
            if(sourceExists("No Resource Center record")) {
                logAndReportFailure("No Resource Center record found");
                driver.navigate().back();
                return false;
            }
            if (isElementDisplayed("ACCESSIBILITYID", filename)) {
                click("ACCESSIBILITYID", filename);
                webdriverWait("ACCESSIBILITYID", "Refresh", 5);
                if (sourceExists("Refresh")) {
                    Thread.sleep(900);
                    logAndReportSuccess("TestCase Passed : " + filename + " file downloaded Successfully");
                    driver.navigate().back();
                    return true;
                } else {
                    logAndReportFailure("TestCase Failed : " + filename + " is not downloaded Successfully");
                    return false;
                }
            } else {
                logAndReportFailure("TestCase Failed : No record found for filename: " + filename + " in Resource center");
                return false;
            }
        } catch (Exception e) {
            logAndReportFailure("TestCase Failed : Exception occurred: " + e.getMessage());
            return false;
        }
    }


}
