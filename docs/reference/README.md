# Reference

Concrete layout and Kotlin to implement against. Product rules live in the docs above this folder. These files are the examples.

The repo is a **registry and development environment**. The consumer project gets **clean Kotlin they own**.

| Doc | What it shows |
| --- | --- |
| [Repo layout](repo-layout.md) | Source tree, Gradle modules, root registry |
| [Button](button.md) | Copyable component: Kotlin, preview, test, metadata |
| [Empty state](empty-state.md) | Opinionated pattern |
| [Settings screen](settings-screen.md) | Block that composes patterns and components |
| [Theme](theme.md) | Spacing tokens and `DroidTheme` |
| [Consumer project](consumer.md) | `init` / `add`, their tree, their imports |

V0: build `registry/ + showcase/ + theme/` first. Prove 15–20 items feel great. Then `droidui add`.
