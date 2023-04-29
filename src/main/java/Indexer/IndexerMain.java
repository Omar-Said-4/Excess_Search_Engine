package Indexer;

import MongoDB.MongoInterface;
import QueryProcessor.queryP;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IndexerMain {
    public static void main(String[] args) {
        Set<String> parsedWords = new HashSet<String>();
        MongoInterface.Initialize();
        MongoCursor<Document> cursor= MongoInterface.getCursor("URlS_DOCS");
        //int c=0;
        HashMap<String, wordAttr> toInsert = new HashMap<>();
       // int c=0;
        while (cursor.hasNext()) {
         //   System.out.println(c++);

            Document doc = cursor.next();
            String URl = doc.getString("URL");
            String document=doc.getString("DOC");

            org.jsoup.nodes.Document toParse = Jsoup.parse(document);
            String text = toParse.text();

            // Remove any non-word characters
            text = text.replaceAll("[^\\p{L}\\p{N}]+", " ");

            // Split the text into words
            String[] words = text.split("\\s+");

            // Count the number of words
            int count = words.length;
            //System.out.println(count);

            Elements h1Tags = toParse.getElementsByTag("h1");
            for (Element h1Tag : h1Tags) {
                ArrayList<String> currWords=queryP.QueryProcessor(h1Tag.text());
                //System.out.println(currWords);
                for (String word : currWords) {
                    wordAttr tmp=new wordAttr();
                        if(!toInsert.containsKey(word))
                        {
                            tmp.link=URl;
                            tmp.priority+=10;
                            tmp.TF++;
                            tmp.tags.add("h1");
                            toInsert.put(word,tmp);
                        }
                        else
                        {
                            wordAttr tmp2=toInsert.get(word);
                            tmp2.TF++;
                            if(!tmp2.tags.contains("h1")) {
                                tmp2.tags.add("h1");
                                tmp2.priority += 10;
                            }
                        }
                }

            }







         toInsert.clear();
        }


        MongoInterface.terminate();
    }
}