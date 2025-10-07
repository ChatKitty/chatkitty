const {colorize} = require('json-colorizer');

module.exports = (scope, command) => {
	return async (args) => {
		const name = args[0];

		if (!name) {
			console.error('❌ Missing command name');

			return 1;
		}

		if (name === '--help' || name === '-h') {
			let help = `ChatKitty CLI\n\nUsage: chatkitty ${scope} <command> [options]\n\nCommands:\n`;
			for (const cmd in command) {
				help += `  - ${cmd}\n`;
			}
			console.log(help);
			return 0;
		}

		const cmd = command[name];

		if (!cmd) {
			console.error(`❌ Unknown command: ${name}`);

			return 1;
		}

		try {
			const result = await cmd(args.slice(1));

			if (result) {
				console.log(colorize(result));
			}

			return 0;
		} catch (err) {
			console.error('❌ Command failed:', err.message || err);

			return 1;
		}
	}
}
