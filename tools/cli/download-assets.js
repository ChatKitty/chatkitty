const fs = require('fs');
const https = require('https');
const path = require('path');

const pkg = JSON.parse(fs.readFileSync('package.json', 'utf8'));

const PKG_NAME = (process.argv[2] || '').trim();
if (!PKG_NAME) {
	console.error('Usage: node download-assets.js <package>');
	process.exit(1);
}

const MANIFEST_SECTION = `dist/${PKG_NAME}`;
const DIST_DIR = 'dist'

const MANIFEST_URL = `https://cdn.chatkitty.com/dist/${pkg.version}/manifest.json`;

console.log(`Fetching from manifest: ${MANIFEST_URL}`);

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

const getJson = (url, timeoutMs = 10000, redirectsLeft = 5) =>
	new Promise((resolve, reject) => {
		const req = https.get(url, (res) => {
			const { statusCode, headers } = res;

			if (statusCode >= 300 && statusCode < 400 && headers.location && redirectsLeft > 0) {
				res.resume();

				return resolve(getJson(headers.location, timeoutMs, redirectsLeft - 1));
			}

			if (statusCode < 200 || statusCode >= 300) {
				res.resume();

				return reject(new Error(`GET ${url} failed with status ${statusCode}`));
			}

			let data = '';
			res.setEncoding('utf8');
			res.on('data', (chunk) => (data += chunk));
			res.on('end', () => {
				try {
					resolve(JSON.parse(data));
				} catch (e) {
					reject(new Error(`Invalid JSON from ${url}: ${e.message}`));
				}
			});
		});

		req.on('error', reject);

		req.setTimeout(timeoutMs, () => req.destroy(new Error('Request timed out')));
	});

const download = (url, dest, timeoutMs = 20000, redirectsLeft = 5) =>
	new Promise((resolve, reject) => {
		mkdir(path.dirname(dest));

		const file = fs.createWriteStream(dest);

		const doGet = (currentUrl, redirectsRemain) => {
			const req = https.get(currentUrl, (res) => {
				const { statusCode, headers } = res;

				if (statusCode >= 300 && statusCode < 400 && headers.location && redirectsRemain > 0) {
					res.resume();
					return doGet(headers.location, redirectsRemain - 1);
				}

				if (statusCode < 200 || statusCode >= 300) {
					res.resume();
					file.close(() => {
						fs.unlink(dest, () => {
							reject(new Error(`GET ${currentUrl} failed with status ${statusCode}`));
						});
					});
					return;
				}

				res.pipe(file);

				file.on('finish', () => file.close(resolve));
			});

			req.on('error', (err) => {
				file.close(() => {
					fs.unlink(dest, () => reject(err));
				});
			});

			req.setTimeout(timeoutMs, () => req.destroy(new Error('Request timed out')));
		};

		doGet(url, redirectsLeft);
	});

(async () => {
	try {
		const manifest = await getJson(MANIFEST_URL);

		const pkg = manifest[MANIFEST_SECTION];

		if (!pkg || !pkg.files || typeof pkg.files !== 'object') {
			throw new Error(`Manifest does not contain ${MANIFEST_SECTION} files`);
		}

		const entries = Object.entries(pkg.files);

		if (entries.length === 0) {
			console.log(`No files listed under ${MANIFEST_SECTION} in manifest. Nothing to do.`);

			return;
		}

		mkdir(DIST_DIR);

		console.log(
			`Downloading ${entries.length} file(s) for ChatKitty ${PKG_NAME} v${manifest.version} -> ${DIST_DIR}`,
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
			console.error(
				`❌ ${failures.length} download(s) failed:\n` +
					failures.map((f, i) => `  ${i + 1}. ${f.reason?.message || f.reason}`).join('\n'),
			);

			process.exit(1);
		}

		console.log('All assets downloaded successfully.');
	} catch (err) {
		console.error('Fatal error:', err?.message || err);

		process.exit(1);
	}
})();
