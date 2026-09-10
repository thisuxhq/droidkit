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
import com.droidkit.registry.components.AppPasswordField
import com.droidkit.registry.theme.AppTheme

@Composable
fun PasswordFieldScreen(onBack: () -> Unit) {
    var password by remember { mutableStateOf("") }
    val isError = password.isNotEmpty() && password.length < 8

    ShowcaseScaffold(title = "password-field", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            AppPasswordField(
                value = password,
                onValueChange = { password = it },
                supportingText = if (isError) "Use at least 8 characters" else null,
                isError = isError,
            )
        }
    }
}
