import Pagination from "react-bootstrap/Pagination";

const PageBar = ({currentPage}) => {
  var previousDisabled = false;

  if(currentPage === 1){
    previousDisabled = true;
  }


  return (
    <Pagination style={{marginLeft:"9%"}}>
      <Pagination.First />
      <Pagination.Prev disabled={previousDisabled}/>   
      <Pagination.Item active>{1}</Pagination.Item>
      <Pagination.Item>{2}</Pagination.Item>
      <Pagination.Item>{3}</Pagination.Item>
      <Pagination.Item>{4}</Pagination.Item>
      <Pagination.Item>{5}</Pagination.Item>
      <Pagination.Ellipsis />
      <Pagination.Next />
      <Pagination.Last />
    </Pagination>
  );
};

export default PageBar;
