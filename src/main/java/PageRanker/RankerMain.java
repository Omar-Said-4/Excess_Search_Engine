package PageRanker;

import MongoDB.MongoInterface;
import org.bson.Document;

import java.util.*;

import static QueryProcessor.queryP.QueryProcessor;

public class RankerMain {


    public static void main(String[] args){

        String prompt = "game Patricia";



        ArrayList<String> values = QueryProcessor(prompt);

        String searchQuery[]= new String[values.size()];
        searchQuery=  values.toArray(searchQuery);
        Map<String, linkAttr> toDisplayTmp = new HashMap<>();
        MongoInterface.Initialize();
        for (int i = 0; i < searchQuery.length; i++) {

            final double numOfdocs = 6000;

            List<Object> Websites = MongoInterface.getWordDocs(searchQuery[i]);
            if(Websites==null)
                continue;
            int df = Websites.size();
            for (Object obj : Websites) {
                if (obj instanceof Document) {
                    Document websiteDoc = (Document) obj;
                    String url = websiteDoc.getString("URL");
                    String title = websiteDoc.getString("Title");
                    double tf = Double.parseDouble(websiteDoc.getString("TF"));
                    double pri = 2*Math.log(Double.parseDouble(websiteDoc.getString("Pri")))* 0.1;
                    double idf = Math.log(numOfdocs / df);
                    ArrayList<String>snips= (ArrayList<String>) websiteDoc.get("Snippets");


                    if (!toDisplayTmp.containsKey(url)) {
                        linkAttr tmp = new linkAttr();
                        tmp.title = title;
                        tmp.pri += (tf * idf);
                        tmp.pri+=pri;
                        for (String snippet : snips) {
                            tmp.Snippets.put(snippet,0);
                        }
                        toDisplayTmp.put(url, tmp);
                    } else {
                        linkAttr tmp = toDisplayTmp.get(url);
                        tmp.pri += (tf * idf + pri);
                        for (String snippet : snips) {
                            if(!tmp.Snippets.containsKey(snippet)) {
                                tmp.Snippets.put(snippet, 0);
                            }
                            else
                            {
                                tmp.Snippets.put(snippet,tmp.Snippets.get(snippet)+1);
                            }
                        }
                        toDisplayTmp.put(url, tmp);
                    }
                    // System.out.println("Website URL: " + url);
                }
            }
        }

        // use the popularity in the calculation

        Comparator<String> valueComparator = new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                double value1 = toDisplayTmp.get(s1).pri;
                double value2 = toDisplayTmp.get(s2).pri;
                return Double.compare(value2, value1); // descending order
            }
        };

        Map<String, linkAttr> toDisplay = new TreeMap<>(valueComparator);
        toDisplay.putAll(toDisplayTmp);

        toDisplay.forEach((key, value) -> {


            Map<String, Integer> descendingMap = new LinkedHashMap<>();
            value.Snippets.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .forEachOrdered(e -> descendingMap.put(e.getKey(), e.getValue()));
            Map.Entry<String, Integer> firstEntry = descendingMap.entrySet().iterator().next();
            String toShow=MongoInterface.getSnippet(firstEntry.getKey());
            System.out.println("Website: " + key + ", Priority: " + value.pri +"best Snippet: "+ toShow.replaceAll("[^\\p{ASCII}]", "'"));
        });
        MongoInterface.terminate();

    }
    }
