package SpringBoot;

import MongoDB.MongoInterface;
import PageRanker.RankerMain;
import PageRanker.linkAttr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RestController
public class SpringBootInterface {
//    public static Map<String, linkAttr> toDisplay;

    public static void runSpring() throws InterruptedException {
        SpringApplication app = new SpringApplication(SpringBootInterface.class);
        CountDownLatch latch = new CountDownLatch(1);
        app.run("");
        latch.await();
    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/")
    public ResponseEntity<String> rchResult(@RequestParam("query") String query, @RequestParam("pageNumber") int pageNumber) {
        System.out.println(query);
        JSONObject r = new JSONObject();

//        JSONArray response = new JSONArray();
        Map<String, linkAttr> toDisplay = RankerMain.handleQuery(query, pageNumber);




        long startTime = System.currentTimeMillis();

        JSONArray toDisp = new JSONArray();

        List<JSONObject> resultList = toDisplay.entrySet().parallelStream()
                .map(entry -> {
                    String key = entry.getKey();
                    linkAttr value = entry.getValue();
                    String firstSnippetKey = value.Snippets.keySet().iterator().next();
                    JSONObject temp = new JSONObject();
                    temp.put("URL", key);
                    temp.put("title", value.title);
                    temp.put("Snippet", MongoInterface.getSnippet(firstSnippetKey));
                    return temp;
                })
                .collect(Collectors.toList());

        resultList.forEach(toDisp::put);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("Elapsed time: " + elapsedTime + " milliseconds");




        if (toDisp.length() != 0)
            MongoInterface.addSuggestion(query);

        r.put("results", toDisp);
        r.put("size", toDisp.length());
        return ResponseEntity.ok(r.toString());

    }

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/suggest")
    public ResponseEntity<String> suggestionResult(@RequestParam("query") String query) {
        System.out.println(query);

        return ResponseEntity.ok(MongoInterface.getSuggestions(query));
    }



}
