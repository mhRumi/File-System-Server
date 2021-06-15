const fs = require('fs');

// const st=fs.readFile();
const st = 'pneumonoultramicroscopicsilicovolcanoconiosis';
const ln = st.lenght();
if (ln > 4) {
  console.log(st[0], ln, st[ln - 1]);
} else {
  console.log(st);
}
// save
