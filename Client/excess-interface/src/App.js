import { useCallback, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import "./App.css";
import Results from "./Results";

function App() {
  const handleKeyDown = useCallback((event) => {
    if (event.key === "/") {
      let element = document.getElementById("input-field");
      element.focus();
      event.preventDefault();
    } else if (
      event.keyCode === 13 &&
      document.getElementById("input-field") === document.activeElement
    ) {
      console.log("Start Searching!");
    }
  }, []);

  useEffect(() => {
    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [handleKeyDown]);

  var data = [];
  var description =
    "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";

  var title = "Test Test Test";

  var url = "www.test.com";
  var item = {};

  for (var i = 0; i < 10; i++) {
    item = {};
    item["description"] = description;
    item["title"] = title;
    item["url"] = url;
    data.push(item);
  }

  return (
    <div className="App">
      <Router>
        <Routes>
            <Route path="/search" element={<Results data={data} exact/>}>
            </Route>
        </Routes>
      </Router>
    </div>
  );
}

export default App;

// const [result, setResult] = useState([]);

// for (var i = 0; i < 2; i++) {
//   var item = {};
//   var text;

//   fetch("https://api.api-ninjas.com/v1/loremipsum?max_length=" + "4000", {
//     method: "GET",
//     headers: {
//       "X-Api-Key": "hQcTTURnVwOoH04wB8VOkQ==aDmqaQlA96owJVaY",
//       "Content-Type": "application/json",
//     },
//   })
//     .then((response) => response.json())
//     .then((data) => {
//       console.log(data["text"]);
//       text = data["text"];

//       item["description"] = text.slice(0, 1000);
//       item["title"] = text.slice(1001, 1030);
//       item["url"] =
//         "www." + text.slice(1031, 1040).split(" ").join("") + ".com";

//       setResult((result) => [...result, item]);
//     })
//     .catch((error) => {
//       console.error(error);
//     });
// }
