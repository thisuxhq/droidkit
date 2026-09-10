# CLI

The CLI is how humans and agents install DroidUI. It is not the first thing to build. It is the thing that turns a Compose library into a system. See [Roadmap](roadmap.md).

## Commands

| Command | Job |
| --- | --- |
| `droidui init` | Write theme + `ui/` into the app |
| `droidui add <item>` | Resolve deps, copy files, add Gradle deps if needed |
| `droidui search <query>` | Find components, patterns, blocks, recipes |
| `droidui view <item>` | Show metadata, files, examples |
| `droidui diff <item>` | Local vs upstream |
| `droidui update <item>` | Offer a diff, never silent overwrite |

Later: `droidui create` for templates (SaaS companion, AI app, finance, social, productivity). That is Phase 5.

## Init

```bash
droidui init
```

Creates:

```text
app/src/main/java/com/example/app/ui/
├── theme/
│   ├── Color.kt
│   ├── Theme.kt
│   ├── Typography.kt
│   └── Spacing.kt
└── components/
```

Optional style flag, once more than one style exists:

```bash
droidui init --style clean
droidui init --style expressive
droidui init --style minimal
```

Detect the app package from the Gradle project. Do not make the developer type `com.example.app` if the project already knows it. Rewrite package names in copied files to match.

## Add

```bash
droidui add button
droidui add otp-input
droidui add empty-state
droidui add search
droidui add auth
```

Flow:

```text
1. Fetch registry item
2. Read registryDependencies and Maven dependencies
3. Check the project (package, existing files, Gradle)
4. Show the file plan
5. Copy Kotlin (rewritten to the app package)
6. Install Gradle dependencies if needed
7. Format
```

Result:

```text
app/src/main/java/com/example/app/ui/
├── theme/
└── components/
    └── Button.kt
```

```kotlin
import com.example.app.ui.components.Button
```

## Search and view

```bash
droidui search payment
droidui view paywall
```

`view` prints description, use/avoid, files, deps, and a short example. This is the same payload MCP serves to agents. Keep them in sync.

## Diff and update

The developer may have edited the file. Updates are a review, not a replace.

```bash
droidui diff button
droidui update button
```

```diff
- old implementation
+ new upstream implementation
```

If the local file is unchanged from the installed revision, update can apply cleanly. If it drifted, show the diff and stop.

## What the CLI must not do

- Require a DroidUI Gradle plugin to compile the app
- Rewrite the developer's theme without asking
- Fetch from anywhere except the configured registry
- Hide Maven dependencies. If `otp-input` needs a library, say so in the plan
- Target XML layouts

## Shape of the binary

`npx droidui` is fine as a distribution channel for JS-native developers and agents. A native binary (`droidui`) should exist too. Both call the same registry. The Android project remains Gradle/Kotlin.

V0 can wait on the CLI entirely. Ship the registry and showcase first so `add` has something worth adding.
