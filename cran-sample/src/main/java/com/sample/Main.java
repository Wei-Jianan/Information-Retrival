package com.sample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Main {
    private static int numToRank = 30; // num of result to ranked because 30 is the most people are will to see.
    private static float cutoff = 3.1f; // the score larger than 3 will be considered as not relevant.
    public static void main(String[] args) {
//        try {
//            Utils.initialize();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        ArrayList<HashMap> rawDocDic = FileParser.parseCranFile(Utils.RAW_DOC);
        ArrayList<String> qryList = FileParser.parseCranQry(Utils.RAW_QRY);
        ArrayList<Set<String>> qrel = FileParser.parseQrel(Utils.RAW_QREL, cutoff);

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
        ArrayList<ArrayList<String>> result = searcher.searchQryList(qryList, numToRank);
//        System.out.println(result.get(225));
        Evaluation.customizedEvaluate(result, qrel);
    }
}
