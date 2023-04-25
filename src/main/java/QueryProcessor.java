import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import java.util.ArrayList;


import java.io.IOException;
import java.io.StringReader;

class query {
    public static void QueryProcessor(String prompt) {
        ArrayList<String> words = new ArrayList<String>();
        Analyzer analyzer = new EnglishAnalyzer();
        TokenStream tokenStream = analyzer.tokenStream(null, new StringReader(prompt));

        TokenStream porterFilter = new PorterStemFilter(tokenStream);
        CharTermAttribute termAtt = porterFilter.addAttribute(CharTermAttribute.class);

        try {
            porterFilter.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                if (!porterFilter.incrementToken()) break;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            String term = termAtt.toString();
            words.add(term);
        }

        System.out.println(words);
    }


}

