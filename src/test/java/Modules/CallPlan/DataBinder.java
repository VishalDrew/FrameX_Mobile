package Modules.CallPlan;

import Utilities.AppUtils;

import java.text.MessageFormat;
import java.util.LinkedHashMap;
import java.util.List;

import static Base.TestSetup.*;
import static Modules.CallPlanModule.*;
import static Pages.CallPlan_page.*;
import static Utilities.Actions.*;
import static Utilities.Actions.webdriverWait;
import static Utilities.Constants.formmasterquerygenerator;
import static Utilities.DatabaseUtility.getColumnNamesFromDatabase;
import static Utilities.DatabaseUtility.getDataObject;
import static Utilities.AppUtils.*;

/**
 * CallPlanValidationHandler class.
 */
public class DataBinder {

    private static String Ctrltype;
    private  static String Datatype;
    private  static String enumfieldName;
    private static  String formcondition;
    public static String fieldName;
    private static String previousCtrltype ;


    /**
     * Binds data for the given categories and field type.
     *
     * @param categories the list of categories to bind data for
     * @param fieldType the type of field to bind data for
     * @return true if the data binding is successful for all categories, false otherwise
     * @throws Exception if an error occurs during the data binding process
     */
    public static boolean dataBinder(List<String> categories, String fieldType) throws Exception {
        boolean isExecutionSuccessful = true;
        for (String category : categories) {
            String modifiedCategory = SetSpecialCharacter(category);

            if (!sourceExists(modifiedCategory)) {
                AppUtils.scroll(driver, 600);

                if (!sourceExists(modifiedCategory)) {
                    isExecutionSuccessful = false;
                    break;
                }
            }
            boolean isCategoryExecutionSuccessful = categoryProcess(category, fieldType);

            if (!isCategoryExecutionSuccessful) {
                isExecutionSuccessful = false;
                break;
            }
        }

        return isExecutionSuccessful;
    }

    /**
     * Processes the given category and fieldType.
     *
     * @param category the category to be processed
     * @param fieldType the type of field
     * @return true if the category exists and is processed successfully, false otherwise
     * @throws Exception if an error occurs during processing
     */
    private static boolean categoryProcess(String category, String fieldType) throws Exception {
        String modifiedCategory = SetSpecialCharacter(category);
        if (!sourceExists(modifiedCategory)) {
            return false;
        }
        String categoryXpath = generatecategorylocator(category);
        click("Xpath", categoryXpath);
        String formQuery = formmasterquerygenerator(targetid, category);
        List<Object> formDatas = getDataObject(formQuery);
        for (Object formData : formDatas) {
            if (formData instanceof LinkedHashMap<?, ?>) {
                LinkedHashMap<?, ?> formDataMap = (LinkedHashMap<?, ?>) formData;
                String formName = (String) formDataMap.get("formName");
                String isQuestionForm = (String) formDataMap.get("IsQuestionForm");
                formcondition = (String) formDataMap.get("condition");

                formprocess(category, formName, isQuestionForm, fieldType);
            }
        }

        return true;
    }



    /**
     * Processes a form based on the given parameters.
     *
     * @param category        the category of the form
     * @param form            the form to be processed
     * @param IsQuestionForm  indicates if the form is a question form
     * @param fieldtype       the type of field to be processed
     * @return                true if the form execution is successful, false otherwise
     * @throws Exception      if an error occurs during form processing
     */
    private static boolean formprocess(String category, String form, String IsQuestionForm, String fieldtype) throws Exception {
        form = removeUnderscores(form);
        if (!sourceExists(form)) {
            return false;
        }

        String formName = form.replace(" ", "_");

        String productColumnQuery = MessageFormat.format(sqlQueries.get("ProductColumnquery"), "'" + formName + "'");
        String productColumn = getColumnNamesFromDatabase(productColumnQuery, "ProductColumn").get(0);
        productColumn = productColumn.replace("*", "+ ' *'");

        boolean pictureform = formName.equalsIgnoreCase("picture");
        String questionColumns = (IsQuestionForm.equals("1") || pictureform) ?
                "Rtrim(Ltrim(" + productColumn + ")) + CASE\n" +
                        "        WHEN sm.QuestionRequired = '1' THEN ' *'\n" +
                        "        ELSE  '' End " :
                "Rtrim(Ltrim(" + productColumn + "))";

        String productQuery = MessageFormat.format(sqlQueries.get("Productquery"), formName, targetid, "'" + category + "'", questionColumns, formcondition);
        List<String> productNames = getColumnNamesFromDatabase(productQuery, "ProductName");

        if (fieldtype.contains("Mandatory only")) {
            productNames.removeIf(ele -> !ele.contains("*"));
        }

        for (String productName : productNames) {
            try {
                productprocess(formName, productName, IsQuestionForm);
            } catch (Exception e) {
                log.error("Error processing product : " + productName, e);
                throw new RuntimeException(e);
            }
        }

      /*  if (driver.isKeyboardShown()) {
            driver.hideKeyboard();
        }
*/
        Thread.sleep(600);
        if (sourceExists("Next")) {
            click("ACCESSIBILITYID", NextButton);
            log.info("Next button is clicked");
        } else if (sourceExists("Done")) {
            click("ACCESSIBILITYID", Donebutton);
            log.info("Done button is clicked");
        }

        return true;
    }



    /**
     * Processes the product in the form.
     *
     * @param formName         the name of the form
     * @param productName     the name of the product
     * @param IsQuestionForm   the flag indicating if it is a question form
     * @return                 true if the product is processed successfully, false otherwise
     * @throws Exception       if an error occurs while processing the product
     */
    private static boolean productprocess(String formName, String productName, String IsQuestionForm) throws Exception {
        String modifiedProductName = SetSpecialCharacter(productName);
        String cleanProductName = modifiedProductName.replace("  *", "");

        try {
            if (!sourceExists(cleanProductName)) {
                AppUtils.scroll(driver, 600);
            }

            if (sourceExists(cleanProductName)) {

                if(!fieldtypes.equalsIgnoreCase("Mandatory only")){
                    productClickValidation( formName,  IsQuestionForm ,productName);
                }else{
                    click("ACCESSIBILITYID", adjustproductname(productName));
                }


                if (!formName.contains("Picture")) {
                    cleanProductName = productName.replace(" *", "");
                }

                enterfieldprocess(formName, IsQuestionForm, cleanProductName);
                return true;
            }
        } catch (Exception e) {
            log.error("Error processing product in form " + productName, e);
        }
        return false;
    }



    /**
     * Processes the fields for entering data in a form.
     *
     * @param formName         the name of the form
     * @param IsQuestionForm   a flag indicating if the form is a question form (1 for true, 0 for false)
     * @param productName      the name of the product
     * @return                 true if the field processing is successful, false otherwise
     * @throws Exception       if an error occurs during field processing
     */


    //Need to improve performance from this method



    private static boolean enterfieldprocess(String formName, String IsQuestionForm, String productName) throws Exception {
        int forDEO = IsQuestionForm.equals("1") ? 0 : 1;
        List<Object> fieldNames = getDataObject(IsQuestionForm.equals("1") ? MessageFormat.format(sqlQueries.get("QuestionFormFieldsquery"), formName, "'" + productName + "'", "'" + formName + "'") : MessageFormat.format(sqlQueries.get("FormFieldsquery"), "'" + formName + "'", IsQuestionForm, forDEO));

        for (Object field : fieldNames) {
            if (!(field instanceof LinkedHashMap<?, ?>)) {
                continue;
            }

            LinkedHashMap<?, ?> fieldData = (LinkedHashMap<?, ?>) field;
            Ctrltype = (String) fieldData.get("ControlType");
            Datatype = (String) fieldData.get("DataType");
            fieldName = IsQuestionForm.equals("1") ? Ctrltype : (String) fieldData.get("FieldName");
            enumfieldName = (String) fieldData.get("FieldName");

            updateFieldNameForRequired(formName, productName, fieldData);

            // Skip field if it contains "Gap Facings"
            if (fieldName.contains("Gap Facings")) {
                continue;
            }

            handleControlType(formName, Ctrltype, fieldName, Datatype, productName, IsQuestionForm);
        }
        return true;
    }


    /**
     * Updates the field name for required fields.
     *
     * @param formName    the name of the form
     * @param productName the name of the product
     * @param fieldData   the field data
     * @throws NullPointerException if formName, productName, or fieldData is null
     */
    private static void updateFieldNameForRequired(String formName, String productName, LinkedHashMap<?, ?> fieldData) {
        if(formName.contains("Picture")){
            fieldName += productName.contains("*")?  " *":"";
        } else
        if ("1".equals(fieldData.get("Required"))) {
            fieldName += " *";
        }
    }

    /**
     * Handles the control type for a given form.
     *
     * @param formName      the name of the form
     * @param Ctrltype      the type of control
     * @param fieldName     the name of the field
     * @param Datatype      the data type of the field
     * @param prodname      the product name
     * @param IsQuestionForm indicates if the form is a question form
     * @throws Exception if an error occurs during handling the control type
     */
    private static void handleControlType(String formName, String Ctrltype, String fieldName, String Datatype, String prodname, String IsQuestionForm) throws Exception {

        switch (Ctrltype) {
            case "TextBox":
                log.info("Control type is TextBox");
                enter("Xpath", generatetextfieldlocator(fieldName), generateTestData(Datatype, fieldName));
                driver.hideKeyboard();
                break;
            case "DropDownList":
                log.info("Control type is DropDownList");
                Dropdownsetter(formName, prodname, IsQuestionForm, fieldName);
                break;
            case "FileUpload":
                imageCapture();
                break;
        }
    }

    /**
     * Adjusts the product name by removing any trailing whitespace and adding a space before an asterisk if it is missing.
     *
     * @param prodname the product name to be adjusted
     * @return the adjusted product name
     * @throws NullPointerException if prodname is null
     */
    private static String adjustproductname(String prodname) {
        if (prodname.contains("  *")) {
            return prodname.replace("  *", " *");
        } else {
            return prodname;
        }
    }

    private static void productClickValidation(String formName, String IsQuestionForm, String productName) throws Exception {

        int forDEO = IsQuestionForm.equals("1") ? 0 : 1;
        List<Object> fieldNames = getDataObject(IsQuestionForm.equals("1") ? MessageFormat.format(sqlQueries.get("QuestionFormFieldsquery"), formName, "'" + productName + "'", "'" + formName + "'") : MessageFormat.format(sqlQueries.get("FormFieldsquery"), "'" + formName + "'", IsQuestionForm, forDEO));

        for (Object field : fieldNames) {
            if (!(field instanceof LinkedHashMap<?, ?>)) {
                continue;
            }
            LinkedHashMap<?, ?> fieldData = (LinkedHashMap<?, ?>) field;
            String Ctrltype = (String) fieldData.get("ControlType");

            if (previousCtrltype != null && !previousCtrltype.equals("DropDownList")) {
                click("ACCESSIBILITYID", adjustproductname(productName));
            }
            previousCtrltype = Ctrltype;
        }
    }



    private static void Dropdownsetter(String formName, String productName, String IsQuestionForm, String fieldName) throws Exception {
        try {
            String Enumquery;
            if (IsQuestionForm.equals("1")) {
                Enumquery = MessageFormat.format(sqlQueries.get("EnumQuestionFieldquery"), formName, "'" + productName + "'", "'" + formName + "'");
            } else {
                Enumquery = MessageFormat.format(sqlQueries.get("EnumFieldquery"), "'" + fieldName.replace(" *", "").replace(" ", "_") + "'", "'" + formName + "'");
            }
            List<String> dropList = getColumnNamesFromDatabase(Enumquery, "FieldOption");

            for (String drop : dropList) {
                if (sourceExists(fieldName)) {
                    dropdown(fieldName,drop);
                    break;
                } else{
                    if (sourceExists(drop)) {
                        click("ACCESSIBILITYID", drop);
                        break;
                    }
                    AppUtils.scroll(driver,600);
                    dropdown(fieldName,drop);
                    break;
                }
            }
        } catch (Exception e) {
            log.error("Error setting dropdown for field " + fieldName + " in form " + formName);
            log.error(e.getMessage());
        }
    }

    private static void dropdown(String fieldName,String drop){

        if (sourceExists(fieldName)) {
            click("ACCESSIBILITYID", fieldName);
            click("ACCESSIBILITYID", drop);

        } else if (sourceExists(drop)) {
            click("ACCESSIBILITYID", drop);
        }
    }


    private static void imageCapture() throws InterruptedException {

        if(fieldName.contains("Photo *")){
            click("Xpath", Camerabutton_M);
        }else{
            click("Xpath", Camerabutton_NM);
        }
        webdriverWait("Xpath", Shutterbutton, 4);
        click("Xpath", Shutterbutton);
        webdriverWait("ACCESSIBILITYID", "Done", 3);
        click("ACCESSIBILITYID", "Done");
        Thread.sleep(700);
        totalimagescaptured++;

    }


    /**
     * Generates dataset based on the given type and facing type.
     *
     * @param type        the type of dataset (Int or Varchar)
     * @param facingtype  the facing type (Industry Facing * or Our Brand Facing *)
     * @return            the generated dataset
     * @throws            NullPointerException if type is null
     */

    private static String generateTestData(String dataType, String facingType) {
        final String INDUSTRY_FACINGS = "Industry Facings *";
        final String OUR_BRAND_FACINGS = "Our Brand Facings *";

        if ("Int".equals(dataType)) {
            if (facingType.contains(INDUSTRY_FACINGS)) {
                return "5";
            } else if (facingType.contains(OUR_BRAND_FACINGS)) {
                return "2";
            }
            return "3";
        } else if ("integer".equals(dataType)) {
            return "6";
        } else if (dataType.contains("Varchar") || dataType.contains("string")) {
            return "Testdata";
        } else {
            // Handle unknown data types
            throw new IllegalArgumentException("Unknown data type: " + dataType);
        }
    }
}
