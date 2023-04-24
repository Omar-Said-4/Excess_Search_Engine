package Crawler;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.*;

public class Crawler implements  Runnable{

    Queue<String> seed;
    Set<String>links;
    String startLink;
    Queue<String> localseed = new LinkedList<>();

    Set<String> Doc_Spec_txt;
    public Crawler(String startLink,Queue<String>seed,  Set<String>links, Set<String> DST)
    {
        this.seed=seed;
        this.links=links;
        this.startLink=startLink;
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
    public static String splitStringIntoLines(String input) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < input.length(); i += 30) {
            int endIndex = Math.min(i + 30, input.length());
            sb.append(input.substring(i, endIndex)).append("\n");
        }

        return sb.toString();
    }

    public  String normalizeUrl(String Url) throws URISyntaxException {
        URL url = null;
        try {
            url = new URL(Url);
        } catch (MalformedURLException e) {
            return null;
        }
        URI uri = null;
        try {
            if(url!=null) {
                uri = new URI(url.getProtocol(),
                        url.getUserInfo(),
                        url.getHost(),
                        url.getPort(),
                        url.getPath(),
                        url.getQuery(),
                        url.getRef());
            }
        } catch (URISyntaxException e) {
            return null;
        }


        StringBuffer sb = new StringBuffer(uri.toString());
        int index = sb.indexOf("%");
        while (index >= 0) {
            if(sb.charAt(index)>=97 && sb.charAt(index)<=122)
            {
                sb.replace(index, index+1, Character.toString(sb.charAt(index)-32));
            }
            if(sb.charAt(index+1)>=97 && sb.charAt(index+1)<=122)
            {
                sb.replace(index+1, index+2,  Character.toString(sb.charAt(index+1)-32));
            }
            index = sb.indexOf("%", index + 1);
        }

        sb = new StringBuffer(uri.toString());




        String result;
        uri = uri.normalize();
        try{result = URLDecoder.decode(sb.toString(), "UTF-8").replaceAll("\\+", "%20")
                .replaceAll("\\%7E", "~").replaceAll("%2D", "-").replaceAll("%2E", ".").replaceAll("%5F", "_");} catch (UnsupportedEncodingException e)
        {
            result = sb.toString();
        }
        try
        {
            url = new URL (result);


                try {
                    uri = new URI(url.getProtocol(),
                            url.getUserInfo(),
                            url.getHost(),
                            url.getPort(),
                            url.getPath(),
                            url.getQuery(),
                            url.getRef());

                }
                catch (URISyntaxException e) {
                    return null;

                }

        } catch (MalformedURLException e) {
            return null;
        }

        uri = uri.normalize();

        sb = new StringBuffer(uri.toString());
        String NormalizedUrl=sb.toString();
        if(sb.charAt(sb.length()-1) == '/')
        {
            NormalizedUrl=NormalizedUrl.substring(0,NormalizedUrl.length()-1);
        }


        return NormalizedUrl;
//        String encodedUrl = url.replaceAll("\\{", "%7B").replaceAll("\\}", "%7D");
//        encodedUrl=encodedUrl.replaceAll("&#038;", "&").replaceAll(" ","%20").replace("|", "%7C");
//
//        URI uri = new URI(encodedUrl);
//        URI normalizedUri = uri.normalize();
//
//        return normalizedUri.toString();
    }

    private boolean RobotAllowed(String URl) throws IOException {
        URL cURl=new URL(URl);
        String host=cURl.getHost();
        // blocked due to cookies validation
        if(host.equals("www.pinterest.com")||host.equals("www.medscape.com")) {
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
            return  true;
        }
        boolean check=false;
        String lineCheck;
        while((lineCheck=Robotfile.readLine())!=null)
        {
            lineCheck = lineCheck.trim();
            if((!check)&&(lineCheck.toLowerCase().startsWith("user-agent")))
            {
                int si=lineCheck.indexOf(':')+1;
                int ei=lineCheck.length();
                String agentCheck=lineCheck.substring(si,ei).trim();
                if(agentCheck.equals("*"))
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
                if(disallowedPath.equals("/"))
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
                if(allowedPath.equals("/"))
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

        try{
            if(!RobotAllowed(URl))
            {
                System.out.println("Access denied by robots.txt");
                return null;
            }
            Connection con= Jsoup.connect(URl).timeout(20*1000);


            Document doc=con.get();

            if(con.response().statusCode()==200)
            {


                String type = con.response().contentType();
                if(type==null||!type.startsWith("text/html"))
                {
                    System.out.println("Not an HTML Page");
                    return null;
                }

                String textonly = splitStringIntoLines(doc.text()).trim();

                String[] lines = textonly.split("\n");
                StringBuilder builder = new StringBuilder();
                for (String line : lines) {
                    if(!line.isEmpty())
                        builder.append(line.charAt(0));
                    if(builder.length()>30)
                        break;
                }

                String dspec = builder.toString().trim().replaceAll("\\s+", "");;
                boolean add=false;
                //       synchronized (this.Doc_Spec_txt)
                //     {
                if(!Doc_Spec_txt.contains(dspec))
                {
                    if(!dspec.isEmpty())
                        Doc_Spec_txt.add(dspec);
                    add=true;
                }
                //  else {
//                        System.out.println(dspec);
//                        System.out.println(Doc_Spec_txt);
//                        System.out.println(URl);
                //}
                // }
                if(add) {
                    System.out.println("Link: "+ URl);
                    System.out.println(doc.title());
                    //  synchronized (this.links) {
                    links.add(URl);
                    // }

                    if(add)
                        return doc;
                    else
                        return null;
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
        int c=Globals.portion;
        localseed.add(startLink);
         //  System.out.println(startLink);


        while(c>0 &&!localseed.isEmpty())
        {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            String currURL=localseed.peek();
            localseed.remove();
            boolean proceed=true;
            //   synchronized (this.links) {
            if (links.contains(currURL)){
                proceed=false;
            }
            if(!proceed)continue;
            try {
                currURL=normalizeUrl(currURL);
            } catch (URISyntaxException e) {
                continue;
            }
            Document doc = null;
            try {
                doc = request(currURL);
            } catch (URISyntaxException e) {
                continue;
            }
            if (doc != null) {
                // synchronized (this.URL_Docs) {
                //   URL_Docs.clear();
                // URL_Docs.put(currURL, doc);
                //   Globals.count--;
                c--;
                //}
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String next_link = link.absUrl("href");
                    if (isValidURL(next_link)) {
                        localseed.add(next_link);
                    }
                }
                // System.out.println( Thread.currentThread().getName()+" "+localseed.size());
//                    if(localseed.size()<=10) {
//                        System.out.println(localseed);
//                        System.out.println(doc);
//                    }
            }

            System.out.println("Thread : " + Thread.currentThread().getName()+" remaining: "+c+" curr local seed size: "+localseed.size());

        }
        localseed.clear();
        System.out.println("thread " + Thread.currentThread().getName()+" finished with "+(Globals.portion-c) +" websites");
    }

}