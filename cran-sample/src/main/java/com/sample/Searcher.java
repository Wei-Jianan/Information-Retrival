package com.sample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.synonym.SynonymMap;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.queryparser.classic.QueryParser;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Searcher {
    private Analyzer analyzer;
    private File indexDir;
    private IndexSearcher indexSearcher;
    public static String[] fields = {"text", "abstraction", "author", "bibligraphy"};
    private int counter = 0;

    public Searcher(Indexer indexer) {
        this.indexDir = indexer.indexDir;
        this.analyzer = indexer.analyzer;
        IndexReader indexReader = null;

        try {
            indexReader = DirectoryReader.open(FSDirectory.open(this.indexDir.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.indexSearcher = new IndexSearcher(indexReader);
    }

//    public ArrayList<ArrayList> searchQryList(ArrayList<String> qryList, int numToRanked) {
    public ArrayList<ArrayList<String>> searchQryList(ArrayList<String> qryList, int numToRanked) {
        // functional programming is amazing!!

        return qryList.stream()
                .map(questionStr ->
                        this.search(questionStr, numToRanked)
                )
//                .forEach(System.out::println);
                .map(topDocs -> topDocs.scoreDocs)
                .map(scoreDocs ->
                        Arrays.stream(scoreDocs)
                                .map(scoreDoc -> scoreDoc.doc)
                                .map(doc -> doc.toString())
                                .collect(Collectors.toCollection(ArrayList<String>::new)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public TopDocs search(String questionStr, int numToRanked) {
//        this.counter += 1;
//        System.out.println(" the is the " + this.counter + " question!");
        Query query = null;
        QueryParser queryParser = new MyQueryParser(fields, this.analyzer);
        try {
            query = queryParser.parse(questionStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            return this.indexSearcher.search(query, numToRanked);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws IOException {
        //test code
        Indexer indexer = new Indexer(Utils.INDEX_DIR, new StandardAnalyzer());
        Searcher searcher = new Searcher(indexer);
        String questionStr = FileParser.parseCranQry(Utils.RAW_QRY).get(50);
        System.out.println(questionStr + " !!!!!!!!!!!!!!! the null pointer exection raiser!");
//        searcher.search(questionStr, 10);
//        TopDocs topDocs = searcher.search("what are the structural and aeroelastic problems associated with flight\n" +
//                "of high speed aircraft .", 100);
//        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
//        Arrays.stream(scoreDocs)
//                .map(scoreDoc -> scoreDoc.doc)
//                .forEach(System.out::println);

        System.out.println(searcher.searchQryList(FileParser.parseCranQry(Utils.RAW_QRY), 120 ).get(224).size());

    }
}
