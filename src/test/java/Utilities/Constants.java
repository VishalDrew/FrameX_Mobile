package Utilities;

import java.util.Date;

import static Base.TestSetup.*;
import static Utilities.AppUtils.generateFormattedDate;
import static Utilities.AppUtils.getDeviceName;

/**
 * Constants class.
 */
public class Constants {

    public static final String Devicename  = getDeviceName();
    public static final String  configfilepath = System.getProperty("user.dir") + "\\src\\test\\resources\\Properties\\Config.properties";
    public static final String queryfilepath = System.getProperty("user.dir")+"\\src\\test\\resources\\Properties\\queries.sql";
    public static final String TEST_DATA_FILE = System.getProperty("user.dir")+"\\src\\test\\resources\\Datas\\Testdatas.json";
    public static final String CALLPLAN_TEST_DATA_FILE = System.getProperty("user.dir")+"\\src\\test\\resources\\Datas\\CallPlandata.json";
    public static final String TESTSUITES_DATA_FILE = "./src/main/java/TestConfig/TestSuites.json";
    public static final String allureDirpath = System.getProperty("user.dir") + "\\src\\test\\resources\\Allure Reports\\";
    public static final String framenewmainURL = "jdbc:sqlserver://192.168.0.124:1433;DatabaseName=framenew_main;encrypt=true;trustServerCertificate=true";
    private static String databaseURL ;
    public static final String allureExecutable = "E:\\allure-2.29.0\\bin\\allure.bat";
    public static final String  allureReportDir = System.getProperty("user.dir") + "\\src\\test\\resources\\Allure Reports";
    public static final String  allureResultsDir = System.getProperty("user.dir") + "\\allure-results";
    public static final String command = String.format("\"%s\" generate --clean --single-file -o \"%s\" \"%s\"", allureExecutable, allureReportDir, allureResultsDir);


    /**
     * Returns the production URL for connecting to the SQL Server database.
     * 
     * @return the production URL in the format "jdbc:sqlserver://65.1.119.118:1433;DatabaseName={project};encrypt=true;trustServerCertificate=true"
     * @throws JSONException if there is an error retrieving the project name from the globalproject object
     * 
     * @param globalproject the JSONObject containing the project information
     */
    private static final String getProdUrl() {
        return "jdbc:sqlserver://65.1.119.118:1433;DatabaseName=" + globalData.getString("project") + ";encrypt=true;trustServerCertificate=true";      //LIVE URL
    }

    /**
     * Returns the test server URL for connecting to the SQL Server database.
     * 
     * @return the test server URL in the format "jdbc:sqlserver://192.168.0.124:1433;DatabaseName=<project>;encrypt=true;trustServerCertificate=true"
     * @throws JSONException if there is an error retrieving the project name from the global project object
     */
    private static final String gettestserverurl() {
        return "jdbc:sqlserver://192.168.0.124:1433;DatabaseName="+ globalData.getString("project")+" ;encrypt=true;trustServerCertificate=true";
    }


    private static final String getglobalserverurl() {
        String testserverurl = "jdbc:sqlserver://183.83.187.133:1433;DatabaseName=" + globalData.getString("project") + ";encrypt=true;trustServerCertificate=true";
        return testserverurl;
    }

    public static final String getURL(){
        return getdatabseurl();
    }

    public static final String databasebUsername = "Field2020";
    public static final String databasePassword = "Fieldlytics@#@2020";

    public static final  String body = "Dear Team,\n" +
            "\n" +
            "Please find the attached test automation report for FrameX Mobile executed on "+generateFormattedDate("dd-MM-yy")+" . The test suite covered various scenarios validating the functionalities of FrameX mobile.\n" +
            "\n" +
            "The test suite execution results indicate [summary of test outcomes - overall success, challenges, critical issues, etc.].\n" +
            "\n" +
            "Attached Test Report:\n" +
            "The attached test report provides detailed information on individual test cases, their status, logs, and any errors encountered during execution.\n" +
            "\n" +
            "Please review the attached report for a comprehensive understanding of the test execution results.\n" +
            "\n" +
            "\n" +
            "Thank you,\n" +
            "Fieldlytics QA Team\n";


    public static String Categorymasterquerygenerator(String targetid){

        String categoryquery = "DECLARE @NAME NVARCHAR(MAX), @FormMapWith NVARCHAR(MAX), @sql VARCHAR(MAX) = 'select distinct name,catSequence from (';\n" +
                "DECLARE Form_Name CURSOR LOCAL FORWARD_ONLY FOR\n" +
                "SELECT Name,FormMapWith FROM formmaster WHERE FormStatus = 1;\n" +
                "\n" +
                "OPEN Form_Name;\n" +
                "FETCH NEXT FROM Form_Name INTO @NAME,@FormMapWith;\n" +
                "\n" +
                "WHILE @@FETCH_STATUS = 0\n" +
                "BEGIN\n" +
                "    DECLARE @relation NVARCHAR(100);\n" +
                "    DECLARE @master NVARCHAR(100);\n" +
                "    DECLARE @ID NVARCHAR(100);\n" +
                "      \n" +
                "    SET @relation = @NAME + 'Relation';\n" +
                "    SET @master = @NAME + 'Master';\n" +
                "    SET @ID = @NAME + 'ID';\n" +
                "   \n" +
                "   DECLARE @Delimiter CHAR(1)\n" +
                "   SET @Delimiter = '%'\n" +
                "\n" +
                "   DECLARE @Pos INT\n" +
                "   DECLARE @Part VARCHAR(MAX)\n" +
                "   DECLARE @result VARCHAR(MAX) = ''\n" +
                "\n" +
                "   WHILE CHARINDEX(@Delimiter, @FormMapWith) > 0\n" +
                "   BEGIN\n" +
                "      SET @Pos = CHARINDEX(@Delimiter, @FormMapWith)\n" +
                "      SET @Part = SUBSTRING(@FormMapWith, 1, @Pos - 1)\n" +
                "      SET @FormMapWith = SUBSTRING(@FormMapWith, @Pos + 1, LEN(@FormMapWith) - @Pos)\n" +
                "\n" +
                "      SET @result = @result + 'tm.' + @Part + ' = mr.' + @Part + ' AND '\n" +
                "   END\n" +
                "\n" +
                "   -- Remove the trailing 'AND'\n" +
                "   SET @result = LEFT(@result, LEN(@result) - LEN(' AND ')) \n" +
                "    SET @sql += 'SELECT CM.Name AS name, CM.Sequence AS catSequence FROM ' + @relation + ' mr \n" +
                "                 JOIN ' + @master + ' mm ON mm.' + @ID + ' = mr.' + @ID + ' \n" +
                "                 INNER JOIN CategoryMaster CM ON CM.CategoryId = mm.CategoryId   \n" +
                "             Inner Join targetmaster tm ON '+ @result +'\n" +
                "                 WHERE CM.Status = 1 \n" +
                "                 AND RoleGroupID IN (SELECT RoleGroupID FROM RoleGroupMapping WHERE RoleID = 4) and Tm.targetid = "+targetid+"';\n" +
                "\n" +
                "    FETCH NEXT FROM Form_Name INTO @NAME,@FormMapWith;\n" +
                "\n" +
                "    IF @@FETCH_STATUS = 0\n" +
                "    BEGIN\n" +
                "        SET @sql += ' UNION ALL ';\n" +
                "    END\n" +
                "END;\n" +
                "\n" +
                "CLOSE Form_Name;\n" +
                "DEALLOCATE Form_Name;\n" +
                "\n" +
                "SET @sql += ') A ORDER BY catSequence'\n" +
                "print @sql\n" +
                "Exec(@sql);";


        return categoryquery;
    }


    public static String formmasterquerygenerator(String target,String category){

        String formquery = "\n" +
                "DECLARE @NAME NVARCHAR(400), @FormMapWith NVARCHAR(500),@IsQuestionForm varchar(10),@FormSequence varchar(10), @sql VARCHAR(MAX) = 'select distinct formName,IsQuestionForm, condition,FormSequence from (';\n" +
                "DECLARE Form_Name CURSOR LOCAL FORWARD_ONLY FOR\n" +
                "SELECT Name,FormMapWith,IsQuestionForm,FormSequence FROM formmaster WHERE FormStatus = 1 order by FormSequence ;\n" +
                "\n" +
                "OPEN Form_Name;\n" +
                "FETCH NEXT FROM Form_Name INTO @NAME,@FormMapWith,@IsQuestionForm,@FormSequence;\n" +
                "\n" +
                "WHILE @@FETCH_STATUS = 0\n" +
                "BEGIN\n" +
                "    DECLARE @relation NVARCHAR(100);\n" +
                "    DECLARE @master NVARCHAR(100);\n" +
                "    DECLARE @ID NVARCHAR(100);\n" +
                "      \n" +
                "    SET @relation = @NAME + 'Relation';\n" +
                "    SET @master = @NAME + 'Master';\n" +
                "    SET @ID = @NAME + 'ID';\n" +
                "   \n" +
                "   DECLARE @Delimiter CHAR(1)\n" +
                "   SET @Delimiter = '%'\n" +
                "\n" +
                "   DECLARE @Pos INT\n" +
                "   DECLARE @Part VARCHAR(MAX)\n" +
                "   DECLARE @result VARCHAR(MAX) = ''\n" +
                "\n" +
                "   WHILE CHARINDEX(@Delimiter, @FormMapWith) > 0\n" +
                "   BEGIN\n" +
                "      SET @Pos = CHARINDEX(@Delimiter, @FormMapWith)\n" +
                "      SET @Part = SUBSTRING(@FormMapWith, 1, @Pos - 1)\n" +
                "      SET @FormMapWith = SUBSTRING(@FormMapWith, @Pos + 1, LEN(@FormMapWith) - @Pos)\n" +
                "\n" +
                "      SET @result = @result + 'tm.' + @Part + ' = mr.' + @Part + ' AND '\n" +
                "   END\n" +
                "\n" +
                "   -- Remove the trailing 'AND'\n" +
                "   SET @result = LEFT(@result, LEN(@result) - LEN(' AND ')) \n" +
                "    SET @sql += 'SELECT '''+@Name+''' as FormName,'+@IsQuestionForm+' as IsQuestionForm,'+ @FormSequence+' as FormSequence,'''+ @result+'''AS condition FROM ' + @relation + ' mr \n" +
                "                 JOIN ' + @master + ' mm ON mm.' + @ID + ' = mr.' + @ID + ' \n" +
                "                 INNER JOIN CategoryMaster CM ON CM.CategoryId = mm.CategoryId   \n" +
                "             Inner Join targetmaster tm ON '+ @result +'\n" +
                "                 WHERE CM.Status = 1 \n" +
                "                 AND RoleGroupID IN (SELECT RoleGroupID FROM RoleGroupMapping WHERE RoleID = 4) and Tm.targetid = "+target+" and cm.Name = ''"+category+"''';\n" +
                "\n" +
                "    FETCH NEXT FROM Form_Name INTO @NAME,@FormMapWith,@IsQuestionForm,@FormSequence;\n" +
                "\n" +
                "    IF @@FETCH_STATUS = 0\n" +
                "    BEGIN\n" +
                "        SET @sql += ' UNION ALL ';\n" +
                "    END\n" +
                "END;\n" +
                "\n" +
                "CLOSE Form_Name;\n" +
                "DEALLOCATE Form_Name;\n" +
                "\n" +
                "SET @sql += ') A order by FormSequence'\n" +
                "print @sql\n" +
                "Exec(@sql);";

        return formquery;
    }

    public static String isShopFrontPhotoRequired() {
        String projectmaster = "select * from Projectmaster where ProjectName = '"+globalData.getString("project")+"'";
        return projectmaster;
    }

    public static String generateStoreRequiredQuery(){

        String storereqquery = "select ExitButtonRequired, * from Projectmaster where ProjectName = '"+ globalData.getString("project")+"'";
        return storereqquery;

    }

    private static String getdatabseurl(){

        String env = properties.get("Environment");
        if(env.equalsIgnoreCase("Production")){
            databaseURL =  getProdUrl();
        } else if (env.equalsIgnoreCase("QA")) {
            databaseURL =  gettestserverurl();
        } else if (env.equalsIgnoreCase("Global")) {
            databaseURL =  getglobalserverurl();
        }else{
            log.error(env+" is not found");
            System.out.println(env+" is not found");
            return null;
        }
        return databaseURL;
    }
}
