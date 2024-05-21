package Utilities;

import org.json.simple.JSONObject; // Import from org.json.simple package
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JsonConfigReader {

    private JSONObject suites;

    public JsonConfigReader(String filePath) {
        JSONParser parser = new JSONParser();
        try {
            FileReader reader = new FileReader(filePath);
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            suites = (JSONObject) jsonObject.get("SUITES");
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    public boolean isSuiteRunnable(String suiteName) {
        JSONObject suite = (JSONObject) suites.get(suiteName.toUpperCase());
        return suite != null && "Y".equalsIgnoreCase((String) suite.get("Runmode"));
    }
}
