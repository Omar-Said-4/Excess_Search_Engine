import { useState } from "react";
import search from "./search.png";

const SearchField = ({ query, place }) => {
  const [text, setText] = useState(query);

  const style = {};

  if (place === "home") {
    style.height = 50;
    style.fontSize = 28;
  }

  const makeSearch = () => {
    window.location.href =
      "/search/?query=" +
      document.getElementById("input-field").value +
      "&page=1";
  };

  const data = ["test", "Kiro", "test1"];

  return (
    <>
      <div className="search-field">
        <input
          type="text"
          placeholder="Search"
          id="input-field"
          value={text}
          onChange={(e) => setText(e.target.value)}
          style={style}
        />

        {/* <img
        src={search}
        className="search-field-icon"
        alt="search-icon"
        onClick={makeSearch}
      ></img> */}
      </div>

      {text !== "" ? (
        <div className="dropdown">
          {data.map((item) => (
            <div className="suggestion-item">{item}</div>
          ))}
        </div>
      ) : null}
    </>
  );
};

export default SearchField;
