package ComplexPhraseSearching;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComplexPhraseSearching {
    public static void main(String[] args) {
//        System.out.println("test");
        complexPhraseSearch("NOT \"kiri baghdad\"");
    }

    static void complexPhraseSearch(String phrase) {
        Set<String> phrases = null;
        String op = null;

        Pattern pattern = Pattern.compile("\"([^\"]*)\"");
        Matcher matcher = pattern.matcher(phrase);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
            assert false;
            phrases.add(matcher.group(1));
        }


        pattern = Pattern.compile("(?<=\\s|^)(\"[^\"]*\"|\\S+)+");
        matcher = pattern.matcher(phrase);

        while (matcher.find()) {
            String p = matcher.group();
            if (!p.startsWith("\"")) {
                System.out.println("Found phrase not between quotes: " + p);
                op = p;
            }
        }

        // AND Operation
        if(Objects.equals(op, "AND")){

        }
    }
}
