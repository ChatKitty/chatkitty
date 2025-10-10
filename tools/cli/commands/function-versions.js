const { buildCliExports } = require('../helpers');

module.exports = {
	name: 'function-versions',
	description: 'Commands for managing chat function versions',
	commands: buildCliExports(
		{
			api: 'FunctionVersionsApi'
		}
	)
};
