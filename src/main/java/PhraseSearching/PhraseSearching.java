package PhraseSearching;

import MongoDB.MongoInterface;
import com.mongodb.internal.connection.SslHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PhraseSearching {

    public static void main(String[] args) {
//        List<String> results = PhraseSearching.search("Picture of the day");

//        List<String> words = new ArrayList<>();
//        words.add("It");
//        words.add("was");
//        words.add("popular");
//
//        System.out.println(findPhraseInSnippet(words, "Dolkart, Andrew S. June 2, 2011, at the , . Accessed May 15, 2007. \"It is at a triangular site where Broadway and Fifth Avenue—the two most important streets of New York—meet at Madison Square, and because of the juxtaposition of the streets and the park across the street, there was a wind-tunnel effect here. In the early twentieth century, men would hang out on the corner here on Twenty-third Street and watch the wind blowing women's dresses up so that they could catch a little bit of ankle. This entered into popular culture and there are hundreds of postcards and illustrations of women with their dresses blowing up in front of the Flatiron Building. And it supposedly is where the slang expression \"23 skidoo\" comes from because the police would come and give the voyeurs the 23 skidoo to tell them to get out of the area.\"\n"));

        String s = "Dolkart, Andrew S. June 2, 2011, at the , . Accessed May 15, 2007. \"It is at a triangular site where Broadway and Fifth Avenue—the two most important streets of New York—meet at Madison Square, and because of the juxtaposition of the streets and the park across the street, there was a wind-tunnel effect here. In the early twentieth century, men would hang out on the corner here on Twenty-third Street and watch the wind blowing women's dresses up so that they could catch a little bit of ankle. This entered into popular culture and there are hundreds of postcards and illustrations of women with their dresses blowing up in front of the Flatiron Building. And it supposedly is where the slang expression \"23 skidoo\" comes from because the police would come and give the voyeurs the 23 skidoo to tell them to get out of the area.\"\n";

        List<String> l = removeStoppingWords(s);


        for (String i : l)
            System.out.println(i);
    }

    static final List<String> StopWords = List.of("a", "about", "actually", "almost", "also",
            "although", "always", "am", "an", "and", "any", "are",
            "as", "at", "be", "became", "become", "but", "by", "can", "could", "did", "do", "does",
            "each", "either", "else", "for", "from", "had", "has", "have", "hence", "how", "i", "if", "in",
            "is", "it", "its", "just", "may", "maybe", "me", "might", "mine", "must", "my", "neither", "nor",
            "not", "of", "oh", "ok", "when", "whenever", "where", "whereas", "wherever", "whether", "which", "while",
            "who", "whoever", "whom", "whose", "why", "will", "with", "within", "without", "would", "yes", "yet", "you", "your", "all");


    private static boolean findPhraseInSnippet(List<String> words, String snippet) {

        String[] snippetWords = snippet.split(" ");
        int index = 0;
        Pattern pattern;
        Matcher matcher;

        Iterator<String> iterator = words.iterator();

        List<Integer> arr = new ArrayList<>();

        for (String word : words) {
            iterator.next();
            pattern = Pattern.compile("\\b" + word + "\\b|\\b" + word + "$", Pattern.CASE_INSENSITIVE);

            matcher = pattern.matcher(snippet.substring(index));

//            System.out.println(snippet);
//            System.out.println(index + " " + (snippet.length() - 1));

            if (matcher.find()) {
                // Check if not the first word
                if (!Objects.equals(words.get(0), word))
                    if (matcher.start() > 40)
                        return false;
                arr.add(matcher.start() + index);
                index = matcher.end() + index;
//                System.out.println(index);
//                System.out.println("Found the word " + word + " in the sentence! " + snippet);
            }
            ////////////// Not Important
//            else if (snippet.endsWith(" " + word) || snippet.endsWith(" " + word + "\n") || snippet.endsWith(" " + word + "\r\n")) {
//                if (iterator.hasNext())
//                    return false;
//                else break;
//
//            }
            else return false;

        }

//        System.out.println(snippet);
//        for (Integer i : arr) {
//            System.out.println(i);
//        }

        return true;
    }

    public static boolean hasNoneDisplayParent(Element element) {
        if (element == null) {
            return false;
        }
        String displayProperty = element.attr("style");
        if (displayProperty.replaceAll(" ", "").contains("display:none")) {
            return true;
        }
        return hasNoneDisplayParent(element.parent());
    }

    private static List<String> removeStoppingWords(String s) {
        List<String> result = new ArrayList<>(Arrays.asList(s.split(" ")));

        result.removeAll(StopWords);

        return result;
    }

    private static List<String> searchPhraseInSnippets(Element element, String s) {
        List<String> result = new ArrayList<>();
        if (element.children().size() == 0) {
            // This is a leaf node
            if ((element.tagName().equals("h1") || element.tagName().equals("h2") || element.tagName().equals("h3") || element.tagName().equals("h4") || element.tagName().equals("h5") || element.tagName().equals("h6") || element.tagName().equals("p") || element.tagName().equals("div") || element.tagName().equals("b") || element.tagName().equals("i") || element.tagName().equals("strong") || element.tagName().equals("scan") || element.tagName().equals("q") || element.tagName().equals("a") || element.tagName().equals("code") || element.tagName().equals("cite") || element.tagName().equals("li") || element.tagName().equals("caption") || element.tagName().equals("dd")) && !hasNoneDisplayParent(element)) {

                if (findPhraseInSnippet(removeStoppingWords(s), element.ownText())) {
//                    if(element.text().contains("Thank you"))
//                        System.out.println(hasNoneDisplayParent(element));

                    result.add(element.text());
                }
            }
        } else {
            // Recursively find leaf nodes in each child element
            if (!element.ownText().equals("")) {
//                System.out.println(element.ownText());
//                result.add(element.ownText());

                if (!hasNoneDisplayParent(element) && findPhraseInSnippet(removeStoppingWords(s), element.ownText()))
                    result.add(element.ownText());
            }
            for (Element child : element.children()) {
                result.addAll(searchPhraseInSnippets(child, s));
            }
        }

        return result;
    }

    public static List<String> phraseSearching(String firstString, String secondString, String thirdString, String op0, String op1, int complexity) {
        System.out.println(firstString + " " + secondString + " " + thirdString);
        System.out.println(op0 + " " + op1);

        String filePath = "Websites.ser";
        File file = new File(filePath);

        List<String> results = new ArrayList<>();

        HashMap<String, String> webPages = new HashMap<>();

        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                webPages = (HashMap<String, String>) in.readObject();

                System.out.println("All websites loaded from file: " + filePath);
            } catch (IOException |
                     ClassNotFoundException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }

        System.out.println(webPages.size());

        String doc_string;
        int count = 0;

        for (String link : webPages.keySet()) {
            doc_string = webPages.get(link);

            if (link.contains("Art_Nouveau"))
                System.out.println();


            org.jsoup.nodes.Document doc = Jsoup.parse(doc_string);


            // Get All the Rendered Text
            List<String> firstElements = searchPhraseInSnippets(doc.body(), firstString);

//            if (count == 0) {
//                System.out.println(leafNodes.size());
//                count++;
//
//                System.out.println(link);
//
//                for (String l : leafNodes)
//                    System.out.println(l);
//            }

            List<String> secondElements = Collections.emptyList();
            List<String> thirdElements = Collections.emptyList();

            if (complexity == 0) {
                if (firstElements.size() > 0) {
                    results.add(link);
//                    System.out.println(link);
                }

            } else if (complexity == 1) {

                if (firstElements.size() > 0 && Objects.equals(op0, "OR")) {
                    results.add(link);
                    for (String l : firstElements)
                        System.out.println(l + "\n");

                    System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    System.out.println();
                    continue;
                }

                if (Objects.equals(op0, "NOT")) {
                    if (firstElements.size() == 0) {
                        results.add(link);
                    }
                    continue;
                }

//                secondElements = doc.getElementsContainingOwnText(secondString);

                secondElements = searchPhraseInSnippets(doc.body(), secondString);

                if (Objects.equals(op0, "AND")) {
                    if (firstElements.size() > 0 && secondElements.size() > 0) {
                        results.add(link);
                        for (String l : firstElements)
                            System.out.println(l + "\n");

                        for (String l : secondElements)
                            System.out.println(l + "\n");

                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                        System.out.println();
                    }
                } else if (Objects.equals(op0, "OR")) {
                    if (firstElements.size() > 0 || secondElements.size() > 0) {
//                        for (String l : firstElements)
//                            System.out.println(l + "\n");

                        for (String l : secondElements)
                            System.out.println(l + "\n");

                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                        System.out.println();

                        results.add(link);
//                        System.out.println(firstElements.size() + " " + secondElements.size());
                    }
                }

            } else if (complexity == 2) {

                secondElements = searchPhraseInSnippets(doc.body(), secondString);

                if (!Objects.equals(op0, "NOT") && !Objects.equals(op1, "NOT")) {

                    thirdElements = searchPhraseInSnippets(doc.body(), thirdString);

                }


                if (Objects.equals(op0, "AND") && Objects.equals(op1, "AND")) {
                    if (firstElements.size() > 0 && secondElements.size() > 0 && thirdElements.size() > 0) {
                        results.add(link);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "AND") && Objects.equals(op1, "OR")) {
                    if ((firstElements.size() > 0 && secondElements.size() > 0) || thirdElements.size() > 0) {
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);
                        results.add(link);
                    }
                } else if (Objects.equals(op0, "AND") && Objects.equals(op1, "NOT")) {
                    if (firstElements.size() > 0 && secondElements.size() == 0) {
                        results.add(link);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "OR") && Objects.equals(op1, "AND")) {
                    if (firstElements.size() > 0 || (secondElements.size() > 0 && thirdElements.size() > 0)) {
                        results.add(link);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);

                    }
                } else if (Objects.equals(op0, "OR") && Objects.equals(op1, "OR")) {
                    if (firstElements.size() > 0 || secondElements.size() > 0 || thirdElements.size() > 0) {
                        results.add(link);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);

                    }
                } else if (Objects.equals(op0, "OR") && Objects.equals(op1, "NOT")) {
                    if (firstElements.size() > 0 || secondElements.size() == 0) {
                        results.add(link);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "NOT") && Objects.equals(op1, "AND")) {
                    if (firstElements.size() == 0 && secondElements.size() > 0) {
                        results.add(link);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "NOT") && Objects.equals(op1, "OR")) {
                    if (firstElements.size() == 0 || secondElements.size() > 0) {
                        results.add(link);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                }
            }
        }

        for (String l : results)
            System.out.println(l);

        return results;
    }

    public static List<String> search(String phrase) {
        return PhraseSearching.phraseSearching(phrase, null, null, null, null, 0);
    }
}


//            Elements elements = doc.getAllElements();
//
//            if (count == 0) {
//                System.out.println(link);
//                for (String element : leafNodes) {
////                    if (count < 5)
//////                        System.out.println(element);
////                    else break;
////                    count++;
////                    Elements firsElements = element.selec
////                    System.out.println(element);
////                    if(element.tagName().equals("h1") || element.tagName().equals("h2") || element.tagName().equals("h3") || element.tagName().equals("h4") || element.tagName().equals("h5") || element.tagName().equals("h6") || element.tagName().equals("p") || element.tagName().equals("div") || element.tagName().equals("b") || element.tagName().equals("i") || element.tagName().equals("strong") || element.tagName().equals("scan") || element.tagName().equals("q") || element.tagName().equals("a") || element.tagName().equals("code") || element.tagName().equals("cite") || element.tagName().equals("li") || element.tagName().equals("caption") || element.tagName().equals("dd"))
////                        System.out.println(element.text());
//
//
//                }
////                System.out.println(leafNodes.size());
//                count = 1;
//            }
//            if (count == 0) {
//                System.out.println(link);
//                for (Element l : elements) {
//                    System.out.println(l.text());
//                    count = 1;
//                }
//            }
//            Elements firstElements = doc.select("div:matchesOwn((?i)^.*" + firstString + ".*$), h1:matchesOwn((?i)^.*" + firstString + ".*$), h2:matchesOwn((?i)^.*" + firstString + ".*$), h3:matchesOwn((?i)^.*" + firstString + ".*$), h4:matchesOwn((?i)^.*" + firstString + ".*$), h5:matchesOwn((?i)^.*" + firstString + ".*$), h6:matchesOwn((?i)^.*" + firstString + ".*$),  p:matchesOwn((?i)^.*" + firstString + ".*$), strong:matchesOwn((?i)^.*" + firstString + ".*$), b:matchesOwn((?i)^.*" + firstString + ".*$), u:matchesOwn((?i)^.*" + firstString + ".*$), i:matchesOwn((?i)^.*" + firstString + ".*$), code:matchesOwn((?i)^.*" + firstString + ".*$), a:matchesOwn((?i)^.*" + firstString + ".*$), caption:matchesOwn((?i)^.*" + firstString + ".*$), cite:matchesOwn((?i)^.*" + firstString + ".*$), li:matchesOwn((?i)^.*" + firstString + ".*$), q:matchesOwn((?i)^.*" + firstString + ".*$), span:matchesOwn((?i)^.*" + firstString + ".*$)");


//            Elements firstElements = findPhraseInSnippet()
//                    thirdElements = doc.select("div:matchesOwn((?i)^.*" + thirdString + ".*$), h1:matchesOwn((?i)^.*" + thirdString + ".*$), h2:matchesOwn((?i)^.*" + thirdString + ".*$), h3:matchesOwn((?i)^.*" + thirdString + ".*$), h4:matchesOwn((?i)^.*" + thirdString + ".*$), h5:matchesOwn((?i)^.*" + thirdString + ".*$), h6:matchesOwn((?i)^.*" + thirdString + ".*$),  p:matchesOwn((?i)^.*" + thirdString + ".*$), strong:matchesOwn((?i)^.*" + thirdString + ".*$), b:matchesOwn((?i)^.*" + thirdString + ".*$), u:matchesOwn((?i)^.*" + thirdString + ".*$), i:matchesOwn((?i)^.*" + thirdString + ".*$), code:matchesOwn((?i)^.*" + thirdString + ".*$), a:matchesOwn((?i)^.*" + thirdString + ".*$), caption:matchesOwn((?i)^.*" + thirdString + ".*$), cite:matchesOwn((?i)^.*" + thirdString + ".*$), li:matchesOwn((?i)^.*" + thirdString + ".*$), q:matchesOwn((?i)^.*" + thirdString + ".*$), span:matchesOwn((?i)^.*" + thirdString + ".*$)");
//                secondElements = doc.select("div:matchesOwn((?i)^.*" + secondString + ".*$), h1:matchesOwn((?i)^.*" + secondString + ".*$), h2:matchesOwn((?i)^.*" + secondString + ".*$), h3:matchesOwn((?i)^.*" + secondString + ".*$), h4:matchesOwn((?i)^.*" + secondString + ".*$), h5:matchesOwn((?i)^.*" + secondString + ".*$), h6:matchesOwn((?i)^.*" + secondString + ".*$),  p:matchesOwn((?i)^.*" + secondString + ".*$), strong:matchesOwn((?i)^.*" + secondString + ".*$), b:matchesOwn((?i)^.*" + secondString + ".*$), u:matchesOwn((?i)^.*" + secondString + ".*$), i:matchesOwn((?i)^.*" + secondString + ".*$), code:matchesOwn((?i)^.*" + secondString + ".*$), a:matchesOwn((?i)^.*" + secondString + ".*$), caption:matchesOwn((?i)^.*" + secondString + ".*$), cite:matchesOwn((?i)^.*" + secondString + ".*$), li:matchesOwn((?i)^.*" + secondString + ".*$), q:matchesOwn((?i)^.*" + secondString + ".*$), span:matchesOwn((?i)^.*" + secondString + ".*$)");
