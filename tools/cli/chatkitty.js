#!/usr/bin/env node

const child_process = require('child_process');

const commands = require('./commands');

let args = process.argv.slice(2);
if (args.length === 0) {
	args = ['--help'];
}

if (args[0] === '--help' || args[0] === '-h') {
	let help = 'ChatKitty CLI\n\nUsage: chatkitty <command> [options]\n\nCommands:\n';
	for (const cmd in commands) {
		help += `  - ${cmd}\n`;
	}
	help += '\nFor detailed help on a command, run: chatkitty <command> --help\n';
	console.log(help);
	process.exit(0);
}

const command = commands[args[0]];

if (command) {
	(async function () {
		process.exitCode = await command(args.slice(1));
	})();
} else {
	const result = child_process.spawnSync('npx', ['moon', ...args], {
		shell: false,
		stdio: 'inherit',
	});

	if (result.error) {
		console.error('❌ Failed to execute command:', result.error.message || result.error);
		process.exitCode = 1;
		return;
	}

	process.exitCode = result.status;
}
