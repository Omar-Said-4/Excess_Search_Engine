package ComplexPhraseSearching;

import MongoDB.MongoInterface;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PageRanker.RankerMain;
import PageRanker.linkAttr;
import PhraseSearching.PhraseSearching;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class ComplexPhraseSearching {
    public static void main(String[] args) {
        Map<String, Object> result = complexPhraseSearch("\"Baltimore was designated \"", 1);

    }



    public static Map<String, Object> complexPhraseSearch(String phrase, int pageNumber) {
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
            else {
                //System.out.println("test");
                results = PhraseSearching.phraseSearching(phrases.get(0), phrases.get(1), phrases.get(2), operators.get(0), operators.get(1), 2);
            }
        }


        Integer size = results.size();

        System.out.println("Size: " +  size);

        // Calculate start and end index
        int start = 0, end = 0;

        // Check if there are enough data
        if (!(RankerMain.PAGE_COUNT * (pageNumber - 1) >= results.size())) {
            start = RankerMain.PAGE_COUNT * (pageNumber - 1);

            // Calculate end index
            if (results.size() < RankerMain.PAGE_COUNT * pageNumber)
                end = results.size();
            else
                end = RankerMain.PAGE_COUNT * pageNumber;
        }


        List<String> requiredURLs = new ArrayList<>(results.keySet()).subList(start, end);
        List<linkAttr> requiredAttrs = new ArrayList<>(results.values()).subList(start, end);

        Map<String, linkAttr> snippets = new HashMap<String, linkAttr>();

        System.out.println("Phrase Searching -> Start: " + start);
        System.out.println("End: " + end);

        for (int i = 0; i < requiredURLs.size(); i++) {
            snippets.put(requiredURLs.get(i), requiredAttrs.get(i));
        }

        Map<String, Object> result = new HashMap<>();

        result.put("snippets", snippets);
        result.put("size", size);


        return result;
    }
}
