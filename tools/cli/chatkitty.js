const child_process = require('child_process');

const result = child_process.spawnSync('moon', process.argv.slice(2), {
	shell: false,
	stdio: 'inherit',
});

if (result.error) {
	throw result.error;
}

process.exitCode = result.status;
