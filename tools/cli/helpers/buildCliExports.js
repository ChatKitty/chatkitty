/**
 * Build a dynamic proxy of CLI-callable methods for a given ChatKitty API surface.
 * - Normalizes command names (kebab/camel/Pascal).
 * - Parses argv into options (with type coercion, aliases, and defaults).
 * - Supports nested objects via dot-notation and JSON values.
 * - Orders options to match target function parameter names.
 * - Reports unknown flags (avoids false positives for nested leaves).
 * - Explicit “bag mode” with a lone "-" token: pass the entire flag bag
 *   as a single object argument.
 */
const { ChatKitty } = require('chatkitty');

const useChatKitty = require('./useChatKitty');

const DUMMY_KITTY = new ChatKitty({
	clientId: 'DUMMY_ID_FOR_INTROSPECTION_ONLY',
	clientSecret: 'DUMMY_SECRET_FOR_INTROSPECTION_ONLY',
});

/** Built-in coercers used as fallbacks when a custom translator isn't provided. */
const DEFAULT_TRANSLATORS = Object.freeze({
	int: (v) => Number.parseInt(v, 10),
	float: (v) => Number.parseFloat(v),
	bool: (v) => /^(true|1|yes)$/i.test(String(v)),
	array: (v) =>
		typeof v === 'string'
			? v
					.split(',')
					.map((x) => x.trim())
					.filter((x) => x.length > 0)
			: [],
	set: (v) =>
		new Set(
			(typeof v === 'string' ? v.split(',') : [])
				.map((x) => String(x).trim())
				.filter((x) => x.length > 0),
		),
	date: (v) => new Date(v),
});

/** String helpers */
const kebabToCamel = (s) => String(s).replace(/-([a-z])/g, (_, c) => c.toUpperCase());
const camelToKebab = (s) =>
	String(s)
		.replace(/([a-z0-9])([A-Z])/g, '$1-$2')
		.replace(/([A-Z])([A-Z][a-z])/g, '$1-$2')
		.replace(/_/g, '-') // also normalize underscores
		.toLowerCase();

/** Normalize a CLI flag: strip leading dashes, apply alias, convert kebab->camel */
const normalizeFlag = (flag, aliases) => {
	const raw = String(flag).replace(/^-+/, '');
	const aliased = aliases[raw] || raw;
	return kebabToCamel(aliased);
};

/** Function parameter name extraction. */
const getParamNames = (fn) => {
	const stripDefaultAndDestructure = (p) => {
		const noDefault = p.replace(/=.*$/, '').trim();
		const id = noDefault.match(/^[A-Za-z_$][\w$]*$/);
		return id ? id[0] : null;
	};

	const src = Function.prototype.toString.call(fn);

	// Try standard function(...) form
	const paren = src.match(/\(([^)]*)\)/);
	if (paren) {
		return paren[1]
			.split(',')
			.map((s) =>
				s
					.replace(/\/\*.*?\*\//g, '')
					.replace(/\/\/.*$/g, '')
					.trim(),
			) // strip inline comments
			.filter(Boolean)
			.map(stripDefaultAndDestructure)
			.filter(Boolean)
			.slice(0, fn.length - 1); // exclude options bag
	}

	// Try single-arg arrow form: arg => ...
	const single = src.match(/^\s*([A-Za-z_$][\w$]*)\s*=>/);
	if (single) return [single[1]];

	return [];
};

/** Try to JSON-parse strings that look like objects/arrays. */
const tryParseJson = (raw) => {
	if (typeof raw !== 'string') return { ok: false };
	const trimmed = raw.trim();
	if (!/^[\[{].*[\]}]$/.test(trimmed)) return { ok: false };
	try {
		return { ok: true, value: JSON.parse(trimmed) };
	} catch {
		return { ok: false };
	}
};

/** Scalar type coercion when no custom translator is present. */
const coerceValue = (key, raw, translators) => {
	if (translators[key]) return translators[key](raw, key);

	// Preserve booleans eagerly
	if (raw === true || raw === false) return raw;

	// Arrays: coerce each element
	if (Array.isArray(raw)) {
		return raw.map((x) => coerceValue(key, x, translators));
	}

	// Objects pass through
	if (raw && typeof raw === 'object') return raw;

	// Non-strings pass through
	if (typeof raw !== 'string') return raw;

	// JSON objects/arrays
	const parsed = tryParseJson(raw);
	if (parsed.ok) return parsed.value;

	// Explicit boolean strings
	if (/^(true|false)$/i.test(raw)) return /^true$/i.test(raw);

	// Epoch millis / seconds (10 or 13 digits)
	if (/^\d{10,13}$/.test(raw)) {
		const ms = raw.length === 10 ? Number(raw) * 1000 : Number(raw);
		const d = new Date(ms);
		if (!Number.isNaN(d.getTime())) return d;
	}

	// Integers / Floats
	if (/^[+-]?\d+$/.test(raw)) return DEFAULT_TRANSLATORS.int(raw);
	if (/^[+-]?\d+\.\d+$/.test(raw)) return DEFAULT_TRANSLATORS.float(raw);

	// Comma lists
	if (raw.includes(',')) return DEFAULT_TRANSLATORS.array(raw);

	// ISO-like dates
	if (
		/^\d{4}-\d{2}-\d{2}(?:[T\s]\d{2}:\d{2}(:\d{2})?(?:\.\d+)?(?:Z|[+-]\d{2}:\d{2})?)?$/.test(raw)
	) {
		const d = DEFAULT_TRANSLATORS.date(raw);
		if (!Number.isNaN(d.getTime())) return d;
	}

	return raw;
};

/** Deep merge for plain objects (used when repeating the same dot-path). */
const isPlainObject = (v) =>
	v != null &&
	typeof v === 'object' &&
	(v.constructor === Object || Object.getPrototypeOf(v) === null);

const deepMerge = (a, b) => {
	if (!isPlainObject(a) || !isPlainObject(b)) return b;
	const out = { ...a };
	for (const [k, v] of Object.entries(b)) {
		if (k in out) out[k] = deepMerge(out[k], v);
		else out[k] = v;
	}
	return out;
};

/**
 * Assign a value into `target` at `key`, supporting dot-notation for nested objects.
 * Repeated flags on the same leaf become arrays (flattened when needed).
 */
const assignOption = (target, key, value) => {
	if (!key.includes('.')) {
		if (key in target) {
			const prev = target[key];
			if (isPlainObject(prev) && isPlainObject(value)) {
				target[key] = deepMerge(prev, value);
			} else {
				// Flatten on repetition (avoid nested arrays)
				const prevArr = Array.isArray(prev) ? prev : [prev];
				const valArr = Array.isArray(value) ? value : [value];
				target[key] = prevArr.concat(valArr);
			}
		} else {
			target[key] = value;
		}
		return;
	}

	const parts = key.split('.');
	let node = target;
	for (let i = 0; i < parts.length - 1; i++) {
		const p = parts[i];
		if (!isPlainObject(node[p])) node[p] = {};
		node = node[p];
	}
	const leaf = parts[parts.length - 1];

	if (leaf in node) {
		const prev = node[leaf];
		if (isPlainObject(prev) && isPlainObject(value)) {
			node[leaf] = deepMerge(prev, value);
		} else {
			// Flatten on repetition (avoid nested arrays)
			const prevArr = Array.isArray(prev) ? prev : [prev];
			const valArr = Array.isArray(value) ? value : [value];
			node[leaf] = prevArr.concat(valArr);
		}
	} else {
		node[leaf] = value;
	}
};

/**
 * Parse argv -> option bag with coercion, defaults, and rest args in "_".
 * Supports:
 *  - --flag
 *  - --flag value
 *  - --flag=value   (empty value becomes empty string)
 *  - --no-flag
 *  - negative numeric values after flags (e.g., --count -1)
 *  - -- terminator for positional args
 *  - repeated flags -> array (e.g., --tag a --tag b)
 *  - dot-notation for nested objects (e.g., --settings.welcomeMessage Hi)
 *  - JSON values for objects/arrays (e.g., --settings '{"a":1}')
 *  - "-" enables targeted bag mode: subsequent positional key/value pairs
 *    are collected into a bag to be placed into the next missing param slot
 *    (preferring "*Resource" parameters) during ordering.
 */
const parseArgs = (argv, { translators, aliases, defaults }) => {
	const opts = {};
	const rest = [];
	let bagMode = false;

	for (let i = 0; i < argv.length; i++) {
		const token = argv[i];

		// Lone "-" enables "targeted bag mode" for subsequent positionals
		if (token === '-') {
			bagMode = true;
			// mark for downstream processing
			opts.__bag = true;
			continue;
		}

		// everything after `--` is positional
		if (token === '--') {
			rest.push(...argv.slice(i + 1));
			break;
		}

		// --no-flag (negation)
		if (/^--no-/.test(token)) {
			const key = normalizeFlag(token.slice(5), aliases); // normalize before dot handling
			assignOption(opts, key, false);
			continue;
		}

		// --flag*, --flag=value, or --flag value
		if (/^--/.test(token)) {
			// --flag=value
			const eqIdx = token.indexOf('=');
			if (eqIdx !== -1) {
				const key = normalizeFlag(token.slice(0, eqIdx), aliases);
				const rawVal = token.slice(eqIdx + 1);
				// QoL: --flag= -> '' (empty string), not true
				const val = rawVal === '' ? '' : coerceValue(key.split('.').pop(), rawVal, translators);
				assignOption(opts, key, val);
				continue;
			}

			const key = normalizeFlag(token, aliases);
			const next = argv[i + 1];

			const looksLikeFlag = typeof next === 'string' && /^--[A-Za-z]/.test(next);
			const isNegativeNumber = typeof next === 'string' && /^-[0-9.]/.test(next);

			if (next != null && (!looksLikeFlag || isNegativeNumber)) {
				const val = coerceValue(key.split('.').pop(), next, translators);
				assignOption(opts, key, val);
				i++;
			} else {
				assignOption(opts, key, true);
			}
			continue;
		}

		// positional
		rest.push(token);
	}

	if (rest.length) opts._ = rest;

	// If bag mode was enabled via "-", consume positional pairs into a bag payload.
	// Example: "-" secret.prop "val"  -> { secret: { prop: "val" } }
	if (bagMode) {
		const bag = {};
		const items = opts._ || [];
		for (let i = 0; i < items.length; i += 2) {
			const key = String(items[i]);
			const rawVal = items[i + 1];
			const leaf = key.includes('.') ? key.split('.').pop() : key;
			const val = rawVal === undefined ? true : coerceValue(leaf, rawVal, translators);
			assignOption(bag, key, val);
		}
		opts.__bagPayload = bag;
	}

	// Apply defaults (respecting dot-notation in keys)
	for (const [k, dv] of Object.entries(defaults)) {
		const parts = k.split('.');
		let node = opts;
		for (let i = 0; i < parts.length - 1; i++) {
			const p = parts[i];
			if (!isPlainObject(node[p])) node[p] = {};
			node = node[p];
		}
		const leaf = parts[parts.length - 1];
		if (!(leaf in node)) {
			const rawDefault = typeof dv === 'function' ? dv() : dv;
			const coerced = coerceValue(leaf, rawDefault, translators);
			node[leaf] = coerced;
		}
	}

	// Unknowns are reported later in toOrderedArgs, where we know which ones were consumed.
	return opts;
};

/** Enumerate function names on instance + prototype (excluding constructor). */
const enumerateApiMethods = (api) => {
	const names = new Set();

	const proto = Object.getPrototypeOf(api);
	if (proto && proto !== Object.prototype) {
		for (const n of Object.getOwnPropertyNames(proto)) {
			if (n !== 'constructor' && typeof api[n] === 'function') names.add(n);
		}
	}

	return Array.from(names);
};

/** Resolve a method name using exact, kebab->camel, case-insensitive, and kebab-equivalence matching. */
const resolveMethod = (api, requestedName) => {
	const candidates = enumerateApiMethods(api);

	// 1) exact
	if (typeof api[requestedName] === 'function') return requestedName;

	// 2) kebab -> camel
	const camel = kebabToCamel(requestedName);
	if (typeof api[camel] === 'function') return camel;

	// 3) case-insensitive
	const lower = requestedName.toLowerCase();
	const ci = candidates.find((n) => n.toLowerCase() === lower);
	if (ci && typeof api[ci] === 'function') return ci;

	// 4) kebabized equality
	const requestedKebab = camelToKebab(requestedName).toLowerCase();
	const viaKebab = candidates.find((n) => camelToKebab(n).toLowerCase() === requestedKebab);
	if (viaKebab && typeof api[viaKebab] === 'function') return viaKebab;

	const suggestions = candidates
		.sort()
		.map((n) => `• ${camelToKebab(n)}`)
		.join('\n');

	throw new Error(`Method '${requestedName}' not found.\n\nAvailable commands:\n${suggestions}`);
};

/**
 * Order parsed options to match function parameter names. Reports unknowns.
 * Returns an array ready to spread into the target function call.
 *
 * Targeted bag mode:
 *  - If the user included a lone "-" token, positional key/value pairs were
 *    collected into opts.__bagPayload.
 *  - Insert that bag into the first missing parameter slot, preferring any
 *    parameter named with a "*Resource" suffix.
 */
const toOrderedArgs = (fn, opts, { aliases, onUnknownArg }) => {
	const names = getParamNames(fn);
	const hadBagMode = !!opts.__bag;

	// Extract and strip internal control flags before any further processing/reporting.
	const bagPayload =
		opts.__bagPayload && isPlainObject(opts.__bagPayload) ? opts.__bagPayload : undefined;
	if ('__bag' in opts) delete opts.__bag;
	if ('__bagPayload' in opts) delete opts.__bagPayload;

	const used = new Set();

	const ordered = names.map((name) => {
		if (!name) return undefined;

		// direct match
		if (name in opts) {
			used.add(name);
			return opts[name];
		}

		// try kebab alias (kebab-case param names map via aliases)
		const kebab = name.replace(/[A-Z]/g, (m) => `-${m.toLowerCase()}`);
		const aliasKey = aliases[kebab] ? kebabToCamel(aliases[kebab]) : null;

		if (aliasKey && aliasKey in opts) {
			used.add(aliasKey);
			return opts[aliasKey];
		}

		// try without Resource suffix (common in SDKs)
		if (name.endsWith('Resource')) {
			const short = name.slice(0, -8);
			if (short in opts) {
				used.add(short);
				return opts[short];
			}

			const shortKebab = short.replace(/[A-Z]/g, (m) => `-${m.toLowerCase()}`);
			const shortAliasKey = aliases[shortKebab] ? kebabToCamel(aliases[shortKebab]) : null;
			if (shortAliasKey && shortAliasKey in opts) {
				used.add(shortAliasKey);
				return opts[shortAliasKey];
			}
		}

		// undefined if not provided
		return undefined;
	});

	// If targeted bag mode was used, place the bag into the best slot:
	// 1) First missing param whose name ends with "Resource"
	// 2) Otherwise, the first missing param
	if (hadBagMode) {
		const placeAt =
			names.findIndex((n, i) => n && n.endsWith('Resource') && ordered[i] === undefined) !== -1
				? names.findIndex((n, i) => n && n.endsWith('Resource') && ordered[i] === undefined)
				: names.findIndex((_, i) => ordered[i] === undefined);

		if (placeAt !== -1) {
			ordered[placeAt] = bagPayload ?? {};
		} else {
			// no missing param slots; append as a last argument
			ordered.push(bagPayload ?? {});
		}
	}

	// consider a flag "used" if a consumed parent path matches
	const isUnderUsed = (full) => {
		for (const u of used) {
			if (full === u || full.startsWith(u + '.')) return true;
		}
		return false;
	};

	// Report unknown flags (ignore "_" and any that were consumed directly or via parent)
	const reportUnknown = (obj, parentKey = '') => {
		Object.keys(obj).forEach((k) => {
			if (k === '_' || k === '__bag') return;
			const full = parentKey ? `${parentKey}.${k}` : k;
			const isNested = isPlainObject(obj[k]) && !Array.isArray(obj[k]);
			if (isNested) {
				reportUnknown(obj[k], full);
			} else if (!isUnderUsed(full)) {
				onUnknownArg(full);
			}
		});
	};

	reportUnknown(opts);

	return ordered;
};

/** Lazily enumerate API method keys (kebab-cased) without leaking credentials or logging. */
const listCommandKeys = (apiName) => {
	try {
		const api = DUMMY_KITTY[apiName];
		if (!api) return [];
		return enumerateApiMethods(api).map((m) => camelToKebab(m));
	} catch {
		return [];
	}
};

/** Render per-command help text. */
const renderMethodHelp = (apiName, methodKebab, fn) => {
	const params = getParamNames(fn);

	const help = [
		`Usage: ${methodKebab} [options]`,
		'',
		`[options]:\n${params.map((p) => `${p}`).join('\n')}`,
	].join('\n');

	console.log(help);
};

const buildCliExports = (config) => {
	const {
		api: apiName,
		translators = {},
		aliases = {},
		defaults = {},
		onUnknownArg = (flag) => console.error(`Unknown argument: --${flag}`),
	} = config || {};

	if (!apiName || typeof apiName !== 'string') {
		throw new Error("buildCliExports: 'api' is required and must be a string.");
	}

	return new Proxy(
		{},
		{
			get(_t, rawKey) {
				if (rawKey === '__esModule') return false;
				if (typeof rawKey !== 'string') return undefined;

				const requested = rawKey; // accept kebab, camel, Pascal

				return async (argv) => {
					const input = Array.isArray(argv) ? argv : [];

					// If the user asked for help on this command, print it.
					// Accept both --help and -h anywhere in argv.
					const wantsHelp = input.some((t) => t === '--help' || t === '-h');

					return useChatKitty(async (chatkitty) => {
						const api = chatkitty[apiName];
						if (!api) {
							throw new Error(`ChatKitty instance has no property '${apiName}'.`);
						}

						const resolvedName = resolveMethod(api, requested);
						const fn = api[resolvedName];
						const methodKebab = camelToKebab(resolvedName);

						if (wantsHelp) {
							renderMethodHelp(apiName, methodKebab, fn);

							return {};
						}

						const opts = parseArgs(input, {
							translators,
							aliases,
							defaults,
							onUnknownArg,
						});

						const callArgs = toOrderedArgs(fn, opts, { aliases, onUnknownArg });
						return fn.apply(api, callArgs);
					});
				};
			},

			ownKeys() {
				// include a synthetic "help" command alongside actual API methods
				return ['help', ...listCommandKeys(apiName)];
			},

			getOwnPropertyDescriptor() {
				return { enumerable: true, configurable: true };
			},
		},
	);
};

module.exports = buildCliExports;

module.exports = buildCliExports;
