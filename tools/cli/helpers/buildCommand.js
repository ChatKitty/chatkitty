const {colorize} = require('json-colorizer');

const buildCommand = (command) => {
	const commands = command.commands || {};

	return async (args) => {
		const printHelp = () => {
			let help = `ChatKitty CLI\n\nUsage: chatkitty ${command.name} <command> [options]\n\nCommands:\n`;
			for (const cmd in commands) {
				help += `  - ${cmd}\n`;
			}
			console.log(help);
		}

		const name = args[0];

		if (!name) {
			console.error('❌ Missing command name\n');

			printHelp();

			return 1;
		}

		if (name === '--help' || name === '-h') {
			printHelp();

			return 0;
		}

		const cmd = commands[name];

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
		} catch (e) {
			console.error('❌ :', e.message || e);

			return 1;
		}
	}
}

module.exports = buildCommand;
