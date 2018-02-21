package com.sample;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Indexer {
    public Analyzer analyzer;
    public File indexDir;
    private IndexWriter writer;
    private IndexWriterConfig config;
    //    private boolean ifCreate = false;
    private boolean ifCreate = true;

    Indexer(File indexDir, Analyzer analyzer) throws IOException {
        if (!indexDir.isDirectory()) {
            indexDir.mkdir();
        }
        this.indexDir = indexDir;
        Directory dir = FSDirectory.open(indexDir.toPath());
        this.analyzer = analyzer;
        this.config = new IndexWriterConfig(this.analyzer);
        if (ifCreate) {
            this.config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            this.config.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }
        this.writer = new IndexWriter(dir, this.config);

    }

    public void indexDicList(ArrayList<HashMap> dicList) {
        for (Map<String, String> dic : dicList) {
            indexDic(dic);
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Index built failed!");
        }
        System.out.println("Index built succeed!");
    }

    public void indexDic(Map<String, String> dic) {
        Field field;
        Document doc = new Document();
        for (String key : dic.keySet()) {
            if (key == "identity") {
                field = new StringField(key, dic.get(key), Field.Store.YES);
            } else {
                field = new TextField(key, dic.getOrDefault(key, ""), Field.Store.YES);
            }
            doc.add(field);
        }
        try {
            if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
                writer.addDocument(doc);
            } else {
                writer.updateDocument(new Term("idendity", dic.get("diendity")), doc);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            Indexer indexer = new Indexer(Utils.INDEX_DIR, new StandardAnalyzer());
            ArrayList<HashMap> hashMaps = FileParser.parseCranFile(Utils.RAW_DOC);
            indexer.indexDicList(hashMaps);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}