package QueryProcessor;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.PorterStemFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import java.util.ArrayList;


import java.io.IOException;
import java.io.StringReader;


public class queryP {

    static final String[] StopWords = new String[]{"a", "about", "actually", "almost", "also",
            "although", "always", "am", "an", "and", "any", "are",
            "as", "at", "be", "became", "become", "but", "by", "can", "could", "did", "do", "does",
            "each", "either", "else", "for", "from", "had", "has", "have", "hence", "how", "i", "if", "in",
            "is", "it", "its", "just", "may", "maybe", "me", "might", "mine", "must", "my", "neither", "nor",
            "not", "of", "oh", "ok", "when", "whenever", "where", "whereas", "wherever", "whether", "which", "while",
            "who", "whoever", "whom", "whose", "why", "will", "with", "within", "without", "would", "yes", "yet", "you", "your"};
    public static ArrayList<String> QueryProcessor(String prompt) {


        int flag = 0;
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

            for(String t : StopWords)
            {
                if(term.equals(t))
                {
                    flag = 1;
                }
            }

            if(flag ==0)
                words.add(term);
        }



        return words;
    }


}

