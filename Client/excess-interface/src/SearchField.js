import React, { useState, useEffect, useCallback } from "react";
import Axios from "axios";
import {
  MDBInputGroup,
  MDBIcon,
  MDBBtn,
  MDBListGroup,
  MDBListGroupItem,
} from "mdb-react-ui-kit";
import LoadingCircle from "./components/results/LoadingCircle";
import { motion } from "framer-motion";
import "mdb-react-ui-kit/dist/css/mdb.min.css";
import "@fortawesome/fontawesome-free/css/all.min.css";
import SpeechRecognition, {
  useSpeechRecognition,
} from "react-speech-recognition";

const SearchField = ({ query, place }) => {
  const [text, setText] = useState(query);
  const [loaded, setLoaded] = useState(false);
  const [index, setIndex] = useState(-1);
  const [suggestions, setSuggestions] = useState([]);
  const [focused, setFocused] = useState(false);

  const [isListening, setIsListening] = useState(false);

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
        console.log(document.getElementById("input-field").value.trim());
        setText(document.getElementById("input-field").value.trim());
        makeSearch();
      } else if (event.keyCode === 27) setFocused(false);
    },
    [index, suggestions]
  );

  window.onload = function () {
    setLoaded(true);
    console.log("loaded");
  };

  useEffect(() => {
    document.addEventListener("keydown", handleKeyDown);
    console.log("test");

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [handleKeyDown]);

  const { transcript, resetTranscript, browserSupportsSpeechRecognition } =
    useSpeechRecognition();

  if (!browserSupportsSpeechRecognition) {
    return console.log("ERROR");
  }

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
      document.getElementById("input-field").value.trim() +
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
      {/* {transcript !== "" ? <div>{transcript}</div> : null} */}
      {loaded === false || place === "result" ? (
        <div
          style={{
            width: "40%",
            display: place === "result" ? "inline-block" : "block",
            left: place === "result" ? "5%" : "30%",
          }}
          className="search-field"
        >
          <MDBInputGroup style={{ height: "100%" }}>
            <input
              className="form-control"
              label="Search"
              id="input-field"
              style={{
                // height: "7vh",
                overflow: "",
                fontSize: "14px",
                borderRadius: "15px",
                boxShadow: focused ? "0 0 10px rgba(0, 0, 0, 1)" : "",
                backgroundColor: place !== "result" ? "#f2f2f2" : "#c6ddf6",
                marginRight: "7px",
                whiteSpace: "normal",
              }}
              value={transcript === "" ? text : transcript}
              spellCheck={false}
              autoComplete="off"
              placeholder="Search!"
              onChange={(e) => queryChanged(e)}
              onFocus={(e) => setFocused(true)}
              onBlur={(e) => setFocused(true)}
            />
            <MDBBtn
              rippleColor="red"
              style={{ borderRadius: "15px", marginRight: "5px" }}
              disabled={text === ""}
              onClick={search}
            >
              <MDBIcon icon="search" />
            </MDBBtn>
            <MDBBtn
              rippleColor="dark"
              style={{
                borderRadius: "15px",
                backgroundColor: "red",
                borderWidth: "0",
              }}
              onClick={(e) => {
                if (isListening === false) {
                  resetTranscript();

                  SpeechRecognition.startListening({
                    language: "en-US",
                    continuous: true,
                  });
                  setIsListening(true);
                } else {
                  if (transcript !== "") {
                    setText(transcript);

                  }

                  SpeechRecognition.abortListening();
                  search();
                  setIsListening(false);
                }
              }}
            >
              <MDBIcon
                icon={
                  isListening === false
                    ? "fas fa-microphone"
                    : "fas fa-ellipsis-h"
                }
              />
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
                  transition={{
                    type: "tween",
                    duration: 0.9,
                  }}
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
                      paddingTop: "6px",
                      userSelect: "none",
                      borderRadius: "5px",
                      fontSize: "14px",
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

// import React from "react";
// import SpeechRecognition, {
//   useSpeechRecognition,
// } from "react-speech-recognition";

// const Home = () => {
//   const {
//     transcript,
//     listening,
//     resetTranscript,
//     browserSupportsSpeechRecognition,
//   } = useSpeechRecognition();

//   if (!browserSupportsSpeechRecognition) {
//     return console.log("ERROR");
//   }

//   return (
//     <div>
//       <p>Microphone: {listening ? "on" : "off"}</p>
//       <button
//         onClick={() =>
//           SpeechRecognition.startListening({
//             language: "en-US",
//             continuous: true,
//           })
//         }
//       >
//         Start
//       </button>
//       <button onClick={SpeechRecognition.abortListening}>Stop</button>
//       <button onClick={resetTranscript}>Reset</button>
//       <p>{transcript}</p>
//     </div>
//   );
// };
// export default Home;
