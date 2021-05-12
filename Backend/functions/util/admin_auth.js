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
			return db.collection('users').where('userId','==', request.user.uid).where('role','==', 'admin').limit(1).get();
		})
		.then((data) => {
			request.user.id = data.docs[0].id;
			request.user.data = data.docs[0].data();
			return next();
		})
		.catch((err) => {
			console.error('Error while verifying token', err);
			return response.status(403).json({ Error: 'Unauthorized User' });
		});
};