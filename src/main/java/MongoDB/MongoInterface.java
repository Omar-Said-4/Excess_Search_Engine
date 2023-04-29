package MongoDB;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class MongoInterface {
    static MongoClient mongoClient;
    static String connectionString = "mongodb+srv://omarali:TAdkQngpQVbSjAiM@excess.f6qpdn9.mongodb.net/?retryWrites=true&w=majority";

    public static void  Initialize() {
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();
        // Create a new client and connect to the server
        mongoClient = MongoClients.create(settings);
        System.out.println("Connection to Excess database is successful");


    }
    public static void terminate()
    {
        mongoClient.close();
        System.out.println("Connection to Excess database is terminated");

    }
    public static void insertDocument(String databaseName, String collectionName, Document document) {

        // Create a MongoClient with the provided connection string

        try {
            // Get the target database and collection
            MongoDatabase database = mongoClient.getDatabase(databaseName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            // Insert the document into the collection
            collection.insertOne(document);

            System.out.println("Document inserted successfully");

        } catch (MongoException e) {
            System.err.println("Error while inserting document: " + e.getMessage());
        }
    }
    public static void deleteAllDocuments(String collectionName) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        DeleteResult deleteResult = collection.deleteMany(new Document());
        System.out.println(deleteResult.getDeletedCount() + " documents deleted from collection " + collectionName);
    }
    public static String insertSnippet(String URl, String snippet)
    {
        try {
            // Get the target database and collection
            MongoDatabase database = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = database.getCollection("Snippets");
            Document document = new Document("URL", URl)
                    .append("Snippet", snippet);
            // Insert the document into the collection
            collection.insertOne(document);

            ObjectId id = document.getObjectId("_id");
            System.out.println("Inserted Snippet with _id: " + id);
            return id.toString();

        } catch (MongoException e) {
            System.err.println("Error while inserting document: " + e.getMessage());
            return null;
        }
    }
    public static void insertWord(String word, String pri, String tf, String link, List<String> snippets)
    {
        ArrayList<Document>docs=new ArrayList<>();
        Document doc=new Document("URL",link).append("Pri",pri).append("TF",tf).append("Snippets",snippets);
        docs.add(doc);
        Document document = new Document("Word", word)
                .append("Websites", docs);
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");
        collection.insertOne(document);
        System.out.println("Inserted  word " + word);
    }

    public static void addWebsiteToDoc(String word, String pri, String tf, String link, List<String> snippets)
    {
        Document doc=new Document("URL",link).append("Pri",pri).append("TF",tf).append("Snippets",snippets);
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");
        collection.updateOne(Filters.eq("Word", word), Updates.push("Websites", doc));
        System.out.println("added website to word " + word);
    }
    public static MongoCursor<Document> getCursor(String c)
    {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection(c);
        return collection.find().iterator();

    }

}
