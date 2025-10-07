const {
	application: { retrieveApplication },
} = require('../api');

module.exports = {
	name: 'application',
	commands: {
		retrieve: retrieveApplication,
	},
};
