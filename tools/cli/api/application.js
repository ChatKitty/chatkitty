const { useChatKitty } = require('../helpers');

const retrieveApplication = async () => {
	const kitty = await useChatKitty();

	const response = await kitty.Application.retrieveAuthenticatedApplication();

	return response.data;
};

module.exports = {
	retrieveApplication,
};
