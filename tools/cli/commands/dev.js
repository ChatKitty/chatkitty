const { downloadSdkAssets } = require('../dev');

module.exports = {
	name: 'dev',
	commands: {
		'download-sdk-assets': downloadSdkAssets
	},
};
