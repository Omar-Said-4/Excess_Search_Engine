package Indexer;

import MongoDB.MongoInterface;
import QueryProcessor.queryP;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class Indexer {
    public static void ParseTag(org.jsoup.nodes.Document toParse, HashMap<String, wordAttr> toInsert, String URl,
            String title, String tagName, int priority) {
        Elements tags = toParse.getElementsByTag(tagName);
        String snippetId = null;
        for (Element tag : tags) {
            ArrayList<String> currWords = queryP.QueryProcessor(tag.text());
            snippetId = MongoInterface.insertSnippet(URl, tag.text(), Integer.toString(currWords.size()));
            // System.out.println(currWords);
            for (String word : currWords) {
                wordAttr tmp = new wordAttr();
                if (!toInsert.containsKey(word)) {
                    tmp.link = URl;
                    tmp.priority += priority;
                    tmp.TF++;
                    tmp.tags.add(tagName);
                    tmp.title = title;
                    tmp.snippets.add(snippetId);
                    toInsert.put(word, tmp);
                } else {
                    wordAttr tmp2 = toInsert.get(word);
                    tmp2.TF++;
                    tmp2.snippets.add(snippetId);
                    if (!tmp2.tags.contains(tagName)) {
                        tmp2.tags.add(tagName);
                        tmp2.priority += priority;
                    }
                }
            }

        }
    }
}