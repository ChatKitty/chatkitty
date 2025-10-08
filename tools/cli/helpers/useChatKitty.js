const { ChatKitty } = require('chatkitty');
const keytar = require('keytar');

const constants = require('../constants');

const SERVICE = constants.keystore.SERVICE;
const ACCOUNT = constants.keystore.ACCOUNT;

/** @type {(callback: (chatkitty: ChatKitty) => Promise<any>) => Promise<any> } */
const useChatKitty = async (callback) => {
	const credentials = await keytar.getPassword(SERVICE, ACCOUNT);

	if (!credentials) {
		throw new Error('No credentials found. Please run "chatkitty login" first.');
	}

	const { client_id, client_secret } = JSON.parse(credentials);

	if (!client_id || !client_secret) {
		throw new Error('Invalid credentials stored. Please run "chatkitty login" again.');
	}

	const chatkitty = new ChatKitty({
		clientId: client_id,
		clientSecret: client_secret,
	});

	const result = await callback(chatkitty);

	return result.data;
};

module.exports = useChatKitty;
