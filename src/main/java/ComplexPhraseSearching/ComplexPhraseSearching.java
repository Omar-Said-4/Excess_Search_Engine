package ComplexPhraseSearching;

import MongoDB.MongoInterface;

import java.util.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import PhraseSearching.PhraseSearching;

public class ComplexPhraseSearching {
    public static void main(String[] args) {
        List<String> result = complexPhraseSearch("\"It was Popular\" OR \"Thank you\"");
//
//        for (String r: result)
//            System.out.println(r);
    }

    static List<String> complexPhraseSearch(String phrase) {
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

        List<String> results = Collections.emptyList();

        // Check for errors in the query
        if (operators.size() > 2 || operators.size() == 0) {
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
            results = PhraseSearching.phraseSearching(phrases.get(0), phrases.get(1), null, operators.get(0), null, 1);
        } else if (operators.size() == 2) {
            System.out.println(2);
            if (Objects.equals(operators.get(0), "NOT") || Objects.equals(operators.get(1), "NOT"))
                results = PhraseSearching.phraseSearching(phrases.get(0), phrases.get(1), null, operators.get(0), operators.get(1), 2);
            else
                results = PhraseSearching.phraseSearching(phrases.get(0), phrases.get(1), phrases.get(2), operators.get(0), operators.get(1), 2);
        } else {
            System.out.println("Error in Query!");
            MongoInterface.terminate();
            return null;
        }


        return results;
    }
}
