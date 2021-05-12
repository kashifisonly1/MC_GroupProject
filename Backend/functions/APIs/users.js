const { admin, db } = require('../util/admin');
const config = require('../util/config');

const firebase = require('firebase');

firebase.initializeApp(config);

exports.loginUser = (request, response) => {
    const user = {
        email: request.body.email,
        password: request.body.password
    }

    const { valid, errors } = validateLoginData(user);
    if (!valid) return response.status(400).json(errors);

    firebase
        .auth()
        .signInWithEmailAndPassword(user.email, user.password)
        .then((data) => {
            return data.user.getIdToken();
        })
        .then((token) => {
            return response.json({ token });
        })
        .catch((error) => {
            console.error(error);
            return response.status(403).json({ Error: 'wrong credentials, please try again'});
        })
};

exports.signUpUser = (request, response) => {
    let token, userId;
    firebase
        .auth()
        .signInWithPhoneNumber("+923209417575", true)
        .then((data) => {
            data.user.updateProfile({displayName: "kashif tariq"});
            userId = data.user.uid;
            return data.user.getIdToken();
        })
        .then((token)=>{
            return response.status(201).json({ token });
        })
        .catch((err) => {
            console.error(err);
            if (err.code === 'auth/email-already-in-use') {
                return response.status(400).json({ Email: 'Email already in use' });
            } else {
	            return response.status(500).json({ ServerError: "Something Went Wrong"});
            }
        });
}

