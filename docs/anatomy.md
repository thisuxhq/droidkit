# Anatomy of a registry item

One component, fully worked. Everything else in the catalog should feel like this.

Source of truth:

```text
registry/components/button/
├── Button.kt
├── ButtonPreview.kt
├── ButtonTest.kt
└── registry.json
```

Copied into the app as `ui/components/Button.kt` (package rewritten). Previews and tests can copy with it or stay upstream — V0 copies all three.

The full source example (including the `DroidButton` name as copied into the app) is in [reference/button.md](reference/button.md).

## Button.kt

Compose-native. Good defaults. Loading and disabled. Material underneath. No DroidUI runtime.

```kotlin
@Composable
fun Button(
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

Height 52 dp is a product decision (touch target, not Material's default). That is the point.

## ButtonPreview.kt

```kotlin
@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    DroidTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Button(text = "Continue", onClick = {})
            Button(text = "Loading", loading = true, onClick = {})
            Button(text = "Disabled", enabled = false, onClick = {})
        }
    }
}
```

Add dark, large-font, icon, and long-text previews in the same file as the component grows. See [Quality](quality.md).

## ButtonTest.kt

```kotlin
class ButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun buttonCallsClick() {
        var clicked = false

        composeRule.setContent {
            DroidTheme {
                Button(text = "Continue", onClick = { clicked = true })
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
  "files": ["Button.kt"],
  "dependencies": [],
  "registryDependencies": ["theme"]
}
```

Expand with `avoidWhen`, `examples`, `accessibility`, and `aiHints` as the item matures. The schema is in [Registry](registry.md).

## Checklist before it ships

- [ ] Reads like Compose, not like a wrapper framework
- [ ] Theme tokens, not magic numbers — except where the magic number *is* the opinion (52 dp)
- [ ] Loading, disabled, dark, large font
- [ ] TalkBack, touch target, RTL
- [ ] Preview and test in the folder
- [ ] Metadata a human and an agent can both use
- [ ] No DroidUI runtime import
