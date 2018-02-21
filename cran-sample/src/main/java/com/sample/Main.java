package com.sample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
        try {
            Utils.initialize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<HashMap> rawDocDic = FileParser.parseCranFile(Utils.RAW_DOC);
        Analyzer analyzer = new StandardAnalyzer(); // to customize the Analyzer;
        try {
            Indexer indexer = new Indexer(Utils.INDEX_DIR, analyzer);
            indexer.indexDicList(rawDocDic);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
