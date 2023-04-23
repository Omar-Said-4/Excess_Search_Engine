import Crawler.Crawler;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //reading seed from file
        Queue<String> seed = new LinkedList<>();
        List<String> Doc_Spec_txt=new ArrayList<String>();
        Set<String>links=new HashSet<String>();
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
      //  System.out.println(seed);
        //Crawling
        //crawling threads
        Thread[] threads = new Thread[20];
        for (int i = 0; i < 20; i++) {
            threads[i] = new Thread(new Crawler(seed.peek(),links,Doc_Spec_txt));
            threads[i].setName(Integer.toString(i));
            seed.remove();
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
