package Pages;

import org.json.JSONObject;

import static Base.TestSetup.globalData;
import static Modules.Login_Module.performLogin;
import static Utilities.TestDataUtil.gettestdata;

public class Login_Page {

    public static final String username = "//*[@hint='Username']";
    public static final String password = "//*[@hint='Password']";
    public static final String project = "//*[@hint='Project']";
    public static final String Mobiileno = "//*[@hint='Mobile no.']";
    public static final String LoginButton = "Login";
    public static final String menubutton = "//android.widget.Button[1]";
    public static final String Logoutbutton = "Logout";
    public static final String yes = "Yes";


    /**
     * Logs in the user using the provided login credentials.
     *
     * @throws JSONException if there is an error parsing the test data
     *
     * @param username the username of the user
     * @param password the password of the user
     * @param project the project name
     * @param mobileno the mobile number of the user
     *
     * @returns void
     */
    public static void lgpage()  {
        JSONObject user1 = gettestdata("Login","User1");
        performLogin(globalData.getString("username"), globalData.getString("password"),globalData.getString("project"),globalData.getString("mobileno"));
    }

}
