package cn.demo;

import org.apache.commons.cli.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    private static void addCommandArgsAndInit(String args[], boolean ifClearIndex) {
        Options options = new Options();

        Option jsons = new Option("j", "jsons", true, "json files directory containing all raw document");
        jsons.setRequired(true);
        options.addOption(jsons);

        Option queries = new Option("q", "queries", true, "queries directory containing 40 topics");
        queries.setRequired(true);
        options.addOption(queries);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("Indexing and searching given 4 journals", options);

            System.exit(1);
            return;
        }

        String jsonsDirPath = cmd.getOptionValue("jsons");
        String queriesDirPath = cmd.getOptionValue("queries");
        // ATTENTION! clear all the index file and remake directory !!
        Utils.initialize(ifClearIndex, jsonsDirPath, queriesDirPath);
    }

    private static void test() {
//        Utils.clear()
    }

    public static void main(String args[]) throws IOException {
        addCommandArgsAndInit(args, false); // TODO change ifclearIndex to false if not debugging indexing phase

        Analyzer analyzer = new StandardAnalyzer();

        Indexer indexer = new Indexer(Utils.INDEX_DIR, analyzer);
        long startTime = System.currentTimeMillis();
        // if you are not sure use defualt number of Threads
//        indexer.indexAll(Utils.JSONS_DIR);
        // second parameter is numThreads , 10 threads take 1 mins to index on my mac
//        indexer.indexAll(Utils.JSONS_DIR, 10); //TODO cancel comment
        long endTime = System.currentTimeMillis();
        System.out.println("Indexing took " + (endTime - startTime) / 1000.0 + " seconds");

        List<TopicQuery> queryObjects = QueryParser.parseQuery(Utils.QUERIES_FIR);
        Searcher searcher = new Searcher(indexer);

        startTime = System.currentTimeMillis();
        searcher.searchTopicQuerysandGenerateDocRank(queryObjects, 1000, Utils.DOC_RANK_FILE);
        endTime = System.currentTimeMillis();
        System.out.println("searching took " + (endTime - startTime) / 1000.0 + " seconds");
        // query the first question[1]
//        TopDocs topDocs = searcher.search(queries.get(1), 50);
//        Arrays.stream(topDocs.scoreDocs)
//                .map((ScoreDoc scoreDoc) -> {
//                    Document document = null;
//                    try {
//                        document = indexReader.document(scoreDoc.doc);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append()
//                    return "docno=  " + document.getField("docno").stringValue() + " score=" + scoreDoc.score;
//                })
//                .forEach(System.out::println);
    }
}
