import { useCallback, useEffect } from "react";
import PageResult from './PageResult';
import './App.css';
import Bar from './Bar';
import ResponseTime from "./ResponseTime";

function App() {

  // document.addEventListener("keydown", function(e) {
  //   if(e.key === "/"){
  //     console.log("test");
  //   }
  // });

  const handleKeyDown = useCallback((event) => {
    if (event.key === '/') {
      let element = document.getElementById("input-field");
      element.focus();
      event.preventDefault();
    }
  }, []);


  useEffect(() => {
    document.addEventListener('keydown', handleKeyDown);

    return () => {
      document.removeEventListener('keydown', handleKeyDown);
    };
  }, [handleKeyDown]);


  return (
    <div className="App">
      <Bar/>
      <div className="results">
        <ResponseTime responseTime={20} resultsCount={2000}/>
        <PageResult description="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." title="Test Test Test" url="www.test.com"/>

        <PageResult description="Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum." title="Test Omar Ali Kiro" url="www.test.com"/>
      </div>
     
    </div>
    
  );
}

export default App;
