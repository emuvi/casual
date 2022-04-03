const fs = require("fs");

const folder = "C:\\CSVMorto\\Flexpel\\2021-06-23\\";

fs.readdir(folder, (_, files) => {
  files.forEach((file) => {
    if (file.endsWith(".tab")) {
      let source = fs.readFileSync(folder + file);
      let table = JSON.parse(source);
      if (table.fields) {
        table.fields.forEach((field) => {
          if (field.nature) {
            field.nature = field.nature.toUpperCase();
          }
        });
      }
      fs.writeFileSync(folder + file, JSON.stringify(table, null, 0));
    }
  });
});
