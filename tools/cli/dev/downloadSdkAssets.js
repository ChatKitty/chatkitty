const fs = require('fs');
const path = require('path');

const { download, getJson, mkdir } = require('../helpers');

const PACKAGE = JSON.parse(fs.readFileSync('package.json', 'utf8'));

const DIST_DIR = 'dist';
const MANIFEST_URL = `https://cdn.chatkitty.com/dist/${PACKAGE.version}/manifest.json`;

module.exports = async (args) => {
	const name = args[0];

	if (!name) {
		throw new Error(
			'Package name argument is required.\nUsage: chatkitty dev download-sdk-assets <package-name>',
		);
	}

	console.log(`Fetching from manifest: ${MANIFEST_URL}`);

	const section = `dist/${name}`;

	const manifest = await getJson(MANIFEST_URL);

	const pkg = manifest[section];

	if (!pkg || !pkg.files || typeof pkg.files !== 'object') {
		throw new Error(`Manifest does not contain ${section} files`);
	}

	const entries = Object.entries(pkg.files);

	if (entries.length === 0) {
		console.log(`No files listed under ${section} in manifest. Nothing to do.`);

		return;
	}

	mkdir(DIST_DIR);

	console.log(
		`Downloading ${entries.length} file(s) for ChatKitty ${name} v${manifest.version} -> ${DIST_DIR}`,
	);

	const results = await Promise.allSettled(
		entries.map(async ([filename, meta]) => {
			const dest = path.join(DIST_DIR, filename);

			await download(meta.url, dest);

			console.log(`✅ ${filename}`);
		}),
	);

	const failures = results.filter((r) => r.status === 'rejected');

	if (failures.length > 0) {
		throw new Error(
			`❌ ${failures.length} download(s) failed:\n` +
				failures.map((f, i) => `  ${i + 1}. ${f.reason?.message || f.reason}`).join('\n'),
		);
	}

	console.log('All assets downloaded successfully.');
};
