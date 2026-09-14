package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppOtpInput
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val DemoCode = "847291"
private const val FakeNetworkMillis = 900L
private const val SuccessHoldMillis = 800L

@Composable
fun OtpInputScreen(onBack: () -> Unit) {
    var code by rememberSaveable { mutableStateOf("") }
    var rejected by rememberSaveable { mutableStateOf(false) }
    var verifying by rememberSaveable { mutableStateOf(false) }
    var verified by rememberSaveable { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ShowcaseScaffold(title = "otp-input", onBack = onBack) { padding ->
        Column(
            modifier =
                Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(AppTheme.spacing.lg),
            verticalArrangement = Arrangement.spacedBy(AppTheme.spacing.md),
        ) {
            Text(
                text = "The code is $DemoCode. It submits itself on the last digit; try a wrong one first.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AppOtpInput(
                value = code,
                onValueChange = {
                    code = it
                    rejected = false
                },
                isError = rejected,
                supportingText = if (rejected) "Code did not match" else null,
                onComplete = { entered ->
                    scope.launch {
                        verifying = true
                        delay(FakeNetworkMillis)
                        verifying = false
                        if (entered == DemoCode) {
                            verified = true
                            delay(SuccessHoldMillis)
                            verified = false
                            code = ""
                        } else {
                            rejected = true
                        }
                    }
                },
            )
            val status =
                when {
                    verifying -> "Checking the code"
                    verified -> "Code accepted"
                    else -> ""
                }
            Text(
                text = status,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
