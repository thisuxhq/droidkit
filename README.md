# DroidKit

**The open UI system for building polished Android apps.**

Beautiful, accessible, production-ready Jetpack Compose components. Copy them into your project and make them yours.

Material gives developers primitives. DroidKit gives them product-quality UI.

> shadcn-style open components for Android, with stronger UX opinions.

That is the 5-second explanation. It is not the long-term identity.

## Why this exists

Jetpack Compose and Material 3 give you Button, Card, Dialog, TextField. Real products also need empty states, OTP inputs, settings rows, paywalls, permission flows, AI composers, and the rest of the screens developers rebuild in every app.

DroidKit is the opinionated layer on top: a design system, a registry of components you own, and the product patterns that make an Android app feel designed rather than assembled.

## How it works

You do not depend on a Maven UI library.

```bash
droidkit init
droidkit add button
droidkit add empty-state
droidkit add settings
```

The files land in your app. You import your own code. You can change anything.

## Start here

Read the docs, in this order:

1. [Introduction](docs/introduction.md) — what this is, and what it is not
2. [Principles](docs/principles.md) — the decisions that do not change
3. [Architecture](docs/architecture.md) — layers, repo, consumer project
4. [Roadmap](docs/roadmap.md) — what we build, and in what order

The full index lives in [docs/](docs/README.md).

## Status

Pre-v0. The design system and registry come before the CLI. Compose-only. No XML. No proprietary runtime.

## Development

Taste-test lives in `:apps:showcase`. It exercises every registered item.

```bash
./gradlew :apps:showcase:assembleDebug
./gradlew :registry:check
```

## License

[Apache-2.0](LICENSE). Files you copy into your app are yours.
