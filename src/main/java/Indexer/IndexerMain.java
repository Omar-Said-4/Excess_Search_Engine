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
            Indexer.ParseH1(toParse,toInsert,URl);

         toInsert.clear();
        }


        MongoInterface.terminate();
    }
}