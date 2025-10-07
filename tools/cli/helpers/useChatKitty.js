const { ChatKitty } = require('chatkitty');
const keytar = require('keytar');

const constants = require('../constants');

const SERVICE = constants.keystore.SERVICE;
const ACCOUNT = constants.keystore.ACCOUNT;

const useChatKitty = async () => {
	const credentials = await keytar.getPassword(SERVICE, ACCOUNT);

	if (!credentials) {
		throw new Error('No credentials found. Please run "chatkitty login" first.');
	}

	const { client_id, client_secret } = JSON.parse(credentials);

	if (!client_id || !client_secret) {
		throw new Error('Invalid credentials stored. Please run "chatkitty login" again.');
	}

	return new ChatKitty({
		clientId: client_id,
		clientSecret: client_secret,
	});
};

module.exports = useChatKitty;
