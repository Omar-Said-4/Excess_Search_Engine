import java.util.ArrayList;


class PhraseSearching {
    public static ArrayList<Integer> search(String phrase, String snippet) {

        ArrayList<Integer> indices = new ArrayList<Integer>();
        int index = snippet.indexOf(phrase);

        do{
            indices.add(index);
            System.out.println("Substring found at index: " + index);
            index = snippet.indexOf(phrase, index + 1);

        }while(index != -1);

        return indices;
    }
}
