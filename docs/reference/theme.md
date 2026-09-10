# Theme

Keep it dead simple. Obvious names. CompositionLocal, not generated token soup.

## Spacing

```kotlin
data class DroidSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)
```

## CompositionLocal

```kotlin
val LocalSpacing = staticCompositionLocalOf {
    DroidSpacing()
}

object DroidTheme {
    val spacing: DroidSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}
```

## Usage

```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(
        DroidTheme.spacing.md
    )
)
```

Color, type, shape, and motion follow the same pattern. See [Design system](../design-system.md).
