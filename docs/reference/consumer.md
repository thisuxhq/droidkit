# Consumer project

The source repo can be complex. Their project stays simple.

**This codebase is a registry and development environment. Their codebase gets clean Kotlin files they own.**

## `droidkit init`

Creates:

```text
app/src/main/java/com/example/app/ui/
├── theme/
│   ├── Color.kt
│   ├── Theme.kt
│   ├── Typography.kt
│   └── Spacing.kt
└── components/
```

## `droidkit add button`

```text
1. fetch registry/button
2. read dependencies
3. check project
4. copy Button.kt
5. install dependencies if needed
6. format code
```

Result:

```text
app/
└── src/main/java/com/example/app/ui/
    ├── theme/
    │   └── ...
    │
    └── components/
        └── DroidButton.kt
```

They import **their own file**:

```kotlin
import com.example.app.ui.components.DroidButton
```

Not:

```kotlin
import com.droidkit.components.Button
```

That distinction is the product.

## What their app looks like

```text
my-app/
└── app/src/main/java/com/acme/app/
    └── ui/
        ├── theme/
        ├── components/
        │   ├── AppButton.kt
        │   ├── AppTextField.kt
        │   ├── Avatar.kt
        │   └── Badge.kt
        │
        ├── patterns/
        │   ├── EmptyState.kt
        │   └── OtpInput.kt
        │
        └── blocks/
            └── SettingsScreen.kt
```

Optimize everything around this tree.
