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

    public CallPlanData(String targetId, String callType, String fields) {
        this.targetId = targetId;
        this.callType = callType;
        this.fields = fields;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getCallType() {
        return callType;
    }

    public String getFields() {
        return fields;
    }

}
