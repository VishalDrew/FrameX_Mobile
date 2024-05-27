package Tests;

import org.json.JSONObject;
import org.testng.annotations.Test;

import static Modules.Attendance_Module.*;
import static Pages.HomePage_page.Attendance;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.AppUtils.*;

/**
 * AttendanceTest class.
 */
public class AttendanceTest {

    /**
     * Test case to verify if the Attendance module is displayed.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 1, groups = {"smoke", "regression"}, enabled = true)
    public void TC_001_VerifyAttendanceModuleDisplayed() throws InterruptedException {
        verifyModuleDisplayedStatus(Attendance);
    }

    /**
     * Test case to verify attendance without capturing image.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 2, dependsOnMethods = {"TC_001_VerifyAttendanceModuleDisplayed"}, groups = {"negative", "regression"}, enabled = true)
    public void TC_002_VerifyAttendanceWithoutCapturingImage() throws InterruptedException {
        JSONObject attendanceData = gettestdata("Attendance", "Attendancewithoutcapturingimage");
        navigateto(Attendance);
        validateimgrequired(attendanceData.getString("type"), attendanceData.getString("expectedErrorMessage"));
    }

    /**
     * Test case to verify the attendance submission functionality.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    @Test(priority = 3, dependsOnMethods = {"TC_001_VerifyAttendanceModuleDisplayed"}, groups = {"smoke", "regression"}, enabled = true)
    public void TC_003_VerifyAttendanceSubmission() throws InterruptedException {
        JSONObject attendanceData = gettestdata("Attendance", "Attendance Submission");
        if (sourceExists("Submit") || (sourceExists("Attendance") && sourceExists("Submit"))) {
            doattendance(attendanceData.getString("type"), attendanceData.getString("Image"), attendanceData.getString("expectedMessage"));
        } else {
            navigateto(Attendance);
            doattendance(attendanceData.getString("type"), attendanceData.getString("Image"), attendanceData.getString("expectedMessage"));
        }
    }

    /**
     * Test case to verify the attendance recorded in the database.
     * It depends on the successful execution of the TC_003_VerifyAttendanceSubmission test case.
     *
     * @throws Exception if an error occurs during the verification process
     */
    @Test(priority = 4, dependsOnMethods = {"TC_003_VerifyAttendanceSubmission"}, groups = {"smoke", "regression"}, enabled = true)
    public void TC_004_VerifyAttendanceRecordedInDatabase() throws Exception {
        verifyattendancedatainDB();
    }

}
