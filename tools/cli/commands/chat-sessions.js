const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'chat-sessions',
	description: 'Commands for managing chat sessions',
	commands: buildCliExports(
		{
			api: 'ChatSessionsApi'
		}
	)
};
