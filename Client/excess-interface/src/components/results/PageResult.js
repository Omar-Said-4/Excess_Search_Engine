import logo from "../../icons/insta.png";
import parse from "html-react-parser";

const PageResult = ({ title, url, description }) => {
  const maxLength = 300;
  console.log(description.length);

  if (description.length > maxLength) {
    description = description.slice(0, maxLength) + "...";
    console.log("test");
  }

  return (
    <div className="search-result">
      <div className="title-div">
        <div className="img-span">
          <img src={logo} alt="test" className="result-image"></img>
        </div>
        <a className="title" href={url}>{title}</a>
      </div>

      <a href={url} className="url">
        {url}
      </a>
      <p className="description">{parse(description)}</p>
    </div>
  );
};

export default PageResult;
