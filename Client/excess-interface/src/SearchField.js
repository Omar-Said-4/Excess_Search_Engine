// import { useState } from "react";
// import search from "./search.png";
// import Axios from "axios";

// const SearchField = ({ query, place }) => {
//   const [text, setText] = useState(query);

//   const style = {};

//   if (place === "home") {
//     style.height = 50;
//     style.fontSize = 28;
//   }

//   const makeSearch = () => {
//     window.location.href =
//       "/search/?query=" +
//       document.getElementById("input-field").value +
//       "&page=1";
//   };

//   const data = ["test", "Kiro", "test1"];

//   const [suggestions, setSuggestions] = useState([]);

//   const queryChanged = async (e) => {
//     setText(e.target.value);

//     console.log("test");

//     if (e.target.value == "") setSuggestions([]);
//     else
//       Axios.get("http://localhost:8080/suggest", {
//         params: { query: e.target.value },
//         headers: {
//           "Access-Control-Allow-Origin": "*",
//         },
//       }).then((response) => {
//         console.log(response);
//         setSuggestions(response.data);
//       });
//   };

//   return (
//     <>
//       <div className="search-field">
//         <input
//           type="text"
//           placeholder="Search"
//           id="input-field"
//           value={text}
//           onChange={async (e) => {
//             await queryChanged(e);
//           }}
//           style={style}
//         />

//         {/* <img
//         src={search}
//         className="search-field-icon"
//         alt="search-icon"
//         onClick={makeSearch}
//       ></img> */}
//       </div>

//       {suggestions.length != 0 ? (
//         <div className="dropdown">
//           {suggestions.map((item) => (
//             <div className="suggestion-item">{item}</div>
//           ))}
//         </div>
//       ) : null}
//     </>
//   );
// };

// export default SearchField;

import React, { useState } from "react";
import Axios from "axios";
import {
  MDBInputGroup,
  MDBInput,
  MDBIcon,
  MDBAlert,
  MDBBtn,
} from "mdb-react-ui-kit";
import "mdb-react-ui-kit/dist/css/mdb.min.css";
import "@fortawesome/fontawesome-free/css/all.min.css";

const SearchField = ({ query }) => {
  const [showSearchAlert, setShowSearchAlert] = useState(false);
  const [text, setText] = useState(query);

  const [suggestions, setSuggestions] = useState([]);

  const queryChanged = async (e) => {
    setText(e.target.value);

    console.log("test");

    if (e.target.value == "") setSuggestions([]);
    else
      Axios.get("http://localhost:8080/suggest", {
        params: { query: e.target.value },
        headers: {
          "Access-Control-Allow-Origin": "*",
        },
      }).then((response) => {
        console.log(response);
        setSuggestions(response.data);
      });
  };

  return (
    <>
      <MDBInputGroup
        className="search-field"
        style={{ width: "40%", height: "40px", marginBottom: "15px" }}
      >
        <input
          className="form-control"
          label="Search"
          id="input-field"
          style={{
            width: "5vh",
            height: "7vh",
            fontSize: "26px",
            borderRadius: "15px",
          }}
          spellCheck={false}
          autoComplete="off"
          placeholder="Search!"
          onChange={(e)=>queryChanged(e)}
        />
        <MDBBtn
          onClick={() => setShowSearchAlert(true)}
          rippleColor="dark"
          style={{ borderRadius: "15px" }}
          disabled={text === ""}
        >
          <MDBIcon icon="search" />
        </MDBBtn>
      </MDBInputGroup>

      {/* <MDBAlert delay={1000} position='top-right' autohide appendToBody show={showSearchAlert}>
        Search!
      </MDBAlert> */}
    </>
  );
};

export default SearchField;
