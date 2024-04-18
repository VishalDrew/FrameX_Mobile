package Tests;

import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Listeners.FrameX_Listeners.logAndReportSuccess;
import static Modules.Attendance_Module.*;
import static Pages.Attendance_page.*;
import static Pages.HomePage_page.Attendance;
import static Pages.Login_Page.menubutton;
import static Utilities.Actions.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.*;

/**
 * AttendanceTest class.
 */
public class AttendanceTest {

    /**
     * Test case to verify if the Attendance module is displayed.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 1, groups = {"smoke", "regression"},enabled = true)
    private  void TC_001_Verify_Attendance_Module_Displayed() throws InterruptedException {
        verifyModuleDisplayedStatus(Attendance);
    }

    /**
     * Test case to verify attendance without capturing image.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test( priority = 2,dependsOnMethods = { "TC_001_Verify_Attendance_Module_Displayed" },groups = {"negative", "regression"},enabled = true)
    private void TC_002_Verify_Attendance_Without_Capturing_Image() throws InterruptedException {
        JSONObject attendancedata = gettestdata("Attendance","Attendancewithoutcapturingimage");
        navigateto(Attendance);
        validateimgrequired(attendancedata.getString("type"),attendancedata.getString("expectedErrorMessage"));
    }


    /**
     * This method is used to verify the attendance submission functionality.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 3,dependsOnMethods = { "TC_001_Verify_Attendance_Module_Displayed" },groups = {"smoke", "regression"},enabled = true)
    private void TC_003_Verify_Attendance_Submission() throws InterruptedException {

        JSONObject attendanceData = gettestdata("Attendance", "Attendance Submission");
        if (sourceExists("Submit") || (sourceExists("Attendance") && sourceExists("Submit"))) {
            doattendance(attendanceData.getString("type"), attendanceData.getString("Image"), attendanceData.getString("expectedMessage"));
        } else {
            navigateto(Attendance);
            doattendance(attendanceData.getString("type"), attendanceData.getString("Image"), attendanceData.getString("expectedMessage"));
        }
    }

    /**
     * This method is used to verify the attendance recorded in the database.
     * It depends on the successful execution of the TC_003_Verify_Attendance_Submission test case.
     */
    @Test(priority = 4,dependsOnMethods = { "TC_003_Verify_Attendance_Submission" },groups = {"smoke", "regression"},enabled = true)
    private void TC_004_Verify_Attendance_Recorded_In_Database() throws Exception {

        verifyattendancedatainDB();
    }


}
