package QueryProcessor;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class query_test {

    public static boolean isStringEnclosed(String input) {
        return input.contains("\"");
//        Pattern pattern = Pattern.compile("^\".*\"$");
//        Matcher matcher = pattern.matcher(input);
//        return matcher.matches();
    }

    public static String fetchIconUrl(org.jsoup.nodes.Document toParse, String URl) {

        Elements faviconElements = toParse.select("link[rel~=(?i)^(shortcut|icon)$]");

        for (Element faviconElement : faviconElements) {
            // Get the href attribute of the favicon link tag
            String faviconUrl = faviconElement.attr("href");

            // Handle relative URLs
            if (!faviconUrl.startsWith("http")) {
                faviconUrl = URl + faviconUrl;
            }
            return faviconUrl;
        }

        return null;
    }
    public static void main(String args[])
    {
//        ArrayList<String> b = queryP.QueryProcessor("Iphone 12 is the best phone ever in 2023");






    }


}
