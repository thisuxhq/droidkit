package com.droidkit.showcase.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.droidkit.registry.components.AppButton
import com.droidkit.registry.components.AppDestructiveButton
import com.droidkit.registry.components.AppSecondaryButton
import com.droidkit.registry.components.AppTextButton
import com.droidkit.registry.theme.AppTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val FastCallMillis = 100L
private const val SlowCallMillis = 2_000L
private const val SuccessHoldMillis = 800L

@Composable
fun ButtonScreen(onBack: () -> Unit) {
    val scope = rememberCoroutineScope()
    var fastLoading by remember { mutableStateOf(false) }
    var slowLoading by remember { mutableStateOf(false) }
    var paying by remember { mutableStateOf(false) }
    var paid by remember { mutableStateOf(false) }

    ShowcaseScaffold(title = "button", onBack = onBack) { padding ->
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
                text = "Press and hold: the pill scales. Tap twice fast: one click.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            AppButton(text = "Continue", onClick = {}, modifier = Modifier.fillMaxWidth())

            Text(
                text = "Loading",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = AppTheme.spacing.md),
            )
            AppButton(
                text = "Simulate 100 ms call",
                loading = fastLoading,
                onClick = {
                    scope.launch {
                        fastLoading = true
                        delay(FastCallMillis)
                        fastLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            AppButton(
                text = "Simulate 2 s call",
                loading = slowLoading,
                onClick = {
                    scope.launch {
                        slowLoading = true
                        delay(SlowCallMillis)
                        slowLoading = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )
            AppButton(
                text = "Pay now",
                loading = paying,
                success = paid,
                onClick = {
                    scope.launch {
                        paying = true
                        delay(SlowCallMillis / 2)
                        paying = false
                        paid = true
                        delay(SuccessHoldMillis)
                        paid = false
                    }
                },
                modifier = Modifier.fillMaxWidth(),
            )

            Text(
                text = "Variants",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = AppTheme.spacing.md),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
                AppSecondaryButton(text = "Not now", onClick = {}, modifier = Modifier.weight(1f))
                AppButton(text = "Allow", onClick = {}, modifier = Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(AppTheme.spacing.sm)) {
                AppTextButton(text = "Skip", onClick = {})
                AppDestructiveButton(text = "Delete account", onClick = {})
            }
            AppButton(text = "Disabled", enabled = false, onClick = {}, modifier = Modifier.fillMaxWidth())
            AppButton(text = "Continue with a much longer label", onClick = {}, modifier = Modifier.fillMaxWidth())
        }
    }
}
