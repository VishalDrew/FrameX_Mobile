package Utilities;


import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;
import static Base.TestSetup.log;
import static Utilities.Constants.TEST_DATA_FILE;

public class TestDataUtil {

    public static JSONObject gettestdata(String globaljsonName, String jsonname) {
        try {
            String jsonData = new String(Files.readAllBytes(Paths.get(TEST_DATA_FILE)));
            JSONObject jsonObject = new JSONObject(jsonData);
            return jsonObject.getJSONObject(globaljsonName).getJSONObject(jsonname);
        } catch (Exception e) {
            log.error("Error occurred while fetching test data: " + e.getMessage());
            e.getMessage();
            return null;
        }
    }
}