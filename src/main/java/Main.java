import Crawler.Crawler;
import Crawler.Globals;
import MongoDB.MongoInterface;
import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
public class Main {
    public static void main(String[] args) throws InterruptedException {


        MongoInterface.Initialize();
        MongoInterface.deleteAllDocuments("URlS_DOCS");
        MongoInterface.terminate();
       // Thread.sleep(3000000);
        //reading seed from file
        Queue<String> seed = new LinkedList<>();
        Set<String> Doc_Spec_txt=ConcurrentHashMap.newKeySet();
        Set<String>links= ConcurrentHashMap.newKeySet();
        ConcurrentLinkedQueue<String>[] BFS = new ConcurrentLinkedQueue[50];
        for (int i = 0; i < 50; i++) {
            BFS[i] = new ConcurrentLinkedQueue<>();
        }
        try {
            File myObj = new File("src/main/java/Crawler/seed.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNext()) {
                String link = myReader.next();
                seed.add(link);
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
          //System.out.println(seed);
        //Crawling
        //crawling threads
        System.out.println("Please, enter the desired number of threads");

        Scanner input = new Scanner(System.in);
        Globals.numThreads = input.nextInt();

        Thread[] threads = new Thread[ Globals.numThreads];

        for(int i=0;i<21;i++)
        {
            BFS[0].add(seed.peek());
            seed.remove();
        }
        Globals.levelNum.set(BFS[0].size());
        for (int i = 0; i < Globals.numThreads; i++) {
            threads[i] = new Thread(new Crawler(BFS,links,Doc_Spec_txt));
            threads[i].setName(Integer.toString(i));
            threads[i].start();
        }

        for (int i = 0; i < Globals.numThreads; i++) {
            threads[i].join();
            System.out.println("Joined " + i );
        }

        System.out.println("Crawler Finished got : "+ links.size()+" websites inserting them to ExcessDB");
        MongoInterface.Initialize();

        links.forEach(link -> {
            try {

                org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
                Document document = new Document("URL", link)
                        .append("DOC", doc.toString());

                // Insert the text into the MongoDB collection
                MongoInterface.insertDocument("ExcessDB","URlS_DOCS",document);

            } catch (IOException e) {
                System.out.println("Error fetching URL: " + e.getMessage());
            }
        });
        MongoInterface.terminate();

        //User Query Processing
//        query.QueryProcessor("This is a new play field");
//        query.QueryProcessor("Hello world from the computer department with a new computing power");
    }
}