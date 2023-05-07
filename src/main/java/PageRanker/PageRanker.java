package PageRanker;

import CrawlerState.CrawlerState;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.HashMap;


public class PageRanker {

    private static HashMap<String, String> webPages;
    private static final double DAMPING_FACTOR = 0.85; // The damping factor
    private static final int MAX_ITERATIONS = 3; // The maximum number of iterations
    private static final double CONVERGENCE_THRESHOLD = 0.001; // The convergence threshold

    public PageRanker() {
        String filePath = "Websites.ser";
        File file = new File(filePath);

        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                webPages = (HashMap<String, String>) in.readObject();

                for (String url : webPages.keySet()) {
                    System.out.println(url);
                }
                System.out.println("All websites loaded from file: " + filePath);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                // Handle the exception appropriately
            }
        }
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


    public static boolean isPointingTo(String url1, String url2, String doc) {
        if (doc.contains(url2))
            return true;
        else return false;

//        try {
//            HttpURLConnection connection1 = (HttpURLConnection) new URL(url1).openConnection();
//            connection1.setRequestMethod("GET");
//            connection1.connect();
//
//            String linkToUrl2 = "<a href=\"" + url2 + "\">";
//            String response1 = new String(connection1.getInputStream().readAllBytes());
//            isUrl2LinkedFromUrl1 = response1.contains(linkToUrl2);
//
//            HttpURLConnection connection2 = (HttpURLConnection) new URL(url2).openConnection();
//            connection2.setRequestMethod("GET");
//            connection2.connect();
//
//        } catch (IOException e) {
//            return false;
//        }
////        String linkToUrl1 = "<a href=\"" + url1 + "\">";
////        String response2 = new String(connection2.getInputStream().readAllBytes());
////        boolean isUrl1LinkedFromUrl2 = response2.contains(linkToUrl1);

    }

    private static boolean isLinkingTo(String fromPage, String toPage, HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks) {
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


    private static HashMap<String, ConcurrentLinkedQueue<String>> getGraph() throws IOException {
        HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks = new HashMap<>();

        for (String x : webPages.keySet()) {
            for (String y : webPages.keySet()) {
                if (!Objects.equals(x, y) && isPointingTo(x, y, webPages.get(x))) {
                    ConcurrentLinkedQueue<String> links = outGoingLinks.get(x);
                    if (links == null) {
                        links = new ConcurrentLinkedQueue<>();
                        outGoingLinks.put(x, links);
                    }
                    links.add(y);
                    System.out.print("True");
                }
                else{
                    System.out.print("False");
                }
                System.out.println(" " + x + " " + y);
            }
            System.out.println("Finished getting links for " + x);
        }

        System.out.println("END");


        for (String x : outGoingLinks.keySet()) {
            System.out.println(x + " " + outGoingLinks.get(x));
        }
        return outGoingLinks;
    }

    private static ConcurrentLinkedQueue<String> getIngoingLinks(String page, HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks) {
        ConcurrentLinkedQueue<String> incomingLinks = new ConcurrentLinkedQueue<>();
        for (String site : outGoingLinks.keySet()) {
            if (isLinkingTo(site, page, outGoingLinks)) {
                incomingLinks.add(site);
            }
        }
        return incomingLinks;
    }

    public HashMap<String, Double> computePageRank() throws IOException {

        // Calculate outgoing links
        HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks = getGraph();

        for (String page : outGoingLinks.keySet()) {
            System.out.println(page + " " + outGoingLinks.get(page));
        }

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
                ConcurrentLinkedQueue<String> neighbors = getIngoingLinks(webpage, outGoingLinks);
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

    public static void main(String[] args) throws IOException {
//        CrawlerState state = null;
//        String filePath = "crawler_state.ser";
//        File file = new File(filePath);
////        HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks = new HashMap<>();
//
//
//        if (file.exists()) {
//            try (FileInputStream fileIn = new FileInputStream(file);
//                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
//                state = (CrawlerState) in.readObject();
//                System.out.println("Crawler state loaded from file: " + filePath);
//            } catch (IOException | ClassNotFoundException e) {
//                // Handle the exception appropriately
//            }
//
//            assert state != null;
//        }
//        else {
//            // For Testing
//            ConcurrentLinkedQueue<String> A_Out = new ConcurrentLinkedQueue<>();
//            A_Out.add("X");
//            outGoingLinks.put("A", A_Out);
//
//            ConcurrentLinkedQueue<String> X_Out = new ConcurrentLinkedQueue<>();
//            X_Out.add("A");
//            X_Out.add("Y");
//            X_Out.add("C");
//            X_Out.add("Z");
//            outGoingLinks.put("X", X_Out);
//
//            ConcurrentLinkedQueue<String> Z_Out = new ConcurrentLinkedQueue<>();
//            Z_Out.add("A");
//            outGoingLinks.put("Z", Z_Out);
//
//            ConcurrentLinkedQueue<String> Y_Out = new ConcurrentLinkedQueue<>();
//            Y_Out.add("C");
//            outGoingLinks.put("Y", Y_Out);
//
//            outGoingLinks.put("C", null);
//        }


        PageRanker pageRanker = new PageRanker();

        pageRanker.computePageRank();

    }


}
