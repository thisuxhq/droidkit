# Registry

The registry is the core product. Components are the contents. The registry is how they move.

Shadcn showed that a [registry](https://ui.shadcn.com/docs/registry) can distribute components, hooks, pages, configs, and other files — not just a package. DroidKit applies that model to Kotlin.

## What an item is

One schema, validated in CI: [`registry/item.schema.json`](../registry/item.schema.json).

```json
{
  "name": "empty-state",
  "type": "pattern",
  "description": "Use when a collection has no content.",
  "avoidWhen": "Do not use for loading or errors.",
  "files": [
    { "path": "EmptyState.kt", "kind": "source" },
    { "path": "EmptyStatePreview.kt", "kind": "preview" },
    { "path": "EmptyStateTest.kt", "kind": "test" }
  ],
  "dependencies": [],
  "registryDependencies": ["theme", "button"],
  "platform": "common",
  "minSdk": 24,
  "themeVersion": 1,
  "experimentalApis": [],
  "adaptive": false,
  "accessibility": { "talkback": true, "dynamicType": true, "rtl": true },
  "examples": [],
  "aiHints": [
    "Prefer inside lazy list containers",
    "Provide one primary action maximum"
  ]
}
```

| Field | Purpose |
| --- | --- |
| `type` | `theme` \| `component` \| `pattern` \| `block` \| `recipe`. The only category field |
| `files[].kind` | `source` and `preview` copy by default; `test` needs `--with-tests` |
| `dependencies` | Maven coordinates the CLI may add. Never hidden |
| `registryDependencies` | Items installed first (theme, button, …) |
| `platform` | `android` or `common` (Compose Multiplatform-compatible) |
| `themeVersion` | Theme contract version the item requires |
| `experimentalApis` | Opt-in annotations the copied code carries |
| `description` / `avoidWhen` / `aiHints` | Knowledge, not just files. Read by docs, search, and MCP |

Do not keep a second description in the website. The registry is the source.

## Package convention

Registry source declares placeholder packages by category:

```text
com.droidkit.registry.theme
com.droidkit.registry.components
com.droidkit.registry.patterns
com.droidkit.registry.blocks
```

The CLI rewrites these to the consumer's package on install, and renames the `App` prefix if the developer chose another. Because cross-item imports (EmptyState → AppButton) also use the placeholder, they resolve with the same rule. Full rules in [Config](config.md).

Registry Kotlin may import only `com.droidkit.registry.*`, Compose, Material 3, AndroidX, and its declared `dependencies`. A lint in `:registry` enforces it.

## Root index

```json
{
  "version": 1,
  "themeVersion": 1,
  "items": [
    { "name": "theme", "type": "theme", "path": "theme", "revision": "3f9c2a1" },
    { "name": "button", "type": "component", "path": "components/button", "revision": "a1b2c3d" },
    { "name": "avatar", "type": "component", "path": "components/avatar", "revision": "9e8d7c6" },
    { "name": "empty-state", "type": "pattern", "path": "patterns/empty-state", "revision": "5b4a3f2" },
    { "name": "settings", "type": "block", "path": "blocks/settings", "revision": "1c2d3e4" }
  ]
}
```

A flat list with `type` and per-item `revision`. Revision is what `droidkit.json` records at install and what `diff` compares against.

## Search is a product surface

```bash
droidkit search payment
```

```text
payment-card
pricing-card
subscription-picker
paywall
payment-method-row
purchase-success
```

```bash
droidkit add paywall
```

pulls `Paywall.kt`, `PricingCard.kt`, `FeatureList.kt`, `SubscriptionPicker.kt`, plus required icons, dependencies, and tokens.

## Recipes

A recipe composes existing items. You maintain the graph.

```bash
droidkit add recipe:login
droidkit add recipe:saas-settings
droidkit add recipe:ai-chat
```

```text
AI Chat
   ↓
Message, Avatar, Markdown, CodeBlock,
Composer, Attachment, Thinking, ToolCall
```

Recipes are how blocks stay DRY without becoming a runtime.

## Dependency resolution

```bash
droidkit add ai-chat
```

```text
ai-chat
├── composer
│   ├── button
│   ├── text-field
│   └── attachment
├── message
│   ├── avatar
│   └── markdown
├── thinking-indicator
└── theme
```

```text
Adding 8 files

✓ AIChat.kt
✓ ChatComposer.kt
✓ ChatMessage.kt
✓ Avatar.kt
✓ AppButton.kt
✓ Attachment.kt
✓ ThinkingIndicator.kt
✓ Theme.kt
```

Show the plan before writing files. Never install a mystery graph.

## Ownership and updates

After `droidkit add button`, the developer may change `AppButton.kt`. Six months later, `droidkit update button` must not overwrite it.

`droidkit.json` records the registry revision per installed item. `diff` compares local vs installed vs latest; `update` applies only when the local file is untouched. Details in [Config](config.md). Shadcn's CLI [supports viewing and diffing component changes](https://ui.shadcn.com/docs/cli) for the same reason.

Philosophy, again: once installed, the code belongs to the developer.

## Tests and previews travel with the item

```text
AppOtpInput.kt
AppOtpInputPreview.kt
AppOtpInputTest.kt
```

`droidkit add otp-input` copies the source and preview. `--with-tests` adds the test file and the `androidTest` dependencies it needs. Upstream, every preview is also a screenshot test, so "every state previewed" is enforced in CI, not by convention. See [Quality](quality.md).

## What the registry is not

It is not a Maven repository. It is not a place to hide implementation behind a binary. It is not a dumping ground for unreviewed community files.

Later, namespaced third-party registries (`@acme/maps`, `@stripe/payment-sheet`) can exist. That is Phase 5. The first-party catalog has to be trustworthy before anyone else publishes into the same UX. Shadcn moved this way with [decentralized namespaced registries](https://ui.shadcn.com/docs/changelog/2025-08-cli-3-mcp). Copy the idea after the core is good, not before.
