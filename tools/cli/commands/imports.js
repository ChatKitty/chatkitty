const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'imports',
	description: 'Commands for managing batch imports',
	commands: buildCliExports(
		{
			api: 'ImportsApi'
		}
	)
};
