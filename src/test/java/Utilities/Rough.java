package Utilities;

import Modules.CallPlanData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONObject;

import java.sql.*;
import java.util.*;

import static Utilities.DBConfig.fetchdatafromdb;
import static Utilities.TestDataUtil.gettestdata;

public class Rough {


    public static void main(String[] args) {
        List<String> targetIds = new ArrayList<>();
        // Sample target IDs
        targetIds.add("11345");
        targetIds.add("113434");
        targetIds.add("113567");
        targetIds.add("11453");
        targetIds.add("113445");
        targetIds.add("113487");
        targetIds.add("1134999");
        targetIds.add("1130899");
        targetIds.add("114532");

        String[] callTypes = {"upload", "upload","close"};
        String[] fieldTypes = {"Mandatory only", "All"};
        String[] networkModes = {"Wifi", "Mobiledata"};
        String[] durationInSeconds = {"5","2","10","15","8"};

        List<CallPlanData> callPlanDataList = new ArrayList<>();
        Random random = new Random();

        for (String targetId : targetIds) {
            String callType = callTypes[random.nextInt(callTypes.length)];
            String fields = callType.equals("upload") ? fieldTypes[random.nextInt(fieldTypes.length)] : null;
            String networkMode = networkModes[random.nextInt(networkModes.length)];
            String duration = durationInSeconds[random.nextInt(durationInSeconds.length)];

            CallPlanData callPlanData = new CallPlanData(targetId, callType, fields, networkMode, duration);
            callPlanDataList.add(callPlanData);
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonData = gson.toJson(callPlanDataList);

        System.out.println(jsonData);
    }
}
