package com.sample;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

public class FileParser {
    //    public static List<HashMap> parseCranFile(File rawFile) {
//        List<HashMap> toBeIndexed = new ArrayList<HashMap>();
//        List<String> tobeDiced = new ArrayList<String>();
//        tobeDiced = divideFile(rawFile);
//        tobeDiced.stream().map(  FileParser::transformRawToDic);
//    }

    private static List<String> divideFile(File cranFile) {
        List<String> dividedRawDoc = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(cranFile), 4096)) {
            String wholeRawDoc = reader.lines().collect(Collectors.joining(""));
            dividedRawDoc = new ArrayList(Arrays.asList(wholeRawDoc.split(".I")));
            dividedRawDoc.remove(0);
            System.out.println("the number of doc in the whole doc is :" + dividedRawDoc.size());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return dividedRawDoc;
    }

    private static HashMap transformRawToDic(String atomixRawDoc) {

        final File cranAllFile = new File(Utils.RAW_DOC_DIR, Utils.cranAllName);
        final Pattern regrexI = Pattern.compile("\\s*(\\d)");
        final Pattern regrexT = Pattern.compile("\\s*(\\d)");
        final Pattern regrexA = Pattern.compile("\\s*(\\d)");
        final Pattern regrexB = Pattern.compile("\\s*(\\d)");
        final Pattern regrexW = Pattern.compile("\\s*(\\d)");

        return null;
    }

    public static void main(String[] args) {
//        List<HashMap> toBeIndexed = new ArrayList<HashMap>();
//        toBeIndexed = parseCranFile(cranAllFile);
//        System.out.println(toBeIndexed);


//        System.out.println(  divideFile(cranAllFile).get(0));
//        System.out.println(  divideFile(cranAllFile).get(1));
//        System.out.println(  divideFile(cranAllFile).get(1399));
//        System.out.println(  divideFile(cranAllFile).get(1400));
    }
}

