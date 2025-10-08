const https = require('https');

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

module.exports = getJson;
