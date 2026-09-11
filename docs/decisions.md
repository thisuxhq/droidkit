# Decisions

Load-bearing choices, written down so they stop being re-argued. Add a row when a decision changes how items are written. Reverse one only with a new entry that says why.

| # | Decision | Status |
| --- | --- | --- |
| 1 | Name: DroidKit | Decided |
| 2 | Composable prefix: `App`, configurable | Decided |
| 3 | Placeholder package and import rewrite | Decided |
| 4 | Theme is `MaterialTheme` + extra locals, versioned | Decided |
| 5 | One schema field: `type` | Decided |
| 6 | Tests opt-in, previews default, screenshot tests upstream | Decided |
| 7 | Blocks are stateless; no ViewModel, Nav, or DI | Decided |
| 8 | Experimental Material APIs declared, not hidden | Decided |
| 9 | License: Apache-2.0 | Decided |
| 10 | Android first, `platform` tagged from day one | Decided |
| 11 | Website previews are generated screenshots | Decided |
| 12 | `init` writes agent instructions into the consumer project | Decided |
| 13 | Color/type: `MaterialTheme.*`; spacing/motion/elevation: `AppTheme.*` | Decided |
| 14 | Dogfood / taste-test target is `:apps:showcase` | Decided |
| 18 | Default theme is ink on paper | Decided |

## 1. Name: DroidKit

"DroidUI" collided with `iptux/DroidUi` (Python/SL4A), a GitHub org `Droidui`, the npm package `droid-ui`, and the commercial DroidUX. DroidKit is clear of all four. Repo: `thisuxhq/droidkit`. CLI: `droidkit`.

## 2. Composable prefix: `App`, configurable

Copied composables are named `AppButton`, `AppTextField`, `AppTheme`, `AppSpacing`. Not `Button` — it collides with `androidx.compose.material3.Button`, which our own button wraps. Not `DroidButton` — it leaks the brand into code the developer is supposed to own.

`App` is neutral, common in Android codebases, and reads as "this project's button".

Scope: the prefix applies to **theme and components** — the things that shadow Material or Foundation names (`AppButton`, `AppTextField`, `AppAvatar`, `AppOtpInput`, `AppTheme`). **Patterns and blocks** keep plain names (`EmptyState`, `PermissionRequest`, `SettingsScreen`, `LoginScreen`) because they collide with nothing and read better at call sites. A pattern or block that does collide gets the prefix.

`droidkit init --prefix Acme` rewrites the prefix on install. The registry source uses `App`. See [config.md](config.md).

## 3. Placeholder package and import rewrite

Registry source declares placeholder packages:

```text
com.droidkit.registry.theme
com.droidkit.registry.components
com.droidkit.registry.patterns
com.droidkit.registry.blocks
```

On install the CLI rewrites `com.droidkit.registry.<category>` → `<package>.<paths.category>` and, if a custom prefix is set, `App<Name>` → `<prefix><Name>` in identifiers and file names. Cross-item imports (EmptyState → AppButton) therefore resolve without special cases.

Registry Kotlin never imports anything outside `com.droidkit.registry.*`, Compose, Material 3, AndroidX, and declared `dependencies`. Enforced by a lint in `:registry`.

## 4. Theme is `MaterialTheme` + extra locals, versioned

`AppTheme` calls `MaterialTheme` with our `ColorScheme`, `Typography`, and `Shapes`, then provides `LocalSpacing`, `LocalMotion`, and `LocalElevation`. Material components inside still theme correctly. Items read type and color through `MaterialTheme.*` and read spacing/motion/elevation through `AppTheme.*`.

The theme is a **stable contract**. Adding a token bumps `themeVersion` in the theme item; every item declares the `themeVersion` it needs. `droidkit add` warns when the installed theme is older than an item requires.

Hardcoded dp is allowed only where the number **is** the opinion (button height 52 dp, corner 14 dp) and must be a named `private val` at the top of the file. Layout spacing uses tokens.

## 5. One schema field: `type`

`type` ∈ `theme | component | pattern | block | recipe`. `category` is gone. Schema: [`registry/item.schema.json`](../registry/item.schema.json). CI validates every `registry.json`.

## 6. Tests opt-in, previews default, screenshot tests upstream

Compose UI tests need `androidTest` + `ui-test-junit4` + `ui-test-manifest` or Robolectric. Copying tests uninvited is friction. `droidkit add` copies `*.kt` and `*Preview.kt`; `--with-tests` adds `*Test.kt` and the test dependencies.

Upstream, every preview is also a **Compose Preview Screenshot Test**. That makes "every state previewed" a CI gate and produces the images the website uses (decision 11).

## 7. Blocks are stateless; no ViewModel, Nav, or DI

A block is a `@Composable` screen that takes state and callbacks. It defines its own plain `data class` state (`SettingsState`) in the same file. It never depends on ViewModel, Navigation, Hilt, Koin, or a networking client. The developer wires it.

This keeps blocks usable in any architecture and keeps "no runtime" true.

## 8. Experimental Material APIs declared, not hidden

Avoid `@ExperimentalMaterial3Api` where a stable alternative exists. When an item needs one (e.g. `TopAppBar`), the item lists it in `experimentalApis` and the copied file carries the `@OptIn` itself. `view` and `add` print the list. Nothing surprises the developer at compile time.

## 9. License: Apache-2.0

"You own the code" needs a permissive license. Apache-2.0 is the Android norm and includes a patent grant. Copyright holder: ThisUX. `LICENSE` at repo root; copied files carry no header (they belong to the developer).

## 10. Android first, `platform` tagged from day one

Compose Multiplatform is out of scope for V0. Every item still declares `platform: "android" | "common"`. Items with no Android-only imports are written `commonMain`-compatible where it costs nothing. The field is free now and expensive to retrofit.

## 11. Website previews are generated screenshots

Compose does not run in a browser without a wasm build. The playground renders PNGs produced by the upstream screenshot tests (decision 6), one per preview state, light and dark. No hand-maintained state list. A wasm showcase is a later option, not a dependency.

## 12. `init` writes agent instructions into the consumer project

`droidkit init` adds a short `droidkit` section to the project's `AGENTS.md` (or creates `.agents/skills/droidkit/SKILL.md`): what is installed, the prefix, the four categories, and the design rules endpoint. Agents working in that repo then use the installed components instead of inventing new ones.

## 13. Color/type: `MaterialTheme.*`; extra tokens: `AppTheme.*`

Items read color, type, and shape from `MaterialTheme.*`. They read spacing, motion, and elevation from `AppTheme.*`. No parallel color or type system.

## 14. Dogfood / taste-test target is `:apps:showcase`

Taste is tested in this repo's showcase app, not an external product. `:apps:showcase` builds and runs every registered item.

## 15. States are declared, previewed, and screenshotted from one source

`registry.json` → `states` is the UX state matrix, decided before code. Every state must have exactly one `@Preview(name = "<item> <state>")`; the build generates one screenshot test per state from that preview and stitches the goldens into a contact sheet. No state can be declared without being shown, and no preview can exist without being a declared state. Enforced by `:registry:lintRegistryStates`. See [verification.md](verification.md).

## 16. Items carry `status` and `sources`

`status` is `draft` → `review` → `ready`; the website publishes only `ready`. `sources` lists the official docs (`kb://` URLs from `android docs`) the item was built against, so a reviewer can check trigger topics were grounded rather than guessed. Both are required for `ready`.

## 17. The author never reviews; the gates run before anyone looks

Coding agents write most of this code. An item is reviewed by a fresh-context agent that tries to refute it, then run on a device, then judged from a contact sheet by a human. The deterministic gates (imports, states, conventions, tests, screenshots) run first so the humans and reviewers only spend attention on what machines cannot check. Three skills, three commands (`/item`, `/review`, `/device`); no more unless a phase demands it. See [verification.md](verification.md).

## 18. Default theme is ink on paper

The default `AppTheme` colour opinion is **ink on paper**: near-black ink, white paper, muted gray text, quiet surfaces. `primary` maps to ink so Material filled actions become the dark pill. `tertiary` is a reserved signal (progress, location) — not a second brand and not a button. Destructive is red text, not a red fill. No green identity.

This does not change Decisions #4 or #13: callers still read colour, type, and shape from `MaterialTheme.*` and extras from `AppTheme.*`. Token *roles* are unchanged (`THEME_VERSION` stays 1). Token *values* are the opinion.

The language was distilled from quiet mobility-app UI, not cloned. No third-party branding, wordmarks, or names leak into tokens, composables, or the prefix (`App` stays). See [design-system.md](design-system.md#visual-opinion).
