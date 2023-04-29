package Indexer;

import QueryProcessor.queryP;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class Indexer {
public static void ParseH1(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl)
{
    Elements h1Tags = toParse.getElementsByTag("h1");
    for (Element h1Tag : h1Tags) {
        ArrayList<String> currWords= queryP.QueryProcessor(h1Tag.text());
        //System.out.println(currWords);
        for (String word : currWords) {
            wordAttr tmp=new wordAttr();
            if(!toInsert.containsKey(word))
            {
                tmp.link=URl;
                tmp.priority+=10;
                tmp.TF++;
                tmp.tags.add("h1");
                toInsert.put(word,tmp);
            }
            else
            {
                wordAttr tmp2=toInsert.get(word);
                tmp2.TF++;
                if(!tmp2.tags.contains("h1")) {
                    tmp2.tags.add("h1");
                    tmp2.priority += 10;
                }
            }
        }

    }
}
public static void ParseH2(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl)
{
    Elements h2Tags = toParse.getElementsByTag("h2");
    for (Element h2Tag : h2Tags) {
        ArrayList<String> currWords= queryP.QueryProcessor(h2Tag.text());
        //System.out.println(currWords);
        for (String word : currWords) {
            wordAttr tmp=new wordAttr();
            if(!toInsert.containsKey(word))
            {
                tmp.link=URl;
                tmp.priority+=9;
                tmp.TF++;
                tmp.tags.add("h2");
                toInsert.put(word,tmp);
            }
            else
            {
                wordAttr tmp2=toInsert.get(word);
                tmp2.TF++;
                if(!tmp2.tags.contains("h2")) {
                    tmp2.tags.add("h2");
                    tmp2.priority += 9;
                }
            }
        }

    }
}
public static void ParseH3(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl)
{
        Elements h3Tags = toParse.getElementsByTag("h3");
        for (Element h3Tag : h3Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h3Tag.text());
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=8;
                    tmp.TF++;
                    tmp.tags.add("h3");
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(!tmp2.tags.contains("h3")) {
                        tmp2.tags.add("h3");
                        tmp2.priority += 8;
                    }
                }
            }

        }
}


public static void ParseH4(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl)
    {
        Elements h4Tags = toParse.getElementsByTag("h4");
        for (Element h4Tag : h4Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h4Tag.text());
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=7;
                    tmp.TF++;
                    tmp.tags.add("h4");
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(!tmp2.tags.contains("h4")) {
                        tmp2.tags.add("h4");
                        tmp2.priority += 7;
                    }
                }
            }

        }
    }


    public static void ParseH5(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl)
    {
        Elements h5Tags = toParse.getElementsByTag("h5");
        for (Element h5Tag : h5Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h5Tag.text());
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=6;
                    tmp.TF++;
                    tmp.tags.add("h5");
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(!tmp2.tags.contains("h5")) {
                        tmp2.tags.add("h5");
                        tmp2.priority += 6;
                    }
                }
            }

        }
    }

    public static void ParseH6(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl)
    {
        Elements h6Tags = toParse.getElementsByTag("h6");
        for (Element h6Tag : h6Tags) {
            ArrayList<String> currWords= queryP.QueryProcessor(h6Tag.text());
            //System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=5;
                    tmp.TF++;
                    tmp.tags.add("h6");
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(!tmp2.tags.contains("h6")) {
                        tmp2.tags.add("h6");
                        tmp2.priority += 5;
                    }
                }
            }

        }
    }
}
