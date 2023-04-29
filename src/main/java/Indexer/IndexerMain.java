package Indexer;

import MongoDB.MongoInterface;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

public class IndexerMain {
    public static void main(String[] args) {
        MongoInterface.Initialize();
        MongoCursor<Document> cursor= MongoInterface.getCursor("Indexer");
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            String value = doc.getString("Word");

            // do something with the document
            System.out.println(value);
        }


        MongoInterface.terminate();
    }
}