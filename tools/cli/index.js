#!/usr/bin/env node

const commands = require('./commands');

const printHelp = () => {
	let help = 'ChatKitty CLI\n\nUsage: chatkitty <command> [options]\n\nCommands:\n';
	for (const cmd in commands) {
		help += `  - ${cmd}\n`;
	}
	help += '\nFor detailed help on a command, run: chatkitty <command> --help\n';
	console.log(help);
};

let args = process.argv.slice(2);
if (args.length === 0) {
	args = ['--help'];
}

if (args[0] === '--help' || args[0] === '-h') {
	printHelp();

	process.exit(0);
}

const command = commands[args[0]];

if (command) {
	(async function () {
		process.exitCode = await command(args.slice(1));
	})();
} else {
	console.error(`❌ Unknown command: ${args[0]}\n`);
	printHelp();

	process.exit(1);
}
