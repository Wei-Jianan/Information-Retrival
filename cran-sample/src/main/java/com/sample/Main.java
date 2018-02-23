package com.sample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static int numToRank = 50; // num of result to ranked because 30 is the most people are will to see.
    private static float cutoff = 4f; // the score larger than 3 will be considered as not relevant.
    public static void main(String[] args) {
        try {
            Utils.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        int idx = 0;
        ArrayList<HashMap> rawDocDic = FileParser.parseCranFile(Utils.RAW_DOC);
        ArrayList<String> qryList = FileParser.parseCranQry(Utils.RAW_QRY);
        ArrayList<Set<String>> qrel = FileParser.parseQrel(Utils.RAW_QREL, cutoff);
//        qryList.sort(String::compareTo);
//        String temp = qryList.get(idx);
//        qryList.add(temp);
//        qryList.remove(idx);
//        System.out.println(qrel.get(idx));
        Analyzer analyzer = new StandardAnalyzer(); // to customize the Analyzer;
        Indexer indexer = null;
        try {
            indexer = new Indexer(Utils.INDEX_DIR, analyzer);
            indexer.indexDicList(rawDocDic);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        Searcher searcher  = new Searcher(indexer);
        ArrayList<ArrayList<String>> results = searcher.searchQryList(qryList, numToRank);
//        results = results.stream()
//                .map(list -> list.stream()
//                                    .map(str -> Integer.parseInt(str) + 1)
//                                    .map(num -> num.toString())
//                                    .collect(Collectors.toCollection(ArrayList::new)))
//                .collect(Collectors.toCollection(ArrayList::new));
        Evaluation.customizedEvaluate(results, qrel);

//        TopDocs topDocs = searcher.search(qryList.get(idx), numToRank);
//                ScoreDoc[] scoreDocs = topDocs.scoreDocs;
//        List result = Arrays.stream(scoreDocs)
//                .map(scoreDoc -> scoreDoc.doc)
//                .collect(Collectors.toList());
//        System.out.println("standeard answer: \t" + result);
//        System.out.println("results:\t\t\t" + results.get(idx));
//        Evaluation.findAP(results.get(idx), qrel.get(idx));
    }
}
