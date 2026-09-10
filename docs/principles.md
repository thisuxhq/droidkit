# Principles

These decisions do not change with the phase. If a feature fights one of them, the feature loses.

## 1. Developers own the code

The main model is not:

```kotlin
implementation("com.droidkit:ui:1.4.0")
import com.droidkit.Button
```

The main model is:

```bash
droidkit add button
```

which writes:

```text
ui/components/Button.kt
```

into the app. The developer imports their own file, edits it, and keeps it. No black box.

Once installed, the code belongs to them. Updates never silently overwrite customized files. See [CLI](cli.md) and [Registry](registry.md).

This is the same idea shadcn describes as [open code, composition, and distribution](https://ui.shadcn.com/docs) rather than a normal component package.

## 2. No proprietary runtime

Avoid:

```kotlin
implementation("droidkit:runtime")
```

unless something is impossible without it.

A component may depend on Compose, Material 3, AndroidX, and a real library (Coil, for example) when that library is actually required. Most items should have close to **zero** DroidKit-specific runtime.

Trust comes from readable Kotlin, not from a framework.

## 3. The API is Compose

Bad:

```kotlin
DroidKitButton(
    configuration = ButtonConfiguration(
        variant = ButtonVariant.PRIMARY,
        size = ButtonSize.MEDIUM,
    )
)
```

Better:

```kotlin
Button(
    text = "Continue",
    onClick = ::continueFlow,
)
```

or a content slot:

```kotlin
Button(onClick = ::continueFlow) {
    Text("Continue")
}
```

Do not invent a framework. Use Compose. Give good defaults and escape hatches on the same function:

```kotlin
Button(
    text = "Continue",
    onClick = ::continueFlow,
    modifier = Modifier,
    colors = ...,
    shape = ...,
    contentPadding = ...,
)
```

## 4. Product decisions belong in the component

Do not obsess only over beautiful components. Obsess over **good product decisions built into components.**

Not `AlertDialog`. Instead: `ConfirmationDialog`, `DestructiveDialog`, `PermissionDialog`, `UnsavedChangesDialog`.

Not only `TextField`. Also: `EmailField`, `PasswordField`, `SearchField`, `MoneyField`, `PhoneField`, `OtpField`, `UsernameField` — with the keyboard, focus, validation, error, autofill, and IME behaviour already decided.

A password field that only wraps `TextField` is not the product. A `PasswordField` that handles show/hide, autofill semantics, touch targets, error motion, disabled state, accessibility, dark mode, and focus is.

Rounded corners are not the value. Defaults that match real UX problems are.

## 5. Taste over volume

Do not launch with 300 items. Launch with around 30 that feel finished.

Someone looking at an app built with DroidKit should wonder what UI library that is. That reaction is the signal. A component count is not.

The moat is not `Button.kt`. Everyone can recreate it. The moat is:

```text
Taste
+ component quality
+ UX knowledge
+ Android knowledge
+ registry
+ blocks
+ documentation
+ AI metadata
+ community
```

Especially taste.

## 6. Accessibility, adaptive UI, and performance are the quality bar

TalkBack, dynamic type, RTL, keyboard, dark mode, large screens, and touch targets are not cleanup. They are what “using a DroidKit component” means. See [Quality](quality.md).

Fancy motion that tanks a `LazyColumn` does not ship.

## 7. AI is a first-class user

Coding agents will install, compose, and misuse these components more often than humans will browse the docs. The registry schema, examples, and MCP surface are architecture, not a later integration. See [AI](ai.md).

## 8. Navigation names the job

Do not call everything a component. The site and the registry use four words:

| Word | Job |
| --- | --- |
| **Components** | Solve UI |
| **Patterns** | Solve UX |
| **Blocks** | Solve features |
| **Templates** | Solve apps |

That split is part of the product. It changes how people think about what they are installing.
