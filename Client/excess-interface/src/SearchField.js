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

import React, { useState, useEffect, useCallback } from "react";
import Axios from "axios";
import {
  MDBInputGroup,
  MDBIcon,
  MDBBtn,
  MDBListGroup,
  MDBListGroupItem,
} from "mdb-react-ui-kit";
import "mdb-react-ui-kit/dist/css/mdb.min.css";
import "@fortawesome/fontawesome-free/css/all.min.css";
import LoadingCircle from "./components/results/LoadingCircle";
import { motion } from "framer-motion";

const SearchField = ({ query, place }) => {
  const [text, setText] = useState(query);
  const [loaded, setLoaded] = useState(false);
  const [index, setIndex] = useState(-1);
  const [suggestions, setSuggestions] = useState([]);
  const [focused, setFocused] = useState(false);

  // const handleImageLoad = () => {
  //   setIsLoaded(true);
  // }

  const handleKeyDown = useCallback(
    (event) => {
      if (event.keyCode === 38) {
        // handle up arrow key
        // There is no list -> Do no thing
        // Else
        if (suggestions.length !== 0) {
          // If no item is highlighted
          if (index === -1) setIndex(suggestions.length - 1);
          else if (index === 0) setIndex(-1);
          else if (index !== 0) setIndex(index - 1);
        }
        console.log(index);
      } else if (event.keyCode === 40) {
        // handle down arrow key
        // There is no list -> Do no thing
        // Else
        if (suggestions.length !== 0) {
          // If no item is highlighted
          if (index === suggestions.length - 1) setIndex(-1);
          else setIndex(index + 1);
        }

        console.log(index);
      } else if (
        event.keyCode === 13 &&
        document.getElementById("input-field") === document.activeElement &&
        document.getElementById("input-field").value.replace(/\s/g, "") !== ""
      ) {
        if (index !== -1) {
          setText(suggestions.at(index));

          document.getElementById("input-field").value = suggestions.at(index);
        }
        makeSearch();
      }
      else if(event.keyCode == 27)
        setFocused(false);
    },
    [index, suggestions]
  );

  useEffect(() => {
    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [handleKeyDown]);

  window.onload = function () {
    setLoaded(true);
  };

  const queryChanged = async (e) => {
    setText(e.target.value);
    setFocused(true);

    setIndex(-1);
    if (e.target.value === "") {
      setSuggestions([]);
    } else
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

  const makeSearch = () => {
    window.location.href =
      "/search/?query=" +
      document.getElementById("input-field").value +
      "&page=1";
  };

  const search = () => {
    if (index !== -1) {
      setText(suggestions.at(index));

      document.getElementById("input-field").value = suggestions.at(index);
    }

    makeSearch();
  };

  return (
    <>
      {loaded || place === "result" ? (
        <div
          style={{
            width: "40%",
            display: place === "result" ? "inline-block" : "block",
            left: place === "result" ? "5%" : "30%",
          }}
          className="search-field"
        >
          <MDBInputGroup style={{ height: "100%"}}>
            <input
              className="form-control"
              label="Search"
              id="input-field"
              style={{
                // height: "7vh",
                fontSize: "20px",
                borderRadius: "15px",
                boxShadow: focused ? "0 0 10px rgba(0, 0, 0, 1)" : "",
                backgroundColor: place !== "result" ? "#f2f2f2" : "#c6ddf6",
                marginRight: "7px"
              }}
              value={text}
              spellCheck={false}
              autoComplete="off"
              placeholder="Search!"
              onChange={(e) => queryChanged(e)}
              onFocus={(e) => setFocused(true)}
              onBlur={(e) => setFocused(true)}
            />
            <MDBBtn
              rippleColor="dark"
              style={{ borderRadius: "15px" }}
              disabled={text === ""}
              onClick={search}
            >
              <MDBIcon icon="search" />
            </MDBBtn>
          </MDBInputGroup>

          {suggestions.length !== 0 && focused ? (
            <MDBListGroup
              style={{
                marginTop: "5px",
                // paddingLeft: "5px",
                position: "absolute",
              }}
              light
              className="my-list"
            >
              {suggestions.map((suggestion, i) => (
                <motion.div
                  initial={{ opacity: "0%" }}
                  animate={{ opacity: "100%" }}
                  // transition={{
                  //   type: "tween",
                  //   duration: 0.9,
                  // }}
                >
                  <MDBListGroupItem
                    active
                    aria-current="true"
                    className="px-3"
                    style={{
                      backgroundColor: index === i ? "#5ea8e9" : "#e2e0e0",
                      color: "black",
                      // marginBottom: "4px",
                      height: "35px",
                      padding: "0",
                      paddingTop: "4px",
                      borderRadius: "0px",
                      userSelect: "none",
                      borderRadius: "5px",
                      // marginBottom: "3px",
                    }}
                    onMouseEnter={(event) => {
                      event.target.style.backgroundColor = "#5ea8e9";
                      setIndex(i);
                    }}
                    onMouseLeave={(event) => {
                      event.target.style.backgroundColor = "#e0e1e2";
                      setIndex(-1);
                    }}
                    onClick={(event) => {
                      console.log(event);

                      setText(event.target.innerHTML);

                      document.getElementById("input-field").value =
                        event.target.innerHTML;
                      makeSearch();
                    }}
                  >
                    {suggestion}
                  </MDBListGroupItem>
                </motion.div>
              ))}
            </MDBListGroup>
          ) : null}
        </div>
      ) : (
        <LoadingCircle />
      )}
    </>
  );
};

export default SearchField;
