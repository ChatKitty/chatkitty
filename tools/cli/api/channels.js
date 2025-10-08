const { useChatKitty } = require('../helpers');

const listChannels = () => useChatKitty(async (chatkitty) => await chatkitty.Channels.listChannels());

module.exports = {
	listChannels,
};
