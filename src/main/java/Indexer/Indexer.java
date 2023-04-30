package Indexer;

import MongoDB.MongoInterface;
import QueryProcessor.queryP;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class Indexer {
public static void ParseH1(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
{
    Elements h1Tags = toParse.getElementsByTag("h1");
    String snippetId=null;
    for (Element h1Tag : h1Tags) {
        ArrayList<String> currWords= queryP.QueryProcessor(h1Tag.text());
        snippetId= MongoInterface.insertSnippet(URl,h1Tag.text(),Integer.toString(currWords.size()));
        //System.out.println(currWords);
        for (String word : currWords) {
            wordAttr tmp=new wordAttr();
            if(!toInsert.containsKey(word))
            {
                tmp.link=URl;
                tmp.priority+=10;
                tmp.TF++;
                tmp.tags.add("h1");
                tmp.title=title;
                tmp.snippets.add(snippetId);
                toInsert.put(word,tmp);
            }
            else
            {
                wordAttr tmp2=toInsert.get(word);
                tmp2.TF++;
                tmp2.snippets.add(snippetId);
                if(!tmp2.tags.contains("h1")) {
                    tmp2.tags.add("h1");
                    tmp2.priority += 10;
                }
            }
        }

    }
}
public static void ParseH2(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
{
    Elements h2Tags = toParse.getElementsByTag("h2");
    String snippetId=null;
    for (Element h2Tag : h2Tags) {
        ArrayList<String> currWords= queryP.QueryProcessor(h2Tag.text());
        snippetId= MongoInterface.insertSnippet(URl,h2Tag.text(),Integer.toString(currWords.size()));
        //System.out.println(currWords);
        for (String word : currWords) {
            wordAttr tmp=new wordAttr();
            if(!toInsert.containsKey(word))
            {
                tmp.link=URl;
                tmp.priority+=9;
                tmp.TF++;
                tmp.tags.add("h2");
                tmp.title=title;
                tmp.snippets.add(snippetId);
                toInsert.put(word,tmp);
            }
            else
            {
                wordAttr tmp2=toInsert.get(word);
                tmp2.TF++;
                tmp2.snippets.add(snippetId);
                if(!tmp2.tags.contains("h2")) {
                    tmp2.tags.add("h2");
                    tmp2.priority += 9;
                }
            }
        }

    }
}
public static void ParseH3(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
{
        Elements h3Tags = toParse.getElementsByTag("h3");
    String snippetId=null;
    for (Element h3Tag : h3Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h3Tag.text());
        snippetId= MongoInterface.insertSnippet(URl,h3Tag.text(),Integer.toString(currWords.size()));
        //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=8;
                    tmp.TF++;
                    tmp.tags.add("h3");
                    tmp.title=title;
                    tmp.snippets.add(snippetId);
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if(!tmp2.tags.contains("h3")) {
                        tmp2.tags.add("h3");
                        tmp2.priority += 8;
                    }
                }
            }

        }
}


public static void ParseH4(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements h4Tags = toParse.getElementsByTag("h4");
        String snippetId=null;
        for (Element h4Tag : h4Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h4Tag.text());
            snippetId= MongoInterface.insertSnippet(URl,h4Tag.text(),Integer.toString(currWords.size()));
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=7;
                    tmp.TF++;
                    tmp.tags.add("h4");
                    tmp.title=title;
                    tmp.snippets.add(snippetId);
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if(!tmp2.tags.contains("h4")) {
                        tmp2.tags.add("h4");
                        tmp2.priority += 7;
                    }
                }
            }

        }
    }


    public static void ParseH5(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements h5Tags = toParse.getElementsByTag("h5");
        String snippetId=null;
        for (Element h5Tag : h5Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h5Tag.text());
            snippetId= MongoInterface.insertSnippet(URl,h5Tag.text(),Integer.toString(currWords.size()));
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=6;
                    tmp.TF++;
                    tmp.tags.add("h5");
                    tmp.title=title;
                    tmp.snippets.add(snippetId);
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if(!tmp2.tags.contains("h5")) {
                        tmp2.tags.add("h5");
                        tmp2.priority += 6;
                    }
                }
            }

        }
    }

    public static void ParseH6(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements h6Tags = toParse.getElementsByTag("h6");
        String snippetId=null;
        for (Element h6Tag : h6Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h6Tag.text());
            snippetId= MongoInterface.insertSnippet(URl,h6Tag.text(),Integer.toString(currWords.size()));
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=5;
                    tmp.TF++;
                    tmp.title=title;
                    tmp.tags.add("h6");
                    tmp.snippets.add(snippetId);
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if(!tmp2.tags.contains("h6")) {
                        tmp2.tags.add("h6");
                        tmp2.priority += 5;
                    }
                }
            }

        }
    }
    public static void ParseP(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements pTags = toParse.getElementsByTag("p");
        String snippetId=null;
        for (Element pTag : pTags) {
            ArrayList<String> currWords= queryP.QueryProcessor(pTag.text());
            snippetId= MongoInterface.insertSnippet(URl,pTag.text(),Integer.toString(currWords.size()));
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=4;
                    tmp.TF++;
                    tmp.tags.add("p");
                    tmp.title=title;
                    tmp.snippets.add(snippetId);
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if(!tmp2.tags.contains("p")) {
                        tmp2.tags.add("p");
                        tmp2.priority += 4;
                    }
                }
            }

        }
    }
    public static void ParseDiv(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements dTags = toParse.getElementsByTag("div");
        String snippetId=null;
        for (Element dTag : dTags) {
           // dTag.unwrap();

            dTag.select("p").remove(); // remove all p elements
        //    System.out.println(dTag);
         //   System.out.println(dTag.text());

            ArrayList<String> currWords= queryP.QueryProcessor(dTag.text());
            snippetId= MongoInterface.insertSnippet(URl,dTag.text(),Integer.toString(currWords.size()));
            // System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=4;
                    tmp.TF++;
                    tmp.title=title;
                    tmp.tags.add("d");
                    tmp.snippets.add(snippetId);
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if(!tmp2.tags.contains("d")) {
                        tmp2.tags.add("d");
                        tmp2.priority += 4;
                    }
                }
            }

        }
    }
    public static void ParseTitle(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements tTags = toParse.getElementsByTag("title");
        String snippetId=null;
        for (Element tTag : tTags) {

            ArrayList<String> currWords= queryP.QueryProcessor(tTag.text());
            snippetId= MongoInterface.insertSnippet(URl,tTag.text(),Integer.toString(currWords.size()));

            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=10;
                    tmp.TF++;
                    tmp.tags.add("t");
                    tmp.title=title;
                    tmp.snippets.add(snippetId);
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if(!tmp2.tags.contains("t")) {
                        tmp2.tags.add("t");
                        tmp2.priority += 10;
                    }
                }
            }

        }
    }
}
