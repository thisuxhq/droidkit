package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppButton
import com.droidkit.registry.theme.AppTheme

@Composable
fun ButtonScreen(onBack: () -> Unit) {
    var loading by remember { mutableStateOf(false) }

    ShowcaseScaffold(title = "button", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            AppButton(text = "Continue", onClick = {})
            AppButton(
                text = if (loading) "Loading" else "Toggle loading",
                loading = loading,
                onClick = { loading = !loading },
            )
            AppButton(text = "Disabled", enabled = false, onClick = {})
            AppButton(text = "Continue with a much longer label", onClick = {})
        }
    }
}
