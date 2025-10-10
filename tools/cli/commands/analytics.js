const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'analytics',
	description: 'Commands for managing analytics',
	commands: buildCliExports(
		{
			api: 'AnalyticsApi'
		}
	)
};
