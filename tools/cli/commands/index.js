const { buildCommand } = require('../helpers');

const login = require('./login');
const application = require('./application');
const channels = require('./channels');

module.exports = {
	login: login,
	application: buildCommand('application', application),
	channels: buildCommand('channels', channels),
};
