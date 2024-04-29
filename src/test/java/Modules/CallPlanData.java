package Modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CallPlanData {

    private String targetId;
    private String callType;
    private String fields;
    private String networkMode;
    private String durationInSeconds;

    // Constructor, getters, and setters
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

}
