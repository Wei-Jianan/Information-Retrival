package com.sample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.BM25Similarity;

import java.io.IOException;
import java.util.*;

public class Main {
    private static int numToRank = 50; // num of result to ranked because 30 is the most people are will to see.
    private static float cutoff = 4f; // the score larger than 3 will be considered as not relevant.

    public static void main(String[] args) throws IOException {
        try {
            Utils.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Analyzer analyzer;
        Indexer indexer;
        Searcher searcher;

        ArrayList<HashMap> rawDocDic = FileParser.parseCranFile(Utils.RAW_DOC);
        ArrayList<String> qryList = FileParser.parseCranQry(Utils.RAW_QRY);
        ArrayList<Set<String>> qrel = FileParser.parseQrel(Utils.RAW_QREL, cutoff);
        ArrayList<ArrayList<String>> results;
        analyzer = new StandardAnalyzer();
        indexer = new Indexer(Utils.INDEX_DIR, analyzer);
        indexer.indexDicList(rawDocDic);
        searcher = new Searcher(indexer);
        searcher.indexSearcher.setSimilarity(new ClassicSimilarity());
        results = searcher.searchQryList(qryList, numToRank);
//        results = results.stream()
//                .map(list -> list.stream()
//                                    .map(str -> Integer.parseInt(str) + 1)
//                                    .map(num -> num.toString())
//                                    .collect(Collectors.toCollection(ArrayList::new)))
//                .collect(Collectors.toCollection(ArrayList::new));
        System.out.println("StandardAnalyzer, with ClassicSimilarity");
        Evaluation.customizedEvaluate(results, qrel);

        searcher.indexSearcher.setSimilarity(new BM25Similarity());
        results = searcher.searchQryList(qryList, numToRank);
        System.out.println("StandardAnalyzer, with BM25Similarity");
        Evaluation.customizedEvaluate(results, qrel);

        analyzer = new MyAnalyzer();
        indexer = new Indexer(Utils.INDEX_DIR, analyzer);
        indexer.indexDicList(rawDocDic);
        searcher = new Searcher(indexer);
        searcher.indexSearcher.setSimilarity(new ClassicSimilarity());
        results = searcher.searchQryList(qryList, numToRank);
        System.out.println("Costumized Analyzer, with ClassicSimilarity");
        Evaluation.customizedEvaluate(results, qrel);

        searcher.indexSearcher.setSimilarity(new BM25Similarity());
        results = searcher.searchQryList(qryList, numToRank);
        System.out.println("Costumized Analyzer, with BM25Similarity");
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
