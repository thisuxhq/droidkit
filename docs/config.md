# Consumer config: `droidkit.json`

Written by `droidkit init` at the Gradle module root (usually `app/`). Read by every other command. Without it, `diff` and `update` cannot know what was installed or where.

```json
{
  "$schema": "https://droidkit.dev/schema/config.json",
  "version": 1,
  "registry": "https://registry.droidkit.dev",
  "style": "clean",
  "package": "com.acme.app.ui",
  "prefix": "App",
  "sourceSet": "app/src/main/java",
  "testSourceSet": "app/src/androidTest/java",
  "paths": {
    "theme": "theme",
    "components": "components",
    "patterns": "patterns",
    "blocks": "blocks"
  },
  "withTests": false,
  "installed": {
    "theme": {
      "revision": "3f9c2a1",
      "themeVersion": 1,
      "files": ["theme/Color.kt", "theme/Theme.kt", "theme/Typography.kt", "theme/Spacing.kt"]
    },
    "button": {
      "revision": "a1b2c3d",
      "files": ["components/AppButton.kt", "components/AppButtonPreview.kt"]
    }
  }
}
```

## Fields

| Field | Meaning |
| --- | --- |
| `version` | Config schema version |
| `registry` | Base URL. Only source the CLI fetches from |
| `style` | Visual style chosen at init |
| `package` | Root package for copied code. Detected from Gradle; override with `--package` |
| `prefix` | Composable prefix. Default `App`. See [decisions #2](decisions.md#2-composable-prefix-app-configurable) |
| `sourceSet` / `testSourceSet` | Where Kotlin lands. Detected; overridable |
| `paths` | Sub-packages under `package`, one per category |
| `withTests` | Default for `add`. `--with-tests` / `--no-tests` override per call |
| `installed` | Item → registry revision + files written. Basis for `diff` and `update` |

## Rewrite rules

Applied to every copied file:

1. `package com.droidkit.registry.<category>` → `package <package>.<paths.category>`
2. `import com.droidkit.registry.<category>.X` → `import <package>.<paths.category>.X`
3. If `prefix != "App"`: identifier and file name `App<Name>` → `<prefix><Name>`

Nothing else is touched. The file is otherwise byte-identical to the registry, so `diff` is meaningful.

## Drift detection

`installed[item].revision` is the registry revision at install time. `diff` fetches that revision and the latest, then compares three ways: local vs installed (developer edits), installed vs latest (upstream changes), local vs latest (what an update would do).

`update` applies cleanly only when local == installed. Otherwise it prints the three-way diff and stops.

## Agent instructions

`init` also appends a `## DroidKit` section to the project's `AGENTS.md` (or writes `.agents/skills/droidkit/SKILL.md`): installed items, prefix, categories, and a pointer to `droidkit view` / MCP. See [AI](ai.md).
