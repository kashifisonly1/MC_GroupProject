const functions = require("firebase-functions");
const app = require('express')();
var cors = require('cors');
const auth = require('./util/auth');
const admin_auth = require('./util/admin_auth');
app.use(cors());

const {
    signUpUser
} = require('./APIs/users')

app.post('/signup', signUpUser);

exports.api = functions.https.onRequest(app);