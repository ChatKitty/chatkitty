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
			.filter(Boolean);
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
 *  - "-" toggles explicit bag mode (treat all flags as a single params object)
 */
const parseArgs = (argv, { translators, aliases, defaults }) => {
	const opts = {};
	const rest = [];

	for (let i = 0; i < argv.length; i++) {
		const token = argv[i];

		// Lone "-" toggles "bag mode" (treat all flags as a single params object)
		if (token === '-') {
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
 * If the user included a lone "-" token, pass the entire flag bag (minus "_")
 * as the single argument (explicit "bag mode").
 */
const toOrderedArgs = (fn, opts, { aliases, onUnknownArg }) => {
	const names = getParamNames(fn);
	const bagMode = !!opts.__bag;

	// Strip internal control flag before any further processing/reporting.
	if ('__bag' in opts) delete opts.__bag;

	// If user explicitly requested bag mode via "-", pass the whole bag.
	if (bagMode) {
		const { _, ...bag } = opts;
		return [bag];
	}

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

		// undefined if not provided
		return undefined;
	});

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

const buildCliExports = (config) => {
	const {
		api: apiName,
		translators = {},
		aliases = {},
		defaults = {},
		onUnknownArg = (flag) => console.error(`Unknown argument: --${flag}`),
	} = config || {};

	if (!apiName || typeof apiName !== 'string') {
		throw new Error(
			"buildCliExports: 'api' is required and must be a string.",
		);
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
					return useChatKitty(async (chatkitty) => {
						const api = chatkitty[apiName];
						if (!api) {
							throw new Error(`ChatKitty instance has no property '${apiName}'.`);
						}

						const resolvedName = resolveMethod(api, requested);
						const fn = api[resolvedName];

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
				return listCommandKeys(apiName);
			},

			getOwnPropertyDescriptor() {
				return { enumerable: true, configurable: true };
			},
		},
	);
};

module.exports = buildCliExports;
