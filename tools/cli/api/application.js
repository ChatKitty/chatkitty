const { useChatKitty } = require('../helpers');

const retrieveApplication = () =>
	useChatKitty(async (chatkitty) => await chatkitty.Application.retrieveAuthenticatedApplication());

module.exports = {
	retrieveApplication,
};
