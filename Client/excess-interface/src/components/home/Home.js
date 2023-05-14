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