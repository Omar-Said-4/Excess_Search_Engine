package PhraseSearching.java;

import MongoDB.MongoInterface;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import java.util.ArrayList;


class PhraseSearching {


    public static void main(String[] args) throws InterruptedException {
            PhraseSearching.search("pope");
    }
    public static void search(String phrase) {

        MongoInterface.Initialize();
        MongoCursor<Document> cursor = MongoInterface.getCollectionByWord("pope", "ExcessDB", "Indexer");

        MongoInterface.terminate();


//        ArrayList<Integer> indices = new ArrayList<Integer>();
//        int index = snippet.indexOf(phrase);
//
//        do{
//            indices.add(index);
//            System.out.println("Substring found at index: " + index);
//            index = snippet.indexOf(phrase, index + 1);
//
//        }while(index != -1);
//
//        return indices;
    }
}
