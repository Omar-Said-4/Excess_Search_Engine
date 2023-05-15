import web from "../../icons/web.png";
import parse from "html-react-parser";

const PageResult = ({ title, url, description, icon }) => {
  const maxLength = 300;
  console.log(description.length);

  console.log(icon);

  if (description.length > maxLength) {
    description = description.slice(0, maxLength) + "...";
    console.log("test");
  }

  return (
    <div className="search-result">
      <div className="title-div">
        <div className="img-span">
          {icon === null ? (
            <img src={web} alt="text" className="result-image"></img>
          ) : (
            <img src={icon} alt="text" className="result-image"></img>
          )}
        </div>
        <span className="url-span">
          <a className="title" href={url}>
            {title}
          </a>
        </span>
      </div>

      <a href={url} className="url">
        {url}
      </a>
      <p className="description">{parse(description)}</p>
    </div>
  );
};

export default PageResult;
