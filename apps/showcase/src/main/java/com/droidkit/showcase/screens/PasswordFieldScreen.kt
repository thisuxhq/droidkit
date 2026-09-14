package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import com.droidkit.registry.components.AppButton
import com.droidkit.registry.components.AppPasswordField
import com.droidkit.registry.components.PasswordRule
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DemoPassword = "hunter2"
private const val FakeNetworkMillis = 900L

private val SignUpRules =
    listOf(
        PasswordRule.minLength(8),
        PasswordRule.number(),
        PasswordRule.upperCase(),
    )

@Composable
fun PasswordFieldScreen(onBack: () -> Unit) {
    var signInPassword by rememberSaveable { mutableStateOf("") }
    var signInRejected by rememberSaveable { mutableStateOf(false) }
    var signingIn by rememberSaveable { mutableStateOf(false) }
    var signUpPassword by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    // Clear the error when the request starts, set it when the response arrives: that is the
    // edge the field shakes on, and it is how a real sign-in behaves.
    val signIn = {
        if (!signingIn) {
            signInRejected = false
            signingIn = true
            scope.launch {
                delay(FakeNetworkMillis)
                signingIn = false
                signInRejected = signInPassword != DemoPassword
            }
        }
    }

    ShowcaseScaffold(title = "password-field", onBack = onBack) { padding ->
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
                text = "Sign in",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "The password is $DemoPassword. Try a wrong one, then start fixing it.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AppPasswordField(
                value = signInPassword,
                onValueChange = { signInPassword = it },
                supportingText = if (signInRejected) "Incorrect password" else null,
                isError = signInRejected,
                onImeAction = { signIn() },
            )
            AppButton(
                text = "Sign in",
                onClick = { signIn() },
                loading = signingIn,
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "Sign up",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = AppTheme.spacing.lg),
            )
            AppPasswordField(
                value = signUpPassword,
                onValueChange = { signUpPassword = it },
                label = "Create password",
                rules = SignUpRules,
                contentType = ContentType.NewPassword,
            )
        }
    }
}
