package MongoDB;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.Document;
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

}