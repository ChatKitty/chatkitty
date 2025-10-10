const { buildCommand } = require('../helpers');

module.exports = {
	login: require('./login'),
	run: require('./run'),
	dev: buildCommand(require('./dev')),
	analytics: buildCommand(require('./analytics')),
	application: buildCommand(require('./application')),
	channels: buildCommand(require('./channels')),
	'chat-sessions': buildCommand(require('./chat-sessions')),
	'function-versions': buildCommand(require('./function-versions')),
	functions: buildCommand(require('./functions')),
	imports: buildCommand(require('./imports')),
	jobs: buildCommand(require('./jobs')),
	messages: buildCommand(require('./messages')),
	runtime: buildCommand(require('./runtime')),
	threads: buildCommand(require('./threads.js')),
	'user-sessions': buildCommand(require('./user-sessions')),
	users: buildCommand(require('./users')),
};
