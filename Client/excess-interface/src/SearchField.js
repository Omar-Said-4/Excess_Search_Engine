import { useState } from "react";

const SearchField = ({query}) =>{
    const [text, setText] = useState(query);

    return <div className="search-field">
        <input type="text" placeholder="Search" className="search-field-input" id="input-field" value={text} onChange={(e) => setText(e.target.value)}/>
    </div>

}

export default SearchField;