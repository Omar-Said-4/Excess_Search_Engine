package CrawlerState;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class CrawlerState implements Serializable {


    private static final long serialVersionUID = 1L;
    private final AtomicInteger current_Turn;
    private final int flag;
    private final ConcurrentLinkedQueue<String>[] BFS;
    private final int numThreads;
    private final AtomicInteger count ;
    private final AtomicInteger level ;
    private final Set<String> links;
    private final Set<String> Doc;

    private final HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks;

    public CrawlerState(ConcurrentLinkedQueue<String>[] BFS , int numThreads, AtomicInteger current_Turn , int flag,AtomicInteger c , AtomicInteger l  , Set<String> li , Set<String> doc, HashMap<String, ConcurrentLinkedQueue<String>> outGoingLinks) {
        this.BFS = BFS;
        this.numThreads = numThreads;
        this.current_Turn = current_Turn;
        this.flag =flag ;
        this.outGoingLinks = outGoingLinks;
        count = c;
        level = l;
        links = li;
        Doc = doc;
    }


    public ConcurrentLinkedQueue<String>[] getBFS() {
        return BFS;
    }

    public HashMap<String, ConcurrentLinkedQueue<String>> getOutGoingLinks()
    {
        return outGoingLinks;
    }
    public int getNumThreads() {
        return numThreads;
    }

    public AtomicInteger getCurrent_Turn(){
        return current_Turn;
    }

    public int getFlag()
    {
        return flag;
    }


    public AtomicInteger getCount()
    {
        return count;
    }
    public AtomicInteger getLevel()
    {
        return level;
    }

    public Set<String> getLinks()
    {
        return links;
    }

    public Set<String> getDoc()
    {
        return Doc;
    }
}
