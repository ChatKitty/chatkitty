const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'functions',
	description: 'Commands for managing chat functions',
	commands: buildCliExports(
		{
			api: 'FunctionsApi'
		}
	)
};
