package com.droidkit.registry.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.droidkit.registry.theme.AppTheme
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppSkeletonTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun hiddenFromTalkBack() {
        composeRule.setContent {
            AppTheme { AppSkeleton() }
        }
        val invisible = composeRule.onRoot().fetchSemanticsNode().children.any { child ->
            SemanticsProperties.InvisibleToUser in child.config
        }
        assertTrue(invisible)
    }
}
