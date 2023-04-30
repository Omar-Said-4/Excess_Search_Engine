package Indexer;

import CrawlerState.CrawlerState;
import MongoDB.MongoInterface;
import QueryProcessor.queryP;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import javax.swing.event.DocumentEvent;

public class IndexerMain {
    public static void main(String[] args) {


        String filePath = "Websites.ser";
        File file = new File(filePath);

        HashMap<String, String> webs = new HashMap<>();

        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                webs = (HashMap<String, String>) in.readObject();
                System.out.println("All websites loaded from file: " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }

        Set<String> parsedWords = new HashSet<String>();
        MongoInterface.Initialize();
       // MongoInterface.terminate();
        MongoCursor<Document> cursor= MongoInterface.getCursor("URlS_DOCS");
        //int c=0;
        HashMap<String, wordAttr> toInsert = new HashMap<>();
       // int c=0;
        while (cursor.hasNext()) {

            //System.out.println(c++);
            Document doc = cursor.next();
            String URl = doc.getString("URL");
            String document=doc.getString("DOC");;


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