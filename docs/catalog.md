# Catalog

Four categories. That is the taxonomy. Everything in the registry is one of these.

## 1. Primitives

Button, Input, Card, Avatar, Badge, Switch, Checkbox, Chip, Divider, Progress, Skeleton, Tooltip, Dialog, Sheet.

These are the design-system pieces. They must be excellent, but they are not why people switch.

## 2. Components

OTP input, search bar, setting row, preference selector, rating, stepper, quantity selector, segmented control, expandable card, avatar stack, swipe actions, pull to refresh.

These already encode behaviour. Keyboards, focus, validation, and motion live here.

## 3. Patterns

Empty state, error state, offline state, loading state, permission request, destructive confirmation, upgrade prompt, success screen, form section.

Patterns solve UX problems. They teach the right default while giving the code.

### Empty state

Good:

```text
No projects yet

Create your first project to get started.

[ Create project ]
```

Bad:

```text
Oops!

Looks like nothing is here :(

[ Try something ]
```

Rule of thumb: one title, one short description, at most one primary action. Do not use an empty state for loading or errors.

```kotlin
@Composable
fun EmptyState(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    action: String? = null,
    onAction: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(40.dp),
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
        )
        if (action != null && onAction != null) {
            Spacer(Modifier.height(4.dp))
            AppButton(text = action, onClick = onAction)
        }
    }
}
```

```kotlin
EmptyState(
    title = "No projects yet",
    description = "Create your first project to get started.",
    action = "Create project",
    onAction = { createProject() },
)
```

Every pattern should ship with a short **use / avoid** rule in its registry metadata. That rule is part of the product. See [Registry](registry.md) and [AI](ai.md).

## 4. Blocks

Login, signup, onboarding, settings, profile, search, chat, AI chat, paywall, notifications, account management, media gallery.

Blocks will drive adoption. Developers may not care about another button. They care about:

```bash
droidkit add auth
```

```text
LoginScreen.kt
SignupScreen.kt
ForgotPassword.kt
OtpVerification.kt
SocialLogin.kt
```

```bash
droidkit add settings
```

```bash
droidkit add ai-chat
```

```text
ChatScreen
MessageBubble
ThinkingIndicator
StreamingText
Composer
AttachmentPreview
ToolCall
CodeBlock
```

### Blocks are stateless

A block is a `@Composable` screen that takes state and callbacks. It defines a plain `data class` for its state in the same file. It never depends on ViewModel, Navigation, Hilt/Koin, or a networking client — the developer wires those. This keeps blocks usable in any architecture and keeps "no runtime" true. See [Decisions #7](decisions.md).

When a block needs an experimental Material API (`TopAppBar` today), the copied file carries the `@OptIn` and the item lists it in `experimentalApis`. Nothing surprises the developer at compile time.

A settings block composes primitives and patterns:

```kotlin
data class SettingsState(
    val notifications: Boolean,
    val theme: ThemeOption,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onNotificationsChanged: (Boolean) -> Unit,
    onLogout: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        },
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            item { SettingsSectionTitle("General") }
            item {
                SettingsToggleRow(
                    title = "Notifications",
                    description = "Receive important updates",
                    checked = state.notifications,
                    onCheckedChange = onNotificationsChanged,
                )
            }
            item {
                SettingsRow(
                    title = "Appearance",
                    value = state.theme.label,
                    onClick = {},
                )
            }
            item { Spacer(Modifier.height(24.dp)) }
            item { AppButton(text = "Log out", onClick = onLogout) }
        }
    }
}
```

## Android-only items

Web libraries cannot offer these. They are a moat if they are actually good:

```text
PermissionFlow
BiometricPrompt
CameraCapture
DocumentPicker
ShareSheet
AppUpdatePrompt
NotificationPermission
LocationPermission
KeyboardAwareForm
PredictiveBackScreen
EdgeToEdgeScaffold
HapticButton
SystemBarController
InstallPrompt
ConnectivityState
InAppReview
```

This is how DroidKit stops being a port and becomes an Android product UI standard.

## Adaptive UI

Phone-only thinking is a mistake. Compose already has [Material 3 Adaptive](https://developer.android.com/jetpack/androidx/releases/compose-material3-adaptive) for window sizes, foldables, and multi-pane layouts.

```kotlin
AdaptiveSettingsScreen(...)
```

Phone: list, then detail. Tablet: list | detail. The block knows. The developer does not implement both.

Mark adaptive items in registry metadata (`"adaptive": true`) so agents and the docs site can filter them.

## Launch set

Launch: about 30 things. Not 300. Phase 1 is the first 25 of these.

```text
Button
IconButton
Input
PasswordInput
SearchInput
OTPInput
Textarea
Card
Avatar
Badge
Chip
Switch
Checkbox
Radio
SegmentedControl
Tabs
Progress
Skeleton
Snackbar
Dialog
BottomSheet
EmptyState
ErrorState
SettingRow
PreferencePicker
ExpandableCard
SwipeAction
SearchScreen
SettingsScreen
LoginScreen
AIChat
```

That is enough to build something meaningful and to take screenshots that primitives cannot.

Phase 1 is smaller still: theme + 10 primitives + 10 components + 5 patterns. Get the visual language right before filling the catalog. See [Roadmap](roadmap.md).

## Phase 1 status

The first 25 of the launch set, plus theme, foundation, and the settings block already in the registry.

| Launch-set name | Registry id | Kind | In registry |
| --- | --- | --- | --- |
| Button | `button` | component | yes |
| IconButton | `icon-button` | component | yes |
| Input | `text-field` | component | yes |
| PasswordInput | `password-field` | component | yes |
| SearchInput | `search-field` | component | yes |
| OTPInput | `otp-input` | component | yes |
| Textarea | `text-area` | component | yes |
| Card | `card` | component | yes |
| Avatar | `avatar` | component | yes |
| Badge | `badge` | component | yes |
| Chip | `chip` | component | yes |
| Switch | `switch` | component | yes |
| Checkbox | `checkbox` | component | yes |
| Radio | `radio` | component | yes |
| SegmentedControl | `segmented-control` | component | yes |
| Tabs | `tabs` | component | yes |
| Progress | `progress` | component | yes |
| Skeleton | `skeleton` | component | yes |
| Snackbar | `snackbar` | component | yes |
| Dialog | `confirm-dialog` | pattern | yes |
| BottomSheet | `bottom-sheet` | component | yes |
| EmptyState | `empty-state` | pattern | yes |
| ErrorState | `error-state` | pattern | yes |
| SettingRow | `setting-row` | component | yes |
| PreferencePicker | `preference-picker` | component | yes |
| Inline citation | `inline-citation` | component | yes |
| ExpandableCard | — | — | later |
| SwipeAction | — | — | later |
| SearchScreen | — | block | later |
| SettingsScreen | `settings` | block | yes |
| LoginScreen | — | block | later |
| AIChat | — | block | later |

Theme and foundation are contracts, not catalog items. They ship with every install.
