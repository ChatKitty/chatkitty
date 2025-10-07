const readline = require('readline');
const keytar = require('keytar');

const constants = require('../constants');

const SERVICE = constants.keystore.SERVICE;
const ACCOUNT = constants.keystore.ACCOUNT;

function ask(question, { mask = false } = {}) {
	const rl = readline.createInterface({
		input: process.stdin,
		output: process.stdout,
	});

	if (mask) {
		rl._writeToOutput = function (stringToWrite) {
			if (rl.stdoutMuted) rl.output.write('*');
			else rl.output.write(stringToWrite);
		};
	}

	return new Promise(resolve => {
		rl.question(question, answer => {
			rl.close();
			resolve(answer.trim());
		});
		if (mask) rl.stdoutMuted = true;
	});
}

async function run() {
	console.log('\n🔐 ChatKitty CLI Login');
	console.log('Enter your application OAuth 2.0 client credentials.\n');

	const clientId = await ask('Client ID: ');
	const clientSecret = await ask('Client Secret: ', { mask: true });

	if (!clientId || !clientSecret) {
		console.error('❌ Both client ID and secret are required.');
		return 1;
	}

	const creds = {
		client_id: clientId,
		client_secret: clientSecret,
		saved_at: new Date().toISOString(),
	};

	await keytar.setPassword(SERVICE, ACCOUNT, JSON.stringify(creds));

	console.log('\n✅ Credentials securely saved for ChatKitty CLI.\n');
	return 0;
}

module.exports = async function login() {
	try {
		return await run();
	} catch (err) {
		console.error('❌ Login failed:', err.message || err);
		return 1;
	}
};
