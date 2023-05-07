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
    private static final int MAX_ITERATIONS = 3; // The maximum number of iterations
    private static final double CONVERGENCE_THRESHOLD = 0.001; // The convergence threshold

    public PageRanker(HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks) {
        PageRanker.outGoingLinks = outGoingLinks;
    }

//    private static boolean isLinkingTo(String page, String otherPage, HashMap<String, ConcurrentLinkedQueue<String>> sitePagesMap) {
//        for (String site : sitePagesMap.keySet()) {
//            String[] pages = sitePagesMap.get(site).toArray(new String[0]);
//            if (Arrays.asList(pages).contains(page) && Arrays.asList(pages).contains(otherPage)) {
//                return true;
//            }
//        }
//        return false;
//    }

    private static boolean isLinkingTo(String fromPage, String toPage) {
        ConcurrentLinkedQueue<String> outgoingLinks = outGoingLinks.get(fromPage);
        return outgoingLinks != null && outgoingLinks.contains(toPage);
    }

//    private static int getNumLinks(String page, HashMap<String, ConcurrentLinkedQueue<String>> sitePagesMap) throws InterruptedException {
//        int numLinks = 0;
//        System.out.println("test" + sitePagesMap.size());
//        Thread.sleep(5000);
//        for (String site : sitePagesMap.keySet()) {
//            String[] pages = sitePagesMap.get(site).toArray(new String[0]);
//            if (Arrays.asList(pages).contains(page)) {
//                numLinks += pages.length;
//            }
//        }
//        return numLinks;
//    }


    private static ConcurrentLinkedQueue<String> getIngoingLinks(String page) {
        ConcurrentLinkedQueue<String> incomingLinks = new ConcurrentLinkedQueue<>();
        for (String site : outGoingLinks.keySet()) {
            if (isLinkingTo(site, page)) {
                incomingLinks.add(site);
            }
        }
        return incomingLinks;

    }

    public HashMap<String, Double> computePageRank() {

        HashMap<String, Double> pageRanks = new HashMap<>();

        double initialPageRank = 1.0 / outGoingLinks.size();
        for (String webpage : outGoingLinks.keySet()) {
            pageRanks.put(webpage, initialPageRank);
        }


        for (int i = 0; i < MAX_ITERATIONS; i++) {
            HashMap<String, Double> newPageRanks = new HashMap<>();
            double sum;

            // Iterate through the webpages and calculate the new page rank
            for (String webpage : outGoingLinks.keySet()) {
                // Getting the neighbors of the current page
                ConcurrentLinkedQueue<String> neighbors = getIngoingLinks(webpage);
                sum = 0.0;

                for (String neighbor : neighbors) {
                    double neighborPageRank = pageRanks.get(neighbor);
                    int outDegree = outGoingLinks.get(neighbor).size();
                    sum += neighborPageRank / outDegree;
                }

                double newPageRank = (1 - DAMPING_FACTOR) / outGoingLinks.size() + DAMPING_FACTOR * sum;

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


        List<Map.Entry<String, Double>> list = new ArrayList<>(pageRanks.entrySet());

        list.sort(Map.Entry.comparingByValue());

        Map<String, Double> sortedMap = new LinkedHashMap<>();

        for (Map.Entry<String, Double> entry : list) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        // print the sorted map
        System.out.println("Sorted HashMap by value:");

        for (String page : sortedMap.keySet()) {
            System.out.println(page + ": " + pageRanks.get(page));
        }

        return pageRanks;
    }

    public static void main(String[] args) {
        CrawlerState state = null;
        String filePath = "crawler_state.ser";
        File file = new File(filePath);
        HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks = new HashMap<>();


        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                state = (CrawlerState) in.readObject();
                System.out.println("Crawler state loaded from file: " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                // Handle the exception appropriately
            }

            assert state != null;
        } else {
            // For Testing
            ConcurrentLinkedQueue<String> A_Out = new ConcurrentLinkedQueue<>();
            A_Out.add("X");
            outGoingLinks.put("A", A_Out);

            ConcurrentLinkedQueue<String> X_Out = new ConcurrentLinkedQueue<>();
            X_Out.add("A");
            X_Out.add("Y");
            X_Out.add("C");
            X_Out.add("Z");
            outGoingLinks.put("X", X_Out);

            ConcurrentLinkedQueue<String> Z_Out = new ConcurrentLinkedQueue<>();
            Z_Out.add("A");
            outGoingLinks.put("Z", Z_Out);

            ConcurrentLinkedQueue<String> Y_Out = new ConcurrentLinkedQueue<>();
            Y_Out.add("C");
            outGoingLinks.put("Y", Y_Out);

            outGoingLinks.put("C", null);
        }


        PageRanker pageRanker = new PageRanker(outGoingLinks);


        pageRanker.computePageRank();

    }


}
