import SearchField from "../../SearchField";
import logo from "../../icons/logo.png";

const Bar = ({ query }) => {
  console.log(query);

  return (
    <div className="bar">
      <img
        src={logo}
        alt="logo"
        className="logo"
        style={{ display: "inline-block" }}
        onClick={() => {
          window.location.href = "/";
        }}
        onMouseEnter={(e)=> e.target.style.cursor = "pointer"}
      />
      <SearchField query={query} place={"result"} />
    </div>
  );
};

export default Bar;
