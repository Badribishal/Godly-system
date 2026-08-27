package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun MyApplicationTheme(
    themeMode: AppThemeMode = AppThemeMode.AUTO,
    palette: RarePalette = RarePalette.ARCHETYPE_RESONANCE,
    soul: com.example.data.model.SoulIdentity? = null,
    content: @Composable () -> Unit
) {
    val isSystemDark = isSystemInDarkTheme()
    val isDark = when (themeMode) {
        AppThemeMode.AUTO -> isSystemDark
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
    }

    val archetypeProfile = ArchetypeThemeEngine.getThemeForSoul(soul)
    val colorScheme = DynamicThemeBuilder.buildColorScheme(palette, isDark, soul)

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography
    ) {
        // Enforce default bodyMedium text style and provide ambient active archetype profile
        CompositionLocalProvider(
            LocalTextStyle provides Typography.bodyMedium,
            LocalArchetypeTheme provides archetypeProfile
        ) {
            content()
        }
    }
}



