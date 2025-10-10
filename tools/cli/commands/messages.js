const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'messages',
	description: 'Commands for managing messages',
	commands: buildCliExports(
		{
			api: 'MessagesApi'
		}
	)
};
