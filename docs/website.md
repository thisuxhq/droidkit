# Website

The site is a playground that happens to have docs, not a docs site that happens to have screenshots.

## Component page

```text
/components/button
```

On one screen:

- Preview images, one per state, light and dark
- Variants: primary, secondary, ghost, destructive
- States: default, loading, disabled
- Theme: light / dark
- Install: `droidkit add button`
- Snippet:

```kotlin
AppButton(
    text = "Continue",
    onClick = {},
)
```

Show. Copy. Install. Done.

Do not write giant documentation pages for primitives. If a component needs a UX rule (empty state, destructive dialog), put that rule next to the preview — short, with a good and a bad example. See [Catalog](catalog.md).

## Navigation

```text
Components
Patterns
Blocks
Templates
```

Those four words are the product. Do not flatten them into a single “UI kit” list.

## Same states as Android Studio

Compose does not run in a browser without a wasm build, so the playground does not try. Every `@Preview` is a screenshot test upstream; CI emits one PNG per state per theme, and the site renders those. If a state exists in `AppButtonPreview.kt` it is on the website by construction. No hand-maintained list. A Compose Multiplatform wasm showcase is a later option, not a dependency. See [Decisions #11](decisions.md).

## What the site is not in v0

- Not a Figma embed
- Not a marketing site with no components
- Not the only way to read the system (agents get markdown and MCP; see [AI](ai.md))

Stack: SvelteKit is fine. Registry hosting is described in [Architecture](architecture.md).
