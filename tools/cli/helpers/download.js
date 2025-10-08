const fs = require('fs');
const https = require('https');
const path = require('path');

const mkdir = require('./mkdir');

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

			req.on('error', (e) => {
				file.close(() => {
					fs.unlink(dest, () => reject(e));
				});
			});

			req.setTimeout(timeoutMs, () => req.destroy(new Error('Request timed out')));
		};

		doGet(url, redirectsLeft);
	});

module.exports = download;
