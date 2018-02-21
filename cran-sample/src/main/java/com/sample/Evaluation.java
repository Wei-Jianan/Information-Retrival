package com.sample;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Evaluation {
    public static void customizedEvaluate(ArrayList<ArrayList<String>> result, ArrayList<Set<String>> qrel) {
        if (qrel.size() != result.size()) {
            System.out.println("the number of queries is not equal to the number of Qrel.");
        }
        float recall = 0;
        for (int i = 0; i < qrel.size(); ++i) {
            recall += findRecall(new HashSet<String>(result.get(i)), qrel.get(i));
        }
        recall /= qrel.size();

        System.out.println("the average recall:\t" + recall + ",  the MAP:\t" + "pass");
    }

    public static float findMAO()
    public static float findRecall(Set<String> predicted, Set<String> actical) {
        HashSet<String> intersection = new HashSet<String>(predicted);
        intersection.retainAll(actical);
        float recall = (float) intersection.size() / (float) actical.size();
//        System.out.println(recall);
        return  Float.isNaN(recall) ? 1 : recall;
    }

    public static void main(String[] args) {

    }
}

