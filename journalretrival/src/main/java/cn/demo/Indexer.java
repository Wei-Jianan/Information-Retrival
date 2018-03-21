package cn.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Indexer {
    //    private int counter;
    public Analyzer analyzer;
    public File indexDir;
    private IndexWriter writer;
    private IndexWriterConfig config;
    private ObjectMapper mapper = new ObjectMapper();

    Indexer(File indexDir, Analyzer analyzer) throws IOException {
        if (!indexDir.isDirectory()) {
            indexDir.mkdir();
        }
        this.indexDir = indexDir;
        Directory dir = FSDirectory.open(indexDir.toPath());
        this.analyzer = analyzer;
        this.config = new IndexWriterConfig(this.analyzer);
        this.config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
//        this.config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        this.config.setRAMPerThreadHardLimitMB(200);
        this.writer = new IndexWriter(dir, this.config);

    }

    public void indexAll(File jsonDir) {
        this.indexAll(jsonDir, 0);
    }

    public void indexAll(File jsonDir, int numThreads) {
        if (numThreads > 0) {
            System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(numThreads));
        }
        this.getTasks(jsonDir).stream()
                .parallel()
                .map(this::getMapFromJson)
                .forEach(this::indexDic);
        try {
            writer.close();
            System.out.println("Index built succeed!");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Index built failed!");
        }

    }

    private List<File> getTasks(File jsonDir) {
        // for convenience of debugging, actually no need
        List<File> tasks = Arrays.asList(jsonDir.listFiles());
        return tasks;
    }

    private Map<String, String> getMapFromJson(File jsonFile) {
        HashMap<String, String> map = null;
        Map<String, String> typedMap = null;
        try {
            map = this.mapper.readValue(jsonFile, HashMap.class);
//            typedMap = new ObjectMapper().readValue(jsonFile, new TypeReference<Map<String, String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private void indexDic(Map<String, String> dic) {
        Field field;
        Document doc = new Document();
        for (String key : dic.keySet()) {
            if (key == "docno") {
                field = new StringField(key, dic.get(key), Field.Store.YES);
            } else {
                field = new TextField(key, dic.getOrDefault(key, ""), Field.Store.NO);
            }
            doc.add(field);
        }
        try {
            this.writer.addDocument(doc);
//            System.out.println(this.counter++);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {


//         It show that there are quite a lot of LA files without "text" tag
        Indexer indexer = new Indexer(Utils.INDEX_DIR, new StandardAnalyzer());
        indexer.getTasks(Utils.JSONS_DIR).stream()
                .map(indexer::getMapFromJson)
                .filter(map ->
                        !map.containsKey("docno") || !map.containsKey("text"))// row document without <TEXT>
                .map(map -> map.getOrDefault("docno", "!!!!!!!!!!No docno"))
                .forEach(System.out::println);
    }


}
