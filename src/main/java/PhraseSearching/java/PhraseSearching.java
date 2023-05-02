package PhraseSearching.java;

import MongoDB.MongoInterface;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;


class PhraseSearching {


    public static void main(String[] args) throws InterruptedException {
            PhraseSearching.search("test case");
    }
    public static void search(String phrase) {

        MongoInterface.Initialize();

//        String[] words = phrase.split("\\s+");
        Document doc = null;

        MongoInterface.searchSubstrInSnippet(phrase);

//        for(String word : words){
//            doc = MongoInterface.getCollectionByWord("pope", "ExcessDB", "Indexer");
/////            cursor
////            Document doc = cursor.next();
////
////            System.out.println(doc);
//        }

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
