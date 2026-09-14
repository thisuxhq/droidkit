package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.droidkit.registry.components.AppButton
import com.droidkit.registry.components.AppTextField
import com.droidkit.registry.theme.AppTheme

private const val HandleMaxLength = 15

@Composable
fun TextFieldScreen(onBack: () -> Unit) {
    var email by rememberSaveable { mutableStateOf("") }
    var emailRejected by rememberSaveable { mutableStateOf(false) }
    var handle by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Validate on submit, not per keystroke: the field goes quiet while you fix it and comes back
    // on blur if the value is still wrong.
    val submit = {
        emailRejected = !email.contains("@")
        focusManager.clearFocus()
    }

    ShowcaseScaffold(title = "text-field", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            Text(
                text = "Submit an address without @, then start fixing it.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AppTextField(
                value = email,
                onValueChange = {
                    email = it
                    if (it.contains("@")) emailRejected = false
                },
                label = "Email",
                placeholder = "you@example.com",
                supportingText = if (emailRejected) "Enter a valid email" else "We only use this to sign you in",
                isError = emailRejected,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
            )
            AppTextField(
                value = handle,
                onValueChange = { handle = it },
                label = "Handle",
                supportingText = "Letters, numbers, and underscores",
                maxLength = HandleMaxLength,
            )
            AppTextField(
                value = "Ada Lovelace",
                onValueChange = {},
                label = "Name",
                enabled = false,
            )
            AppButton(
                text = "Continue",
                onClick = submit,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
