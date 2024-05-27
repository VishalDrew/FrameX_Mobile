package Tests;

import org.json.JSONObject;
import org.testng.annotations.Test;

import static Modules.Resourcentre_Module.validateFiles;
import static Pages.HomePage_page.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;

public class ResourceCentreTest {

    /**
     * This method is used to verify the resource centre files.
     *
     * @throws Exception if an error occurs during the execution of the method.
     *
     */
    @Test(priority = 1, groups = {"smoke", "regression"},enabled = true)
    public static void TC_001_Verify_ResourceCenter_Module_Displayed() throws Exception {
        verifyModuleDisplayedStatus(ResourceCentre);
    }

    /**
     * This method is used to verify if a PPTX file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     *
     *
     * @returns void.
     */
    @Test(priority = 2,dependsOnMethods = { "TC_001_Verify_ResourceCenter_Module_Displayed" } ,groups = {"smoke", "regression"},enabled = true)
    public static void TC002_VerifyPPTXFileDownloaded() throws Exception {

        JSONObject resourcecenterdata = gettestdata("ResourceCenter","validatingpptx");
        validateFiles(resourcecenterdata.getString("filename"));
    }


    /**
     * This method is used to verify if an MP4 file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     * @returns void.
     */
    @Test(priority = 3, dependsOnMethods = { "TC_001_Verify_ResourceCenter_Module_Displayed" } ,groups = {"smoke", "regression"},enabled = true)
    public static void TC003_VerifyMP4FileDownloaded() throws Exception {

        JSONObject resourcecenterdata = gettestdata("ResourceCenter","validatingmp4");
        validateFiles(resourcecenterdata.getString("filename"));

    }


    /**
     * This method is used to verify if a PDF file is downloaded successfully.
     *
     * @throws Exception if there is an error while validating the PDF file.
     *
     * @returns void.
     */
    @Test(priority = 4,dependsOnMethods = { "TC_001_Verify_ResourceCenter_Module_Displayed" } , groups = {"smoke", "regression"},enabled = true)
    public static void TC004_VerifyPDFFileDownloaded() throws Exception {

        JSONObject resourcecenterdata = gettestdata("ResourceCenter","validatingpdf");
        validateFiles(resourcecenterdata.getString("filename"));

    }



    /**
     * This method is used to verify if a DOCX file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     * @returns void.
     */
    @Test(priority = 5, dependsOnMethods = { "TC_001_Verify_ResourceCenter_Module_Displayed" } ,groups = {"smoke", "regression"},enabled = true)
    public static void TC005_VerifyDOCXFileDownloaded() throws Exception {

        JSONObject resourcecenterdata = gettestdata("ResourceCenter","validatingdocx");
        validateFiles(resourcecenterdata.getString("filename"));

    }

    /**
     * This method is used to verify the download of a JPG file.
     *
     * @throws Exception if an error occurs during the execution of the test case
     * @returns void
     */
    @Test(priority = 6,dependsOnMethods = { "TC_001_Verify_ResourceCenter_Module_Displayed" } , groups = {"smoke", "regression"},enabled = true)
    public static void TC006_VerifyJPGFileDownloaded() throws Exception {

        JSONObject resourcecenterdata = gettestdata("ResourceCenter","validatingjpg");
        validateFiles(resourcecenterdata.getString("filename"));

    }


    /**
     * This method is used to verify if an XLSX file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     * @returns void.
     */
    @Test(priority = 7,dependsOnMethods = { "TC_001_Verify_ResourceCenter_Module_Displayed" } , groups = {"smoke", "regression"},enabled = true)
    public static void TC007_VerifyXLSXFileDownloaded() throws Exception {

        JSONObject resourcecenterdata = gettestdata("ResourceCenter","validatingxlsx");
        validateFiles(resourcecenterdata.getString("filename"));

    }


}
