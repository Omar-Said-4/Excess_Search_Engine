package PageRanker;

import CrawlerState.CrawlerState;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.HashMap;


public class PageRanker {

    private static HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks;
    private static final double DAMPING_FACTOR = 0.85; // The damping factor
    private static final int MAX_ITERATIONS = 5; // The maximum number of iterations
    private static final double CONVERGENCE_THRESHOLD = 0.001; // The convergence threshold

    public PageRanker(HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks) {
        PageRanker.outGoingLinks = outGoingLinks;
    }

    private static boolean isLinkingTo(String page, String otherPage, HashMap<String, ConcurrentLinkedQueue<String>> sitePagesMap) {
        for (String site : sitePagesMap.keySet()) {
            String[] pages = sitePagesMap.get(site).toArray(new String[0]);
            if (Arrays.asList(pages).contains(page) && Arrays.asList(pages).contains(otherPage)) {
                return true;
            }
        }
        return false;
    }

    private static int getNumLinks(String page, HashMap<String, ConcurrentLinkedQueue<String>> sitePagesMap) throws InterruptedException {
        int numLinks = 0;
        System.out.println("test" + sitePagesMap.size());
        Thread.sleep(5000);
        for (String site : sitePagesMap.keySet()) {
            String[] pages = sitePagesMap.get(site).toArray(new String[0]);
            if (Arrays.asList(pages).contains(page)) {
                numLinks += pages.length;
            }
        }
        return numLinks;
    }

    public HashMap<String, Double> computePageRank() throws InterruptedException {

        HashMap<String, Double> pageRanks = new HashMap<>();

        double initialPageRank = 1.0 / outGoingLinks.size();
        for (String webpage : outGoingLinks.keySet()) {
            pageRanks.put(webpage, initialPageRank);
        }


        for (int i = 0; i < MAX_ITERATIONS; i++) {
            HashMap<String, Double> newPageRanks = new HashMap<>();
            double sum = 0.0;

            for (String webpage : outGoingLinks.keySet()) {
                ConcurrentLinkedQueue<String> neighbors = outGoingLinks.get(webpage);
                sum = 0.0;

                for (String neighbor : neighbors) {
                    System.out.println(neighbor);
                    Thread.sleep(5000);
                    double neighborPageRank = pageRanks.get(neighbor);
//                    int outDegree = outGoingLinks.get(neighbor).size();
//                    sum += neighborPageRank / outDegree;
                }

                double newPageRank = (1 - DAMPING_FACTOR) / outGoingLinks.size() + DAMPING_FACTOR * sum;

                System.out.println(newPageRank);
                Thread.sleep(5000);
                newPageRanks.put(webpage, newPageRank);
            }

            boolean hasConverged = true;

            for (String webpage : outGoingLinks.keySet()) {
                double oldPageRank = pageRanks.get(webpage);
                double newPageRank = newPageRanks.get(webpage);
                if (Math.abs(newPageRank - oldPageRank) > CONVERGENCE_THRESHOLD) {
                    hasConverged = false;
                    break;
                }
            }

            if (hasConverged) {
                break;
            }
            pageRanks = newPageRanks;
        }

        return pageRanks;

//            boolean converged = false;
//        HashMap<String, Double> pageRankMap = new HashMap<>();
//
//        for (String site : outGoingLinks.keySet()) {
//            String[] pages = outGoingLinks.get(site).toArray(new String[0]);
//            for (String page : pages) {
//                pageRankMap.put(page, 1.0);
//            }
//        }
//
//        for (int i = 0; i < MAX_ITERATIONS; i++) {
//            HashMap<String, Double> newPageRankMap = new HashMap<>();
//            for (String site : outGoingLinks.keySet()) {
//                String[] pages = outGoingLinks.get(site).toArray(new String[0]);
//
//                System.out.println(Arrays.toString(pages));
//                System.out.println(pages.length);
//                System.out.println("test kiro");
//
//                for (String page : pages) {
//                    double newRank = (1 - DAMPING_FACTOR) / (double) pageRankMap.size();
//                    System.out.println(pageRankMap.size());
//                    Thread.sleep(5000);
//
//                    for (String otherPage : pageRankMap.keySet()) {
//                        if (!page.equals(otherPage) && isLinkingTo(page, otherPage, outGoingLinks)) {
//                            System.out.println(otherPage + " is linked to " + page);
//                            double num = getNumLinks(otherPage, outGoingLinks);
//                            System.out.println(num);
//                            Thread.sleep(5000);
//                            newRank += DAMPING_FACTOR * pageRankMap.get(otherPage) / num;
//                        }
//                    }
//
//                    newPageRankMap.put(page, newRank);
//                }
//            }
//            pageRankMap = newPageRankMap;
//        }
//
//        return pageRankMap;
    }

    public static void main(String[] args) throws InterruptedException {
        CrawlerState state = null;
        String filePath = "crawler_state.ser";
        File file = new File(filePath);

        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                state = (CrawlerState) in.readObject();
                System.out.println("Crawler state loaded from file: " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                // Handle the exception appropriately
            }
        }



        assert state != null;
       // HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks = state.getOutGoingLinks();
        System.out.println(outGoingLinks.size());
        Thread.sleep(5000);



        PageRanker pageRanker = new PageRanker(outGoingLinks);
        HashMap<String, Double> page_rank = pageRanker.computePageRank();



        List<Map.Entry<String, Double>> list = new ArrayList<>(page_rank.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
                return o1.getValue().compareTo(o2.getValue());
            }
        });

        Map<String, Double> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        // print the sorted map
        System.out.println("Sorted HashMap by value:");
//        System.out.println(sortedMap);

        for (String page : sortedMap.keySet()) {
            System.out.println(page + ": " + page_rank.get(page));
        }


    }


}
