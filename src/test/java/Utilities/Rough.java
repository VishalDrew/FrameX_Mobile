package Utilities;

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
