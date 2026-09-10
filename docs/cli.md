# CLI

The CLI is how humans and agents install DroidKit. It is not the first thing to build. It is the thing that turns a Compose library into a system. See [Roadmap](roadmap.md).

## Commands

| Command | Job |
| --- | --- |
| `droidkit init` | Write `droidkit.json`, theme, `ui/`, and agent instructions |
| `droidkit add <item>` | Resolve deps, copy files, add Gradle deps if needed |
| `droidkit search <query>` | Find components, patterns, blocks, recipes |
| `droidkit view <item>` | Show metadata, files, examples |
| `droidkit diff <item>` | Local vs upstream |
| `droidkit update <item>` | Offer a diff, never silent overwrite |

Later: `droidkit create` for templates (SaaS companion, AI app, finance, social, productivity). That is Phase 5.

## Init

```bash
droidkit init
```

Creates:

```text
app/
├── droidkit.json
└── src/main/java/com/example/app/ui/
    ├── theme/
    │   ├── Color.kt
    │   ├── Theme.kt
    │   ├── Typography.kt
    │   └── Spacing.kt
    └── components/
```

and appends a `## DroidKit` section to the project's `AGENTS.md` (or writes `.agents/skills/droidkit/SKILL.md`) so coding agents use the installed components instead of inventing new ones.

Flags:

```bash
droidkit init --package com.acme.app.ui   # default: detected from Gradle
droidkit init --prefix Acme               # default: App  → AcmeButton, AcmeTheme
droidkit init --style clean               # once more than one style exists
droidkit init --with-tests                # copy *Test.kt by default
```

Detect the app package from the Gradle project. Do not make the developer type `com.example.app` if the project already knows it. Everything `init` decides is written to [`droidkit.json`](config.md); every other command reads it.

## Add

```bash
droidkit add button
droidkit add otp-input
droidkit add empty-state
droidkit add search
droidkit add auth
```

Flow:

```text
1. Read droidkit.json
2. Fetch registry item and resolve registryDependencies
3. Check themeVersion against the installed theme
4. Show the plan: files, Maven deps, experimental APIs
5. Copy source + preview (tests with --with-tests), rewriting package and prefix
6. Add Gradle dependencies if needed
7. Format; record revision in droidkit.json
```

Result:

```text
app/src/main/java/com/example/app/ui/
├── theme/
└── components/
    ├── AppButton.kt
    └── AppButtonPreview.kt
```

```kotlin
import com.example.app.ui.components.AppButton
```

Per-call overrides: `--with-tests`, `--no-tests`, `--dry-run`.

## Search and view

```bash
droidkit search payment
droidkit view paywall
```

`view` prints description, use/avoid, files, deps, and a short example. This is the same payload MCP serves to agents. Keep them in sync.

## Diff and update

The developer may have edited the file. Updates are a review, not a replace.

```bash
droidkit diff button
droidkit update button
```

```diff
- old implementation
+ new upstream implementation
```

`droidkit.json` holds the installed revision per item. `diff` is three-way: local vs installed (your edits), installed vs latest (upstream), local vs latest (what update would do). `update` applies only when local == installed; otherwise it prints the diff and stops. See [Config](config.md).

## What the CLI must not do

- Require a DroidKit Gradle plugin to compile the app
- Rewrite the developer's theme without asking
- Fetch from anywhere except the configured registry
- Hide Maven dependencies or experimental opt-ins. If `otp-input` needs a library or `TopAppBar` needs `@OptIn`, say so in the plan
- Copy tests uninvited
- Target XML layouts

## Shape of the binary

`npx droidkit` is fine as a distribution channel for JS-native developers and agents. A native binary (`droidkit`) should exist too. Both call the same registry. The Android project remains Gradle/Kotlin.

V0 can wait on the CLI entirely. Ship the registry and showcase first so `add` has something worth adding.
