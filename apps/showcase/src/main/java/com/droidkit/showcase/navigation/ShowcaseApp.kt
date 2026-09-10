package com.droidkit.showcase.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.droidkit.showcase.screens.ButtonScreen
import com.droidkit.showcase.screens.EmptyStateScreen
import com.droidkit.showcase.screens.HomeScreen
import com.droidkit.showcase.screens.OtpInputScreen
import com.droidkit.showcase.screens.PasswordFieldScreen
import com.droidkit.showcase.screens.SettingsDemoScreen
import com.droidkit.showcase.screens.TextFieldScreen

@Composable
fun ShowcaseApp() {
    var destination by rememberSaveable { mutableStateOf(ShowcaseDestination.Home.title) }

    val current =
        when (destination) {
            ShowcaseDestination.Button.title -> ShowcaseDestination.Button
            ShowcaseDestination.TextField.title -> ShowcaseDestination.TextField
            ShowcaseDestination.PasswordField.title -> ShowcaseDestination.PasswordField
            ShowcaseDestination.EmptyState.title -> ShowcaseDestination.EmptyState
            ShowcaseDestination.Settings.title -> ShowcaseDestination.Settings
            ShowcaseDestination.OtpInput.title -> ShowcaseDestination.OtpInput
            else -> ShowcaseDestination.Home
        }

    if (current !is ShowcaseDestination.Home) {
        BackHandler { destination = ShowcaseDestination.Home.title }
    }

    when (current) {
        ShowcaseDestination.Home ->
            HomeScreen(onOpen = { destination = it.title })
        ShowcaseDestination.Button ->
            ButtonScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.TextField ->
            TextFieldScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.PasswordField ->
            PasswordFieldScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.EmptyState ->
            EmptyStateScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Settings ->
            SettingsDemoScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.OtpInput ->
            OtpInputScreen(onBack = { destination = ShowcaseDestination.Home.title })
    }
}
