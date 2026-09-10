# Roadmap

Start with Compose-only, an open-code registry, and about 25 excellent components and patterns. Resist XML. Resist a huge runtime. Compose as Google's primary UI direction makes that bet much safer than it was a few years ago.

## Phase 1 — Foundation

Build the system before the catalog.

```text
Theme, Color, Type, Spacing, Shape, Motion, Icons
```

Then:

```text
10 primitives
10 components
5 patterns
```

Showcase app that uses all of them. Previews and tests on every item. Get the visual language right. Do not start the CLI yet.

## Phase 2 — Distribution

```bash
droidui init
droidui add
droidui search
droidui view
droidui diff
```

Then the public registry.

This is when the project stops being “a Compose library” and becomes a system.

## Phase 3 — Product blocks

Add the things developers hate rebuilding:

```text
Auth
Onboarding
Settings
Search
Profile
Paywall
Chat
AI Chat
Media
Forms
```

These generate screenshots and posts that primitives will not.

## Phase 4 — AI

```text
MCP
llms.txt
structured component metadata
AI usage instructions
component search
component composition
```

Then somebody can say “make this screen look polished” and the agent understands the design system. See [AI](ai.md).

## Phase 5 — Ecosystem

Other people publish:

```bash
droidui add @acme/maps
droidui add @stripe/payment-sheet
droidui add @community/chart
```

And:

```bash
droidui create
```

```text
What are you building?

> SaaS companion
  AI app
  Finance app
  Social app
  Productivity app
```

A beautiful Android starter. At that point this is no longer a component kit. It is the starting point for modern Android product UI.

Do not open the ecosystem before the first-party catalog is a quality bar people will copy.

## The ecosystem, drawn

```text
                 Android Developer
                        │
                        ▼
                  CLI / AI Agent
                        │
                ┌───────┴────────┐
                ▼                ▼
             Search            MCP
                │                │
                └───────┬────────┘
                        ▼
                    Registry
                        │
      ┌─────────────────┼─────────────────┐
      ▼                 ▼                 ▼
 Components          Patterns           Blocks
      │                 │                 │
      └─────────────────┼─────────────────┘
                        ▼
                  Design System
                        │
                        ▼
            Compose + Android APIs
```

That is the product. Not “150 Kotlin components.”

## Figma

Eventually:

```text
Figma  ↔  Design tokens  ↔  Registry  →  Kotlin
```

A community Figma file with the same components developers install closes the designer–developer gap. Launch Compose first. Figma is not Phase 1.

## Positioning over time

Avoid: “the largest Compose component library.”

Use:

> **Build Android apps that feel designed, not assembled.**

Then:

> Beautiful, accessible, and production-ready Jetpack Compose components. Copy them into your project and make them yours.

Short developer explanation, when needed:

> **shadcn-style open components for Android, with stronger UX opinions.**

The long-term identity:

> **The open UI system for building polished Android apps.**

Or, later:

> **the starting point for modern Android product UI.**

## What winning looks like

Someone looks at an Android app built with this and thinks: “What UI library is that?”

Taste, plus everything around it — quality, UX knowledge, Android knowledge, registry, blocks, docs, AI metadata, community. Not `Button.kt`.
