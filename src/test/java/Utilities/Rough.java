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
        // Sample target IDs
        List<String> targetIds = new ArrayList<>();
        targetIds.add("11345");
        targetIds.add("113434");
        targetIds.add("113567");
        targetIds.add("11453");
        targetIds.add("113445");
        targetIds.add("113487");
        targetIds.add("1134999");
        targetIds.add("1130899");
        targetIds.add("114532");

        // Sample JSON data structure
        String[] callTypes = {"upload", "upload", "close", "close"};
        String[] fieldTypes = {"Mandatory only", "Non Mandatory only"};

        // Create a list to store JSON data for each target ID
        List<CallPlanData> callPlanDataList = new ArrayList<>();

        // Generate JSON data for each target ID
        Random random = new Random();
        for (String targetId : targetIds) {
            // Randomly select call type and fields
            String callType = callTypes[random.nextInt(callTypes.length)];
            String fields = fieldTypes[random.nextInt(fieldTypes.length)];

            // Create CallPlanData object
            CallPlanData callPlanData = new CallPlanData(targetId, callType, fields);

            // Add to the list
            callPlanDataList.add(callPlanData);
        }

        // Convert the list to JSON format using Gson
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonData = gson.toJson(callPlanDataList);

        // Print JSON data
        System.out.println(jsonData);
    }

}
