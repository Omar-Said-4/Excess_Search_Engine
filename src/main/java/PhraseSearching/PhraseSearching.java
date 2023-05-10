package PhraseSearching;

import MongoDB.MongoInterface;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class PhraseSearching {

    public static void main(String[] args) {
        MongoInterface.Initialize();
        List<String> results = PhraseSearching.search("Picture of the day");
        MongoInterface.terminate();
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

        String doc_string;

        for (String link : webPages.keySet()) {
            doc_string = webPages.get(link);

            org.jsoup.nodes.Document doc = Jsoup.parse(doc_string);

            Elements firstElements = doc.select("div:matchesOwn((?i)^.*" + firstString + ".*$), h1:matchesOwn((?i)^.*" + firstString + ".*$), h2:matchesOwn((?i)^.*" + firstString + ".*$), h3:matchesOwn((?i)^.*" + firstString + ".*$), h4:matchesOwn((?i)^.*" + firstString + ".*$), h5:matchesOwn((?i)^.*" + firstString + ".*$), h6:matchesOwn((?i)^.*" + firstString + ".*$),  p:matchesOwn((?i)^.*" + firstString + ".*$), strong:matchesOwn((?i)^.*" + firstString + ".*$), b:matchesOwn((?i)^.*" + firstString + ".*$), u:matchesOwn((?i)^.*" + firstString + ".*$), i:matchesOwn((?i)^.*" + firstString + ".*$), code:matchesOwn((?i)^.*" + firstString + ".*$), a:matchesOwn((?i)^.*" + firstString + ".*$), caption:matchesOwn((?i)^.*" + firstString + ".*$), cite:matchesOwn((?i)^.*" + firstString + ".*$), li:matchesOwn((?i)^.*" + firstString + ".*$), q:matchesOwn((?i)^.*" + firstString + ".*$), span:matchesOwn((?i)^.*" + firstString + ".*$)");
            ;

            Elements secondElements = new Elements();
            Elements thirdElements = new Elements();

            if (complexity == 0) {
                if (firstElements.size() > 0) {
                    results.add(link);
                    System.out.println(link);
                }

            } else if (complexity == 1) {

                if (firstElements.size() > 0 && Objects.equals(op0, "OR")) {
                    results.add(link);
                    System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);

                    continue;
                }

                if (Objects.equals(op0, "NOT")) {
                    if (firstElements.size() == 0) {
                        results.add(link);
                    }
                    continue;
                }

                secondElements = doc.getElementsContainingOwnText(secondString);

                if (Objects.equals(op0, "AND")) {
                    if (firstElements.size() > 0 && secondElements.size() > 0) {
                        results.add(link);
//                        System.out.println(firstElements.size() + " " + secondElements.size());
                    }
                } else if (Objects.equals(op0, "OR")) {
                    if (firstElements.size() > 0 || secondElements.size() > 0) {
                        System.out.println(firstElements.size() + " " + secondElements.size() + " " + link);

                        results.add(link);
//                        System.out.println(firstElements.size() + " " + secondElements.size());
                    }
                }

            } else if (complexity == 2) {
                secondElements = doc.select("div:matchesOwn((?i)^.*" + secondString + ".*$), h1:matchesOwn((?i)^.*" + secondString + ".*$), h2:matchesOwn((?i)^.*" + secondString + ".*$), h3:matchesOwn((?i)^.*" + secondString + ".*$), h4:matchesOwn((?i)^.*" + secondString + ".*$), h5:matchesOwn((?i)^.*" + secondString + ".*$), h6:matchesOwn((?i)^.*" + secondString + ".*$),  p:matchesOwn((?i)^.*" + secondString + ".*$), strong:matchesOwn((?i)^.*" + secondString + ".*$), b:matchesOwn((?i)^.*" + secondString + ".*$), u:matchesOwn((?i)^.*" + secondString + ".*$), i:matchesOwn((?i)^.*" + secondString + ".*$), code:matchesOwn((?i)^.*" + secondString + ".*$), a:matchesOwn((?i)^.*" + secondString + ".*$), caption:matchesOwn((?i)^.*" + secondString + ".*$), cite:matchesOwn((?i)^.*" + secondString + ".*$), li:matchesOwn((?i)^.*" + secondString + ".*$), q:matchesOwn((?i)^.*" + secondString + ".*$), span:matchesOwn((?i)^.*" + secondString + ".*$)");

                if (!Objects.equals(op0, "NOT") && !Objects.equals(op1, "NOT")) {
                    thirdElements = doc.select("div:matchesOwn((?i)^.*" + thirdString + ".*$), h1:matchesOwn((?i)^.*" + thirdString + ".*$), h2:matchesOwn((?i)^.*" + thirdString + ".*$), h3:matchesOwn((?i)^.*" + thirdString + ".*$), h4:matchesOwn((?i)^.*" + thirdString + ".*$), h5:matchesOwn((?i)^.*" + thirdString + ".*$), h6:matchesOwn((?i)^.*" + thirdString + ".*$),  p:matchesOwn((?i)^.*" + thirdString + ".*$), strong:matchesOwn((?i)^.*" + thirdString + ".*$), b:matchesOwn((?i)^.*" + thirdString + ".*$), u:matchesOwn((?i)^.*" + thirdString + ".*$), i:matchesOwn((?i)^.*" + thirdString + ".*$), code:matchesOwn((?i)^.*" + thirdString + ".*$), a:matchesOwn((?i)^.*" + thirdString + ".*$), caption:matchesOwn((?i)^.*" + thirdString + ".*$), cite:matchesOwn((?i)^.*" + thirdString + ".*$), li:matchesOwn((?i)^.*" + thirdString + ".*$), q:matchesOwn((?i)^.*" + thirdString + ".*$), span:matchesOwn((?i)^.*" + thirdString + ".*$)");
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

        return results;
    }

    public static List<String> search(String phrase) {
        return PhraseSearching.phraseSearching(phrase, null, null, null, null, 0);
    }
}