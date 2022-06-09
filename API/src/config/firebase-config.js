const admin = require("firebase-admin");

const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
  databaseURL: "https://truthordare-898fa-default-rtdb.asia-southeast1.firebasedatabase.app/",
  serviceAccount: 'config.json'
});


module.exports = admin;