package com.sample;


import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.LowerCaseFilter;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.StopwordAnalyzerBase;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.WordlistLoader;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardFilter;
import org.apache.lucene.analysis.standard.StandardTokenizer;

public final class MyAnalyzer extends StopwordAnalyzerBase {

    public static final CharArraySet ENGLISH_STOP_WORDS_SET;

    static {
        final List<String> stopWords = Arrays.asList(
                "a", "an", "and", "are", "as", "at", "be", "but", "by",
                "for", "if", "in", "into", "is", "it",
                "no", "not", "of", "on", "or", "such",
                "that", "the", "their", "then", "there", "these",
                "they", "this", "to", "was", "will", "with",
                "can", "simple", "good", "finally", "new", "fairly", "near", "over", "where", "give", "been", "has",
                "theory"
                , "part", "consider", "near", "one", "two", "why", "made", "thin", "problem", "problems", "local", "global"
                , "more", "paper", "shows", "brief", "aspect", "mach"//,"points"
                , "detail", "details", "although", "more", "may", "including", "its", "say", "said", "says"
                , "really", "per", "were", "was", "do", "can", "slowly", "what", "previous", "regular", "still", "though", "effected", "term"
                , "form", "forms", "pure", "method", "methods", "subjected", "approach", "process", "processes", "without", "due",
                "et", "al", "measurement", "treatment", "conduction"


        );

        final CharArraySet stopSet = new CharArraySet(stopWords, true);
        ENGLISH_STOP_WORDS_SET = CharArraySet.unmodifiableSet(stopSet);
    }


    private int maxTokenLength = 2000;

    public static final CharArraySet STOP_WORDS_SET = ENGLISH_STOP_WORDS_SET;

    public MyAnalyzer(CharArraySet stopWords) {
        super(stopWords);
    }

    public MyAnalyzer() {
        this(STOP_WORDS_SET);
    }
//
//  public void setMaxTokenLength(int length) {
//    maxTokenLength = length;
//  }
//  public int getMaxTokenLength() {
//    return maxTokenLength;
//  }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        final StandardTokenizer src = new StandardTokenizer();
        src.setMaxTokenLength(maxTokenLength);
        TokenStream tok = new StandardFilter(src);
        tok = new LowerCaseFilter(tok);
        tok = new StopFilter(tok, stopwords);
        return new TokenStreamComponents(src, tok) {
            @Override
            protected void setReader(final Reader reader) {
                // So that if maxTokenLength was changed, the change takes
                // effect next time tokenStream is called:
                src.setMaxTokenLength(MyAnalyzer.this.maxTokenLength);
                super.setReader(reader);
            }
        };
    }

    @Override
    protected TokenStream normalize(String fieldName, TokenStream in) {
        TokenStream result = new StandardFilter(in);
        result = new LowerCaseFilter(result);
        return result;
    }
}
