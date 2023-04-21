var data = [{}];
const fs = require('fs');

const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789                               ';
const charactersLength = characters.length;
var descLength = 200;
var titleLength = 20;
var urlLength = 6;

for(var i = 0;i < 1000;i++){
    let description = '';
    let title = '';
    let url = '';

    for (let i = 0; i < descLength; i++) {
      description += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    for (let i = 0; i < titleLength; i++) {
        title += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    for (let i = 0; i < urlLength; i++) {
        url += characters.charAt(Math.floor(Math.random() * charactersLength));
    }

    data.push({"description": description, "title": title, "url": url});
}

const string = (JSON.stringify(data));

fs.writeFile('myFile.json', string, err => {
    if (err) {
      console.error(err);
      return;
    }
    console.log('File written successfully');
});