package Modules;

import Base.TestSetup;

import java.util.List;

import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Pages.CallPlan_page.*;
import static Pages.HomePage_page.Callplan;
import static Utilities.Actions.*;
import static Utilities.Constants.Categorymasterquerygenerator;
import static Utilities.Constants.formmasterquerygenerator;
import static Utilities.DBConfig.getColumnNamesFromDatabase;
import static Utilities.DBConfig.getdatafromdatabase;
import static Utilities.Utils.*;

public class Callplan_Module extends TestSetup {

    public static String currenttarget;
    private static  String formcondition;


    public static boolean validateUploadCall() throws Exception {

        applogin(Callplan);
        navigateto(Callplan);
        for (String storeID : targets) {
            currenttarget = storeID;
            List<String> categories = getColumnNamesFromDatabase(Categorymasterquerygenerator(storeID), "name");
            String targetXPath = "//android.view.View[contains(@content-desc, 'Target ID: " + storeID + "')]";
            if (sourceExists(storeID)) {
                click("Xpath", targetXPath);
                String startTime = datevisitedvalidation();
                click("Xpath", Startworkbutton);
                Thread.sleep(2000);
                if (sourceExists("Take Photo")) {
                    pssshopfrontimage();
                }
                webdriverWait("ACCESSIBILITYID", UploadcallButton, 15);

                //Category process Starting
                for(String category : categories){
                    String catXpath = generatecategorylocator(category);
                    click("Xpath", catXpath);

                    //Form process Starting
                    String formquery = formmasterquerygenerator(storeID,category);
                    List<String> forms = getColumnNamesFromDatabase(formquery, "formName");
                    formcondition = getdatafromdatabase(formquery,"condition");
                    for(String form : forms){
                        click("ACCESSIBILITYID", form);

                        //product process Starting
                        List<String> products = getColumnNamesFromDatabase(formmasterquerygenerator(storeID,category), "formName");






                    }




                }







            } else {
                logAndReportFailure("Target ID " + storeID + " is not Displayed ");
            }
        }
        return false;
    }







    private static String datevisitedvalidation(){
        String devicetime = datevisitedtime();
        if(sourceExists(devicetime)){
            log.info("Target start time  :  "+devicetime);
            log.info("Visited date and time is Showing");
        }else{
            logAndReportFailure("Visited date and time is not Showing");
        }
        return devicetime;
    }


    private static void pssshopfrontimage() throws InterruptedException {

        log.info("Starting PSS Shop front image capture process");
        webdriverWait("Xpath", Shutterbutton, 4);
        click("Xpath", Shutterbutton);
        webdriverWait("ACCESSIBILITYID", "Done", 3);
        click("ACCESSIBILITYID", "Done");
        log.info("PSS Shop front image capture process completed successfully");

    }

}

