# Architecture

The source repo is a registry and a development environment. The consumer project is a handful of Kotlin files they own. Optimize for the second one.

## Five layers

```text
┌─────────────────────────────┐
│         App Blocks          │
│ onboarding / auth / chat    │
│ settings / paywall / search │
├─────────────────────────────┤
│      Smart Components       │
│ OTP / EmptyState / Picker   │
│ SearchBar / AIComposer      │
├─────────────────────────────┤
│        UI Components        │
│ Button / Card / Sheet       │
│ Input / Badge / Avatar      │
├─────────────────────────────┤
│       Design System         │
│ color / type / shape        │
│ spacing / motion / elevation│
├─────────────────────────────┤
│ Compose + Material 3        │
└─────────────────────────────┘
```

Material stays at the bottom. DroidUI is the opinionated layer on top.

## Use Material where it helps

You do not rebuild everything.

```text
YourButton
      ↓
Material 3 Button
      ↓
Compose Foundation
```

Drop to [Compose Foundation](https://developer.android.com/jetpack/androidx/releases/compose-foundation) when Material fights the UX. Foundation is the documented building block for custom design-system pieces.

That keeps platform behaviour (ripples, focus, text, accessibility) without forcing every component to look like stock Material.

[Material 3 Expressive](https://developer.android.com/develop/ui/compose/designsystems/material3) is not competition. It is a foundation you can sit on, then style. Same APIs, different visual systems — see [Design system](design-system.md).

## Source repo

Treat this like a real Android product, not a dump of `components/`.

```text
droidui/
├── apps/
│   ├── showcase/
│   └── benchmark/
├── core/
│   ├── theme/          Color, Typography, Shape, Spacing, Motion, DroidTheme
│   ├── foundation/     modifiers, haptics, accessibility
│   └── icons/
├── registry/
│   ├── components/     button/, text-field/, avatar/, …
│   ├── patterns/       empty-state/, otp-input/, …
│   ├── blocks/         login/, settings/, ai-chat/, …
│   └── registry.json
├── tooling/
│   ├── cli/
│   ├── registry-parser/
│   └── generator/
├── website/
├── mcp/
├── figma/
├── gradle/
└── docs/
```

Each registry item is a folder of copyable files:

```text
registry/components/button/
├── Button.kt
├── ButtonPreview.kt
├── ButtonTest.kt
└── registry.json
```

The website, CLI, and MCP all read the same registry. They do not keep a second source of truth.

## What the developer actually gets

They never need this repo structure. After `droidui init` and a few `add`s:

```text
my-app/
└── app/src/main/java/com/acme/app/
    └── ui/
        ├── theme/
        ├── components/
        │   ├── Button.kt
        │   ├── TextField.kt
        │   ├── Avatar.kt
        │   └── Badge.kt
        ├── patterns/
        │   ├── EmptyState.kt
        │   └── OtpInput.kt
        └── blocks/
            └── SettingsScreen.kt
```

They import their own package:

```kotlin
import com.acme.app.ui.components.Button
```

not `com.droidui.components.Button`.

That distinction is the product.

## Gradle is for us, not for them

Development modules can depend on everything:

```kotlin
include(
    ":apps:showcase",
    ":core:theme",
    ":core:foundation",
    ":registry",
)
```

The consumer does not take these modules. They take files.

## No required runtime

See [Principles](principles.md). Copied Kotlin + Compose + Material 3 + AndroidX. External libraries only when a component truly needs them.

## Website and registry hosting

The site can be SvelteKit. Registry, CDN, and API can live on Cloudflare:

```text
SvelteKit
    ↓
Cloudflare

Registry  → R2
Metadata  → D1
Search    → D1 / Vectorize
Downloads → CDN
Analytics → Analytics Engine
```

The Android components themselves remain plain Kotlin files. Infrastructure must never leak into the copied code.

## V0 scope

Do not build the CLI first. Prove `registry/ + showcase/ + theme/` with 15–20 components that feel great, then add `droidui add`. Tooling before a design system produces a distribution machine with nothing worth distributing.
