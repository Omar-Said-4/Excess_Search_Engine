import { useSearchParams } from "react-router-dom";
import Axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";

import Bar from "./Bar";
import PageBar from "./PageBar";
import ResponseTime from "./ResponseTime";
import PageResult from "./PageResult";
import LoadingCircle from "./LoadingCircle";

const Results = () => {
  const [searchParams, setSearchParams] = useSearchParams();

  const [query, setQuery] = useState(searchParams.get("query"));
  const [page, setPage] = useState(parseInt(searchParams.get("page")));

  const [loading, setLoading] = useState(true);

  const [data, setData] = useState([]);
  const [resultsSize, setSize] = useState(0);
  const [responseTime, setResponseTime] = useState(0);

  const navigate = useNavigate();
  console.log(query + page);

  const nextPage = () => {
    setPage(page + 1);
    navigate("/search/?query=" + query + "&page=" + (parseInt(page) + 1));
  };

  const firstPage = () => {
    setPage(1);
    navigate("/search/?query=" + query + "&page=" + 1);
  };

  const prevPage = () => {
    setPage(page - 1);
    navigate("/search/?query=" + query + "&page=" + (parseInt(page) - 1));
  };

  const changePage = (index) => {
    setPage(index);
    navigate("/search/?query=" + query + "&page=" + index);
  };

  useEffect(() => {
    setLoading(true);

    const startTime = performance.now();

    const now = new Date();
    const hours = now.getHours().toString().padStart(2, "0");
    const minutes = now.getMinutes().toString().padStart(2, "0");
    const seconds = now.getSeconds().toString().padStart(2, "0");
    const millis = now.getMilliseconds().toString().padStart(3, "0")
    const formattedTime = `${hours}:${minutes}:${seconds}:${millis}`;
    console.log(formattedTime);

    Axios.get("http://localhost:8080/", {
      params: {
        query: query,
        pageNumber: page,
      },
      headers: {
        "Access-Control-Allow-Origin": "*",
      },
    })
      .then((response) => {
        const endTime = performance.now();

        const now = new Date();
        const hours = now.getHours().toString().padStart(2, "0");
        const minutes = now.getMinutes().toString().padStart(2, "0");
        const seconds = now.getSeconds().toString().padStart(2, "0");
        const millis = now.getMilliseconds().toString().padStart(3, "0")
        const formattedTime = `${hours}:${minutes}:${seconds}:${millis}`;
        console.log(formattedTime);

        const time = endTime - startTime;

        setResponseTime(time);

        console.log(response.data);
        setData(response.data.results);
        setSize(response.data.size);

        // response.data.results.map((item) => {
        //   console.log(item.Snippet);
        // });`

        setLoading(false);
      })
      .catch((error) => {
        console.error(error);
      });
    // Axios.get(
    //   "https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/Search/WebSearchAPI",
    //   {
    //     params: {
    //       q: query,
    //       pageNumber: page,
    //       pageSize: "10",
    //       autoCorrect: "true",
    //     },
    //     headers: {
    //       "content-type": "application/octet-stream",
    //       "X-RapidAPI-Key":
    //         "9bed59de6dmsh99ed317ac6f065bp108d18jsnaa3975d2a67f",
    //       "X-RapidAPI-Host": "contextualwebsearch-websearch-v1.p.rapidapi.com",
    //     },
    //   }
    // )
    //   .then((response) => {
    //     setData(response.data.value);
    //     setLoading(false);
    //   })
    //   .catch((err) => {
    //     console.error(err);
    //   });
  }, [query, page]);

  return (
    <div>
      <Bar query={query} />
      {loading ? (
        <LoadingCircle />
      ) : (
        <>
          <div className="results">
            <ResponseTime
              responseTime={responseTime}
              resultsCount={resultsSize}
            />

            {data.map((item, index) => {
              return (
                <PageResult
                  key={index}
                  description={item.Snippet}
                  title={item.title}
                  url={item.URL}
                />
              );
            })}
          </div>

          <PageBar
            currentPage={page}
            nextPage={nextPage}
            prevPage={prevPage}
            firstPage={firstPage}
            changePage={changePage}
          />
        </>
      )}
    </div>
  );
};

export default Results;
