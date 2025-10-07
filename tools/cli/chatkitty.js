#!/usr/bin/env node

const child_process = require('child_process');

const commands = require('./commands');

let args = process.argv.slice(2);
if (args.length === 0) {
	args = ['help'];
}

const command = commands[args[0]];

if (command) {
	(async function () {
		process.exitCode = await command(args.slice(1));
	})();
} else {
	const result = child_process.spawnSync('moon', args, {
		shell: false,
		stdio: 'inherit',
	});

	if (result.error) {
		console.error('❌ Unknown command:', args[0]);
        process.exitCode = 1;
        return;
	}

	process.exitCode = result.status;
}
