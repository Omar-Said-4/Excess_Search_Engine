package Crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Crawler implements  Runnable{

    String startLink;
    Queue<String> localseed = new LinkedList<>();
    ArrayList<String>visited=new ArrayList<String>();
    Map<String, Document> URL_Docs;
    List<String> Doc_Spec_txt;
    public Crawler(String stl, Map<String, Document>UD, List<String> DST)
    {
        startLink=stl;
        URL_Docs=UD;
        Doc_Spec_txt=DST;
    }
    private boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Document request (String URl)
    {

        try{

            Connection con= Jsoup.connect(URl);


            Document doc=con.get();

            if(con.response().statusCode()==200)
            {


                Elements paragraphs = doc.select("p");
                StringBuilder builder = new StringBuilder();

                for (Element paragraph : paragraphs) {
                    String text = paragraph.text();// get the text of the paragraph
                    if(text.length()>0) {
                        char firstLetter = text.charAt(0);
                        builder.append(firstLetter);
                    }
                    // get the first letter of the text
                    //  System.out.println(firstLetter); // print the first letter
                }
                String dspec = builder.toString();
                Boolean add=false;
                synchronized (this.Doc_Spec_txt)
                {
                    if(!Doc_Spec_txt.contains(dspec))
                    {
                        Doc_Spec_txt.add(dspec);
                        add=true;
                    }
                }
                if(add) {
                    System.out.println("Link: "+ URl);
                    System.out.println(doc.title());
                    visited.add(URl);
                    return doc;
                }
            }
            else
                return null;
        }
        catch (IOException e){
            return null;
        }
        return null;
    }
    @Override
    public void run() {
        int count=0;
        localseed.add(startLink);
        while(count<400&&!localseed.isEmpty())
        {
            String currURL=localseed.peek();
            if(!localseed.isEmpty()) {
                Document doc = request(localseed.peek());
                localseed.remove();
                if (doc != null) {
                    synchronized (this.URL_Docs) {
                        URL_Docs.put(currURL, doc);
                    }
                    for (Element link : doc.select("a[href]")) {
                        String next_link = link.absUrl("href");
                        if (visited.contains(next_link) == false&&isValidURL(next_link)) {
                            localseed.add(next_link);
                        }
                    }
                }
                count++;
            }

        }
      System.out.println("thread " + Thread.currentThread().getName()+" finished");
    }

}
