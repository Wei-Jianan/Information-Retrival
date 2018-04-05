package cn.demo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.mlt.MoreLikeThisQuery;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.*;
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
    private IndexSearcher indexSearcher;
    private static String[] fields = {"text"};
    //If searching over multiple fields - can use this to weight them
    private static Map<String, Float> boosts = new HashMap<>();
    static {
      boosts.put(fields[0], 1.0f);
    }

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
//        this.indexSearcher.setSimilarity(new BM25Similarity());
        Similarity similarities[] = {
                new BM25Similarity(2, (float) 0.89),
                new DFRSimilarity(new BasicModelIn(), new AfterEffectB(), new NormalizationH1()),
                new LMDirichletSimilarity(1500)
        };
        this.indexSearcher.setSimilarity(new MultiSimilarity(similarities));
    }

    private Query expandQuery(Query query_to_expand, int num_docs_for_expansion) throws IOException {
        System.out.println("Original query: " + query_to_expand.toString());
        TopDocs topDocs = this.indexSearcher.search(query_to_expand, num_docs_for_expansion);
        BooleanQuery.Builder query_builder = new BooleanQuery.Builder();
        query_builder.add(query_to_expand, BooleanClause.Occur.SHOULD);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document topDoc = this.indexReader.document(scoreDoc.doc);
            String top_doc_text = topDoc.getField("text").stringValue();
            String[] moreLikeFields = {"text"};
            MoreLikeThisQuery mltq = new MoreLikeThisQuery(top_doc_text,
                    moreLikeFields,
                    this.analyzer,
                    "text");
            Query new_query = mltq.rewrite(this.indexReader);
            query_builder.add(new_query, BooleanClause.Occur.SHOULD);
        }
        Query expanded_query = query_builder.build();
        System.out.println("Expanded query" + expanded_query.toString());
        return expanded_query;
    }

    public TopDocs search(String questionStr, int numToRanked) {
        TopDocs topDocs = null;
        QueryParser queryParser = new QueryParser("text", this.analyzer);
        try {
            Query query = queryParser.parse(QueryParser.escape(questionStr));
            //topDocs = this.indexSearcher.search(query, numToRanked);
            Query expanded_query = expandQuery(query, 5);
            topDocs = this.indexSearcher.search(expanded_query, numToRanked);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return topDocs;
    }

    private TopDocs searchTopicQuery(TopicQuery queryObject, int numToRanked) {
        TopDocs topDocs = this.search(queryObject.formQuery(), numToRanked);
        return topDocs;
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

    public void searchTopicQuerysandGenerateDocRank(List<TopicQuery> queryObjects, int numToRanked, File DocRank) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(DocRank));
            for (TopicQuery queryObject : queryObjects) {
                TopDocs topDocs = this.searchTopicQuery(queryObject, numToRanked);
                String string = null;
                for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                     string = String.format("%d Q0 %s 0 %f STANDARD\n", queryObject.getNum(), this.getDocNo(scoreDoc), scoreDoc.score );
                    writer.write(string);
                }
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


    private ArrayList<ArrayList<String>> searchQueries(ArrayList<String> qryList, int numToRanked) {
        // functional programming is amazing!!
        return qryList.stream()
                .parallel()
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

}
