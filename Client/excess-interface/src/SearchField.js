// import { useState } from "react";
// import search from "./search.png";

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


//   const [searchInput, setSearchInput] = useState('');
//   const [isFocused, setIsFocused] = useState(false);

//   const handleInputChange = (event) => {
//     setSearchInput(event.target.value);
//   };

//   const handleFocus = () => {
//     setIsFocused(true);
//   };

//   const handleBlur = () => {
//     setIsFocused(false);
//   };

//   return (
//     <div className={`search-bar ${isFocused ? 'focused' : ''}`}>
//     <input
//       type="text"
//       value={searchInput}
//       onChange={handleInputChange}
//       onFocus={handleFocus}
//       onBlur={handleBlur}
//       placeholder="Search..."
//     />
//     <button type="button">
//       <i className="fa fa-search"></i>
//     </button>
//   </div>
//     // <>
//     //   <div className="search-field">
//     //     <input
//     //       type="text"
//     //       placeholder="Search"
//     //       id="input-field"
//     //       value={text}
//     //       onChange={(e) => setText(e.target.value)}
//     //       style={style}
//     //     />

//     //     {/* <img
//     //     src={search}
//     //     className="search-field-icon"
//     //     alt="search-icon"
//     //     onClick={makeSearch}
//     //   ></img> */}
//     //   </div>

//     //   {text !== "" ? (
//     //     <div className="dropdown">
//     //       {data.map((item) => (
//     //         <div className="suggestion-item">{item}</div>
//     //       ))}
//     //     </div>
//     //   ) : null}
//     // </>
//   );
// };

// export default SearchField;

import React, { useState } from "react";
import { FaSearch } from "react-icons/fa";
import { CSSTransition } from "react-transition-group";

const SearchField = () => {
  const [isExpanded, setIsExpanded] = useState(false);

  const handleInputFocus = () => {
    setIsExpanded(true);
  };

  const handleInputBlur = () => {
    setIsExpanded(false);
  };

  return (
    <div className="search-bar">
      <div className="search-container">
        <div className="search-icon">
          <FaSearch />
        </div>
        <input
          type="text"
          placeholder="Search"
          onFocus={handleInputFocus}
          onBlur={handleInputBlur}
        />
      </div>
      <CSSTransition
        in={isExpanded}
        timeout={300}
        classNames="search-container-expanded"
        unmountOnExit
      >
        <div className="search-container-expanded">
          {/* Your expanded search bar content goes here */}
        </div>
      </CSSTransition>
    </div>
  );
};

export default SearchField;

