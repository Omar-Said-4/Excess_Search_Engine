package PageRanking;

import MongoDB.MongoInterface;

import java.util.HashMap;
import java.util.Map;

public class PageRankingAlgo {

    public static void main(String args[])
    {
        MongoInterface.Initialize();

        HashMap<String,Double> hash = new HashMap<>();

        String [] webs = {"www.google.com","www.facebook.com"};

        hash = MongoInterface.getWebsitePopularity(webs);

        for (HashMap.Entry<String, Double> entry : hash.entrySet()) {
            System.out.println(entry.getValue());
        }


        MongoInterface.terminate();

    }

}
