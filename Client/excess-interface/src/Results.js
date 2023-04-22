import Bar from "./Bar";
import PageBar from "./PageBar";
import ResponseTime from "./ResponseTime";
import PageResult from "./PageResult";
import { useSearchParams } from "react-router-dom";

const Results = ({ data }) => {
//   const params = useParams();
  
  const [searchParams, setSearchParams] = useSearchParams();

  const query = searchParams.get('query');
  const page = searchParams.get('page');
  
//   const text = searchParams.get('text');
//   const page = searchParams.get('page');

  console.log(query + page);

  return (
    <div>
      <Bar query={query}/>
      <div className="results">
        <ResponseTime responseTime={20} resultsCount={2000} />

        {data.map((item, index) => {
          return (
            <PageResult
              key={index}
              description={item.description}
              title={item.title}
              url={item.url}
            />
          );
        })}

        {/* 
        {/* {result.map((item, index) => (
          <PageResult
            key={index}
            description={item.description}
            title={item.title}
            url={item.url}
          />
        ))} */}
      </div>

      <PageBar currentPage={1} />
    </div>
  );
};

export default Results;
