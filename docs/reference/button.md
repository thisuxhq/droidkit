# Button

Folder:

```text
registry/components/button/
├── Button.kt
├── ButtonPreview.kt
├── ButtonTest.kt
└── registry.json
```

## Button.kt

```kotlin
@Composable
fun DroidButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    loading: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        enabled = enabled && !loading,
        shape = RoundedCornerShape(14.dp),
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

## ButtonPreview.kt

```kotlin
@Preview(showBackground = true)
@Composable
private fun ButtonPreview() {
    DroidTheme {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DroidButton(
                text = "Continue",
                onClick = {}
            )

            DroidButton(
                text = "Loading",
                loading = true,
                onClick = {}
            )

            DroidButton(
                text = "Disabled",
                enabled = false,
                onClick = {}
            )
        }
    }
}
```

## ButtonTest.kt

```kotlin
class DroidButtonTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun buttonCallsClick() {
        var clicked = false

        composeRule.setContent {
            DroidTheme {
                DroidButton(
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
  "files": [
    "Button.kt"
  ],
  "dependencies": [],
  "registryDependencies": [
    "theme"
  ]
}
```
