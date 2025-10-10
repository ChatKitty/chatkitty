const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'users',
	description: 'Commands for managing users',
	commands: buildCliExports(
		{
			api: 'UsersApi'
		}
	)
};
