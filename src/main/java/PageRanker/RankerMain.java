package PageRanker;

import MongoDB.MongoInterface;
import SpringBoot.SpringBootInterface;
import com.mongodb.MongoException;
import org.bson.Document;

import javax.swing.plaf.metal.MetalBorders;
import java.util.*;

import static QueryProcessor.queryP.QueryProcessor;

public class RankerMain {


    public static Map<String, linkAttr> handleQuery(String prompt, int pageNumber) {
        ArrayList<String> values = QueryProcessor(prompt);
        String[] searchQuery = values.toArray(new String[0]);

        Map<String, linkAttr> toDisplayTmp = new HashMap<>();

        final double numOfdocs = 6000;

        for (String s : searchQuery) {
            List<Object> websites = MongoInterface.getWordDocs(s);
            if (websites == null)
                continue;
            int df = websites.size();

            for (Object obj : websites) {
                if (obj instanceof Document websiteDoc) {
                    String url = websiteDoc.getString("URL");
                    String title = websiteDoc.getString("Title");
                    double tf = Double.parseDouble(websiteDoc.getString("TF"));
                    double pri = 2 * Math.log(Double.parseDouble(websiteDoc.getString("Pri"))) * 0.1;
                    double idf = Math.log(numOfdocs / df);
                    ArrayList<String> snips = (ArrayList<String>) websiteDoc.get("Snippets");

                    linkAttr tmp = toDisplayTmp.get(url);
                    if (tmp == null) {
                        tmp = new linkAttr();
                        tmp.title = title;
                        toDisplayTmp.put(url, tmp);
                    }

                    tmp.pri += (tf * idf * 0.3 + pri);

                    for (String snippet : snips) {
                        tmp.Snippets.put(snippet, tmp.Snippets.getOrDefault(snippet, 0) + 1000);
                    }
                }
            }
        }

        // Use the popularity in the calculation
        Comparator<String> valueComparator = (s1, s2) -> {
            double value1 = toDisplayTmp.get(s1).pri;
            double value2 = toDisplayTmp.get(s2).pri;
            return Double.compare(value2, value1); // descending order
        };

        Map<String, linkAttr> toDisplay = new TreeMap<>(valueComparator);
        toDisplay.putAll(toDisplayTmp);

        toDisplay.forEach((key, value) -> {
            Map<String, Integer> descendingMap = new LinkedHashMap<>(value.Snippets.size());
            value.Snippets.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEach(entry -> descendingMap.put(entry.getKey(), entry.getValue()));

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
