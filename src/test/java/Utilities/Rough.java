package Utilities;

import Modules.CallPlan.CallPlanData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.*;

public class Rough {

    public static void main(String[] args) {

        String[] targets = {"5","2","10","4","6"};
        Random rand = new Random();
        int randomIndex = rand.nextInt(targets.length);
        String count = targets[randomIndex];

        System.out.println(count);
    }

}
