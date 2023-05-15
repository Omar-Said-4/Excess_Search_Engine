package PageRanker;

import CrawlerState.CrawlerState;
import MongoDB.MongoInterface;
import SpringBoot.SpringBootInterface;
import com.mongodb.MongoException;
import org.bson.Document;

import javax.swing.plaf.metal.MetalBorders;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static QueryProcessor.queryP.QueryProcessor;

public class RankerMain {

    public static Map<String, linkAttr> handleQuery(String prompt, int pageNumber) {

        String filePath = "map.ser";
        File file = new File(filePath);
        HashMap<String, Double> page = null;
        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                page = (HashMap<String, Double>) in.readObject();
                System.out.println("Crawler state loaded from file: " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                // Handle the exception appropriately
            }
        }


        final HashMap<String,Double> pageR =page;



        ArrayList<String> values = QueryProcessor(prompt);
        String[] searchQuery = values.toArray(new String[0]);

        Map<String, linkAttr> toDisplayTmp = new ConcurrentHashMap<>();
        final double numOfdocs = 6000;


        List<Object> websites = MongoInterface.getWordDocsAll(searchQuery); // Retrieve websites related to search query

        List<linkAttr> results = websites.parallelStream() // Make the stream parallel
                .filter(obj -> obj instanceof Document)
                .map(obj -> (Document) obj)
                .map(websiteDoc -> {
                    String url = websiteDoc.getString("URL");
                    String title = websiteDoc.getString("Title");
                    double tf = Double.parseDouble(websiteDoc.getString("TF"));
                    double pri = 2 * Math.log(Double.parseDouble(websiteDoc.getString("Pri"))) * 0.1;
                    int df = websites.size(); // Calculate document frequency based on the retrieved websites
                    double idf = Math.log(numOfdocs / df);

                    // Rest of the mapping and processing
                    ArrayList<String> snips = (ArrayList<String>) websiteDoc.get("Snippets");
                    linkAttr tmp = toDisplayTmp.computeIfAbsent(url, k -> new linkAttr());
                    tmp.title = title;
                    double snippetScore = tf * idf * 0.3 + pri;
                    tmp.pri += snippetScore;
                    if(pageR.containsKey(url))
                        tmp.pri *= pageR.get(url);
                    for (String snippet : snips) {
                        tmp.Snippets.merge(snippet, 1000, Integer::sum);
                    }
                    return tmp;
                })
                .collect(Collectors.toList());


        Map<String, linkAttr> toDisplay = new ConcurrentSkipListMap<>(Comparator.comparingDouble(s -> toDisplayTmp.get(s).pri).reversed());
        toDisplayTmp.keySet().parallelStream().forEach(key -> {
            linkAttr attr = toDisplayTmp.get(key);
            Map<String, Integer> descendingMap = attr.Snippets.entrySet().parallelStream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            String toShow = MongoInterface.getSnippet(descendingMap.keySet().iterator().next());
            attr.BestSnip = toShow.replaceAll("[^\\p{ASCII}]", "'");
            StringBuilder output = new StringBuilder();
            output.append("Website: ").append(key)
                    .append(", Priority: ").append(attr.pri)
                    .append(", Best Snippet: ").append(attr.BestSnip);
            toDisplay.put(key, attr);
            System.out.println(output.toString());
        });


        return toDisplay;
    }


    public static void main(String[] args) throws InterruptedException {
        try {
            MongoInterface.Initialize();
            SpringBootInterface.runSpring();
        } catch (MongoException e) {
            System.out.println("Error");
        } finally {
            MongoInterface.terminate();
        }

    }
}
