import SearchField from "../../SearchField";
import logo from "../../icons/logo.png";

const Bar = ({query}) => {

    return <div className="bar">
        <img src={logo} alt="logo" className="logo"></img>
        <SearchField query={query} placce={"result"}/>
    </div>
}

export default Bar;