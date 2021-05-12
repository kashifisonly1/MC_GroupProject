const { admin, db } = require('./admin');

module.exports = (request, response, next) => {
	let idToken;
	if (request.headers.authorization && request.headers.authorization.startsWith('Bearer ')) {
		idToken = request.headers.authorization.split('Bearer ')[1];
	} else {
		console.error('No token found');
		return response.status(403).json({ Error: 'Unauthorized User' });
	}
	admin
		.auth()
		.verifyIdToken(idToken)
		.then((decodedToken) => {
			request.user = decodedToken;
			request.user.isEmailVerified = decodedToken.email_verified;
			return db.collection('users').where('userId', '==', request.user.uid).limit(1).get();
		})
		.then((data) => {
			request.user.id = data.docs[0].id;
			request.user.data = {userId:data.docs[0].data().userId,
				brokerId:(data.docs[0].data().role==="agent"?data.docs[0].data().brokerId:data.docs[0].data().userId),
				phone: data.docs[0].data().phoneNumber,
				expiryTime: data.docs[0].data().expiryTime,
				firstName: data.docs[0].data().firstName,
				lastName: data.docs[0].data().lastName,
				estateName: data.docs[0].data().estateName,
				role:data.docs[0].data().role,
				address:data.docs[0].data().address,
				allowedPhases:(data.docs[0].data().role==="agent"?data.docs[0].data().allowedPhases:"all"),
				allowedTypes:(data.docs[0].data().role==="agent"?data.docs[0].data().allowedTypes:"all")
			};
			return next();
		})
		.catch((err) => {
			console.error('Error while verifying token', err);
			return response.status(403).json({ Error: 'Unauthorized User' });
		});
};