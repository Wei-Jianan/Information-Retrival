package com.sample;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

public class FileParser {
    public static final File RAW_DOC = new File(Utils.RAW_DOC_DIR, Utils.cranAllName);

    public static ArrayList<HashMap> parseCranFile(File rawFile) {
        List<String> tobeDiced = new ArrayList<String>();
        tobeDiced = divideFile(rawFile);
        List<HashMap> toBeIndexed =
          tobeDiced.stream()
                .map(FileParser::transformRawToDic)
                .collect(Collectors.toList());

        return new ArrayList(toBeIndexed);
    }

    private static List<String> divideFile(File cranFile) {
        List<String> dividedRawDoc = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(cranFile), 4096)) {
            String wholeRawDoc = reader.lines().collect(Collectors.joining(""));
            dividedRawDoc = new ArrayList(Arrays.asList(wholeRawDoc.split(".I")));
            dividedRawDoc.remove(0);
//            System.out.println("the number of doc in the whole doc is :" + dividedRawDoc.size());

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

        HashMap dic = new HashMap();

        final Pattern regex = Pattern.compile("^.?(\\d*)\\.T(.*)\\.A(.*)\\.B(.*)\\.W(.*)");
        Matcher matcher = regex.matcher(atomixRawDoc);
        if (matcher.matches()) {

            String identity = matcher.group(1);
            String abstraction = matcher.group(2);
            String author = matcher.group(3);
            String bibliography = matcher.group(4);
            String text = matcher.group(5);

            dic.put("identity", identity);
            dic.put("abstraction", abstraction);
            dic.put("author", author);
            dic.put("bibliography", bibliography);
            dic.put("text", text);
        }

        return dic;
    }

    public static void main(String[] args) {
//        List<HashMap> toBeIndexed = new ArrayList<HashMap>();
//        toBeIndexed = parseCranFile(cranAllFile);
        System.out.println(divideFile(RAW_DOC).get(1300));
//        String totested = divideFile(RAW_DOC).get(2);
//        System.out.println(transformRawToDic(totested));

        List<HashMap> hashMaps = parseCranFile(RAW_DOC);
        System.out.println(hashMaps.get(3).get("identity"));
        System.out.println(hashMaps.get(1).get("identity"));
        System.out.println(hashMaps.get(1398).get("identity"));

    }
}

