const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'user-sessions',
	description: 'Commands for managing user sessions',
	commands: buildCliExports(
		{
			api: 'UserSessionsApi'
		}
	)
};
