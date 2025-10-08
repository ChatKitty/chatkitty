const { input, password } = require('@inquirer/prompts');
const keytar = require('keytar');

const constants = require('../constants');

const {
	application: { retrieveApplication },
} = require('../api');

const SERVICE = constants.keystore.SERVICE;
const ACCOUNT = constants.keystore.ACCOUNT;

module.exports = async () => {
	try {
		console.log('\n🔐 ChatKitty CLI Login');
		console.log('Enter your application OAuth 2.0 client credentials.\n');

		const clientId = await input({ message: 'Client ID: ' });
		const clientSecret = await password({ message: 'Client Secret: ' });

		if (!clientId || !clientSecret) {
			console.error('❌ Both client ID and secret are required.');

			return 1;
		}

		const credentials = {
			client_id: clientId,
			client_secret: clientSecret,
			saved_at: new Date().toISOString(),
		};

		await keytar.setPassword(SERVICE, ACCOUNT, JSON.stringify(credentials));

		console.log('\n🔍 Verifying credentials...');

		const app = await retrieveApplication();

		console.log(`\n✅ Successfully authenticated as application: (ID: ${app.id})`);

		console.log('\n✅ Credentials securely saved for ChatKitty CLI.\n');
	} catch (err) {
		console.error('❌ Login failed:', err.message || err);

		return 1;
	}

	return 0;
};
