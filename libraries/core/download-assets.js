const fs = require('fs');
const https = require('https');
const path = require('path');

const assets = ['index.d.ts', 'index.d.mts', 'index.mjs', 'index.js'];

const pkg = JSON.parse(fs.readFileSync('package.json', 'utf8'));

console.log(`Downloading assets for ChatKitty SDK v${pkg.version}`);

const downloadAssets = async () => {
    // Ensure the 'dist' directory exists
    if (!fs.existsSync('dist')) {
        fs.mkdirSync('dist');
    }

    for (const asset of assets) {
        const url = `https://cdn.chatkitty.com/dist/core/${pkg.version}/${asset}`;
        const dest = path.join('dist', asset);

        try {
            await download(url, dest);

            console.log(`✅ Downloaded ${asset}`);
        } catch (error) {
            console.error(`❌ Error downloading ${asset}: ${error}`);
        }
    }
};

const download = (url, dest) => {
    return new Promise((resolve, reject) => {
        const file = fs.createWriteStream(dest);

        https
            .get(url, (response) => {
                // Check if the response status is 2xx
                if (response.statusCode >= 200 && response.statusCode < 300) {
                    response.pipe(file);
                    file.on('finish', () => {
                        file.close(resolve); // Close the file and resolve the promise
                    });
                } else {
                    // Handle non-successful status codes
                    response.resume(); // Consume the response data to free up memory
                    reject(`Request for ${url} failed with status code: ${response.statusCode}`);
                }
            })
            .on('error', (err) => {
                fs.unlink(dest, () => reject(err.message)); // Delete the file in case of error
            });
    });
};

downloadAssets()
    .then(() => {
        console.log('All assets downloaded successfully');
    })
    .catch((err) => {
        console.error('Error downloading assets:', err);
        process.exit(1);
    });
