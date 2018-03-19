import org.apache.commons.cli.*;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;

public class Main {
    private static void addCommandArgsAndInit(String args[]) {
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
        Utils.initialize(true, jsonsDirPath, queriesDirPath);
    }

    private static void test() {

//        Utils.clear()
    }

    public static void main(String args[]) throws IOException {
        addCommandArgsAndInit(args);

        test();

        Analyzer analyzer = new StandardAnalyzer();

        Indexer indexer = new Indexer(Utils.INDEX_DIR, analyzer);
        long startTime = System.currentTimeMillis();
        // if you are not sure use defualt number of Threads
        indexer.indexAll(Utils.JSONS_DIR);
        // second parameter is numThreads , 10 threads take 1 mins to index on my mac
//        indexer.indexAll(Utils.JSONS_DIR, 10);

        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime) / 1000.0 + " seconds");


    }
}
