import SearchField from "../../SearchField";
import logo from "../../icons/logo.png";

const Home = () => {

  return (
    <div id="home">
      <img src={logo} alt="logo" className="search-field-image"></img>
      <SearchField place={"home"} query={""} />
      <div className="description-home">
        "Find what you're looking for, faster than ever before!"<br/>
      </div>
    </div>
  );
};

export default Home;