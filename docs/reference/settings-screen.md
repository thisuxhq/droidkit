# Settings screen

Blocks compose patterns and components. The developer installs a feature, not a widget.

Blocks are stateless: state and callbacks in, no ViewModel, Navigation, or DI. The state class lives in the same file. Experimental Material opt-ins are carried by the file and declared in `registry.json`. See [Decisions #7–8](../decisions.md).

## SettingsScreen.kt

```kotlin
package com.droidkit.registry.blocks

data class SettingsState(
    val notifications: Boolean,
    val theme: ThemeOption,
)

enum class ThemeOption(val label: String) {
    System("System"), Light("Light"), Dark("Dark")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    state: SettingsState,
    onNotificationsChanged: (Boolean) -> Unit,
    onLogout: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Settings")
                }
            )
        }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                SettingsSectionTitle("General")
            }

            item {
                SettingsToggleRow(
                    title = "Notifications",
                    description = "Receive important updates",
                    checked = state.notifications,
                    onCheckedChange = onNotificationsChanged
                )
            }

            item {
                SettingsRow(
                    title = "Appearance",
                    value = state.theme.label,
                    onClick = {}
                )
            }

            item {
                Spacer(
                    Modifier.height(24.dp)
                )
            }

            item {
                AppButton(
                    text = "Log out",
                    onClick = onLogout
                )
            }
        }
    }
}
```
