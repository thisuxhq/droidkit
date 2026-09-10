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
DroidTheme.spacing.md
```

over:

```kotlin
SpacingSemanticContainerInteractiveMedium
```

## Theme

```kotlin
AppTheme {
    Button(
        text = "Continue",
        onClick = {},
    )
}
```

Internally:

```text
AppTheme
 ├── AppColors
 ├── AppTypography
 ├── AppSpacing
 ├── AppShapes
 └── AppMotion
```

Spacing as a data class, exposed through a CompositionLocal:

```kotlin
data class DroidSpacing(
    val xs: Dp = 4.dp,
    val sm: Dp = 8.dp,
    val md: Dp = 16.dp,
    val lg: Dp = 24.dp,
    val xl: Dp = 32.dp,
)

val LocalSpacing = staticCompositionLocalOf { DroidSpacing() }

object DroidTheme {
    val spacing: DroidSpacing
        @Composable
        @ReadOnlyComposable
        get() = LocalSpacing.current
}
```

Usage:

```kotlin
Column(
    verticalArrangement = Arrangement.spacedBy(DroidTheme.spacing.md),
)
```

Color, type, shape, and motion follow the same pattern. One object. Obvious names. No generated token soup.

## Material 3 underneath

Sit on Material 3 — including [Expressive](https://developer.android.com/develop/ui/compose/designsystems/material3) — then apply DroidUI opinion.

```text
Android platform principles
           +
Material / M3 Expressive foundation
           +
DroidUI design opinion
           +
real product patterns
           ↓
      the UI system
```

When Material's component fights the desired UX, drop to Compose Foundation for that piece only. Do not fork the entire stack.

## Styles

Same APIs, different visual systems, chosen at init:

```bash
droidui init --style clean
droidui init --style expressive
droidui init --style minimal
```

V0 ships one style well. Extra styles are a later lever, not a launch requirement.

## Icons

One size scale. One stroke language. Do not mix icon sets inside a single install. If a component needs an icon, the registry item declares it and the CLI copies or depends on the same set the theme uses.

## What not to do

- Do not start in Figma. Compose first. A community Figma file that matches installed components comes after the Kotlin system exists.
- Do not expose every Material color role on day one. Map a small semantic set and expand when a component actually needs a new role.
- Do not encode brand in the copied theme so deeply that swapping accent color requires surgery. Accent, background, and surface should be the obvious edit points.
