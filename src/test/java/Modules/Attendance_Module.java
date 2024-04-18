package Modules;

import Base.TestSetup;
import org.json.JSONObject;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Listeners.FrameX_Listeners.*;
import static Pages.Attendance_page.*;
import static Pages.CallPlan_page.currentdate;
import static Pages.HomePage_page.Attendance;
import static Pages.HomePage_page.Callplan;
import static Utilities.Actions.*;
import static Utilities.DBConfig.fetchdatafromdb;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.*;

public class Attendance_Module extends TestSetup {
    public static String attendancetime;

    /**
     * Validates the attendance submission for a given attendance type, image and expected message.
     *
     * @param attendancetype The type of attendance being submitted.
     * @param image The image associated with the attendance submission.
     * @param expected The expected message after the attendance submission.
     * @return true if the attendance submission is successful and matches the expected message, false otherwise.
     * @throws InterruptedException if the thread is interrupted while waiting for the attendance submission.
     */
    public static boolean validateattendancesubmission(String attendancetype,String image,String expected) throws InterruptedException {
        HashMap<String,String>attendancesuccessmessages =  attendancemessages();
        String savedmsg = "";

        try {
            if(sourceExists("Your attendance Marked for today")){
                logAndReportFailure("TestCase Failed : Attendance is already Marked for this user");
                return false;
            }
            performAttendanceActivity(attendancetype,image);
            for (String key : attendancesuccessmessages.keySet()) {
                if(key.equalsIgnoreCase(attendancetype)){
                    savedmsg =  attendancesuccessmessages.get(key);
                }
            }
            webdriverWait("ACCESSIBILITYID",savedmsg,15);
            // Verifying attendance submission success/failure
            return attendancesubmittedvalidation(attendancetype,savedmsg,expected);

        } catch (Exception e) {
            logAndReportFailure("Exception occurred: " + e.getMessage());
            return false;
        }
    }

    /**
     * Validates the attendance submission based on the type, response, and confirm response.
     *
     * @param type the type of attendance
     * @param response the response received after clicking on the Attendance button
     * @param confirmresponse the response received after clicking on the confirm button
     * @return true if the attendance submission is successful, false otherwise
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    static boolean attendancesubmittedvalidation(String type , String response,String confirmresponse) throws InterruptedException {
        if (sourceExists(response)){
            click("ACCESSIBILITYID", Attendance);
            log.info(response);
            if(sourceExists(confirmresponse)){
                return true;
            }else{
                driver.navigate().back();
                click("ACCESSIBILITYID", Callplan);
                Thread.sleep(2000);
                driver.navigate().back();
                click("ACCESSIBILITYID", Attendance);
                Thread.sleep(700);
                if(sourceExists(confirmresponse)) {
                    if(!sourceExists("Submit")){
                        logAndReportSuccess("TestCase Passed : Attendance Submitted successfully");
                        return true;
                    }else{
                        logAndinfo("TestCase Failed : Attendance Submitted successfully But submit button is showing");
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Performs attendance activity.
     *
     * @param type the type of attendance activity
     * @param img a flag indicating if an image is required for the attendance activity
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public static void performAttendanceActivity(String type , String img) throws InterruptedException {
        try {
            for (String key : attendancemessages().keySet()) {
                if(sourceExists(key)) {
                    click("ACCESSIBILITYID", key);
                    break;
                }
            }
            click("ACCESSIBILITYID", type);
            if(img.equalsIgnoreCase("True")){
                click("xpath", Attendancecamera);
                webdriverWait("xpath",shutterbutton,15);
                click("xpath", shutterbutton);
            }
            attendancetime =  getdevicetime();
            click("ACCESSIBILITYID", Submit);
        } catch (Exception e) {
            logAndReportFailure("Error occurred during attendance activity: " + e.getMessage());
            log.error("Error occurred during attendance activity:", e);
        }
    }

    /**
     * Submits attendance for a given type and image and validates the expected message.
     *
     * @param type The type of attendance being submitted.
     * @param image The image associated with the attendance.
     * @param expectedmessage The expected message to be validated.
     * @throws InterruptedException If the thread is interrupted while waiting for validation.
     */
    public static void doattendance(String type,String image,String expectedmessage) throws InterruptedException {
        if(!validateattendancesubmission(type,image,expectedmessage)){
            logAndReportFailure("TestCase Failed : Attendance Submission Failed");
            Assert.fail("TestCase Failed : Attendance Submission Failed");
        }
    }

    /**
     * Validates if image is required for attendance submission.
     *
     * @param type the type of attendance (e.g. "Late", "Early", "On Time")
     * @param errmsg the error message to be displayed if image is not required
     *
     * @throws AssertionError if attendance is already marked
     */
    public static void validateimgrequired(String type,String errmsg){
        if(sourceExists("Your attendance Marked for today")){
            logAndReportFailure("TestCase Failed : Attendance is already Marked for this user Submit button is not showing");
            Assert.fail("Attendance is already Marked ");
        }
        click("ACCESSIBILITYID", "Present");
        click("ACCESSIBILITYID", type);
        click("ACCESSIBILITYID", Submit);
        Assertion(errmsg,"Please, Take Photo for submit attendance is not displayed");
    }

    /**
     * Retrieves the status and time for a given username and column name.
     *
     * @param username   the username to retrieve the status and time for
     * @param columnName the name of the column to retrieve the value from
     * @return the value of the specified column for the given username and current date
     * @throws Exception if there is an error while fetching data from the database
     */
    public static String getstatusandtime(String username, String columnName) throws Exception {
        try {
            String todaydate = generateFormattedDate("yyyy-MM-dd");
            List<Map<String, String>> result = fetchdatafromdb("select username, status, createddate from Attendancedetail where username = '"+username+"' and date = '"+todaydate+"'");
            if (!result.isEmpty()) {
                Map<String, String> firstRow = result.get(0); // Assuming there's only one row
                return firstRow.get(columnName);
            }
        } catch (Exception e) {
            log.error("Error occurred while fetching data from database:", e);
            throw e;
        }
        return null;
    }

    /**
     * Verifies the attendance data in the database.
     *
     * @throws Exception if an error occurs during the verification process.
     */
    public static void verifyattendancedatainDB() throws Exception {
        try {
            String deviceattendancetime  = attendancetime;
            String username = globaldata.getString("username");
            String attendancestatus  = getstatusandtime(username,"status");
            String createdTime = getstatusandtime(username,"createddate").split(" ")[1].substring(0, 5);

            if(attendancestatus.equals("P") && attendancetime.contains(createdTime)){
                logAndReportSuccess("TestCase Passed : Attendance datas is matched in Database");
                fetchdatafromdb("DELETE FROM Attendancedetail where username  ='"+username+"' and date = '"+currentdate+"'");// Attendance Status is reverted
                Assert.assertTrue(true);
            } else {
                logAndinfo("Attendance time : " + attendancetime);
                logAndinfo("Time fetched in Database : " + createdTime);
                logAndReportFailure("Attendance data is Mismatch in Database");
                fetchdatafromdb("DELETE FROM Attendancedetail where username  ='"+username+"' and date = '"+currentdate+"'");;// Attendance Status is reverted
                Assert.fail("TestCase Failed : Attendance data is Mismatch in Database");
            }
        } catch (Exception e) {
            log.error("Error occurred while verifying attendance data in database:", e);
            throw e;
        }
    }
}