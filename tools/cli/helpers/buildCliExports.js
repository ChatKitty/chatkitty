/**
 * Build a dynamic proxy of CLI-callable methods for a given ChatKitty API surface.
 * - Normalizes command names (kebab/camel/Pascal).
 * - Parses argv into options (with type coercion, aliases, and defaults).
 * - Orders options to match target function parameter names.
 * - Reports unknown flags.
 *
 * Usage:
 *   const commands = buildCliExports({ api: 'ChannelsApi' });
 *   await commands['create-channel'](['--name', 'general', '--private']);
 */
const useChatKitty = require('./useChatKitty');
const { ChatKitty } = require('chatkitty');

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
		.toLowerCase();

/** Normalize a CLI flag: strip leading dashes, apply alias, convert kebab->camel */
const normalizeFlag = (flag, aliases) => {
	const raw = String(flag).replace(/^-+/, '');
	const aliased = aliases[raw] || raw;
	return kebabToCamel(aliased);
};

/** Robust-ish function parameter name extraction (best-effort). */
const getParamNames = fn => {
    const stripDefaultAndDestructure = p => {
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
			.map((s) => s.trim())
			.filter(Boolean)
			.map(stripDefaultAndDestructure)
			.filter(Boolean);
	}

	// Try single-arg arrow form: arg => ...
	const single = src.match(/^\s*([A-Za-z_$][\w$]*)\s*=>/);
	if (single) return [single[1]];

	return [];
};

/** Best-effort scalar type coercion when no custom translator is present. */
const coerceValue = (key, raw, translators) => {
	if (translators[key]) return translators[key](raw, key);

	// Preserve booleans eagerly
	if (raw === true || raw === false) return raw;

	// Non-strings pass through
	if (typeof raw !== 'string') return raw;

	// Explicit boolean strings
	if (/^(true|false)$/i.test(raw)) return /^true$/i.test(raw);

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

/** Parse argv -> option bag with coercion, defaults, and rest args in "_" */
const parseArgs = (argv, { translators, aliases, defaults }) => {
	const opts = {};
	const rest = [];

	for (let i = 0; i < argv.length; i++) {
		const token = argv[i];

		// --no-flag
		if (/^--no-/.test(token)) {
			const key = normalizeFlag(token.slice(5), aliases);
			opts[key] = false;
			continue;
		}

		// --flag or --flag value
		if (/^--/.test(token)) {
			const key = normalizeFlag(token, aliases);
			const next = argv[i + 1];

			if (next == null || /^--/.test(next)) {
				opts[key] = true;
			} else {
				opts[key] = next;
				i++;
			}
		} else {
			// positional
			rest.push(token);
		}
	}

	// Coerce values
	for (const [k, v] of Object.entries(opts)) {
		opts[k] = coerceValue(k, v, translators);
	}

	if (rest.length) opts._ = rest;

	// Apply defaults
	for (const [k, dv] of Object.entries(defaults)) {
		if (!(k in opts)) opts[k] = typeof dv === 'function' ? dv() : dv;
	}

	// Unknowns are reported later in toOrderedArgs, where we know which ones were consumed.
	return opts;
};

/** Enumerate function names on instance + prototype (excluding constructor). */
const enumerateApiMethods = api => {
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
const resolveMethod = (api, requestedName, apiName) => {
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
		.map((n) => `• ${n} (aka ${camelToKebab(n)})`)
		.join('\n');

	throw new Error(
		`❌ Method '${requestedName}' not found on ${apiName}.\nAvailable commands:\n${suggestions}`,
	);
};

/**
 * Order parsed options to match function parameter names. Reports unknowns.
 * Returns an array ready to spread into the target function call.
 */
const toOrderedArgs = (fn, opts, { aliases, onUnknownArg }) => {
	const names = getParamNames(fn);
	const used = new Set();

	const ordered = names.map((name) => {
		if (!name) return undefined;

		// direct match
		if (name in opts) {
			used.add(name);
			return opts[name];
		}

		// try kebab alias
		const kebab = name.replace(/[A-Z]/g, (m) => `-${m.toLowerCase()}`);
		const aliasKey = aliases[kebab] ? kebabToCamel(aliases[kebab]) : null;

		if (aliasKey && aliasKey in opts) {
			used.add(aliasKey);
			return opts[aliasKey];
		}

		// undefined if not provided
		return undefined;
	});

	// Report unknown flags (ignore "_" and any that were consumed)
	Object.keys(opts).forEach((k) => {
		if (k === '_' || used.has(k)) return;
		onUnknownArg(k);
	});

	return ordered;
};

/** Lazily enumerate API method keys (kebab-cased) without leaking credentials or logging. */
const listCommandKeys = apiName => {
	const api = DUMMY_KITTY[apiName];

	if (!api) return [];

	return enumerateApiMethods(api).map((m) => camelToKebab(m));
};

const buildCliExports = config => {
	const {
		api: apiName,
		translators = {},
		aliases = {},
		defaults = {},
		onUnknownArg = (flag) => console.error(`❌ Unknown argument: --${flag}`),
	} = config || {};

	if (!apiName || typeof apiName !== 'string') {
		throw new Error(
			"buildCliExports: 'api' is required and must be a string (e.g., 'ChannelsApi').",
		);
	}

	return new Proxy(
		{},
		{
			get(_t, rawKey) {
				if (rawKey === '__esModule') return false;
				if (typeof rawKey !== 'string') return undefined;

				const requested = rawKey; // accept kebab, camel, Pascal

				return async function cliWrapper(argv) {
					const input = Array.isArray(argv) ? argv : [];
					return useChatKitty(async (chatkitty) => {
						const api = chatkitty[apiName];
						if (!api) {
							throw new Error(`❌ ChatKitty instance has no property '${apiName}'.`);
						}

						const resolvedName = resolveMethod(api, requested, apiName);
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
