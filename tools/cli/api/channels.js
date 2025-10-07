const {useChatKitty} = require('../helpers');

const listChannels = async () => {
	const kitty = await useChatKitty();

	const response = await kitty.Channels.listChannels();

	return response.data;
}

module.exports = {
	listChannels,
}
