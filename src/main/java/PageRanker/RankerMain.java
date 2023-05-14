package PageRanker;

import MongoDB.MongoInterface;
import SpringBoot.SpringBootInterface;
import com.mongodb.MongoException;
import org.bson.Document;

import javax.swing.plaf.metal.MetalBorders;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static QueryProcessor.queryP.QueryProcessor;

public class RankerMain {

    public static Map<String, linkAttr> handleQuery(String prompt, int pageNumber) {
        ArrayList<String> values = QueryProcessor(prompt);
        String[] searchQuery = values.toArray(new String[0]);

        Map<String, linkAttr> toDisplayTmp = new ConcurrentHashMap<>();
        final double numOfdocs = 6000;

        Arrays.stream(searchQuery).parallel().forEach(s -> {
            List<Object> websites = MongoInterface.getWordDocs(s);
            if (websites == null)
                return;
            int df = websites.size();

            for (Object obj : websites) {
                if (obj instanceof Document websiteDoc) {
                    String url = websiteDoc.getString("URL");
                    String title = websiteDoc.getString("Title");
                    double tf = Double.parseDouble(websiteDoc.getString("TF"));
                    double pri = 2 * Math.log(Double.parseDouble(websiteDoc.getString("Pri"))) * 0.1;
                    double idf = Math.log(numOfdocs / df);
                    ArrayList<String> snips = (ArrayList<String>) websiteDoc.get("Snippets");

                    linkAttr tmp = toDisplayTmp.computeIfAbsent(url, k -> new linkAttr());
                    tmp.title = title;

                    double snippetScore = tf * idf * 0.3 + pri;
                    tmp.pri += snippetScore;

                    for (String snippet : snips) {
                        tmp.Snippets.merge(snippet, 1000, Integer::sum);
                    }
                }
            }
        });

        Map<String, linkAttr> toDisplay = new TreeMap<>(Comparator.comparingDouble(s -> toDisplayTmp.get(s).pri).reversed());
        toDisplay.putAll(toDisplayTmp);

        toDisplay.forEach((key, value) -> {
            Map<String, Integer> descendingMap = value.Snippets.entrySet()
                    .parallelStream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

            String toShow = MongoInterface.getSnippet(descendingMap.keySet().iterator().next());

            StringBuilder output = new StringBuilder();
            output.append("Website: ").append(key)
                    .append(", Priority: ").append(value.pri)
                    .append(", Best Snippet: ").append(toShow.replaceAll("[^\\p{ASCII}]", "'"));
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
