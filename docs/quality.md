# Quality

Quality is not a polish pass. It is what “using a DroidUI component” means.

## Accessibility

Every item ships with correct semantics. Compose uses the [semantics tree](https://developer.android.com/develop/ui/compose/accessibility/semantics) for accessibility, autofill, and UI testing — so a11y and tests are the same investment.

The promise:

```text
✓ TalkBack ready
✓ Dynamic font ready
✓ RTL ready
✓ Keyboard ready
✓ Dark mode ready
✓ Large-screen ready
✓ Touch-target checked
✓ UI-tested
```

Do not treat these as optional cleanup. If a component cannot meet this list, it is not ready to register.

Concrete defaults:

- Minimum 48 dp touch targets on interactive elements
- Content descriptions on icon-only controls; `null` on decorative icons
- Error text attached to the field, not only a color change
- Loading buttons remain focusable and announce state
- Contrast holds in light and dark, including disabled and muted

## Previews

Non-negotiable. Every component has a preview file next to it.

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

Show at least:

```text
Default
Pressed
Disabled
Loading
Icon
Long text
Dark
Large font
```

The website playground mirrors the same states. One component should feel fully explored, in Android Studio and in the docs.

## Tests

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

Compose UI testing already walks the [semantics tree](https://developer.android.com/develop/ui/compose/testing/semantics). Prefer semantics-based assertions over internal state.

`droidui add otp-input` installs `OtpInput.kt`, `OtpInputTest.kt`, and `OtpInputPreview.kt`. Tested UI, not just UI.

The showcase app and `:apps:benchmark` exist so we can feel scroll, animation, and startup — not only unit-test the happy path.

## Performance

Fancy Compose is expensive if you are careless. For each serious component, watch:

```text
recomposition
allocation
scroll performance
startup
animation jank
layout passes
```

Google's current Compose guidance calls out [stability, recomposition, R8, and Baseline Profiles](https://developer.android.com/develop/ui/compose/performance). Follow it.

Do not ship impressive animations that turn a `LazyColumn` into soup. If a block cannot scroll at 60/120 fps with realistic content, it is not done.

## Adaptive UI

Phone, tablet, foldable. See [Catalog](catalog.md). Adaptive behaviour is a quality requirement on blocks (settings, search, chat), not a separate product line.

Use window size classes and Material 3 Adaptive where they fit. Do not fork layouts with `if (isTablet)` scattered through copied code if a single adaptive scaffold will do.

## Website playground

Docs pages should show, not lecture.

```text
/components/button
```

Preview, variants, states, light/dark, the install command, and the Kotlin snippet. Copy. Install. Done.

Giant prose pages are a last resort. The component, its states, and its UX rule should be enough.
