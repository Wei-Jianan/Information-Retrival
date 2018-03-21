package cn.demo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

//import org.dom4j.DocumentException;
//import org.dom4j.Node;
//import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class QueryParser {


    public static List<String> parseQuery(File queryFile) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(queryFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        scanner.useDelimiter(Pattern.compile("<top>"));
        List<String> queries;
        List<TopicQuery> queriyObjects = new ArrayList<TopicQuery>();
        while (scanner.hasNext()) {
            TopicQuery query = new TopicQuery();
            String[] lines = scanner.next().split("<num>|<title>|<desc>|<narr>");
            query.setNum(Integer.parseInt(lines[1].replaceAll("Number:", "").trim()));
            query.setTitle(lines[2].trim());
            query.setDescription(lines[3].replaceAll("Description:", "").trim());
            query.setNarrative(lines[4].replaceAll("Narrative:|</top>", "").trim());
            queriyObjects.add(query);
        }
        queries = formQueries(queriyObjects);
        return queries;
    }

    public static List<String> formQueries(List<TopicQuery> queriyObjects) {
        List<String> queries = new ArrayList<>();
        for (TopicQuery objects : queriyObjects) {
            queries.add( objects.getTitle()  + objects.getDescription());
        }
        return queries;
    }


}
