package com.sample;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;

import java.util.Map;

public class MyQueryParser extends MultiFieldQueryParser{
    public MyQueryParser(String[] fields, Analyzer analyzer) {
        super(fields, analyzer);
    }
}
