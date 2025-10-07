const {
	channels: { listChannels },
} = require('../api');

module.exports = {
	name: 'channels',
	commands: {
		list: listChannels,
	},
};
