package SpringBoot;

import ComplexPhraseSearching.ComplexPhraseSearching;
import MongoDB.MongoInterface;
import PageRanker.RankerMain;
import PageRanker.linkAttr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@SpringBootApplication
@RestController
public class SpringBootInterface {
//    public static Map<String, linkAttr> toDisplay;

    public static boolean isStringEnclosed(String input) {
        Pattern pattern = Pattern.compile("^\".*\"$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    public static void runSpring() throws InterruptedException {
        SpringApplication app = new SpringApplication(SpringBootInterface.class);
        CountDownLatch latch = new CountDownLatch(1);
        app.run("");

//        Signal.handle(new Signal("INT"), signal -> {
//            System.out.println("Received signal " + signal.getName());
//            latch.countDown();
//        });

        latch.await();


    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/")
    public ResponseEntity<String> rchResult(@RequestParam("query") String query, @RequestParam("pageNumber") int pageNumber) {
        System.out.println(query);

        Map<String, String> icons = new HashMap<>();

        String filePath = "icon.ser";
        File file = new File(filePath);

        if (file.exists()) {
            try (FileInputStream fileIn = new FileInputStream(file);
                 ObjectInputStream in = new ObjectInputStream(fileIn)) {
                icons = (Map<String, String>) in.readObject();
                System.out.println("Crawler state loaded from file: " + filePath);
            } catch (IOException |
                     ClassNotFoundException e) {
                // Handle the exception appropriately
            }
        }


        JSONObject r = new JSONObject();

        Map<String, linkAttr> toDisplay;

        // Check if this is a phrase searching
        boolean isEnclosed = isStringEnclosed(query);
        if (isEnclosed) {
            long startTime = System.currentTimeMillis();
            Map<String, linkAttr> list = ComplexPhraseSearching.complexPhraseSearch(query);

            JSONArray phraseSearchResult = new JSONArray();

            if (list != null) {
                Map<String, String> finalIcons = icons;
                List<JSONObject> resultList = list.entrySet().parallelStream()
                        .map(entry -> {
                            String key = entry.getKey();
                            linkAttr value = entry.getValue();
                            JSONObject temp = new JSONObject();
                            temp.put("URL", key);
                            temp.put("title", value.title);
                            temp.put("Snippet", value.BestSnip);
                            String icon = finalIcons.get(key);
                            temp.put("Icon", icon);
                            System.out.println(icon);

                            return temp;
                        })
                        .toList();

                resultList.forEach(phraseSearchResult::put);

                r.put("results", phraseSearchResult);
                r.put("size", phraseSearchResult.length());

                long elapsedTime = System.currentTimeMillis() - startTime;
                System.out.println("Elapsed time: " + elapsedTime + " milliseconds");
            }

        } else {

            toDisplay = RankerMain.handleQuery(query, pageNumber);

            JSONArray toDisp = new JSONArray();

            Map<String, String> finalIcons = icons;
            List<JSONObject> resultList = toDisplay.entrySet().parallelStream()
                    .map(entry -> {
                        String key = entry.getKey();
                        linkAttr value = entry.getValue();
                        JSONObject temp = new JSONObject();
                        temp.put("URL", key);
                        temp.put("title", value.title);
                        temp.put("Snippet", value.BestSnip);
                        String icon = finalIcons.get(key);
                        temp.put("Icon", icon);
                        System.out.println(icon);

                        return temp;
                    })
                    .toList();
            resultList.forEach(toDisp::put);

            if (toDisp.length() != 0)
                MongoInterface.addSuggestion(query);

            r.put("results", toDisp);
            r.put("size", toDisp.length());


        }

        return ResponseEntity.ok(r.toString());
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/suggest")
    public ResponseEntity<String> suggestionResult(@RequestParam("query") String query) {
        System.out.println(query);

        return ResponseEntity.ok(MongoInterface.getSuggestions(query));
    }


}
