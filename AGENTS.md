# AGENTS.md

Canonical instructions for coding agents in this repo. `CLAUDE.md` is a symlink here.

## Product

**DroidKit** is the open UI system for building polished Android apps.

Material gives primitives. DroidKit gives product-quality UI. Developers **own the copied Kotlin** — this is not a Maven component library.

5-second explanation (not the identity): shadcn-style open components for Android, with stronger UX opinions.

Long-term identity: *the open UI system for building polished Android apps.*

Pitch: **Build Android apps that feel designed, not assembled.**

## Status

Pre-v0. **Phase 1 — Foundation.** Theme + registry + showcase first. Do not build the CLI until 15–20 items feel great.

Compose-only. No XML. No proprietary runtime.

## Read first

Product decisions live in `docs/`. Do not contradict them.

1. [docs/introduction.md](docs/introduction.md)
2. [docs/principles.md](docs/principles.md)
3. [docs/architecture.md](docs/architecture.md)
4. [docs/roadmap.md](docs/roadmap.md)

Settled choices: [docs/decisions.md](docs/decisions.md). Do not re-argue them in code; add a decision entry if one must change.

Index: [docs/README.md](docs/README.md)

## Non-negotiables

If a change fights one of these, the change loses.

- **Own the code.** Registry items are copyable Kotlin. Consumers import their package, not `com.droidkit.*`.
- **Prefix is `App`.** `AppButton`, `AppTheme`, `AppSpacing`. Never bare `Button` (collides with Material), never `DroidButton` (brand leak).
- **No DroidKit runtime** unless something is impossible without it. Compose + Material 3 + AndroidX. Extra libraries only when required (e.g. Coil).
- **APIs are Compose.** No `ButtonConfiguration` objects. Defaults + escape hatches (`modifier`, colors, shape, slots) on the same function.
- **Product decisions in the component.** `PasswordField` / `EmptyState` / `DestructiveDialog`, not restyled primitives with no behaviour.
- **Taste over volume.** Do not add the 31st item to look busy. Finish the ones we have.
- **Four catalog words:** components (UI), patterns (UX), blocks (features), templates (apps). Do not flatten them.
- **Quality is the bar.** TalkBack, dynamic type, RTL, keyboard, dark mode, large screens, 48 dp targets, previews, tests. Fancy motion that janks a `LazyColumn` does not ship.
- **AI is a first-class user.** Registry metadata (`description`, `avoidWhen`, `aiHints`) is architecture. Keep it in sync with the Kotlin.

## What to build now

Phase 1 only:

```text
core/theme          Color, type, spacing, shape, motion, AppTheme
core/foundation     modifiers, haptics, accessibility
registry/           copyable items with Preview + Test + registry.json
apps/showcase       uses every item
```

Not now: CLI, MCP, Figma, XML, Maven publish, namespaced third-party registries, extra visual styles.

Target: ~10 primitives, ~10 components, ~5 patterns. See [docs/catalog.md](docs/catalog.md) and [docs/anatomy.md](docs/anatomy.md).

## Conventions

- Copied code must read like an app file, not like a framework.
- Prefer `AppTheme.spacing.md` over generated token names.
- Sit on Material 3; drop to Foundation only when Material fights the UX.
- Each registry item is a folder: `App<Name>.kt`, `App<Name>Preview.kt`, `App<Name>Test.kt`, `registry.json`.
- `registry.json` validates against `registry/item.schema.json`. Use `type`, not `category`. Fill `platform`, `themeVersion`, `experimentalApis`.
- Registry source declares `package com.droidkit.registry.<category>`; the CLI rewrites it. Import only `com.droidkit.registry.*`, Compose, Material 3, AndroidX, and declared `dependencies`.
- `AppTheme` wraps `MaterialTheme`. Read color/type/shape from `MaterialTheme.*`, spacing/motion from `AppTheme.*`. Hardcoded dp only where the number is the opinion, as a named `private val`.
- Blocks are stateless: state + callbacks in, own `data class`, no ViewModel/Nav/DI. Experimental Material opt-ins go on the file and in `experimentalApis`.
- Every preview is also a screenshot test.
- Docs are markdown in `docs/`. Update them when you change a product decision.
- Keep `AGENTS.md` canonical. Do not create a second instructions file with different rules.

## Verification

Until Gradle modules exist: make sure docs still agree with the code you add, and every new registry item has preview + test + metadata.

Once the Android project exists: assemble showcase, run the item's Compose tests, and check the preview states listed in [docs/quality.md](docs/quality.md).
