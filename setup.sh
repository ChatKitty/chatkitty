#!/usr/bin/env sh
# OS-agnostic installer: ensures project dependencies are available.
# Works in POSIX shells (macOS, Linux, WSL, Git-Bash).

set -eu

log() { printf "%s\n" "$*"; }
err() { printf "Error: %s\n" "$*" >&2; }
have() { command -v "$1" >/dev/null 2>&1; }

ensure_pnpm() {
  if have pnpm; then
    return 0
  fi

  log "pnpm not found. Attempting to install…"

  # 1) Corepack (best if Node >= 16.13 is present)
  if have corepack; then
    log "Using corepack to activate pnpm…"
    # Some shells don't support 'hash -r'; this is safe to ignore if unsupported.
    corepack enable >/dev/null 2>&1 || true
    if corepack prepare pnpm@latest --activate; then
      hash -r 2>/dev/null || true
      if have pnpm; then return 0; fi
      err "corepack prepared pnpm but it's not on PATH yet; continuing…"
    else
      err "corepack prepare failed; continuing…"
    fi
  fi

  # 2) npm (global if writable, else sudo, else user prefix)
  if have npm; then
    log "Using npm to install pnpm…"
    prefix="$(npm config get prefix 2>/dev/null || printf "")"
    can_write=0
    # Try common writable checks
    if [ -n "${prefix}" ] && [ -w "${prefix}" ]; then
      can_write=1
    elif [ -n "${prefix}" ] && [ -w "${prefix}/bin" ] 2>/dev/null; then
      can_write=1
    fi

    if [ "$can_write" -eq 1 ]; then
      npm install -g pnpm
    else
      if have sudo; then
        if ! sudo npm install -g pnpm; then
          err "sudo global install failed; trying user-local prefix…"
          mkdir -p "$HOME/.local"
          npm install -g pnpm --prefix "$HOME/.local"
          export PATH="$HOME/.local/bin:$PATH"
        fi
      else
        log "No sudo; installing pnpm to user-local prefix…"
        mkdir -p "$HOME/.local"
        npm install -g pnpm --prefix "$HOME/.local"
        export PATH="$HOME/.local/bin:$PATH"
      fi
    fi

    # Ensure npm global bin is on PATH for this session
    npm_bin="$(npm bin -g 2>/dev/null || printf "")"
    if [ -n "${npm_bin}" ]; then
      export PATH="${npm_bin}:$PATH"
    fi
    hash -r 2>/dev/null || true
    if have pnpm; then return 0; fi
    err "npm completed but pnpm still not found on PATH; continuing…"
  fi

  # 3) Official install script (curl/wget)
  install_script_url="https://get.pnpm.io/install.sh"
  if have curl; then
    log "Using pnpm official install script via curl…"
    # shellcheck disable=SC3010
    curl -fsSL "$install_script_url" | sh -
  elif have wget; then
    log "Using pnpm official install script via wget…"
    wget -qO- "$install_script_url" | sh -
  else
    err "Neither corepack, npm, curl, nor wget are available to install pnpm."
    return 1
  fi

  # Common post-install locations for the official script
  for candidate in "$HOME/.local/share/pnpm" "$HOME/.pnpm" "$HOME/Library/pnpm"; do
    if [ -d "$candidate" ]; then
      export PATH="$candidate:$PATH"
    fi
  done
  hash -r 2>/dev/null || true

  if have pnpm; then
    return 0
  fi

  err "pnpm installation finished but 'pnpm' is not on PATH."
  err "You may need to restart your shell or add pnpm's bin dir to PATH."
  return 1
}

main() {
  if ! ensure_pnpm; then
    err "Failed to install pnpm automatically."
    exit 1
  fi

  log "Running 'pnpm install'…"
  pnpm install
}

main "$@"
