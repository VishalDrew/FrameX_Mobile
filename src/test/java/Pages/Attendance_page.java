package Pages;

import org.json.JSONObject;
import org.testng.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;

import static Listeners.FrameX_Listeners.logAndReportFailure;
import static Modules.Attendance_Module.validateattendancesubmission;
import static Pages.HomePage_page.Attendance;
import static Pages.Login_Page.menubutton;
import static Utilities.Actions.*;
import static Utilities.TestDataUtil.gettestdata;
import static Utilities.Utils.*;

public class Attendance_page {

    public static String Attendancecamera = "//android.view.View[2]";
    public static String Frontcamerabutton = "//android.widget.ImageView[4]";
    public static String shutterbutton = "//android.view.View[3]";
    public static String Submit = "Submit";
    public static String imgnonmandatorymsg = "No Image Required";
    public static String imgmandatorymsg = "Please, Take Photo for submit attandance.";
    public static final String presentsavedmsg = "Your Attendance is Marked for Today  Present";
    public static final String leavesavedmsg = "Your Attendance is Marked for Today  Leave";
    public static final String absentsavedmsg = "Your Attendance is Marked for Today  Absent";
    public static final String atofficesavedmsg = "Your Attendance is Marked for Today  At Office";
    public static final String weekoffsavedmsg = "Your Attendance is Marked for Today  Week off";
    public static final String holidaysavedmsg = "Your Attendance is Marked for Today  Holiday";
    public static final String toursavedmsg = "Your Attendance is Marked for Today  Tour";
    public static final String trainingsavedmsg = "Your Attendance is Marked for Today  Training";
    public static final String monthlymeetingsavedmsg = "Your Attendance is Marked for Today  Monthly Meeting";
    public static final String resignsavedmsg = "Your Attendance is Marked for Today  Resign";
    public static String Attendacealreadysaved = "Attendance data is already saved";

    public static HashMap<String,String>attendancemessages(){

        LinkedHashMap<String,String> attendancesuccessmesssages = new LinkedHashMap<String,String>();
        attendancesuccessmesssages.put("Present",presentsavedmsg);
        attendancesuccessmesssages.put("Absent",absentsavedmsg);
        attendancesuccessmesssages.put("Leave",leavesavedmsg);
        attendancesuccessmesssages.put("At office",atofficesavedmsg);
        attendancesuccessmesssages.put("Week off",weekoffsavedmsg);
        attendancesuccessmesssages.put("Holiday",holidaysavedmsg);
        attendancesuccessmesssages.put("Tour",toursavedmsg);
        attendancesuccessmesssages.put("Training",trainingsavedmsg);
        attendancesuccessmesssages.put("Monthly Meeting",monthlymeetingsavedmsg);
        attendancesuccessmesssages.put("Resign",resignsavedmsg);

        return attendancesuccessmesssages;
    }

    

}



