const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'application',
	description: 'Commands for managing the application',
	commands: buildCliExports(
		{
			api: 'ApplicationApi'
		}
	)
};
