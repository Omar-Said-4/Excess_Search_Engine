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
        Set<String> Words = new HashSet<String>();
        MongoInterface.Initialize();
        MongoCursor<Document> cursor= MongoInterface.getCursor("URlS_DOCS");
        //int c=0;
        HashMap<String, wordAttr> toInsert = new HashMap<>();

        while (cursor.hasNext()) {
          //  System.out.println(c++);
            Document doc = cursor.next();
            String URl = doc.getString("URL");
            String document=doc.getString("DOC");
            org.jsoup.nodes.Document toParse = Jsoup.parse(document);
            Elements h1Tags = toParse.getElementsByTag("h1");
            for (Element h1Tag : h1Tags) {
                ArrayList<String> currWords=queryP.QueryProcessor(h1Tag.text());


            }







         toInsert.clear();
        }


        MongoInterface.terminate();
    }
}