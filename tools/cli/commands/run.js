const child_process = require('child_process');

module.exports = async (args) => {
	const result = child_process.spawnSync('npx', ['moon', 'run', ...args], {
		shell: false,
		stdio: 'inherit',
	});

	if (result.error) {
		new Error('❌ Failed to execute command:', result.error.message || result.error);
	}
};
