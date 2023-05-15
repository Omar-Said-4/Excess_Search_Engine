import { useState } from "react";
import SearchField from "../../SearchField";
import logo from "../../icons/image.png";

const Home = () => {
  const [loaded, setLoaded] = useState(false);

  return (
    <div
      id="home"
      onLoad={() => {
        setLoaded(true);
      }}
    >
      <img src={logo} alt="logo" className="search-field-image"></img>
      <div className="description-home">
        "Find what you're looking for, faster than ever before!"
        <br />
      </div>
      <SearchField place={"home"} query={""} loaded={loaded}/>
    </div>
  );
};

export default Home;
