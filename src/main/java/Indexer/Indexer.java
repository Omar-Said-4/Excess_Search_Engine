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
        snippetId=null;
        ArrayList<String> currWords= queryP.QueryProcessor(h1Tag.text());
        if(h1Tag.text() != "")
        {snippetId= MongoInterface.insertSnippet(URl,h1Tag.text(),Integer.toString(currWords.size()),"h1");}
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
                if(snippetId!=null)
                {tmp.snippets.add(snippetId);}
                toInsert.put(word,tmp);
            }
            else
            {
                wordAttr tmp2=toInsert.get(word);
                tmp2.TF++;
                if(snippetId!=null)
                {tmp2.snippets.add(snippetId);}
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
        snippetId=null;
        ArrayList<String> currWords= queryP.QueryProcessor(h2Tag.text());
        if(h2Tag.text() != "")
        { snippetId= MongoInterface.insertSnippet(URl,h2Tag.text(),Integer.toString(currWords.size()),"h2");}
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
                if(snippetId!=null)
                {tmp.snippets.add(snippetId);}
                toInsert.put(word,tmp);
            }
            else
            {
                wordAttr tmp2=toInsert.get(word);
                tmp2.TF++;
                if(snippetId!=null)
                {tmp2.snippets.add(snippetId);}
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
            snippetId=null;
            if(h3Tag.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,h3Tag.text(),Integer.toString(currWords.size()),"h3");}
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
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
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
            snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(h4Tag.text());
            if(h4Tag.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,h4Tag.text(),Integer.toString(currWords.size()),"h4");}
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
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
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
            snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(h5Tag.text());
            if(h5Tag.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,h5Tag.text(),Integer.toString(currWords.size()),"h5");}
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
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
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
            snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(h6Tag.text());
            if(h6Tag.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,h6Tag.text(),Integer.toString(currWords.size()),"h6");}
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
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
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
            snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(pTag.text());
            if(pTag.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,pTag.text(),Integer.toString(currWords.size()),"p");}
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
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
                    if(!tmp2.tags.contains("p")) {
                        tmp2.tags.add("p");
                        tmp2.priority += 4;
                    }
                }
            }

        }
    }

//
//    public static void ParseMeta(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
//    {
//        Elements meta = toParse.getElementsByTag("meta");
//        String snippetId=null;
//        for (Element Meta : meta) {
//            ArrayList<String> currWords= queryP.QueryProcessor(Meta.attr("content"));
//            if(meta.hasText())
//            {snippetId= MongoInterface.insertSnippet(URl,Meta.text(),Integer.toString(currWords.size()),"meta");}
//            //System.out.println(currWords);
//
//            for (String word : currWords) {
//                wordAttr tmp=new wordAttr();
//                if(!toInsert.containsKey(word))
//                {
//                    tmp.link=URl;
//                    tmp.priority+=10;
//                    tmp.TF++;
//                    tmp.title=title;
//                    tmp.tags.add("meta");
//                    tmp.snippets.add(snippetId);
//                    toInsert.put(word,tmp);
//                }
//                else
//                {
//                    wordAttr tmp2=toInsert.get(word);
//                    tmp2.TF++;
//                    tmp2.snippets.add(snippetId);
//                    if(!tmp2.tags.contains("meta")) {
//                        tmp2.tags.add("meta");
//                        tmp2.priority += 10;
//                    }
//                }
//            }
//
//        }
//    }
//

    public static void ParseStrong(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements strong = toParse.select("strong:not(p > strong):not(div > strong)");


        String snippetId=null;
        for (Element Strong : strong) {
            snippetId = null;
            ArrayList<String> currWords= queryP.QueryProcessor(Strong.text());
            if(strong.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,Strong.text(),Integer.toString(currWords.size()),"strong");}
            //System.out.println(currWords);

            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=4;
                    tmp.TF++;
                    tmp.title=title;
                    tmp.tags.add("strong");
                    if(snippetId!= null)
                    {tmp.snippets.add(snippetId);}

                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
                    if(!tmp2.tags.contains("strong")) {
                        tmp2.tags.add("strong");
                        tmp2.priority += 4;
                    }
                }
            }

        }
    }


    public static void ParseUL(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements ul = toParse.select("u:not(p > u):not(div > u)");


        String snippetId=null;
        for (Element underline : ul) {
            snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(underline.text());
            if(underline.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,underline.text(),Integer.toString(currWords.size()),"u");}
            //System.out.println(currWords);

            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=4;
                    tmp.TF++;
                    tmp.title=title;
                    tmp.tags.add("u");
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
                    if(!tmp2.tags.contains("u")) {
                        tmp2.tags.add("u");
                        tmp2.priority += 4;
                    }
                }
            }

        }
    }



    public static void Parseblockquote(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements block = toParse.select("blockquote:not(p > blockquote):not(div > blockquote)");


        String snippetId=null;
        for (Element Blockq : block) {
            snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(Blockq.text());
            if(Blockq.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,Blockq.text(),Integer.toString(currWords.size()),"blockquote");}
            //System.out.println(currWords);

            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=4;
                    tmp.TF++;
                    tmp.title=title;
                    tmp.tags.add("blockquote");
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
                    if(!tmp2.tags.contains("blockquote")) {
                        tmp2.tags.add("blockquote");
                        tmp2.priority += 4;
                    }
                }
            }

        }
    }


    public static void ParseCode(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements code = toParse.select("code:not(p > code):not(div > code)");


        String snippetId=null;
        for (Element Co : code)
            {
                snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(Co.text());
            if(Co.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,Co.text(),Integer.toString(currWords.size()),"code");}
            //System.out.println(currWords);

            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=4;
                    tmp.TF++;
                    tmp.title=title;
                    tmp.tags.add("code");
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
                    if(!tmp2.tags.contains("code")) {
                        tmp2.tags.add("code");
                        tmp2.priority += 4;
                    }
                }
            }

        }
    }



    public static void ParseItalic(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert,String URl,String title)
    {
        Elements italic = toParse.select("em:not(p > em):not(div > em)");


        String snippetId=null;
        for (Element em : italic) {
            snippetId=null;
            ArrayList<String> currWords= queryP.QueryProcessor(em.text());
            if(em.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,em.text(),Integer.toString(currWords.size()),"em");}
            //System.out.println(currWords);

            for (String word : currWords) {
                wordAttr tmp=new wordAttr();
                if(!toInsert.containsKey(word))
                {
                    tmp.link=URl;
                    tmp.priority+=4;
                    tmp.TF++;
                    tmp.title=title;
                    tmp.tags.add("em");
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
                    if(!tmp2.tags.contains("em")) {
                        tmp2.tags.add("em");
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
            snippetId=null;

            dTag.select("p").remove(); // remove all p elements

            ArrayList<String> currWords= queryP.QueryProcessor(dTag.text());
            if(dTag.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,dTag.text(),Integer.toString(currWords.size()),"div");}
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
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
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
            snippetId=null;

            ArrayList<String> currWords= queryP.QueryProcessor(tTag.text());
            if(tTag.hasText())
            {snippetId= MongoInterface.insertSnippet(URl,tTag.text(),Integer.toString(currWords.size()),"title");}

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
                    if(snippetId!=null)
                    {tmp.snippets.add(snippetId);}
                    toInsert.put(word,tmp);
                }
                else
                {
                    wordAttr tmp2=toInsert.get(word);
                    tmp2.TF++;
                    if(snippetId!=null)
                    {tmp2.snippets.add(snippetId);}
                    if(!tmp2.tags.contains("t")) {
                        tmp2.tags.add("t");
                        tmp2.priority += 10;
                    }
                }
            }

        }
    }
}
