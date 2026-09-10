# Repo layout

Look like a real Android product, not a giant `components/` dump.

```text
droidkit/
├── apps/
│   ├── showcase/
│   │   └── src/main/java/com/droidkit/showcase/
│   │       ├── MainActivity.kt
│   │       ├── navigation/
│   │       └── screens/
│   │
│   └── benchmark/
│
├── core/
│   ├── theme/
│   │   ├── Color.kt
│   │   ├── Typography.kt
│   │   ├── Shape.kt
│   │   ├── Spacing.kt
│   │   ├── Motion.kt
│   │   └── AppTheme.kt
│   │
│   ├── foundation/
│   │   ├── ModifierExtensions.kt
│   │   ├── Haptics.kt
│   │   └── Accessibility.kt
│   │
│   └── icons/
│
├── registry/
│   ├── components/
│   │   ├── button/
│   │   │   ├── AppButton.kt
│   │   │   ├── AppButtonPreview.kt
│   │   │   ├── AppButtonTest.kt
│   │   │   └── registry.json
│   │   │
│   │   ├── text-field/
│   │   ├── avatar/
│   │   ├── badge/
│   │   ├── otp-input/
│   │   └── bottom-sheet/
│   │
│   ├── patterns/
│   │   ├── empty-state/
│   │   ├── error-state/
│   │   ├── loading-state/
│   │   └── permission-request/
│   │
│   ├── blocks/
│   │   ├── login/
│   │   ├── settings/
│   │   ├── onboarding/
│   │   └── ai-chat/
│   │
│   └── registry.json
│
├── tooling/
│   ├── cli/
│   ├── registry-parser/
│   └── generator/
│
├── website/
│
├── gradle/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

Registry code is copyable. The file the CLI installs is:

```text
registry/components/button/AppButton.kt
```

## Gradle

Only development modules depend on everything. The consumer never takes these modules. They take files.

```kotlin
include(
    ":apps:showcase",
    ":core:theme",
    ":core:foundation",
    ":registry",
)
```

## Root registry

```json
{
  "components": [
    {
      "name": "button",
      "path": "components/button"
    },
    {
      "name": "avatar",
      "path": "components/avatar"
    }
  ],
  "patterns": [
    {
      "name": "empty-state",
      "path": "patterns/empty-state"
    }
  ],
  "blocks": [
    {
      "name": "settings",
      "path": "blocks/settings"
    }
  ]
}
```
