const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'threads',
	description: 'Commands for managing message threads',
	commands: buildCliExports(
		{
			api: 'ThreadsApi'
		}
	)
};
