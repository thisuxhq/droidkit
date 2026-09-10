package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.droidkit.registry.components.AppTextField
import com.droidkit.registry.theme.AppTheme

@Composable
fun TextFieldScreen(onBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    val isError = email.isNotEmpty() && !email.contains("@")

    ShowcaseScaffold(title = "text-field", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            AppTextField(
                value = email,
                onValueChange = { email = it },
                label = "Email",
                placeholder = "you@example.com",
                supportingText = if (isError) "Enter a valid email" else null,
                isError = isError,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    ),
            )
            AppTextField(
                value = "Disabled",
                onValueChange = {},
                label = "Name",
                enabled = false,
            )
        }
    }
}
