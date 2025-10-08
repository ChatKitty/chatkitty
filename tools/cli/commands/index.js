const { buildCommand } = require('../helpers');

module.exports = {
	login: require('./login'),
	run: require('./run'),
	dev: buildCommand(require('./dev')),
	application: buildCommand(require('./application')),
	channels: buildCommand(require('./channels')),
};
