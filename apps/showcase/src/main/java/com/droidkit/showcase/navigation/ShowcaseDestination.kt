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

    data object IconButton : ShowcaseDestination("icon-button")

    data object SearchField : ShowcaseDestination("search-field")

    data object TextArea : ShowcaseDestination("text-area")

    data object Switch : ShowcaseDestination("switch")

    data object Checkbox : ShowcaseDestination("checkbox")

    data object Radio : ShowcaseDestination("radio")

    data object Chip : ShowcaseDestination("chip")

    data object Avatar : ShowcaseDestination("avatar")

    data object Badge : ShowcaseDestination("badge")

    data object Card : ShowcaseDestination("card")

    data object SegmentedControl : ShowcaseDestination("segmented-control")

    data object Tabs : ShowcaseDestination("tabs")

    data object Progress : ShowcaseDestination("progress")

    data object Skeleton : ShowcaseDestination("skeleton")

    data object Snackbar : ShowcaseDestination("snackbar")

    data object ConfirmDialog : ShowcaseDestination("confirm-dialog")

    data object BottomSheet : ShowcaseDestination("bottom-sheet")

    data object ErrorState : ShowcaseDestination("error-state")

    data object SettingRow : ShowcaseDestination("setting-row")

    data object PreferencePicker : ShowcaseDestination("preference-picker")
}

val showcaseCatalog =
    listOf(
        ShowcaseDestination.Button,
        ShowcaseDestination.TextField,
        ShowcaseDestination.PasswordField,
        ShowcaseDestination.SearchField,
        ShowcaseDestination.TextArea,
        ShowcaseDestination.OtpInput,
        ShowcaseDestination.IconButton,
        ShowcaseDestination.Switch,
        ShowcaseDestination.Checkbox,
        ShowcaseDestination.Radio,
        ShowcaseDestination.Chip,
        ShowcaseDestination.Avatar,
        ShowcaseDestination.Badge,
        ShowcaseDestination.Card,
        ShowcaseDestination.SegmentedControl,
        ShowcaseDestination.Tabs,
        ShowcaseDestination.Progress,
        ShowcaseDestination.Skeleton,
        ShowcaseDestination.Snackbar,
        ShowcaseDestination.ConfirmDialog,
        ShowcaseDestination.BottomSheet,
        ShowcaseDestination.EmptyState,
        ShowcaseDestination.ErrorState,
        ShowcaseDestination.SettingRow,
        ShowcaseDestination.PreferencePicker,
        ShowcaseDestination.Settings,
    )
