# Verification

Most DroidKit code is written by coding agents. An agent that cannot see its output will ship a spinner that is invisible and call the task done. This document is the loop that stops that. It is a product decision, not tooling trivia: the loop is why "a DroidKit item" means something.

Three ideas:

1. **Declared = previewed = screenshotted.** `registry.json` → `states` is the state matrix. Every state must have a `@Preview`; every preview becomes a golden PNG; the PNGs become the contact sheet and the website. One source of truth, enforced by Gradle.
2. **The author never reviews.** A fresh-context agent tries to refute the item. Blockers go back; nothing is approved by the agent that wrote it.
3. **Cheap gates first, taste last.** Deterministic checks run on every edit. A human looks at one image per item at the end. That look is the only gate that cannot be automated, so everything before it exists to make it ten seconds long.

## The loop

```text
L0  Gates            ./gradlew :registry:check        seconds    every edit
L1  Look             read the golden PNGs             a minute   every edit
L2  Fresh eyes       /review <item>                   minutes    before every commit to registry/
L3  Device           /device <item>                   minutes    before status: ready
L4  Taste            contact sheet, a human           10 s       before status: ready
```

### L0 — Gates (`./gradlew :registry:check`)

| Task | Fails when |
| --- | --- |
| `lintRegistryImports` | copied Kotlin imports anything outside `com.droidkit.registry.*`, Compose, AndroidX, JDK |
| `lintRegistryStates` | a declared state has no `@Preview(name = "<item> <state>")`, a preview is not a declared state, or a preview is `private` |
| `lintRegistryConventions` | missing `App` prefix on theme/component composables · `modifier` not the first optional param or not defaulting to `Modifier` · inline `.dp` literal in a source file · `!` or "Oops" in UI copy |
| `testDebugUnitTest` | behaviour tests fail |
| `validateDebugScreenshotTest` | a golden PNG differs from the rendered preview |
| CI: `check-jsonschema` | any `registry.json` violates `registry/item.schema.json` |

Screenshot tests are generated, not written. `generateRegistryScreenshotTests` reads `states`, finds the matching preview, and emits one `@PreviewTest` wrapper per state into the `screenshotTest` source set carrying the original `@Preview` arguments (dark, font scale, width). The copied preview file stays free of test-tool imports. Rules live in `buildSrc/src/main/kotlin/droidkit/Registry.kt`.

```bash
./gradlew :registry:updateDebugScreenshotTest    # record goldens after an intentional visual change
./gradlew :registry:contactSheets                # build/contact-sheets/<item>.png
```

Goldens are committed under `registry/src/screenshotTestDebug/reference/`.

### L1 — Look

The author reads every golden for the item and the contact sheet, and walks the state matrix row by row. A "loading" image with no visible spinner is a lie. This step alone caught, on the first item it ran on, a loading state rendered in disabled colours and a long label clipped by a fixed height.

### L2 — Fresh eyes (`/review`)

A subagent with no conversation history loads `droidkit-review`, runs the gates, reads the code, PNGs, tests, and metadata, cites official docs for any behavioural claim, and returns a structured verdict: blockers, shoulds, taste, deletions, unverified. Zero blockers and every PNG read → `ready`. Two rounds without `ready` means the design is wrong; it comes back to a human.

### L3 — Device (`/device`)

The showcase app is the harness. `droidkit://item/<name>` opens one item; `tools/device/env.sh` flips dark, font scale, RTL, animator scale, TalkBack; `tools/device/check-layout.sh` reads the `android layout` dump and fails on any clickable under 48 dp or without a label; screenshots are captured and read. Items may ship a `journey.xml` the android CLI runs as a scripted test. Required for anything with input, IME, focus, motion, dialogs, sheets, or scrolling.

### L4 — Taste

Open `build/contact-sheets/<item>.png`. Does it look like an app you would want to have built? That reaction is the gate. Nothing here replaces it.

## Grounding in official docs

Compose moves faster than any model's training data. The `android` CLI ships an offline index of developer.android.com:

```bash
android docs search "minimumInteractiveComponentSize"
android docs fetch kb://android/develop/ui/compose/accessibility/api-defaults
```

Authors search before touching a trigger topic (semantics, Material params, touch targets, IME/autofill, focus, animation, insets, adaptive, back, lazy lists, experimental APIs) and record the URLs in `registry.json` → `sources`. Reviewers must cite a `kb://` URL for any blocker that claims "this is how Compose behaves"; uncited claims are downgraded to taste. Docs beat memory; silence in the docs is reported, not filled with a guess.

## Status

`registry.json` → `status`:

| Status | Meaning | To leave it |
| --- | --- | --- |
| `draft` | being built | L0 green, state matrix complete, author has looked (L1) |
| `review` | awaiting fresh eyes and device | `/review` returns `ready`, `/device` clean, human has seen the contact sheet |
| `ready` | publishable | — |

The showcase shows the badge. The website publishes only `ready`.

## The skills

Three skills, three commands. Knowledge lives in the skills, orchestration in the prompts, truth in Gradle.

| | Skill | Command | Use when |
| --- | --- | --- | --- |
| Build | `.agents/skills/droidkit-item` | `/item <name> [change]` | creating or revising anything under `registry/` or `core/` |
| Refute | `.agents/skills/droidkit-review` | `/review <name>` | before every commit touching `registry/`; always a fresh subagent |
| Test | `.agents/skills/droidkit-device` | `/device <name>` | after gates, for input / IME / motion / sheets / scroll; before `ready` |

Perf, accessibility, copy, and metadata are sections of `droidkit-item` and rows in the review rubric, not separate skills. Splitting them would mean an agent loads one and skips the rest.

## What the loop does not do

- It does not judge taste. A human does, from the contact sheet.
- It does not measure frame time. Until `:apps:benchmark` exists, perf is a review rubric row and a slow swipe on the device.
- It does not run TalkBack assertions. Semantics tests and the layout dump cover structure; a human with TalkBack on covers the rest.
