package Indexer;

import MongoDB.MongoInterface;
import QueryProcessor.queryP;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class IndexerMain {
    public static void main(String[] args) {
        Set<String> parsedWords = new HashSet<String>();
        MongoInterface.Initialize();
       // MongoInterface.terminate();
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
            double count = words.length;
           // System.out.println(count);
            Indexer.ParseH1(toParse,toInsert,URl);
            Indexer.ParseH2(toParse,toInsert,URl);
            Indexer.ParseH3(toParse,toInsert,URl);
            Indexer.ParseH4(toParse,toInsert,URl);
            Indexer.ParseH5(toParse,toInsert,URl);
            Indexer.ParseH6(toParse,toInsert,URl);
            Indexer.ParseP(toParse,toInsert,URl);
            Indexer.ParseDiv(toParse,toInsert,URl);
            Indexer.ParseTitle(toParse,toInsert,URl);
            Iterator<Map.Entry<String, wordAttr>> iterator = toInsert.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, wordAttr> entry = iterator.next();
                double x =entry.getValue().TF/=count;
                if (x > 0.5||entry.getKey().length()==1) {
                    iterator.remove(); // remove the current element from the map
                }
            }
            //System.out.println(toInsert);
            iterator=toInsert.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, wordAttr> entry = iterator.next();
                if(!parsedWords.contains(entry.getKey()))
                {
                    parsedWords.add(entry.getKey());
                    MongoInterface.insertWord(entry.getKey(),Integer.toString(entry.getValue().priority),Double.toString(entry.getValue().TF),URl,entry.getValue().snippets);
                }
                else
                {
                  MongoInterface.addWebsiteToDoc(entry.getKey(),Integer.toString(entry.getValue().priority),Double.toString(entry.getValue().TF),URl,entry.getValue().snippets);
                }
            }

         toInsert.clear();
        }


        MongoInterface.terminate();
    }
}