import SearchField from "./SearchField";
import logo from "./logo.png";

const Bar = () => {

    return <div className="bar">
        <img src={logo} alt="logo" className="logo"></img>
        <SearchField/>
    </div>
}

export default Bar;