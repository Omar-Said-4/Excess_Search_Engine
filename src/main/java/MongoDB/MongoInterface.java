package MongoDB;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.jsoup.Jsoup;

import javax.print.Doc;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Projections.include;

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

    public static List<String> getDistinctWebsites() {
        MongoDatabase db = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = db.getCollection("Snippets");

        MongoCursor<String> cursor = collection.distinct("URL", String.class).iterator();
        List<String> results = new ArrayList<>();

        while (cursor.hasNext()) {
            results.add(cursor.next());
        }

        return results;
    }

    public static List<String> getSnippetsByURL(String url) {
        MongoDatabase db = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = db.getCollection("Snippets");

        var filter = Filters.eq("URL", url);

        MongoCursor<Document> cursor = collection.find(filter).iterator();

        List<String> results = new ArrayList<>();

        while (cursor.hasNext()) {
            Document document = cursor.next();
            Object fieldValue = document.get("Snippet");
//            System.out.println(fieldValue);
            results.add(fieldValue.toString());
        }

        return results;
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

    public static String insertSnippet(String URl, String snippet, String count, String tag) {
        try {
            // Get the target database and collection
            MongoDatabase database = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = database.getCollection("Snippets");
            Document document = new Document("URL", URl)
                    .append("Snippet", snippet).append("Count", count).append("Tag", tag);
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


    public static void insertWebsitePopularity(HashMap<String, Double> webs) {
        try {
            MongoDatabase database = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = database.getCollection("Websites");

            for (HashMap.Entry<String, Double> entry : webs.entrySet()) {
                String URl = entry.getKey();
                Double Popularity = entry.getValue();
                Document document = new Document("URL", URl).append("Popularity", Popularity);
                collection.insertOne(document);
            }

        } catch (MongoException e) {
            System.err.println("Error while inserting document: " + e.getMessage());
        }
    }


    public static HashMap<String, Double> getWebsitePopularity(String[] weblist) {

        HashMap<String, Double> webs = new HashMap<>();
        try {
            MongoDatabase db = mongoClient.getDatabase("ExcessDB");
            MongoCollection<Document> collection = db.getCollection("Websites");
            List<Document> results = collection.find(Filters.in("URL", weblist)).into(new ArrayList<>());
            for (Document website : results) {
                webs.put(website.getString("URL"), website.getDouble("Popularity"));
            }

        } catch (MongoException e) {
            System.err.println("Error while getting websites-popularity: " + e.getMessage());
            return null;
        }
        return webs;
    }


    public static void insertWord(String word, String pri, String tf, String link, List<String> snippets, String
            title) {
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

    public static void addWebsiteToDoc(String word, String pri, String tf, String
            link, List<String> snippets, String title) {
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

    public static List<Object> getWordDocs(String word) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");

        if (collection.getNamespace().getCollectionName().equals("Indexer") && collection.getNamespace().getDatabaseName().equals("ExcessDB")) {
            FindIterable<Document> cursor = collection.find(Filters.and(
                    Filters.eq("Word", word),
                    Filters.exists("Websites")
            ));

            Document document = cursor.first();

            if (document != null) {
                List<Object> arrayField = document.getList("Websites", Object.class);
                return arrayField;
            }
        } else System.out.println("ERROR");

        return null;
    }


    public static List<Object> getWordDocsAll(String[] words) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Indexer");

        if (collection.getNamespace().getCollectionName().equals("Indexer") && collection.getNamespace().getDatabaseName().equals("ExcessDB")) {
            FindIterable<Document> cursor = collection.find(Filters.and(
                    Filters.in("Word", words),
                    Filters.exists("Websites")
            ));

            List<Object> result = new ArrayList<>();

            for (Document document : cursor) {
                List<Object> arrayField = document.getList("Websites", Object.class);
                result.addAll(arrayField);
            }

            return result;
        } else {
            System.out.println("ERROR");
            return null;
        }
    }


    public static String getSnippet(String id) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Snippets");

        Document projection = new Document("Snippet", 1); // Include only the "Snippet" field

        Document document = collection.find(Filters.eq("_id", new ObjectId(id)))
                .projection(projection)
                .first();

        return document.getString("Snippet");

    }

    public static void addSuggestion(String suggestion) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Suggestions");

        Document document = new Document("Query", suggestion);


        Document query = new Document("Query", new Document("$regex", suggestion).append("$options", "i"));

        FindIterable<Document> result = collection.find(query);

        if (!result.iterator().hasNext())
            collection.insertOne(document);

    }


    public static String getSuggestions(String q) {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<Document> collection = database.getCollection("Suggestions");

        Document query = new Document("Query", new Document("$regex", q).append("$options", "i"));

        MongoCursor<Document> cursor = collection.find(query).iterator();

        JSONArray result = new JSONArray();

        while (cursor.hasNext())
            result.put(cursor.next().get("Query"));

        return result.toString();
    }


    public static Map<String, List<String>> getAllSnippets() {
        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
        MongoCollection<org.bson.Document> collection = database.getCollection("Snippets");

        var projection = include("URL", "Snippet");

        var cursor = collection.find().projection(projection).iterator();

        Map<String, List<String>> results = new HashMap<>();

        while (cursor.hasNext()) {
            Document doc = cursor.next();
            if (results.get(doc.getString("URL")) == null) {
                List<String> snippets = new ArrayList<>();
                snippets.add(doc.getString("Snippet"));

                results.put(doc.getString("URL"), snippets);
            } else {
                List<String> snippets = results.get(doc.getString("URL"));
                snippets.add(doc.getString("Snippet"));

            }
        }


        return results;
    }

    public static Map<String, String> getAllTitles() {
//        MongoDatabase database = mongoClient.getDatabase("ExcessDB");
//        MongoCollection<org.bson.Document> collection = database.getCollection("Snippets");
//
//        var projection = include("URL", "Tag", "Snippet");
//
//        var cursor = collection.find().projection(projection).iterator();
//
//        Map<String, String> results = new HashMap<>();
//
//        while (cursor.hasNext()) {
//            Document doc = cursor.next();
//            if (results.get(doc.getString("URL")) == null) {
//                String tag = doc.getString("Tag");
//
//                if(tag.equals("title"))
//                {
//                    System.out.println(doc.getString("Snippet"));
//                    results.put(doc.getString("URL"), doc.getString("Snippet"));
//                }
//
//            }
//        }

        Map<String, String> results = new HashMap<>();

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


        for (String link : webs.keySet()) {
            String doc = webs.get(link);
            org.jsoup.nodes.Document document = Jsoup.parse(doc);

            results.put(link, document.title());

        }


        return results;
    }
}


