import SearchField from "../../SearchField";
import logo from "../../icons/logo.png";
import { useState } from "react";

const Bar = ({ query }) => {
  console.log(query);
  const [focused, setFocused] = useState(false);

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
      <SearchField query={query} place={"result"} focused={focused} setFocused={setFocused}/>
    </div>
  );
};

export default Bar;
