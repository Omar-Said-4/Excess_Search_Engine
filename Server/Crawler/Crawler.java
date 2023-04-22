package Crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
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
    public  String normalizeUrl(String url) throws URISyntaxException {
        String encodedUrl = url.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D");
        encodedUrl=encodedUrl.replaceAll("&#038;", "&").replaceAll(" ","%20");

        URI uri = new URI(encodedUrl);
        URI normalizedUri = uri.normalize();

        return normalizedUri.toString();
    }

    private boolean RobotAllowed(String URl) throws IOException {
        URL cURl=new URL(URl);
        String host=cURl.getHost();
        // blocked due to cookies validation
        if(host.equals("www.pinterest.com")||host.equals("www.medscape.com")) {
            System.out.println("dasssssad");
            return false;
        }
        String RobotsURl = cURl.getProtocol()+"://"+host+(cURl.getPort()>-1?":"+cURl.getPort():"")+"/robots.txt";
        URL RobotUrl;
        String path =cURl.getPath();
        try{
            RobotUrl= new URL(RobotsURl);
        }
        catch (MalformedURLException e)
        {
            return  true;
        }
        BufferedReader Robotfile;
        try{
            Robotfile = new BufferedReader(new InputStreamReader(RobotUrl.openStream()));
        }
        catch (IOException e)
        {
            return  false;
        }
        boolean check=false;
        String lineCheck;
        while((lineCheck=Robotfile.readLine())!=null)
        {
            lineCheck.trim();
            if((!check)&&(lineCheck.toLowerCase().startsWith("user-agent")))
            {
               int si=lineCheck.indexOf(':')+1;
               int ei=lineCheck.length();
               String agentCheck=lineCheck.substring(si,ei).trim();
               if(agentCheck=="*")
               {
                   check=true;
               }
            }
            else if((check)&&(lineCheck.toLowerCase().startsWith("user-agent")))
            {
                Robotfile.close();
                return true;
            }
            else if(check&&lineCheck.toLowerCase().startsWith("disallow"))
            {
                int si=lineCheck.indexOf(':')+1;
                int ei=lineCheck.length();
                String disallowedPath=lineCheck.substring(si,ei).trim();
                if(disallowedPath=="/")
                {
                    Robotfile.close();
                    return false;
                }
                else if(disallowedPath.length()==0)
                {
                    Robotfile.close();
                    return true;
                }
                else if(disallowedPath.length()<=path.length())
                {
                    String checkPath=path.substring(0,disallowedPath.length());
                    if(checkPath.equals(disallowedPath))
                    {
                        Robotfile.close();
                        return false;
                    }
                }
            }
            else if(check&&lineCheck.toLowerCase().startsWith("allow"))
            {
                int si=lineCheck.indexOf(':')+1;
                int ei=lineCheck.length();
                String allowedPath=lineCheck.substring(si,ei).trim();
                if(allowedPath=="/")
                {
                    Robotfile.close();
                    return true;
                }
                else if(allowedPath.length()==0)
                {
                    Robotfile.close();
                    return false;
                }
                else if(allowedPath.length()<=path.length())
                {
                    String checkPath=path.substring(0,allowedPath.length());
                    if(checkPath.equals(allowedPath))
                    {
                        Robotfile.close();
                        return true;
                    }
                }
            }
        }
        Robotfile.close();
        return true;
    }
    private Document request (String URl) throws URISyntaxException {
        URl=normalizeUrl(URl);

        try{
            if(!RobotAllowed(URl))
            {
                System.out.println("Access denied by robots.txt");
                return null;
            }
            Connection con= Jsoup.connect(URl);


            Document doc=con.get();

            if(con.response().statusCode()==200)
            {


                String type = con.response().contentType();
                if(type==null||!type.startsWith("text/html"))
                {
                    System.out.println("Not an HTML Page");
                    return null;
                }

                String textonly = doc.text();
                String[] lines = textonly.split("\n");
                // Elements paragraphs = doc.select("p");
                StringBuilder builder = new StringBuilder();
                for (String line : lines) {
                    if(!line.isEmpty())
                    builder.append(line.charAt(0));
                }
//                for (Element paragraph : paragraphs) {
//                    String text = paragraph.text();// get the text of the paragraph
//                    if(text.length()>0) {
//                        char firstLetter = text.charAt(0);
//                        builder.append(firstLetter);
//                    }
//                    // get the first letter of the text
//                    //  System.out.println(firstLetter); // print the first letter
//                }
                String dspec = builder.toString().trim();
                Boolean add=false;
                synchronized (this.Doc_Spec_txt)
                {
                    if(!Doc_Spec_txt.contains(dspec))
                    {
                        if(!dspec.isEmpty())
                        Doc_Spec_txt.add(dspec);
                        add=true;
                    }
                }
                if(add=true) {
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
    }    @Override
    public void run() {
        int count=0;
        localseed.add(startLink);
        while(count<400&&!localseed.isEmpty())
        {
            String currURL=localseed.peek();
            if(!localseed.isEmpty()) {
                Document doc = null;
                try {
                    doc = request(localseed.peek());
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
                localseed.remove();
                if (doc != null) {
                    synchronized (this.URL_Docs) {
                        URL_Docs.put(currURL, doc);
                    }
                    for (Element link : doc.select("a[href]")) {
                        String next_link = link.attr("abs:href");
                        synchronized (this.visited) {
                            if (visited.contains(next_link) == false && isValidURL(next_link)) {
                                localseed.add(next_link);
                            }
                        }
                    }
                }
                count++;
            }

        }
      System.out.println("thread " + Thread.currentThread().getName()+" finished with "+ count +" websites");
    }

}
