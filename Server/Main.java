import Crawler.Crawler;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        //reading seed from file
        Queue<String> seed = new LinkedList<>();
        Map<String, Document> URl_Docs = new HashMap<>();
        List<String> Doc_Spec_txt=new ArrayList<>();
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
        Thread th1=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th1.setName("1");
        seed.remove();
        Thread th2=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th2.setName("2");
        seed.remove();
        Thread th3=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th3.setName("3");
        seed.remove();
        Thread th4=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th4.setName("4");
        seed.remove();
        Thread th5=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th5.setName("5");
        seed.remove();
        Thread th6=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th6.setName("6");
        seed.remove();
        Thread th7=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th7.setName("7");
        seed.remove();
        Thread th8=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th8.setName("8");
        seed.remove();
        Thread th9=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th9.setName("9");
        seed.remove();
        Thread th10=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th10.setName("10");
        seed.remove();
        Thread th11=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th11.setName("11");
        seed.remove();
        Thread th12=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th12.setName("12");
        seed.remove();
        Thread th13=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th13.setName("13");
        seed.remove();
        Thread th14=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th14.setName("14");
        seed.remove();
        Thread th15=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th15.setName("15");
        seed.remove();
        Thread th16=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th16.setName("16");
        seed.remove();
        Thread th17=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th17.setName("17");
        seed.remove();
        Thread th18=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th18.setName("18");
        seed.remove();
        Thread th19=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th19.setName("19");
        seed.remove();
        Thread th20=new Thread(new Crawler(seed.peek(),URl_Docs,Doc_Spec_txt));
        th20.setName("20");
        seed.remove();
        th1.start();
        th2.start();
        th3.start();
        th4.start();
        th5.start();
        th6.start();
        th7.start();
        th8.start();
        th9.start();
        th10.start();
        th11.start();
        th12.start();
        th13.start();
        th14.start();
        th15.start();
        th16.start();
        th17.start();
        th18.start();
        th19.start();
        th20.start();
        th1.join();
        th2.join();
        th3.join();
        th4.join();
        th5.join();
        th6.join();
        th7.join();
        th8.join();
        th9.join();
        th10.join();
        th11.join();
        th12.join();
        th13.join();
        th14.join();
        th15.join();
        th16.join();
        th17.join();
        th18.join();
        th19.join();
        th20.join();
        System.out.println("Crawler Finished got : "+ URl_Docs.size()+" websites");
        //User Query Processing
//        query.QueryProcessor("This is a new play field");
//        query.QueryProcessor("Hello world from the computer department with a new computing power");
    }
}
