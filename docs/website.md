# Website

The site is a playground that happens to have docs, not a docs site that happens to have screenshots.

## Component page

```text
/components/button
```

On one screen:

- Live preview
- Variants: primary, secondary, ghost, destructive
- States: default, loading, disabled
- Theme: light / dark
- Install: `droidui add button`
- Snippet:

```kotlin
Button(
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

The playground mirrors `@Preview` states. If a state exists in `ButtonPreview.kt` and not on the website, the website is behind. Generate playground cases from preview metadata where possible, rather than hand-maintaining two lists.

## What the site is not in v0

- Not a Figma embed
- Not a marketing site with no components
- Not the only way to read the system (agents get markdown and MCP; see [AI](ai.md))

Stack: SvelteKit is fine. Registry hosting is described in [Architecture](architecture.md).
