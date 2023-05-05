package MongoDB;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MongoInterface {
    static MongoClient mongoClient;
    static String connectionString = "mongodb+srv://omarali:TAdkQngpQVbSjAiM@excess.f6qpdn9.mongodb.net/?retryWrites=true&w=majority";

    public static void Initialize() {
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

    public static void terminate() {
        mongoClient.close();
        System.out.println("Connection to Excess database is terminated");

    }

    public static Document getDocumentByWord(String word, String dbName, String collectionName) {
        // Try to get the id the of the document that has the word in the phrase
        Document doc = null;

        try {
            MongoDatabase database = mongoClient.getDatabase(dbName);
            MongoCollection<Document> collection = database.getCollection(collectionName);

            Document query = new Document("Word", word);
            doc = collection.find(query).first();

//            doc = cursor.first();
            // Iterate over the documents and print them

            System.out.println(doc.toBsonDocument().getArray("Websites"));


//            // Close the cursor and the MongoDB client
//            cursor.close();
        } catch (MongoException e) {
            System.err.println("err");
        }

        return doc;
    }


    public static void searchSubstrInSnippet(String subStr) {

        try {
            MongoDatabase db = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = db.getCollection("Snippets");

            Document query = new Document("Snippet", new Document("$regex", subStr).append("$options", "i"));

            MongoCursor<Document> cursor = collection.find(query).iterator();

            // Print the results
            List<Document> results = new ArrayList<>();
            while (cursor.hasNext()) {
                Document document = cursor.next();
                results.add(document);
                System.out.println(document.toJson());
            }

            System.out.println("Found " + results.size() + " documents.");

        }
        catch (MongoException e){
            System.out.println("Error");
        }

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

    public static String insertSnippet(String URl, String snippet,String count , String tag) {
        try {
            // Get the target database and collection
            MongoDatabase database = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = database.getCollection("Snippets");
            Document document = new Document("URL", URl)
                    .append("Snippet", snippet).append("Count",count).append("Tag", tag);
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


    public static void insertWebsitePopularity(HashMap<String , Double> webs)
    {
        try{
            MongoDatabase database = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = database.getCollection("Websites");

            for (HashMap.Entry<String, Double> entry : webs.entrySet()) {
                String URl = entry.getKey();
                Double Popularity = entry.getValue();
                Document document = new Document("URL", URl).append("Popularity", Popularity);
                collection.insertOne(document);
            }

        }catch (MongoException e){
            System.err.println("Error while inserting document: " + e.getMessage());
        }
    }



    public static HashMap<String , Double> getWebsitePopularity(String[] weblist)
    {

        HashMap<String , Double> webs = new HashMap<>();
        try{
            MongoDatabase db = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = db.getCollection("Websites");
            List<Document> results = collection.find(Filters.in("URL", weblist)).into(new ArrayList<>());
            for(Document website : results)
            {
                webs.put(website.getString("URL"),website.getDouble("Popularity"));
            }

        }catch(MongoException e)
        {
            System.err.println("Error while getting websites-popularity: " + e.getMessage());
            return null;
        }
        return webs;
    }



    public static void insertWord(String word, String pri, String tf, String link, List<String> snippets, String title) {
        ArrayList<Document> docs = new ArrayList<>();
        Document doc = new Document("URL", link).append("Pri", pri).append("TF", tf).append("Snippets", snippets).append("Title", title);
        docs.add(doc);
        Document document = new Document("Word", word)
                .append("Websites", docs);
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");
        collection.insertOne(document);
        System.out.println("Inserted  word " + word);
    }

    public static void addWebsiteToDoc(String word, String pri, String tf, String link, List<String> snippets, String title) {
        Document doc = new Document("URL", link).append("Pri", pri).append("TF", tf).append("Snippets", snippets).append("Title", title);
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");
        collection.updateOne(Filters.eq("Word", word), Updates.push("Websites", doc));
        System.out.println("added website to word " + word);
    }

    public static MongoCursor<Document> getCursor(String c) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("URlS_DOCS");

        MongoCursor<Document> cursor = collection.find().limit(5).iterator();
        return cursor;

    }

    public static void decCount(String id) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");

        MongoCollection<Document> collection = database.getCollection("Snippets");
        Document filter = new Document("_id", new ObjectId(id));
        Document document = collection.find(filter).first();

        String c = document.getString("Count");
// create a filter that matches the document you want to update
        Bson filter2 = Filters.eq("_id", new ObjectId(id));
        c = Integer.toString((Integer.parseInt(c)) - 1);
        Bson updateOperation = Updates.set("Count", c);

// perform the update operation
        UpdateResult result = collection.updateOne(filter2, updateOperation);

// print the number of documents that were updated
        System.out.println(result.getModifiedCount() + " document(s) updated");

    }

    public static DistinctIterable<String> getWords() {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");
        DistinctIterable<String> distinctWords = collection.distinct("Word", String.class);
        return distinctWords;
    }
    public static  List<Object> getWordDocs(String word)
    {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");
        Document document =  collection.find(Filters.and(
                Filters.eq("Word", word),
                Filters.exists("Websites")
        )).first();
        List<Object> arrayField = document.getList("Websites", Object.class);

        return arrayField;
    }

}
