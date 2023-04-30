import Crawler.Crawler;
import Crawler.Globals;
import CrawlerState.CrawlerState;
import MongoDB.MongoInterface;
import org.bson.Document;
import org.jsoup.Jsoup;


import javax.print.Doc;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    public static void main(String[] args) throws InterruptedException {



        CrawlerState state = null;
        String filePath = "crawler_state.ser";
        File file = new File(filePath);

        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                state = (CrawlerState) in.readObject();
                System.out.println("Crawler state loaded from file: " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                // Handle the exception appropriately
            }
        }

//        MongoInterface.Initialize();
//        MongoInterface.deleteAllDocuments("URlS_DOCS");
//        MongoInterface.terminate();
       // Thread.sleep(3000000);
        //reading seed from file
        Queue<String> seed = new LinkedList<>();
        Set<String> Doc_Spec_txt=ConcurrentHashMap.newKeySet();
        Set<String>links= ConcurrentHashMap.newKeySet();
        ConcurrentLinkedQueue<String>[] BFS = new ConcurrentLinkedQueue[50];

        
        if (state==null || state.getFlag() == 1) {
            for (int i = 0; i < 50; i++) {
                BFS[i] = new ConcurrentLinkedQueue<>();
            }
            try {
                File myObj = new File("Excess_Search_Engine/src/main/java/Crawler/seed.txt");
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


            for (int i = 0; i < 21; i++) {
                BFS[0].add(seed.peek());
                seed.remove();
            }
            Globals.levelNum.set(BFS[0].size());

        }
        else
        {
            BFS = state.getBFS();
            links = state.getLinks();
            Globals.numThreads = state.getNumThreads();
            Globals.turn = (state.getCurrent_Turn());
            Globals.count = state.getCount();
            Globals.levelNum.set(BFS[Globals.turn.get()].size());
            Doc_Spec_txt = state.getDoc();

        }

        Thread[] threads = new Thread[Globals.numThreads];





        for (int i = 0; i < Globals.numThreads; i++) {
            threads[i] = new Thread(new Crawler(BFS,links,Doc_Spec_txt));
            threads[i].setName(Integer.toString(i));
            threads[i].start();
        }



        ConcurrentLinkedQueue<String>[] finalBFS = BFS;
        Set<String> finalLinks = links;
        Set<String> finalDoc_Spec_txt = Doc_Spec_txt;

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            int flag = 0;
            if (Globals.count.get() <= 0) {
                flag = 1;
            }
            CrawlerState Runstate = new CrawlerState(finalBFS, Globals.numThreads, Globals.turn, flag, Globals.count, Globals.levelNum, finalLinks, finalDoc_Spec_txt);
            try (FileOutputStream fileOut = new FileOutputStream("crawler_state.ser");
                 ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
                out.writeObject(Runstate);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));


        for (int i = 0; i < Globals.numThreads; i++) {
            threads[i].join();
            System.out.println("Joined " + i );
        }

        System.out.println("Crawler Finished got : "+ links.size()+" websites inserting them to ExcessDB");


        HashMap<String , String> webs = new HashMap<>() ;

        if (Globals.count.get() <= 0 ) {
//            MongoInterface.Initialize();

            links.forEach(link -> {
                try {

                    org.jsoup.nodes.Document doc = Jsoup.connect(link).get();
                        webs.put(link, doc.toString());
                    // Insert the text into the MongoDB collection
//                    MongoInterface.insertDocument("ExcessDB", "URlS_DOCS", document);

                } catch (IOException e) {
                    System.out.println("Error fetching URL: " + e.getMessage());
                }
            });
//            MongoInterface.terminate();
        }

        try (FileOutputStream fileOut = new FileOutputStream("Websites.ser");
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(webs);
        } catch (IOException e) {
            e.printStackTrace();
        }












        //User Query Processing
//        QueryProcessor.query.QueryProcessor("This is a new play field");
//        QueryProcessor.query.QueryProcessor("Hello world from the computer department with a new computing power");
    }

}