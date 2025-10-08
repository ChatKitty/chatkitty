const { buildCommand } = require('../helpers');

module.exports = {
	login: require('./login'),
	dev: buildCommand(require('./dev')),
	application: buildCommand(require('./application')),
	channels: buildCommand(require('./channels')),
};
