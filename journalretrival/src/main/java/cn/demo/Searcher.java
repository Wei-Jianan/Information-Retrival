package cn.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class Searcher {
    private Analyzer analyzer;
    private File indexDir;
    private IndexReader indexReader;
    public IndexSearcher indexSearcher;
    public static String[] fields = {"text", "date"}; // TODO add all fields

    public Searcher(Indexer indexer) {
        this.indexDir = indexer.indexDir;
        this.analyzer = indexer.analyzer;
        IndexReader indexReader = null;

        try {
            this.indexReader = DirectoryReader.open(FSDirectory.open(this.indexDir.toPath()));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.indexSearcher = new IndexSearcher(this.indexReader);
        this.indexSearcher.setSimilarity(new BM25Similarity());
    }


    public TopDocs search(String questionStr, int numToRanked) {
        TopDocs topDocs = null;
        QueryParser queryParser = new MultiFieldQueryParser(fields, this.analyzer);
        try {
            Query query = queryParser.parse(questionStr);
            topDocs = this.indexSearcher.search(query, numToRanked);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return topDocs;
    }

    public void searchTopicQuerysandGenerateDocRank(List<TopicQuery> queryObjects, int numToRanked, File DocRank) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(DocRank));
            for (TopicQuery queryObject : queryObjects) {
                TopDocs topDocs = this.searchTopicQuery(queryObject, numToRanked);
                StringBuilder stringBuilder = new StringBuilder();
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                    stringBuilder.append(queryObject.getNum());
                    stringBuilder.append(" ");
                    stringBuilder.append("Q0");
                    stringBuilder.append(" ");
                    stringBuilder.append(this.getDocNo(scoreDoc));
                    stringBuilder.append(" ");
                    stringBuilder.append("0");
                    stringBuilder.append(" ");
                    stringBuilder.append(String.valueOf(scoreDoc.score));
                    stringBuilder.append(" ");
                    stringBuilder.append("STANDARD\n");
                }
                writer.write(stringBuilder.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("the RankDoc to be trec_evaled done.");
//        queryObjects.stream()
//                .map((TopicQuery queryObject) -> this.search(queryObject.formQuery(), numToRanked))
//                .map((TopDocs topDocs) -> topDocs.scoreDocs)
//                .map((ScoreDoc[] scoreDocs) ->
//                                Arrays.stream(scoreDocs)
//                                        .map((ScoreDoc scoreDoc) -> {
//                                            Document document = null;
//                                            try {
//                                                document = indexReader.document(scoreDoc.doc);
//                                            } catch (IOException e) {
//                                                e.printStackTrace();
//                                            }
//                                            StringBuilder builder = new StringBuilder();
//                                            builder.append(queryObjects[i]);
//                                            return "docno=  " + document.getField("docno").stringValue() + " score=" + scoreDoc.score;
////                                            return "hahah";
//                                        })
//                );
    }

    private String getDocNo(ScoreDoc scoreDoc) {
        Document document = null;
        try {
            document = this.indexReader.document(scoreDoc.doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document.getField("docno").stringValue();
    }

    private TopDocs searchTopicQuery(TopicQuery queryObject, int numToRanked) {
        TopDocs topDocs = this.search(queryObject.formQuery(), numToRanked);
        return topDocs;
    }

    private ArrayList<ArrayList<String>> searchQueries(ArrayList<String> qryList, int numToRanked) {
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
                                .map(doc -> (String.valueOf(doc.intValue() + 1))) // hacking
                                .collect(Collectors.toCollection(ArrayList::new)))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void main(String[] args) throws IOException, ParseException {

    }
}
