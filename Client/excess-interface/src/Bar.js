import SearchField from "./SearchField";
import logo from "./logo.png";

const Bar = ({query}) => {

    return <div className="bar">
        <img src={logo} alt="logo" className="logo"></img>
        <SearchField query={query}/>
    </div>
}

export default Bar;