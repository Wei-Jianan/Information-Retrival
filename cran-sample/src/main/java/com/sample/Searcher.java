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
import org.apache.lucene.search.similarities.BM25Similarity;
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
    public IndexSearcher indexSearcher;
    public static String[] fields = {"text", "abstraction", "author", "bibliography"};
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
        this.indexSearcher.setSimilarity(new BM25Similarity());// TODO !!!!!!!!!!!!!!!!!
    }

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
                                .map(doc -> (String.valueOf(doc.intValue() + 1))  ) // hacking
                                .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public TopDocs search(String questionStr, int numToRanked) {
//        this.counter += 1;
//        System.out.println(" the is the " + this.counter + " question!");
        Query query = null;
        QueryParser queryParser = new MultiFieldQueryParser(fields, this.analyzer);
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

    public static void main(String[] args) throws IOException, ParseException {
        //test code
        String questionStr = FileParser.parseCranQry(Utils.RAW_QRY).get(0);
//        System.out.println(questionStr + " !!!!!!!!!!!!!!! the null pointer exection raiser!");
        Analyzer analyzer = new StandardAnalyzer();

        QueryParser queryParser = new QueryParser("text", analyzer);
        Query query = queryParser.parse(questionStr);
        IndexReader indexReader = DirectoryReader.open(FSDirectory.open(Utils.INDEX_DIR.toPath()));
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        TopDocs topDocs = indexSearcher.search(query , 50);
//        TopDocs topDocs = searcher.search("what are the structural and aeroelastic problems associated with flight\n" +
//                "of high speed aircraft .", 100);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        List result = Arrays.stream(scoreDocs)
                .map(scoreDoc -> scoreDoc.doc)
                .collect(Collectors.toList());

        System.out.println(result);
        System.out.println("-------------------------\n\n");
        Indexer indexer = new Indexer(Utils.INDEX_DIR, new StandardAnalyzer());
        Searcher searcher = new Searcher(indexer);
//        System.out.println(searcher.searchQryList(FileParser.parseCranQry(Utils.RAW_QRY), 120 ).get(224).size());
        topDocs  = searcher.search(questionStr, 50);
        scoreDocs = topDocs.scoreDocs;
        List list = Arrays.stream(scoreDocs)
                .map(scoreDoc -> scoreDoc.doc)
                .collect(Collectors.toList());
        System.out.println(result);

    }
}
