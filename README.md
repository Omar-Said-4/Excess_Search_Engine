<h1 align="center"> "Excess"</h1>
<h3 align="center"> Find what you are looking for, faster than ever before!</h3>
<img align="center" src="https://github.com/Omar-Said-4/Excess_Search_Engine/assets/87082462/02aad990-8b85-4020-935b-409e2a9ce267" width="800" height="400" />

<h2>Introduction</h2>
<div>
<p>
A search engine developed as a part of the university course "Advanced Programming Techniques"
applying the basic search engines structure and hierarchy.
</p>
</div>
<h2>Main Features</h2>
<div>
<ul>
  <li><strong>Voice Search: </strong> Excess supports voice search in English language through using a text to speech api.</li>
  <li>
   <strong>Phrase Searching: </strong>
Instead of some keywords, excess supports phrase searching to search for an exact match or a semi-exact match of a phrase.<br>
You can also concatenate phrases with operators such as (AND, OR, NOT) and retrieve the results for complex search queries.
  </li>
 <li>
   <strong>Keywords Suggestion: </strong>
Based on your search history, excess can anticipate your keyword, thus facilitating searching process.
  </li>
<li>
   <strong>Results Paging and User-Friendly UI: </strong>
  results are paged 10 results per single page.
  </li>
<li>
   <strong>Crawler can save its state if interrupted</strong>
  </li>
</ul>
</div>
<h2>Project Description</h2>
<p>
There are different packages each one resembles a part of the search-engine structure
</p>

1. *<strong> `Crawler`</strong>:* A thread-safe multithreaded crawler responsible for crawling web pages starting from the seed of links provided in the file `seed.txt` (maximum number of pages tested was 10000), the output of the crawler is a serializable file with crawled html documents together with URLs.
2. *<strong>`Indexer`</strong>:* A thread-safe multithreaded indexer for indexing crawled web pages and uploading the inverted file to a Cloud MongoDB database.
3. *<strong>`QueryProcessor`</strong>:* For processing the search query through removing stop words and stemming.
4. *<strong>`PageRanker`</strong>:* For applying page ranking algorithm on collected webpages
5. *<strong>`Ranker`</strong>:* For Ranking search query results based on term frequency and document frequency.
6. *<strong>`MongoDB`</strong>:* an interface for handling MongoDB connections.
7. *<strong>`ComplexPhraseSearching` </strong>:* for handling both normal and operator-separated search queries.
8. *<strong>`SpringBoot` </strong>:* To interface frontend with backend

<h2>Guidelines</h2>
   <h2>Guidelines</h2>
<div>
<p>
As simple as any search engine just enter the search query and enjoy the results.
</p>
</div>

🔵 To run the React App on your localhost
1. Ensure you have nodejs on your pc.
2. Clone this repo to your pc and navigate to `client` directory.
3. Open `cmd` in your current directory and run the command `npm install`.
4. Wait a while then run the command `npm start`
<p>
Now you should find the search engine running on your default browser (preferred to be  chromium based) 
on <strong>localhost:3000</strong>


🔵 To run the Backend
1. Open the Java Project in your preferred IDE.
2. Navigate to `PageRanker` package.
3. Navigate to `RankerMain` and run it.

<p>
And voilà the search engine is now ready to use
</p>
<div>

To re- crawl navigate to `Main.java` and run it


To re-index navigate to `Indexer` package then run `IndexerMain`
</div>

<h2>Screenshots</h2>
<img align="center" src="https://github.com/Omar-Said-4/Excess_Search_Engine/assets/87082462/753cc194-b344-4c90-8b44-3031f5f6d847" width="800" height="400" />
<br>
<br>
<img align="center" src="https://github.com/Omar-Said-4/Excess_Search_Engine/assets/87082462/aa73f68f-7738-4f56-8778-0fbb61a56889" width="800" height="400" />
<br>
<br>
<img align="center" src=https://github.com/Omar-Said-4/Excess_Search_Engine/assets/87082462/eba1420d-82ea-4146-af3c-a7385188e311" />


