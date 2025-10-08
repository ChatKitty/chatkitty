const fs = require('fs');

const mkdir = (dir) => {
	try {
		if (fs.existsSync(dir)) {
			fs.rmSync(dir, { recursive: true, force: true });
		}
	} catch (e) {
		try {
			fs.rmdirSync(dir, { recursive: true });
		} catch {}
	}

	fs.mkdirSync(dir, { recursive: true });
};

module.exports = mkdir;
