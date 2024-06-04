package Tests;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Step;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Modules.Resourcentre_Module.validateFiles;
import static Pages.HomePage_page.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;

public class ResourceCentreTest {

    /**
     * This method is used to verify the Resource Center module is displayed.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Test(priority = 1, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify the Resource Center module is displayed.")
    @Step("Check if the Resource Center module is displayed.")
    @Severity(SeverityLevel.BLOCKER)
    public static void TC_001_Verify_ResourceCenter_Module_Displayed() throws Exception {
        verifyModuleDisplayedStatus(ResourceCentre);
    }

    /**
     * This method is used to verify if a PPTX file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Test(priority = 2, dependsOnMethods = {"TC_001_Verify_ResourceCenter_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify if a PPTX file is downloaded successfully from the Resource Center.")
    @Step("Download a PPTX file and verify the download process.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC002_VerifyPPTXFileDownloaded() throws Exception {
        JSONObject resourcecenterdata = gettestdata("ResourceCenter", "validatingpptx");
        Assert.assertTrue(validateFiles(resourcecenterdata.getString("filename")));
    }

    /**
     * This method is used to verify if an MP4 file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Test(priority = 3, dependsOnMethods = {"TC_001_Verify_ResourceCenter_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify if an MP4 file is downloaded successfully from the Resource Center.")
    @Step("Download an MP4 file and verify the download process.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC003_VerifyMP4FileDownloaded() throws Exception {
        JSONObject resourcecenterdata = gettestdata("ResourceCenter", "validatingmp4");
        Assert.assertTrue(validateFiles(resourcecenterdata.getString("filename")));

    }

    /**
     * This method is used to verify if a PDF file is downloaded successfully.
     *
     * @throws Exception if there is an error while validating the PDF file.
     */
    @Test(priority = 4, dependsOnMethods = {"TC_001_Verify_ResourceCenter_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify if a PDF file is downloaded successfully from the Resource Center.")
    @Step("Download a PDF file and verify the download process.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC004_VerifyPDFFileDownloaded() throws Exception {
        JSONObject resourcecenterdata = gettestdata("ResourceCenter", "validatingpdf");
        Assert.assertTrue(validateFiles(resourcecenterdata.getString("filename")));
    }

    /**
     * This method is used to verify if a DOCX file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Test(priority = 5, dependsOnMethods = {"TC_001_Verify_ResourceCenter_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify if a DOCX file is downloaded successfully from the Resource Center.")
    @Step("Download a DOCX file and verify the download process.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC005_VerifyDOCXFileDownloaded() throws Exception {
        JSONObject resourcecenterdata = gettestdata("ResourceCenter", "validatingdocx");
        Assert.assertTrue(validateFiles(resourcecenterdata.getString("filename")));

    }

    /**
     * This method is used to verify the download of a JPG file.
     *
     * @throws Exception if an error occurs during the execution of the test case.
     */
    @Test(priority = 6, dependsOnMethods = {"TC_001_Verify_ResourceCenter_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify the download of a JPG file from the Resource Center.")
    @Step("Download a JPG file and verify the download process.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC006_VerifyJPGFileDownloaded() throws Exception {
        JSONObject resourcecenterdata = gettestdata("ResourceCenter", "validatingjpg");
        Assert.assertTrue(validateFiles(resourcecenterdata.getString("filename")));
    }

    /**
     * This method is used to verify if an XLSX file is downloaded successfully.
     *
     * @throws Exception if an error occurs during the execution of the method.
     */
    @Test(priority = 7, dependsOnMethods = {"TC_001_Verify_ResourceCenter_Module_Displayed"}, groups = {"smoke", "regression"}, enabled = true)
    @Description("Verify if an XLSX file is downloaded successfully from the Resource Center.")
    @Step("Download an XLSX file and verify the download process.")
    @Severity(SeverityLevel.NORMAL)
    public static void TC007_VerifyXLSXFileDownloaded() throws Exception {
        JSONObject resourcecenterdata = gettestdata("ResourceCenter", "validatingxlsx");
        Assert.assertTrue(validateFiles(resourcecenterdata.getString("filename")));
    }

}
