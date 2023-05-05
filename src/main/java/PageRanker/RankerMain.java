package PageRanker;

import MongoDB.MongoInterface;
import org.bson.Document;

import java.util.*;

import static QueryProcessor.queryP.QueryProcessor;

public class RankerMain {


    public static void main(String[] args){

        String prompt = "Hello let's play football";

        // placeholder for the query processor

        Scanner s= new Scanner(System.in);

        prompt = s.next();

        ArrayList<String> values = QueryProcessor(prompt);

        String searchQuery[]= values.toArray(new String[values.size()]);

        Map<String, Integer> Snippets = new HashMap<>();

        Map<String, linkAttr> toDisplayTmp = new HashMap<>();
        MongoInterface.Initialize();

        for (int i = 0; i < searchQuery.length; i++) {

            final double numOfdocs = 6000;

            List<Object> Websites = MongoInterface.getWordDocs(searchQuery[i]);
            int df = Websites.size();
            for (Object obj : Websites) {
                if (obj instanceof Document) {
                    Document websiteDoc = (Document) obj;
                    String url = websiteDoc.getString("URL");
                    String title = websiteDoc.getString("Title");
                    Double tf = Double.parseDouble(websiteDoc.getString("TF"));
                    Double pri = Math.log(Double.parseDouble(websiteDoc.getString("Pri")))* 0.1;
                    Double idf = Math.log(numOfdocs / df);

                    if (!toDisplayTmp.containsKey(url)) {
                        linkAttr tmp = new linkAttr();
                        tmp.title = title;
                        tmp.pri += (tf * idf);
                        tmp.pri+=pri;
                        toDisplayTmp.put(url, tmp);
                    } else {
                        linkAttr tmp = toDisplayTmp.get(url);
                        tmp.pri += (tf * idf + pri);
                        toDisplayTmp.put(url, tmp);
                    }
                    // System.out.println("Website URL: " + url);
                }
            }
        }
        MongoInterface.terminate();
        // use the popularity in the calculation

        Comparator<String> valueComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                Double value1 = toDisplayTmp.get(s1).pri;
                Double value2 = toDisplayTmp.get(s2).pri;
                return Double.compare(value2, value1); // descending order
            }
        };
        Map<String, linkAttr> toDisplay = new TreeMap<>(valueComparator);
        toDisplay.putAll(toDisplayTmp);

        toDisplay.forEach((key, value) -> {
            System.out.println("Website: " + key + ", Priority: " + value.pri);
        });
    }
    }
