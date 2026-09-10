# Design system

Build the underlying system before building 100 components. Keep it small enough to hold in your head.

## Tokens

| System | Example |
| --- | --- |
| Color | background, surface, accent, muted, danger |
| Typography | display, title, body, label |
| Spacing | 4, 8, 12, 16, 24, 32 |
| Radius | 8, 12, 16, 24, full |
| Motion | quick, normal, slow |
| Elevation | flat, floating, overlay |
| Icon | consistent icon sizing |
| Feedback | press, hover, focus, loading |
| State | default, disabled, selected, error |

Do not over-engineer names. Prefer:

```kotlin
AppTheme.spacing.md
```

over:

```kotlin
SpacingSemanticContainerInteractiveMedium
```

## Theme

```kotlin
AppTheme {
    AppButton(
        text = "Continue",
        onClick = {},
    )
}
```

`AppTheme` **is** `MaterialTheme` with our color scheme, typography, and shapes, plus extra CompositionLocals Material does not have:

```text
AppTheme
 ├── MaterialTheme(colorScheme, typography, shapes)   ← color, type, shape
 ├── LocalSpacing   → AppTheme.spacing
 ├── LocalMotion    → AppTheme.motion
 └── LocalElevation → AppTheme.elevation
```

So Material components inside it theme correctly, and DroidKit items read color and type through `MaterialTheme.*`, spacing and motion through `AppTheme.*`. One theme, no parallel color system. Full source in [reference/theme.md](reference/theme.md).

Spacing as a data class, exposed through a CompositionLocal:

```kotlin
data class AppSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

val LocalSpacing = staticCompositionLocalOf { AppSpacing() }

object AppTheme {
    val spacing: AppSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}
```

Usage:

```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
)
```

Motion and elevation follow the same pattern. One object. Obvious names. No generated token soup.

## Theme contract

Every item depends on `theme`, so the theme is a stable contract, not a playground.

- Adding a token bumps `THEME_VERSION`; items declare the `themeVersion` they need; `droidkit add` warns on mismatch.
- Removing or renaming a token is breaking and needs a migration note in the theme item.
- Hardcoded dp is allowed only where the number *is* the opinion (button height 52 dp), declared as a named `private val` at the top of the file. Layout spacing uses tokens.

See [Decisions #4](decisions.md#4-theme-is-materialtheme--extra-locals-versioned).

## Material 3 underneath

Sit on Material 3 — including [Expressive](https://developer.android.com/develop/ui/compose/designsystems/material3) — then apply DroidKit opinion.

```text
Android platform principles
           +
Material / M3 Expressive foundation
           +
DroidKit design opinion
           +
real product patterns
           ↓
      the UI system
```

When Material's component fights the desired UX, drop to Compose Foundation for that piece only. Do not fork the entire stack.

## Styles

Same APIs, different visual systems, chosen at init:

```bash
droidkit init --style clean
droidkit init --style expressive
droidkit init --style minimal
```

V0 ships one style well. Extra styles are a later lever, not a launch requirement.

## Icons

One size scale. One stroke language. Do not mix icon sets inside a single install. If a component needs an icon, the registry item declares it and the CLI copies or depends on the same set the theme uses.

## What not to do

- Do not start in Figma. Compose first. A community Figma file that matches installed components comes after the Kotlin system exists.
- Do not expose every Material color role on day one. Map a small semantic set and expand when a component actually needs a new role.
- Do not encode brand in the copied theme so deeply that swapping accent color requires surgery. Accent, background, and surface should be the obvious edit points.
