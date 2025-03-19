module.exports = {
	root: true,
	parser: '@typescript-eslint/parser',
	parserOptions: {
		project: 'tsconfig.eslint.json',
		tsconfigRootDir: __dirname,
		extraFileExtensions: [".vue"],
	},
	extends: [
		'moon'
	]
};
