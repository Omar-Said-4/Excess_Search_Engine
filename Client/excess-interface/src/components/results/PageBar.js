import Pagination from "react-bootstrap/Pagination";

const PageBar = ({ currentPage, nextPage, firstPage, prevPage, changePage }) => {
  var previousDisabled = false;

  if (currentPage === 1) {
    previousDisabled = true;
  }

  return (
    <Pagination style={{ marginLeft: "9%" }}>
      <Pagination.First disabled={previousDisabled} onClick={firstPage} href=""/>
      <Pagination.Prev disabled={previousDisabled} onClick={prevPage} />

      {Array.from({ length: 5 }, (_, index) => (
        <Pagination.Item key={index + 1} active={currentPage === index + 1} onClick={()=>{
          changePage(index + 1)}}>
          {index + 1}
        </Pagination.Item>
      ))}

      <Pagination.Next disabled={currentPage === 5}  onClick={nextPage} />
    </Pagination>
  );
};

export default PageBar;
