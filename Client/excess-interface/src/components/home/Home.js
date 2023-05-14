import SearchField from "../../SearchField";
import logo from "../../icons/image.png";

const Home = () => {

  return (
    <div id="home">
      <img src={logo} alt="logo" className="search-field-image"></img>
      <div className="description-home">
        "Find what you're looking for, faster than ever before!"<br/>
      </div>
      <SearchField place={"home"} query={""} />
    </div>
  );
};

export default Home;

// // import React from "react";
// // import SpeechRecognition, {
// //   useSpeechRecognition,
// // } from "react-speech-recognition";

// // const Home = () => {
// //   const {
// //     transcript,
// //     listening,
// //     resetTranscript,
// //     browserSupportsSpeechRecognition,
// //   } = useSpeechRecognition();

// //   if (!browserSupportsSpeechRecognition) {
// //     return console.log("ERROR");
// //   }


// //   return (
// //     <div>
// //       <p>Microphone: {listening ? "on" : "off"}</p>
// //       <button
// //         onClick={() =>
// //           SpeechRecognition.startListening({
// //             language: "en-US",
// //             continuous: true,
// //           })
// //         }
// //       >
// //         Start
// //       </button>
// //       <button onClick={SpeechRecognition.abortListening}>Stop</button>
// //       <button onClick={resetTranscript}>Reset</button>
// //       <p>{transcript}</p>
// //     </div>
// //   );
// // };
// // export default Home;


// // import React, { useState } from 'react';

// // function Home() {
// //   const [stream, setStream] = useState(null);

// //   const startCapture = async () => {
// //     try {
// //       const constraints = { audio: true, vid };
// //       const stream = await navigator.mediaDevices.getUserMedia(constraints);
// //       setStream(stream);
// //     } catch (error) {
// //       console.error(error);
// //     }
// //   };

// //   const stopCapture = () => {
// //     if (stream) {
// //       console.log(stream);
// //       stream.getTracks().forEach(track => track.stop());
// //       setStream(null);
// //     }
// //   };

// //   return (
// //     <div>
// //       {stream ? (
// //         <button onClick={stopCapture}>Stop Capture</button>
// //       ) : (
// //         <button onClick={startCapture}>Start Capture</button>
// //       )}
// //     </div>
// //   );
// // }

// // export default Home;

// import React, { useEffect, useState } from 'react';

// function Home() {
//   const [stream, setStream] = useState(null);

//   useEffect(() => {
//     navigator.mediaDevices.getUserMedia({ audio: true, video: true })
//       .then(stream => {
//         setStream(stream);
//       })
//       .catch(error => {
//         console.error(error);
//       });
//   }, []);

//   return (
//     <div>
//       {stream && (
//         <video src={stream} autoPlay muted />
//       )}
//     </div>
//   );
// }

// export default Home;