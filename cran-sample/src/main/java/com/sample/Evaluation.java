package com.sample;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Evaluation<T>{
    public static void customizedEvaluate(ArrayList<ArrayList<String>> result, ArrayList<Set<String>> qrel) {
        if (qrel.size() != result.size()) {
            System.out.println("the number of queries is not equal to the number of Qrel.");
        }
        float recall = 0;
        for (int i = 0; i < qrel.size(); ++i) {
//            System.out.println(i);
            recall += findRecall(new HashSet<>(result.get(i)), qrel.get(i));
        }
        recall /= qrel.size();
        double map = 0;
        for(int i = 0; i < qrel.size(); ++i) {
            map +=  findAP(result.get(i), qrel.get(i));
        }
        map /= qrel.size();
        System.out.println("the average recall:\t" + recall + ",  the MAP:\t" + map);
    }

    public static double findAP(ArrayList<String> predicted, Set<String> actual) {
        double precision = 0;
        int count = 0;
        for(int i = 0; i < predicted.size(); ++i) {
            if(actual.contains(predicted.get(i))) {
                HashSet<String> intersection = new HashSet<>(predicted.subList(0, i + 1));
                intersection.retainAll(actual);
                precision += (double) intersection.size() / (double) (i + 1 );
                count += 1;
            }
        }
        double ap = precision / count;
        return Double.isNaN(ap) ? 0 : ap;
    }
    public static float findRecall(Set<String> predicted, Set<String> actual) {
        HashSet<String> intersection = new HashSet<String>(predicted);
        intersection.retainAll(actual);
        float recall = (float) intersection.size() /  actual.size();
        return  Double.isNaN(recall) ? 1 : recall;
    }


    public static void main(String[] args) {

    }
}

