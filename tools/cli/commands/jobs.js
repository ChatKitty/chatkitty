const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'jobs',
	description: 'Commands for managing application jobs',
	commands: buildCliExports(
		{
			api: 'JobsApi'
		}
	)
};
