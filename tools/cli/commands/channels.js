const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'channels',
	description: 'Commands for managing channels',
	commands: buildCliExports(
		{
			api: 'ChannelsApi'
		}
	)
};
