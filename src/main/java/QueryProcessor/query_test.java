package QueryProcessor;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class query_test {

    public static boolean isStringEnclosed(String input) {
        Pattern pattern = Pattern.compile("^\".*\"$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
    public static void main(String args[])
    {
//        ArrayList<String> b = queryP.QueryProcessor("Iphone 12 is the best phone ever in 2023");

        boolean ts =  isStringEnclosed("\"Hello\"");

        System.out.println(ts);





    }

}
