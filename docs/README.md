# Docs

DroidUI is the open UI system for building polished Android apps.

These documents are the source of truth for the product: what it is, how it is structured, and how it should be built.

## Product

| Doc | What it covers |
| --- | --- |
| [Introduction](introduction.md) | Positioning, the problem, the bet |
| [Principles](principles.md) | Ownership, runtime, APIs, taste |
| [Architecture](architecture.md) | Five layers, repo layout, consumer project |
| [Design system](design-system.md) | Tokens, theme, Material 3, styles |
| [Catalog](catalog.md) | Primitives, components, patterns, blocks |
| [Anatomy](anatomy.md) | One component, fully worked |

## Reference

Implement against these examples. [Reference index](reference/README.md).

| Doc | What it shows |
| --- | --- |
| [Repo layout](reference/repo-layout.md) | Source tree, Gradle, root registry |
| [Button](reference/button.md) | Kotlin, preview, test, metadata |
| [Empty state](reference/empty-state.md) | Opinionated pattern |
| [Settings screen](reference/settings-screen.md) | Block composing other items |
| [Theme](reference/theme.md) | Spacing and `DroidTheme` |
| [Consumer project](reference/consumer.md) | `init` / `add` and their tree |

## Distribution

| Doc | What it covers |
| --- | --- |
| [Registry](registry.md) | Schema, recipes, dependency graph, updates |
| [CLI](cli.md) | `init`, `add`, `search`, `diff` |
| [AI](ai.md) | MCP, metadata, agent-first design |

## Quality and plan

| Doc | What it covers |
| --- | --- |
| [Quality](quality.md) | Accessibility, previews, tests, performance, adaptive UI |
| [Website](website.md) | Playground, not a prose dump |
| [Roadmap](roadmap.md) | Phases 1–5, launch set, ecosystem |

## Positioning

**Build Android apps that feel designed, not assembled.**

Beautiful, accessible, and production-ready Jetpack Compose components. Copy them into your project and make them yours.

Do not lead with “the largest Compose component library.” Anyone can copy a count. Lead with taste, product defaults, and code the developer owns.
