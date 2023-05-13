import { useCallback, useEffect } from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";


import "./styles/App.css";
import Results from "./components/results/Results";
import Home from "./components/home/Home";

function App() {
  const handleKeyDown = useCallback((event) => {
    if (event.key === "/") {
      let element = document.getElementById("input-field");
      element.focus();
      event.preventDefault();
    } else if (
      event.keyCode === 13 &&
      document.getElementById("input-field") === document.activeElement && 
      (document.getElementById("input-field").value).replace(/\s/g, "") !== ""
    ) {
      window.location.href = "/search/?query="+ document.getElementById("input-field").value +"&page=1";
    } 
  }, []);


  useEffect(() => {
    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [handleKeyDown]);


  return (
    <div className="App">
      <Router>
        <Routes>
            <Route path="/search" element={<Results exact/>}>
            </Route>
            <Route path="/" element={<Home/>}></Route>
        </Routes>
      </Router>
    </div>
  );
}

export default App;

