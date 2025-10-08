const { downloadSdkAssets, run } = require('../dev');

module.exports = {
	name: 'dev',
	commands: {
		'download-sdk-assets': downloadSdkAssets,
		run: run,
	},
};
