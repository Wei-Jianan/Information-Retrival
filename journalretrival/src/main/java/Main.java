import org.apache.commons.cli.*;

public class Main {
    public static void main(String args[]) {
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
        Utils.initialize(true, jsonsDirPath, queriesDirPath);
    }
}
