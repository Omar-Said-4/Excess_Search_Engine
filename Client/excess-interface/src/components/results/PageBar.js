import Pagination from "react-bootstrap/Pagination";

const PageBar = ({
  currentPage,
  nextPage,
  firstPage,
  prevPage,
  changePage,
  size,
}) => {
  var previousDisabled = false;

  if (currentPage === 1) {
    previousDisabled = true;
  }

  const PAGE_COUNT = 10;

  console.log(size);

  return (
    <Pagination style={{ marginLeft: "9%", marginBottom: "0", paddingBottom: "10px" }}>
      <Pagination.First
        disabled={previousDisabled}
        onClick={firstPage}
        href=""
      />
      <Pagination.Prev disabled={previousDisabled} onClick={prevPage} />

      {Array.from({ length: 5 }, (_, index) => (
        <Pagination.Item
          key={index + 1}
          active={currentPage === index + 1}
          disabled={size < index * PAGE_COUNT}
          onClick={() => {
            changePage(index + 1);
          }}
        >
          {index + 1}
        </Pagination.Item>
      ))}

      <Pagination.Next
        disabled={currentPage === Math.ceil(size / PAGE_COUNT)}
        onClick={nextPage}
      />
    </Pagination>
  );
};

export default PageBar;
