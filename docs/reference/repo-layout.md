# Repo layout

Look like a real Android product, not a giant `components/` dump.

```text
droidui/
├── apps/
│   ├── showcase/
│   │   └── src/main/java/com/droidui/showcase/
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
│   │   └── DroidTheme.kt
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
│   │   │   ├── Button.kt
│   │   │   ├── ButtonPreview.kt
│   │   │   ├── ButtonTest.kt
│   │   │   └── registry.json
│   │   │
│   │   ├── text-field/
│   │   ├── avatar/
│   │   ├── badge/
│   │   └── bottom-sheet/
│   │
│   ├── patterns/
│   │   ├── empty-state/
│   │   ├── error-state/
│   │   ├── otp-input/
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
registry/components/button/Button.kt
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
