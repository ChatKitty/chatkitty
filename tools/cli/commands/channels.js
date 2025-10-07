const {
	channels: { listChannels },
} = require('../api');

module.exports = {
	list: listChannels,
};
