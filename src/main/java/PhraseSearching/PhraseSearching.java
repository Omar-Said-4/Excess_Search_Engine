package PhraseSearching;

import MongoDB.MongoInterface;
import PageRanker.linkAttr;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.internal.connection.SslHelper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static PageRanker.RankerMain.snippets;
import static PageRanker.RankerMain.titles;


public class PhraseSearching {

    public static void main(String[] args) {
//        List<String> results = PhraseSearching.search("Picture of the day");

//        List<String> words = new ArrayList<>();
//        words.add("It");
//        words.add("was");
//        words.add("popular");
//
//        System.out.println(findPhraseInSnippet(words, "Dolkart, Andrew S. June 2, 2011, at the , . Accessed May 15, 2007. \"It is at a triangular site where Broadway and Fifth Avenue—the two most important streets of New York—meet at Madison Square, and because of the juxtaposition of the streets and the park across the street, there was a wind-tunnel effect here. In the early twentieth century, men would hang out on the corner here on Twenty-third Street and watch the wind blowing women's dresses up so that they could catch a little bit of ankle. This entered into popular culture and there are hundreds of postcards and illustrations of women with their dresses blowing up in front of the Flatiron Building. And it supposedly is where the slang expression \"23 skidoo\" comes from because the police would come and give the voyeurs the 23 skidoo to tell them to get out of the area.\"\n"));

//        String s = "Dolkart, Andrew S. June 2, 2011, at the , . Accessed May 15, 2007. \"It is at a triangular site where Broadway and Fifth Avenue—the two most important streets of New York—meet at Madison Square, and because of the juxtaposition of the streets and the park across the street, there was a wind-tunnel effect here. In the early twentieth century, men would hang out on the corner here on Twenty-third Street and watch the wind blowing women's dresses up so that they could catch a little bit of ankle. This entered into popular culture and there are hundreds of postcards and illustrations of women with their dresses blowing up in front of the Flatiron Building. And it supposedly is where the slang expression \"23 skidoo\" comes from because the police would come and give the voyeurs the 23 skidoo to tell them to get out of the area.\"\n";
//
//        List<String> l = removeStoppingWords(s);


//        for (String i : l)
//            System.out.println(i);


        String prompt;
        Scanner s = new Scanner(System.in);
        prompt = s.nextLine();
//
//        List<String> l = search(prompt);
//
//
//        for (String i : l)
//            System.out.println(i);

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

    public static String getPageTitle(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            return doc.title();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
        return null;
    }

    // Gets the longest snippet that contains the given phrase
    private static Map<String, linkAttr> searchPhraseInSnippets(Map<String, List<String>> snippets, String s, String link, String title) {
//        Map<String, linkAttr> result = new HashMap<>();
//        if (element.children().size() == 0) {
//            // This is a leaf node
//            if ((element.tagName().equals("h1") || element.tagName().equals("h2") || element.tagName().equals("h3") || element.tagName().equals("h4") || element.tagName().equals("h5") || element.tagName().equals("h6") || element.tagName().equals("p") || element.tagName().equals("div") || element.tagName().equals("b") || element.tagName().equals("i") || element.tagName().equals("strong") || element.tagName().equals("scan") || element.tagName().equals("q") || element.tagName().equals("a") || element.tagName().equals("code") || element.tagName().equals("cite") || element.tagName().equals("li") || element.tagName().equals("caption") || element.tagName().equals("dd")) && !hasNoneDisplayParent(element)) {
//
//                if (findPhraseInSnippet(removeStoppingWords(s), element.ownText())) {
//                    linkAttr attributes = new linkAttr();
//                    attributes.BestSnip = element.text();
//                    attributes.title = title;
//                    result.put(link, attributes);
//                }
//            }
//        } else {
//            // Recursively find leaf nodes in each child element
//            if (!element.ownText().equals("")) {
//                if (!hasNoneDisplayParent(element) && findPhraseInSnippet(removeStoppingWords(s), element.ownText())) {
//                    linkAttr attributes = new linkAttr();
//                    attributes.BestSnip = element.text();
//                    attributes.title = title;
//                    result.put(link, attributes);
//                }
//            }
//            for (Element child : element.children()) {
//                Map<String, linkAttr> r = searchPhraseInSnippets(child, s, link, title);
//
//                if (r.get(link) != null && result.get(link) != null) {
//                    //System.out.println("test");
//                    if (r.get(link).BestSnip.length() > result.get(link).BestSnip.length())
//                        result.putAll(r);
//                } else if (r.get(link) != null) {
//                    result.putAll(r);
//                }
//            }
//        }
//
//        return result;
        Map<String, linkAttr> result = new HashMap<>();

        List<String> mySnippets = snippets.get(link);

        if (mySnippets != null) {

            mySnippets.parallelStream().forEach(snippet -> {
                if (findPhraseInSnippet(removeStoppingWords(s), snippet)) {
                    linkAttr attributes = new linkAttr();
                    attributes.BestSnip = snippet;
                    attributes.title = title;
                    result.put(link, attributes);
                }
            });
        }

        return result;
    }

    public static Map<String, linkAttr> phraseSearching(String firstString, String secondString, String
            thirdString, String op0, String op1, int complexity) {
        System.out.println(firstString + " " + secondString + " " + thirdString);
        System.out.println(op0 + " " + op1);

//        String filePath = "Websites.ser";
//        File file = new File(filePath);
//
//
//
//        HashMap<String, String> webPages = new HashMap<>();
//        if (file.exists()) {
//            try (FileInputStream fileIn = new FileInputStream(file);
//                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
//                webPages = (HashMap<String, String>) in.readObject();
//
//                System.out.println("All websites loaded from file: " + filePath);
//            } catch (IOException |
//                     ClassNotFoundException e) {
//                e.printStackTrace();
//                // Handle the exception appropriately
//            }
//        }

        Map<String, linkAttr> results = new HashMap<>();


//        System.out.println(webPages.size());

        String doc_string;

//        Map<String, List<String>> snippets = MongoInterface.getAllSnippets();


        for (String link : snippets.keySet()) {
//            doc_string = webPages.get(link);
//            org.jsoup.nodes.Document doc = Jsoup.parse(doc_string);
            String title = null;
            if (titles.get(link) != null)
                title = titles.get(link);
            else title = "NO Title";
            // Get All the Rendered Text
            Map<String, linkAttr> firstElements = searchPhraseInSnippets(snippets, firstString, link, title);
            Map<String, linkAttr> secondElements = new HashMap<>();
            Map<String, linkAttr> thirdElements = new HashMap<>();
            if (complexity == 0) {
                if (firstElements.size() > 0) {
                    results.putAll(firstElements);
                }

            } else if (complexity == 1) {
//                if (firstElements.size() > 0 && Objects.equals(op0, "OR")) {
//                    results.putAll(link);
//
//                    for (String l : firstElements.keySet())
//                        System.out.println(l + "\n");
//
//                    System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
//                    System.out.println();
//                    continue;
//                }

                if (Objects.equals(op0, "NOT")) {
                    if (firstElements.size() == 0) {
                        results.putAll(firstElements);
                    }
                    continue;
                }
//                secondElements = doc.getElementsContainingOwnText(secondString);
                secondElements = searchPhraseInSnippets(snippets, secondString, link, title);
                if (Objects.equals(op0, "AND")) {
                    if (firstElements.size() > 0 && secondElements.size() > 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        for (String l : firstElements.keySet())
                            System.out.println(l + "\n");

                        for (String l : secondElements.keySet())
                            System.out.println(l + "\n");

                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                        System.out.println();
                    }
                } else if (Objects.equals(op0, "OR")) {
                    if (firstElements.size() > 0 || secondElements.size() > 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);

                        for (String l : firstElements.keySet())
                            System.out.println(l + "\n");

                        for (String l : secondElements.keySet())
                            System.out.println(l + "\n");

                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                        System.out.println();
//                        System.out.println(firstElements.size() + " " + secondElements.size());
                    }
                }

            } else if (complexity == 2) {

                secondElements = searchPhraseInSnippets(snippets, secondString, link, title);

                if (!Objects.equals(op0, "NOT") && !Objects.equals(op1, "NOT")) {

                    thirdElements = searchPhraseInSnippets(snippets, thirdString, link, title);

                }


                if (Objects.equals(op0, "AND") && Objects.equals(op1, "AND")) {
                    System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size());
                    if (firstElements.size() > 0 && secondElements.size() > 0 && thirdElements.size() > 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        results.putAll(thirdElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "AND") && Objects.equals(op1, "OR")) {
                    if ((firstElements.size() > 0 && secondElements.size() > 0) || thirdElements.size() > 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        results.putAll(thirdElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "AND") && Objects.equals(op1, "NOT")) {
                    if (firstElements.size() > 0 && secondElements.size() == 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "OR") && Objects.equals(op1, "AND")) {
                    if (firstElements.size() > 0 || (secondElements.size() > 0 && thirdElements.size() > 0)) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        results.putAll(thirdElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);

                    }
                } else if (Objects.equals(op0, "OR") && Objects.equals(op1, "OR")) {
                    if (firstElements.size() > 0 || secondElements.size() > 0 || thirdElements.size() > 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        results.putAll(thirdElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + thirdElements.size() + " " + link);

                    }
                } else if (Objects.equals(op0, "OR") && Objects.equals(op1, "NOT")) {
                    if (firstElements.size() > 0 || secondElements.size() == 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "NOT") && Objects.equals(op1, "AND")) {
                    if (firstElements.size() == 0 && secondElements.size() > 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                } else if (Objects.equals(op0, "NOT") && Objects.equals(op1, "OR")) {
                    if (firstElements.size() == 0 || secondElements.size() > 0) {
                        results.putAll(firstElements);
                        results.putAll(secondElements);
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);
                    }
                }
            }
        }

        for (String l : results.keySet())
            System.out.println(l);

        return results;
    }

//    public static Map<String, linkAttr> search(String phrase) {
//        return PhraseSearching.phraseSearching(phrase, null, null, null, null, 0);
//    }
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
