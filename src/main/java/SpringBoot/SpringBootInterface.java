package SpringBoot;
import MongoDB.MongoInterface;
import PageRanker.linkAttr;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
public class SpringBootInterface {
    public static Map<String, linkAttr> toDisplay;

        public static void runSpring()
        {
            SpringApplication.run(SpringBootInterface.class, "");

        }

        @GetMapping("/")
        public String rchResult()
        {
            JSONArray toDisp = new JSONArray();
            toDisplay.forEach((key, value) -> {
                JSONObject temp = new JSONObject();
                temp.put("URL",key);
                temp.put("title",value.title);
                Map.Entry<String, Integer> firstEntry = value.Snippets.entrySet().iterator().next();
                temp.put("Snippet", MongoInterface.getSnippet(firstEntry.getKey()));
                toDisp.put(temp);
            });
            MongoInterface.terminate();

            return toDisp.toString();
        }


}
