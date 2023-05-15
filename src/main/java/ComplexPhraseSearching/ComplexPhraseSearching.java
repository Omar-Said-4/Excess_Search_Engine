package ComplexPhraseSearching;

import MongoDB.MongoInterface;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PageRanker.linkAttr;
import PhraseSearching.PhraseSearching;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ComplexPhraseSearching {
    public static void main(String[] args) {
        Map<String, linkAttr> result = complexPhraseSearch("\"Football\" AND \"major city in the United States\" AND \"CNN\"");


//        for (String r: result)
//            System.out.println(r);
//
//        String html = "<div>jhkgkj<p>Some text</p><span></span></div>";
//        Document doc = Jsoup.parse(html);
//        Element div = doc.selectFirst("div");
//
//
//        System.out.println(div.ownText());
//        assert div != null;
//        if (div.hasText()) {
//            System.out.println("The div has non-empty text.");
//        } else {
//            System.out.println("The div does not have non-empty text.");
//        }
    }

    public static Map<String, linkAttr> complexPhraseSearch(String phrase) {
        List<String> phrases = new ArrayList<>();
        List<String> operators = new ArrayList<>();

        // Match The Phrases
        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(phrase);
        while (matcher.find()) {
//            System.out.println(matcher.group(1));
            assert false;
            phrases.add(matcher.group(1));
        }

        // Match the Operators
        pattern = Pattern.compile("(?<=\\s|^)(\"[^\"]*\"|\\S+)+");
        matcher = pattern.matcher(phrase);

        while (matcher.find()) {
            String p = matcher.group();
            if (!p.startsWith("\"")) {
//                System.out.println("Found phrase not between quotes: " + p);
                operators.add(p);
            }
        }

        Map<String, linkAttr> results = null;

        // Normal phrase searching
        if (operators.size() == 0) {
            results = PhraseSearching.phraseSearching(phrases.get(0), null, null, null, null, 0);
        }

        // Check for errors in the query
        if (operators.size() > 2) {
            System.out.println("Error in Query!");
            return null;
        }
        if (operators.size() == 2 && Objects.equals(operators.get(0), "NOT") && Objects.equals(operators.get(1), "NOT")) {
            System.out.println("Error in Query!");
            return null;
        }

        if (operators.size() == 2) {
            if (operators.contains("NOT")) {
                if (phrases.size() != 2) {
                    System.out.println("Error in Query!");
                    return null;
                }
            } else {
                if (phrases.size() != 3) {
                    System.out.println("Error in Query!");
                    return null;
                }
            }
        } else if (operators.size() == 1) {
            if (Objects.equals(operators.get(0), "NOT") && phrases.size() != 1) {
                System.out.println("Error in Query!");
                return null;
            } else if (!Objects.equals(operators.get(0), "NOT") && phrases.size() != 2) {
                System.out.println("Error in Query!");
                return null;
            }
        }


        if (operators.size() == 1) {
            if (Objects.equals(operators.get(0), "NOT"))
                results = PhraseSearching.phraseSearching(phrases.get(0), null, null, operators.get(0), null, 1);
            else
                results = PhraseSearching.phraseSearching(phrases.get(0), phrases.get(1), null, operators.get(0), null, 1);
        } else if (operators.size() == 2) {
            System.out.println(2);
            if (Objects.equals(operators.get(0), "NOT") || Objects.equals(operators.get(1), "NOT"))
                results = PhraseSearching.phraseSearching(phrases.get(0), phrases.get(1), null, operators.get(0), operators.get(1), 2);
            else
                results = PhraseSearching.phraseSearching(phrases.get(0), phrases.get(1), phrases.get(2), operators.get(0), operators.get(1), 2);
        }


        return results;
    }
}
