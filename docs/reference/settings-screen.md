# Settings screen

Blocks compose patterns and components. The developer installs a feature, not a widget.

## SettingsScreen.kt

```kotlin
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
                DroidButton(
                    text = "Log out",
                    onClick = onLogout
                )
            }
        }
    }
}
```
