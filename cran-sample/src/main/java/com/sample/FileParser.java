package com.sample;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.util.regex.Pattern.compile;

public class FileParser {

    public static ArrayList<HashMap> parseCranFile(File rawFile) {
        List<String> tobeDiced;
        tobeDiced = divideFile(rawFile);
        List<HashMap> toBeIndexed =
                tobeDiced.stream()
                        .map(FileParser::transformRawToDic)
                        .collect(Collectors.toList());
        return new ArrayList(toBeIndexed);
    }

    public static ArrayList<String> parseCranQry(File rawQry) {
        ArrayList<String> dividedRawQry = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(rawQry), 4096)) {
            String wholeRawQry = reader.lines()
                    .collect(Collectors.joining(" "))
                    .replace("?", " ");
            dividedRawQry = new ArrayList(Arrays.asList(wholeRawQry.split("\\.I.*?\\.W ")));
            dividedRawQry.remove(0);
//            System.out.println(dividedRawQry.toArray().length);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dividedRawQry;
    }

    public static ArrayList<Set<String>> parseQrel(File rawQrel, float cutoff) {
        // the most ugly code I have ever written!!! To be refactor
        // For the scalability reason, The Set type should be change to ArrayList
        ArrayList<Set<String>> dividedRawQrel = new ArrayList();
        if (cutoff > 5 || cutoff < 1) {
            System.out.println("cutoff should between 1 and 5");
            System.exit(1);
        }
        final Pattern regex = Pattern.compile("(\\d+)\\D*?(\\d+)\\D*?(-?\\d+)\\D*");
        Matcher matcher;
        try (BufferedReader reader = new BufferedReader(new FileReader(rawQrel), 4096)) {
            String line;
            while ((line = reader.readLine()) != null) {
                matcher = regex.matcher(line);
                if (matcher.matches()) {
                    int QueryId = Integer.parseInt(matcher.group(1));
                    String RelevantDocId = matcher.group(2);
                    int relevancyScore = Integer.parseInt(matcher.group(3));
//                    System.out.println("QueryID: " + QueryId + " RelevantDocId: " + RelevantDocId + " relevancyScore: " + relevancyScore);
                    try {
                        Set set = dividedRawQrel.get(QueryId - 1);
                        if (relevancyScore <= cutoff && relevancyScore > 0) {
                            set.add(RelevantDocId);
                        }
                    } catch (IndexOutOfBoundsException e) {
//                        e.printStackTrace();
//                        System.out.println(" gotcha ya!!");
                        Set<String> set = new HashSet<String>();
                        if (relevancyScore <= cutoff && relevancyScore > 0) {
                            set.add(RelevantDocId);
                        }
                        dividedRawQrel.add(set);
                    }
//                    System.out.println("still working!");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dividedRawQrel;
    }

    private static List<String> divideFile(File cranFile) {
        List<String> dividedRawDoc = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(cranFile), 4096)) {
            String wholeRawDoc = reader.lines().collect(Collectors.joining(" "));
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

    private static HashMap<String, String> transformRawToDic(String atomixRawDoc) {

        HashMap<String, String> dic = new HashMap();

        final Pattern regex = Pattern.compile("^\\D*(\\d*)\\D*\\.T(.*)\\.A(.*)\\.B(.*)\\.W(.*)");
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
//        System.out.println(parseCranQry(Utils.RAW_QRY).get(0));
//        System.out.println(parseCranQry(Utils.RAW_QRY).get(2));
//        System.out.println(parseCranQry(Utils.RAW_QRY).);
//        String totested = divideFile(RAW_DOC).get(2);
//        System.out.println(transformRawToDic(totested));
////
//        List<String> hashMaps = divideFile(Utils.RAW_DOC);
//        System.out.println(        transformRawToDic(hashMaps.get(3)).get("identity"));
//        System.out.println(parseCranQry(Utils.RAW_QRY).get(4));
//        System.out.println("the parsed cran Qrel look like: " + parseCranFile(Utils.RAW_DOC).get(1));
        System.out.println(parseCranFile(Utils.RAW_DOC).get(1200));
//        System.out.println("the parsed cran Qrel look like: " + parseQrel(Utils.RAW_QREL, (float) 3.1));
    }


}

