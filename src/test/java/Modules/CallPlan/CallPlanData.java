package Modules.CallPlan;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static Base.TestSetup.log;
import static Utilities.Constants.CALLPLAN_TEST_DATA_FILE;

public class CallPlanData {
    public static int targetSize;
    private String targetId;
    private String callType;
    private String fields;
    private String networkMode;
    private String durationInSeconds;


    public CallPlanData(String targetId, String callType, String fields, String networkMode, String durationInSeconds) {
        this.targetId = targetId;
        this.callType = callType;
        this.fields = fields;
        this.networkMode = networkMode;
        this.durationInSeconds = durationInSeconds;
    }

    // Getter and Setter methods
    // Ensure that they are public so that Gson can access them
    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getNetworkMode() {
        return networkMode;
    }

    public void setNetworkMode(String networkMode) {
        this.networkMode = networkMode;
    }

    public String getDurationInSeconds() {
        return durationInSeconds;
    }

    public void setDurationInSeconds(String durationInSeconds) {
        this.durationInSeconds = durationInSeconds;
    }


    public static boolean generateCallPlanScenarios(List<String> targets) {

        String jsonData = setcallplanscenarios(targets);

        try (FileWriter fileWriter = new FileWriter(CALLPLAN_TEST_DATA_FILE)) {
            fileWriter.write(jsonData);
            log.info("JSON data has been written to the file successfully.");
            return true; // Return true if file writing is successful
        } catch (IOException e) {
            log.error("Error writing JSON data to the file: " + e.getMessage());
            return false; // Return false if an error occurs during file writing
        }
    }

    public static String setcallplanscenarios(List<String> targetids) {
        String[] callTypes = {"Close", "Close","Close","Close"};
        String[] fieldTypes = {"Mandatory only", "All"};
        String[] networkModes = {"Enable", "Enable"};
        String[] durationInSeconds = {"5","2","10","15","8"};


        List<JsonObject> callPlanDataList = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < targetids.size(); i++) {
            String targetId = targetids.get(i);
            String callType = callTypes[random.nextInt(callTypes.length)];
            String fields = callType.equalsIgnoreCase("upload") ? fieldTypes[random.nextInt(fieldTypes.length)] : null;
            String networkMode = networkModes[random.nextInt(networkModes.length)];
            String duration = durationInSeconds[random.nextInt(durationInSeconds.length)];

            JsonObject uploadData = new JsonObject();
            uploadData.addProperty("Targetid", targetId);
            uploadData.addProperty("Call Type", callType);
            uploadData.addProperty("Fields", fields);
            uploadData.addProperty("Network Mode", networkMode);
            uploadData.addProperty("Disable Duration", duration);

            String logMessage = "Target ID: " + targetId + ", Call Type: " + callType + ", Fields: " + fields + ", Network Mode: " + networkMode + ", Disable Duration: " + duration;
            log.info(logMessage);
            callPlanDataList.add(uploadData);
        }

        JsonObject callPlanJson = new JsonObject();
        for (int i = 0; i < callPlanDataList.size(); i++) {
            callPlanJson.add("Uploaddata" + (i + 1), callPlanDataList.get(i));
        }

        targetSize = callPlanJson.size();
        JsonObject result = new JsonObject();
        result.add("Call Plan", callPlanJson);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(result);
    }


}
