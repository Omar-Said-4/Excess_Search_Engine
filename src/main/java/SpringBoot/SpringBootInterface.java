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
import java.util.concurrent.CountDownLatch;

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

        JSONArray toDisp = new JSONArray();
//        JSONArray response = new JSONArray();

        Map<String, linkAttr> toDisplay = RankerMain.handleQuery(query, pageNumber);

        toDisplay.forEach((key, value) -> {
            JSONObject temp = new JSONObject();
            temp.put("URL", key);
            temp.put("title", value.title);
            Map.Entry<String, Integer> firstEntry = value.Snippets.entrySet().iterator().next();
            temp.put("Snippet", MongoInterface.getSnippet(firstEntry.getKey()));
            toDisp.put(temp);
        });

        if (toDisp.length() != 0)
            MongoInterface.addSuggestion(query);

        r.put("results", toDisp);
        r.put("size", toDisp.length());


        return ResponseEntity.ok(r.toString());
    }


    @GetMapping("/suggest")
    public ResponseEntity<String> suggestionResult(@RequestParam("query") String query) {
        System.out.println(query);

        return ResponseEntity.ok(MongoInterface.getSuggestions(query).toString());
    }



}
