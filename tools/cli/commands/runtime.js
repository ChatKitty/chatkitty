const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'runtime',
	description: 'Commands for managing the chat function runtime',
	commands: buildCliExports(
		{
			api: 'RuntimeApi'
		}
	)
};
