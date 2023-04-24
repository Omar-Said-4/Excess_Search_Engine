import Crawler.Globals;
import Crawler.Crawler;
import java.util.concurrent.ConcurrentHashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //reading seed from file
        Queue<String> seed = new LinkedList<>();
        Set<String> Doc_Spec_txt=ConcurrentHashMap.newKeySet();
        Set<String>links= ConcurrentHashMap.newKeySet();
        ConcurrentLinkedQueue<String>[] BFS = new ConcurrentLinkedQueue[50];
        for (int i = 0; i < 50; i++) {
            BFS[i] = new ConcurrentLinkedQueue<>();
        }
        try {
            File myObj = new File("Server/Crawler/seed.txt");
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

        for(int i=0;i<25;i++)
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

        for (int i = 0; i < 20; i++) {
            threads[i].join();
            System.out.println("Joined " + i );
        }

        System.out.println("Crawler Finished got : "+ links.size()+" websites");
        //User Query Processing
//        query.QueryProcessor("This is a new play field");
//        query.QueryProcessor("Hello world from the computer department with a new computing power");
    }
}