package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.AUTO,
    palette: RarePalette = RarePalette.CELESTIAL_TWILIGHT,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.AUTO -> isSystemDark
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
    }

    val colorScheme = DynamicThemeBuilder.buildColorScheme(palette, isDark)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        // Enforce default bodyMedium text style with zero font-padding and centered alignment
        CompositionLocalProvider(
            LocalTextStyle provides Typography.bodyMedium
        ) {
            content()
        }
    }
}



