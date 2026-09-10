# Registry

The registry is the core product. Components are the contents. The registry is how they move.

Shadcn showed that a [registry](https://ui.shadcn.com/docs/registry) can distribute components, hooks, pages, configs, and other files — not just a package. DroidKit applies that model to Kotlin.

## What an item is

```json
{
  "name": "button",
  "type": "component",
  "description": "Primary action button with loading and disabled states.",
  "files": ["Button.kt"],
  "dependencies": [],
  "registryDependencies": ["theme"]
}
```

`dependencies` are Gradle/Maven coordinates the CLI may add. `registryDependencies` are other registry items that must be installed first (theme, motion, button, …).

A richer item carries knowledge, not just files:

```json
{
  "name": "empty-state",
  "category": "pattern",
  "description": "Use when a collection has no content.",
  "avoidWhen": "Do not use for loading or errors.",
  "files": ["EmptyState.kt", "EmptyStatePreview.kt", "EmptyStateTest.kt"],
  "dependencies": [],
  "registryDependencies": ["theme", "button"],
  "examples": [],
  "accessibility": {},
  "adaptive": true,
  "aiHints": [
    "Prefer inside lazy list containers",
    "Provide one primary action maximum"
  ]
}
```

AI, docs, and search all read this. Do not keep a second description in the website.

## Root index

```json
{
  "components": [
    { "name": "button", "path": "components/button" },
    { "name": "avatar", "path": "components/avatar" }
  ],
  "patterns": [
    { "name": "empty-state", "path": "patterns/empty-state" }
  ],
  "blocks": [
    { "name": "settings", "path": "blocks/settings" }
  ]
}
```

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
✓ Button.kt
✓ Attachment.kt
✓ ThinkingIndicator.kt
✓ Theme.kt
```

Show the plan before writing files. Never install a mystery graph.

## Ownership and updates

After `droidkit add button`, the developer may change `Button.kt`. Six months later, `droidkit update button` must not overwrite it.

Show a diff. Commands like `droidkit diff button` exist so they can see upstream vs local. Shadcn's CLI [supports viewing and diffing component changes](https://ui.shadcn.com/docs/cli) for the same reason.

Philosophy, again: once installed, the code belongs to the developer.

## Tests and previews travel with the item

```text
OtpInput.kt
OtpInputTest.kt
OtpInputPreview.kt
```

`droidkit add otp-input` does not just install UI. It installs tested UI with every state previewed. See [Quality](quality.md).

## What the registry is not

It is not a Maven repository. It is not a place to hide implementation behind a binary. It is not a dumping ground for unreviewed community files.

Later, namespaced third-party registries (`@acme/maps`, `@stripe/payment-sheet`) can exist. That is Phase 5. The first-party catalog has to be trustworthy before anyone else publishes into the same UX. Shadcn moved this way with [decentralized namespaced registries](https://ui.shadcn.com/docs/changelog/2025-08-cli-3-mcp). Copy the idea after the core is good, not before.
