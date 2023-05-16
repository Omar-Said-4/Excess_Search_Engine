import { useState } from "react";
import SearchField from "../../SearchField";
import logo from "../../icons/image.png";

const Home = () => {
  const [loaded, setLoaded] = useState(false);
  const [focused, setFocused] = useState(false);

  return (
    <div
      id="home"
      onLoad={() => {
        setLoaded(true);
      }}
    >
      <img src={logo} alt="logo" className="search-field-image"></img>
      <div className="description-home" id="slogan">
        "Find what you're looking for, faster than ever before!"
        {/* <br /> */}
      </div>
      <SearchField
        place={"home"}
        query={""}
        loaded={loaded}
        focused={focused}
        setFocused={setFocused}
      />
    </div>
  );
};

export default Home;
