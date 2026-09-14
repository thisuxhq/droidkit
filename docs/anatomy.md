# Anatomy of a registry item

One component, fully worked. Everything else in the catalog should feel like this.

Source of truth:

```text
registry/components/button/
├── AppButton.kt
├── AppButtonPreview.kt
├── AppButtonTest.kt
└── registry.json
```

Source declares `package com.droidkit.registry.components`. On install the CLI rewrites it to `<package>.components` and, if the developer chose a prefix other than `App`, renames `AppButton` to match. Rules in [Config](config.md); rationale in [Decisions #2–3](decisions.md).

`droidkit add button` copies `AppButton.kt` and `AppButtonPreview.kt`. `--with-tests` adds `AppButtonTest.kt` and the test dependencies.

The full source is in [reference/button.md](reference/button.md).

## AppButton.kt

Compose-native. Good defaults. Loading and disabled. Material underneath. No DroidKit runtime.

```kotlin
@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(14.dp),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 12.dp,
        ),
    ) {
        AnimatedContent(
            targetState = loading,
            label = "button-loading",
        ) { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp,
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}
```

A later revision adds `modifier`, colors, shape, and a content slot so the text-only overload is the friendly path and the slot is the escape hatch. Do not start with a configuration object.

Height 52 dp and radius 14 dp are product decisions (touch target, not Material's default). That is the point. Per [Decisions #4](decisions.md), such numbers live as named `private val`s at the top of the file; layout spacing uses `AppTheme.spacing`. Type and color come from `MaterialTheme`, which `AppTheme` configures.

## AppButtonPreview.kt

```kotlin
@Preview(showBackground = true)
@Composable
private fun AppButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            AppButton(text = "Continue", onClick = {})
            AppButton(text = "Loading", loading = true, onClick = {})
            AppButton(text = "Disabled", enabled = false, onClick = {})
        }
    }
}
```

Add dark, large-font, icon, and long-text previews in the same file as the component grows. See [Quality](quality.md).

## AppButtonTest.kt

```kotlin
class AppButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun buttonCallsClick() {
        var clicked = false

        composeRule.setContent {
            AppTheme {
                AppButton(text = "Continue", onClick = { clicked = true })
            }
        }

        composeRule.onNodeWithText("Continue").performClick()
        assert(clicked)
    }
}
```

Also test: disabled does not click, loading does not click, semantics exist for the loading state.

## registry.json

```json
{
  "name": "button",
  "type": "component",
  "description": "Primary action button with loading and disabled states.",
  "avoidWhen": "Do not use for navigation rows or inline text links.",
  "files": [
    { "path": "AppButton.kt", "kind": "source" },
    { "path": "AppButtonPreview.kt", "kind": "preview" },
    { "path": "AppButtonTest.kt", "kind": "test" }
  ],
  "dependencies": [],
  "registryDependencies": ["theme"],
  "platform": "common",
  "themeVersion": 1,
  "experimentalApis": [],
  "aiHints": ["One primary button per screen section"],
  "ux": [
    { "moment": "reach", "behaviour": "Scales to 0.97 on press with a click haptic", "why": "The pill feels pressed, not repainted", "signature": true, "reducedMotion": "No scale; haptic and ripple only" },
    { "moment": "act", "behaviour": "Ignores a second tap for 400 ms after the first fires", "why": "Nobody submits twice because the network was slow" },
    { "moment": "succeed", "behaviour": "Spinner appears after 150 ms and stays at least 500 ms", "why": "Fast requests do not flash a spinner" , "reducedMotion": "Same timing, no crossfade" }
  ]
}
```

Validated against [`registry/item.schema.json`](../registry/item.schema.json) in CI. Expand `examples`, `accessibility`, and `aiHints` as the item matures.

## Moments

`ux` is the experience spec, and it is written **before** the state matrix. The state list says what the button looks like; `ux` says what it does when a person presses it, presses it twice, waits, and succeeds. One entry is `signature: true` — the detail someone would remember. Every entry has to be observable in a golden, a test, or the on-device journey; review checks each one. Rules and the moment vocabulary are in [Verification](verification.md#the-experience-spec).

## Checklist before it ships

- [ ] `ux` written before the code: one signature detail, every row observable, reduced motion stated
- [ ] Reads like Compose, not like a wrapper framework
- [ ] Theme tokens, not magic numbers — except where the magic number *is* the opinion (52 dp)
- [ ] Loading, disabled, dark, large font
- [ ] TalkBack, touch target, RTL
- [ ] Preview and test in the folder; preview doubles as a screenshot test
- [ ] Metadata validates against the schema; `platform`, `themeVersion`, `experimentalApis` filled in
- [ ] Imports only `com.droidkit.registry.*`, Compose, Material 3, AndroidX, and declared `dependencies`
- [ ] No DroidKit runtime import
