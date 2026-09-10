# Theme

Keep it dead simple. `AppTheme` is `MaterialTheme` plus a few extra locals. Material components inside it still theme correctly; DroidKit items get spacing and motion on top. See [Decisions #4](../decisions.md#4-theme-is-materialtheme--extra-locals-versioned).

## Spacing

```kotlin
package com.droidkit.registry.theme

data class AppSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

val LocalSpacing = staticCompositionLocalOf { AppSpacing() }
```

Motion and elevation follow the same shape (`AppMotion`, `LocalMotion`, `AppElevation`, `LocalElevation`).

## Theme.kt

```kotlin
package com.droidkit.registry.theme

const val THEME_VERSION = 1

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalSpacing provides AppSpacing(),
        LocalMotion provides AppMotion(),
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content,
        )
    }
}

object AppTheme {
    val spacing: AppSpacing
        @Composable @ReadOnlyComposable
        get() = LocalSpacing.current

    val motion: AppMotion
        @Composable @ReadOnlyComposable
        get() = LocalMotion.current
}
```

## Reading tokens in items

| Need | Read from |
| --- | --- |
| Color | `MaterialTheme.colorScheme.*` |
| Type | `MaterialTheme.typography.*` |
| Shape | `MaterialTheme.shapes.*` |
| Spacing | `AppTheme.spacing.*` |
| Motion | `AppTheme.motion.*` |

```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
)
```

## Contract

Adding a token bumps `THEME_VERSION` and the theme item's `themeVersion`. Items declare the version they need. `droidkit add` warns when the installed theme is older than an item requires. Removing or renaming a token is a breaking change and needs a migration note in the theme's `registry.json`.
