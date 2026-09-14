package com.droidkit.registry.patterns

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConfirmationDialogTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun confirmAndCancelFire() {
        var confirm = 0
        var dismiss = 0
        composeRule.setContent {
            AppTheme {
                ConfirmationDialog(
                    title = "Discard draft",
                    description = "This draft will be gone.",
                    confirm = "Discard",
                    onConfirm = { confirm += 1 },
                    onDismiss = { dismiss += 1 },
                )
            }
        }
        composeRule.onNodeWithText("Discard").performClick()
        assertEquals(1, confirm)
        composeRule.onNodeWithText("Cancel").performClick()
        assertEquals(1, dismiss)
    }
}
