package Indexer;

import CrawlerState.CrawlerState;
import MongoDB.MongoInterface;
import QueryProcessor.queryP;
import com.mongodb.client.DistinctIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.print.Doc;
import javax.swing.event.DocumentEvent;

public class IndexerMain {

    private static class IndexerT extends Thread {
        String URl;
        String document;

        Set<String> parsedWords;


        public IndexerT(String url, String doc, Set<String> parsedWords) {
            this.URl = url;
            this.document = doc;
            this.parsedWords = parsedWords;
        }

        public void run() {
            HashMap<String, wordAttr> toInsert = new HashMap<>();
            org.jsoup.nodes.Document toParse = Jsoup.parse(document);
            String text = toParse.text();

            // Remove any non-word characters
            text = text.replaceAll("[^\\p{L}\\p{N}]+", " ");

            // Split the text into words
            String[] words = text.split("\\s+");

            // Count the number of words
            double count = words.length;
            String title = toParse.title().toString();

            HashMap<String, Integer> tagName_Priority = new HashMap<>();

            tagName_Priority.put("h1", 10);
            tagName_Priority.put("h2", 9);
            tagName_Priority.put("h3", 8);
            tagName_Priority.put("h4", 7);
            tagName_Priority.put("h5", 6);
            tagName_Priority.put("h6", 5);
            tagName_Priority.put("p", 4);
            tagName_Priority.put("div", 4);
            tagName_Priority.put("title", 10);



            for (String tag : tagName_Priority.keySet()) {
                Integer priority = tagName_Priority.get(tag);
                Indexer.ParseTag(toParse, toInsert, URl, title, tag, priority);
            }

            Iterator<Map.Entry<String, wordAttr>> iterator = toInsert.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, wordAttr> entry = iterator.next();
                double x = entry.getValue().TF /= count;
                if (x > 0.5 || entry.getKey().length() == 1) {
                    ArrayList<String> todec = entry.getValue().snippets;
                    for (int i = 0; i < todec.size(); i++) {
                        String element = todec.get(i);
                        MongoInterface.decCount(element);
                    }
                    iterator.remove(); // remove the current element from the map
                }
            }
            //System.out.println(toInsert);
            iterator = toInsert.entrySet().iterator();
            synchronized (this.parsedWords) {
                while (iterator.hasNext()) {
                    Map.Entry<String, wordAttr> entry = iterator.next();
                    if (!parsedWords.contains(entry.getKey())) {
                        parsedWords.add(entry.getKey());
                        MongoInterface.insertWord(entry.getKey(), Integer.toString(entry.getValue().priority), Double.toString(entry.getValue().TF), URl, entry.getValue().snippets, entry.getValue().title);
                    } else {
                        MongoInterface.addWebsiteToDoc(entry.getKey(), Integer.toString(entry.getValue().priority), Double.toString(entry.getValue().TF), URl, entry.getValue().snippets, entry.getValue().title);
                    }
                }
                System.out.println("Finished" + this.getId());
            }
            toInsert.clear();


        }
    }


    public static void main(String[] args) {

        MongoInterface.Initialize();

        MongoInterface.deleteAllDocuments("Snippets");


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

        DistinctIterable<String> distinctWords = MongoInterface.getWords();
        for (String name : distinctWords) {

            parsedWords.add(name);
        }


        // MongoInterface.terminate();
        //int c=0;

        // int c=0;
        for (Map.Entry<String, String> Wentry : webs.entrySet()) {

            //System.out.println(c++);

            String URl = Wentry.getKey();
            String document = Wentry.getValue();
            ;
            System.out.println(URl);
            //System.out.println(document);

            new Thread(() -> {
                new IndexerT(URl, document, parsedWords).start();
            }).start();

        }

//         MongoInterface.terminate();
    }
}