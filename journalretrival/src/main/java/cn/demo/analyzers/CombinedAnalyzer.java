package cn.demo.analyzers;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.LowerCaseFilterFactory;
import org.apache.lucene.analysis.core.StopFilterFactory;
import org.apache.lucene.analysis.en.KStemFilterFactory;
import org.apache.lucene.analysis.en.PorterStemFilterFactory;
import org.apache.lucene.analysis.ngram.NGramTokenizerFactory;
import org.apache.lucene.analysis.standard.StandardTokenizerFactory;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilterFactory;
import org.apache.lucene.analysis.en.EnglishPossessiveFilterFactory;
import org.apache.lucene.analysis.standard.StandardFilterFactory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

//This class builds a custom analyzer using a Tokenizer factory
//Multiple TokenFilterFactories and CharFilterFactories
//See https://lucene.apache.org/core/7_2_1/core/org/apache/lucene/analysis/package-summary.html
//For an explanation of the differences between these three factories

public class CombinedAnalyzer {
  //NOTE: This is not in use right now - use it if you want to use Ngram Filtering
  private static final Map<String, String> N_GRAM_SIZE;
  static
  {
    N_GRAM_SIZE = new HashMap<String, String>();
    N_GRAM_SIZE.put("minGramSize", "1");
    N_GRAM_SIZE.put("maxGramSize", "2");
  }

  //This creates the analyzer:
  //To change the stopwords, edit the "stopwords.txt" file in the config dir
  //The format of this file is that each line indicates a new stop word
  //Lines beginning with # indicate a comment
  static public Analyzer BuildAnalyzer(String dir) throws IOException {
    return CustomAnalyzer.builder(Paths.get(dir))
            .withTokenizer(StandardTokenizerFactory.class)
            //Normalises tokens from standard tokenizer
            .addTokenFilter(StandardFilterFactory.class)
            //Turns all to lower case - needed for other filters
            .addTokenFilter(LowerCaseFilterFactory.class)
            //Removes 's from tokens
            .addTokenFilter(EnglishPossessiveFilterFactory.class)
            //Performs stopword filtering with the words in "stopwords.txt"
            .addTokenFilter(StopFilterFactory.class,
                    "ignoreCase", "true", "words", "stopwords.txt", "format", "wordset")
            //Performs porter stemming algorithm
            .addTokenFilter(PorterStemFilterFactory.class)
            .build();
  }
}
