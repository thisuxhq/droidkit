package com.droidkit.registry.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AppThemeTest {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun lightPrimaryMatchesLightAccentToken() {
        var primary = Color.Unspecified

        composeRule.setContent {
            AppTheme(darkTheme = false) {
                primary = MaterialTheme.colorScheme.primary
            }
        }

        assertEquals(Color(0xFF1C2B24), primary)
    }

    @Test
    fun darkPrimaryMatchesDarkAccentToken() {
        var primary = Color.Unspecified

        composeRule.setContent {
            AppTheme(darkTheme = true) {
                primary = MaterialTheme.colorScheme.primary
            }
        }

        assertEquals(Color(0xFFC8E6C9), primary)
    }

    @Test
    fun spacingMdIsSixteenDp() {
        var md = 0.dp

        composeRule.setContent {
            AppTheme {
                md = AppTheme.spacing.md
            }
        }

        assertEquals(16.dp, md)
    }

    @Test
    fun themeVersionIsOne() {
        assertEquals(1, THEME_VERSION)
    }
}
