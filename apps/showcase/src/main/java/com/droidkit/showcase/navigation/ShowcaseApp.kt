package com.droidkit.showcase.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.droidkit.showcase.screens.AvatarScreen
import com.droidkit.showcase.screens.BadgeScreen
import com.droidkit.showcase.screens.BeadScreen
import com.droidkit.showcase.screens.BottomSheetScreen
import com.droidkit.showcase.screens.ButtonScreen
import com.droidkit.showcase.screens.CardScreen
import com.droidkit.showcase.screens.CheckboxScreen
import com.droidkit.showcase.screens.ChipScreen
import com.droidkit.showcase.screens.ConfirmDialogScreen
import com.droidkit.showcase.screens.EmptyStateScreen
import com.droidkit.showcase.screens.ErrorStateScreen
import com.droidkit.showcase.screens.HomeScreen
import com.droidkit.showcase.screens.IconButtonScreen
import com.droidkit.showcase.screens.OtpInputScreen
import com.droidkit.showcase.screens.PasswordFieldScreen
import com.droidkit.showcase.screens.PreferencePickerScreen
import com.droidkit.showcase.screens.ProgressScreen
import com.droidkit.showcase.screens.RadioScreen
import com.droidkit.showcase.screens.SearchFieldScreen
import com.droidkit.showcase.screens.SegmentedControlScreen
import com.droidkit.showcase.screens.SettingRowScreen
import com.droidkit.showcase.screens.SettingsDemoScreen
import com.droidkit.showcase.screens.SkeletonScreen
import com.droidkit.showcase.screens.SnackbarScreen
import com.droidkit.showcase.screens.SwitchScreen
import com.droidkit.showcase.screens.TabsScreen
import com.droidkit.showcase.screens.TextAreaScreen
import com.droidkit.showcase.screens.TextFieldScreen

@Composable
fun ShowcaseApp(initialDestination: String? = null) {
    var destination by rememberSaveable { mutableStateOf(initialDestination ?: ShowcaseDestination.Home.title) }

    val current =
        when (destination) {
            ShowcaseDestination.Button.title -> ShowcaseDestination.Button
            ShowcaseDestination.TextField.title -> ShowcaseDestination.TextField
            ShowcaseDestination.PasswordField.title -> ShowcaseDestination.PasswordField
            ShowcaseDestination.EmptyState.title -> ShowcaseDestination.EmptyState
            ShowcaseDestination.Settings.title -> ShowcaseDestination.Settings
            ShowcaseDestination.OtpInput.title -> ShowcaseDestination.OtpInput
            ShowcaseDestination.Bead.title -> ShowcaseDestination.Bead
            ShowcaseDestination.IconButton.title -> ShowcaseDestination.IconButton
            ShowcaseDestination.SearchField.title -> ShowcaseDestination.SearchField
            ShowcaseDestination.TextArea.title -> ShowcaseDestination.TextArea
            ShowcaseDestination.Switch.title -> ShowcaseDestination.Switch
            ShowcaseDestination.Checkbox.title -> ShowcaseDestination.Checkbox
            ShowcaseDestination.Radio.title -> ShowcaseDestination.Radio
            ShowcaseDestination.Chip.title -> ShowcaseDestination.Chip
            ShowcaseDestination.Avatar.title -> ShowcaseDestination.Avatar
            ShowcaseDestination.Badge.title -> ShowcaseDestination.Badge
            ShowcaseDestination.Card.title -> ShowcaseDestination.Card
            ShowcaseDestination.SegmentedControl.title -> ShowcaseDestination.SegmentedControl
            ShowcaseDestination.Tabs.title -> ShowcaseDestination.Tabs
            ShowcaseDestination.Progress.title -> ShowcaseDestination.Progress
            ShowcaseDestination.Skeleton.title -> ShowcaseDestination.Skeleton
            ShowcaseDestination.Snackbar.title -> ShowcaseDestination.Snackbar
            ShowcaseDestination.ConfirmDialog.title -> ShowcaseDestination.ConfirmDialog
            ShowcaseDestination.BottomSheet.title -> ShowcaseDestination.BottomSheet
            ShowcaseDestination.ErrorState.title -> ShowcaseDestination.ErrorState
            ShowcaseDestination.SettingRow.title -> ShowcaseDestination.SettingRow
            ShowcaseDestination.PreferencePicker.title -> ShowcaseDestination.PreferencePicker
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
        ShowcaseDestination.Bead ->
            BeadScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.IconButton ->
            IconButtonScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.SearchField ->
            SearchFieldScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.TextArea ->
            TextAreaScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Switch ->
            SwitchScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Checkbox ->
            CheckboxScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Radio ->
            RadioScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Chip ->
            ChipScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Avatar ->
            AvatarScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Badge ->
            BadgeScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Card ->
            CardScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.SegmentedControl ->
            SegmentedControlScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Tabs ->
            TabsScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Progress ->
            ProgressScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Skeleton ->
            SkeletonScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.Snackbar ->
            SnackbarScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.ConfirmDialog ->
            ConfirmDialogScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.BottomSheet ->
            BottomSheetScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.ErrorState ->
            ErrorStateScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.SettingRow ->
            SettingRowScreen(onBack = { destination = ShowcaseDestination.Home.title })
        ShowcaseDestination.PreferencePicker ->
            PreferencePickerScreen(onBack = { destination = ShowcaseDestination.Home.title })
    }
}
