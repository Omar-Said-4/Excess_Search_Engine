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

    const startTime = performance.now().toPrecision(3);

    const now = new Date();
    const hours = now.getHours().toString().padStart(2, "0");
    const minutes = now.getMinutes().toString().padStart(2, "0");
    const seconds = now.getSeconds().toString().padStart(2, "0");
    const millis = now.getMilliseconds().toString().padStart(3, "0");
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
        const endTime = performance.now().toPrecision(3);

        const now = new Date();
        const hours = now.getHours().toString().padStart(2, "0");
        const minutes = now.getMinutes().toString().padStart(2, "0");
        const seconds = now.getSeconds().toString().padStart(2, "0");
        const millis = now.getMilliseconds().toString().padStart(3, "0");
        const formattedTime = `${hours}:${minutes}:${seconds}:${millis}`;
        console.log(formattedTime);

        const time = endTime - startTime;

        setResponseTime(time);

        console.log(response.data);
        setData(response.data.results);
        setSize(response.data.size);

        setLoading(false);
      })
      .catch((error) => {
        console.error(error);
      });
  }, [query, page]);

  return (
    <div>
      <Bar query={query} />
      {loading ? (
        <LoadingCircle />
      ) : (
        <div className="results-section">
          <div className="results">
            <ResponseTime
              responseTime={responseTime}
              resultsCount={resultsSize}
            />

            {data.map((item, index) => {
              console.log(item);

              return (
                <PageResult
                  key={index}
                  description={item.Snippet}
                  title={item.title}
                  url={item.URL}
                  icon={item.hasOwnProperty("Icon") ? item.Icon : null}
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
            size={resultsSize}
          />
        </div>
      )}
    </div>
  );
};

export default Results;
