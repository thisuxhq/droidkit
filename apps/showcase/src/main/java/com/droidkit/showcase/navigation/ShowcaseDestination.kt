package com.droidkit.showcase.navigation

sealed class ShowcaseDestination(
    val title: String,
) {
    data object Home : ShowcaseDestination("showcase")

    data object Button : ShowcaseDestination("button")

    data object TextField : ShowcaseDestination("text-field")

    data object PasswordField : ShowcaseDestination("password-field")

    data object EmptyState : ShowcaseDestination("empty-state")

    data object Settings : ShowcaseDestination("settings")

    data object OtpInput : ShowcaseDestination("otp-input")
}

val showcaseCatalog =
    listOf(
        ShowcaseDestination.Button,
        ShowcaseDestination.TextField,
        ShowcaseDestination.PasswordField,
        ShowcaseDestination.EmptyState,
        ShowcaseDestination.Settings,
        ShowcaseDestination.OtpInput,
    )
