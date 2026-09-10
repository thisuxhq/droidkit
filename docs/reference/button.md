# Button

Folder:

```text
registry/components/button/
├── AppButton.kt
├── AppButtonPreview.kt
├── AppButtonTest.kt
└── registry.json
```

## AppButton.kt

```kotlin
package com.droidkit.registry.components

private val ButtonHeight = 52.dp
private val ButtonCorner = 14.dp

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
        modifier = modifier.height(ButtonHeight),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(ButtonCorner),
        contentPadding = PaddingValues(
            horizontal = 20.dp,
            vertical = 12.dp
        ),
    ) {
        AnimatedContent(
            targetState = loading,
            label = "button-loading"
        ) { isLoading ->
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(18.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}
```

## AppButtonPreview.kt

```kotlin
@Preview(showBackground = true)
@Composable
private fun AppButtonPreview() {
    AppTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AppButton(
                text = "Continue",
                onClick = {}
            )

            AppButton(
                text = "Loading",
                loading = true,
                onClick = {}
            )

            AppButton(
                text = "Disabled",
                enabled = false,
                onClick = {}
            )
        }
    }
}
```

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
                AppButton(
                    text = "Continue",
                    onClick = {
                        clicked = true
                    }
                )
            }
        }

        composeRule
            .onNodeWithText("Continue")
            .performClick()

        assert(clicked)
    }
}
```

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
  "aiHints": ["One primary button per screen section"]
}
```

Schema: [`registry/item.schema.json`](../../registry/item.schema.json).
