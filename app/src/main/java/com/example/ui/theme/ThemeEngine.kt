package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

enum class AppThemeMode(val displayName: String, val icon: String) {
    AUTO("Device Auto", "⚙️"),
    DARK("Dark Realm", "🌙"),
    LIGHT("Solar Sanctuary", "☀️")
}

enum class RarePalette(
    val id: String,
    val title: String,
    val description: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val darkBackground: Color,
    val lightBackground: Color
) {
    CELESTIAL_TWILIGHT(
        id = "celestial_twilight",
        title = "Celestial Twilight",
        description = "Royal Amethyst & Radiant Sun Gold over a true AMOLED black abyss.",
        primaryColor = Color(0xFFF59E0B),
        secondaryColor = Color(0xFF8B5CF6),
        accentColor = Color(0xFF06B6D4),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFFAF7FF)
    ),
    SOLAR_OBSIDIAN(
        id = "solar_obsidian",
        title = "Solar Obsidian",
        description = "Pure Molten Amber & Auric flares framed by pristine AMOLED onyx.",
        primaryColor = Color(0xFFF59E0B),
        secondaryColor = Color(0xFFD97706),
        accentColor = Color(0xFFFDE047),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFFFFDF5)
    ),
    CYBER_VOID(
        id = "cyber_void",
        title = "Cyber Neon Void",
        description = "Electric Cyan & Neon Magenta sparks cutting through true black void.",
        primaryColor = Color(0xFF06B6D4),
        secondaryColor = Color(0xFFEC4899),
        accentColor = Color(0xFFA855F7),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFF0FDF4)
    ),
    EMERALD_MYSTIC(
        id = "emerald_mystic",
        title = "Emerald Mystic Grove",
        description = "Deep Jade, Ancient Moss & Golden Sunbeams over true black canvas.",
        primaryColor = Color(0xFF10B981),
        secondaryColor = Color(0xFF059669),
        accentColor = Color(0xFFF59E0B),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFF0FDF4)
    ),
    CRIMSON_ECLIPSE(
        id = "crimson_eclipse",
        title = "Crimson Blood Moon",
        description = "Abyssal Ruby, Sanguine Rose & Dark Velvet over pure dark OLED canvas.",
        primaryColor = Color(0xFFEF4444),
        secondaryColor = Color(0xFFF43F5E),
        accentColor = Color(0xFFFB7185),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFFFF1F2)
    ),
    NORDIC_FROST(
        id = "nordic_frost",
        title = "Nordic Glacial Frost",
        description = "Arctic Ice Blue & Silver Steel cutting across deep AMOLED black.",
        primaryColor = Color(0xFF38BDF8),
        secondaryColor = Color(0xFF0284C7),
        accentColor = Color(0xFF94A3B8),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFF0F9FF)
    ),
    ASTRAL_ROSE(
        id = "astral_rose",
        title = "Astral Rose Gold",
        description = "Champagne Gold & Dusty Sunset Rose harmonics over AMOLED black.",
        primaryColor = Color(0xFFFB7185),
        secondaryColor = Color(0xFFFDE68A),
        accentColor = Color(0xFFA78BFA),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFFFF5F7)
    ),
    MIDNIGHT_VELVET(
        id = "midnight_velvet",
        title = "Midnight Velvet",
        description = "Royal Indigo & Electric Violet radiating from true black OLED space.",
        primaryColor = Color(0xFF818CF8),
        secondaryColor = Color(0xFFA855F7),
        accentColor = Color(0xFF38BDF8),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFF5F3FF)
    ),
    RADIANT_SOLAR(
        id = "radiant_solar",
        title = "Radiant Solar Dawn",
        description = "Luminous Golden Amber & Warm Sunlight on AMOLED black canvas.",
        primaryColor = Color(0xFFD97706),
        secondaryColor = Color(0xFFF59E0B),
        accentColor = Color(0xFFFBBF24),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFFFFDF0)
    ),
    PRISMATIC_OPAL(
        id = "prismatic_opal",
        title = "Prismatic Opal Light",
        description = "Iridescent Sky Cyan, Royal Violet & Luminous Crystal aura.",
        primaryColor = Color(0xFF0284C7),
        secondaryColor = Color(0xFF7C3AED),
        accentColor = Color(0xFF06B6D4),
        darkBackground = Color(0xFF000000),
        lightBackground = Color(0xFFF8FAFC)
    )
}

object DynamicThemeBuilder {
    fun buildColorScheme(palette: RarePalette, isDark: Boolean): ColorScheme {
        return if (isDark) {
            darkColorScheme(
                primary = palette.primaryColor,
                onPrimary = Color.Black,
                primaryContainer = palette.primaryColor.copy(alpha = 0.20f),
                onPrimaryContainer = Color(0xFFFDE68A),

                secondary = palette.secondaryColor,
                onSecondary = Color.Black,
                secondaryContainer = palette.secondaryColor.copy(alpha = 0.20f),
                onSecondaryContainer = Color(0xFFDDD6FE),

                tertiary = palette.accentColor,
                onTertiary = Color.Black,
                tertiaryContainer = palette.accentColor.copy(alpha = 0.20f),
                onTertiaryContainer = Color(0xFFCFFAFE),

                background = Color(0xFF000000), // Pure True AMOLED 0-watt Black
                onBackground = Color(0xFFF8FAFC),

                surface = Color(0xFF08080C), // Ultra Low-Power Surface
                onSurface = Color(0xFFF8FAFC),
                surfaceVariant = Color(0xFF12121A), // Battery-conscious surface container
                onSurfaceVariant = Color(0xFFCBD5E1),

                outline = palette.secondaryColor.copy(alpha = 0.30f),
                outlineVariant = palette.primaryColor.copy(alpha = 0.18f),

                error = Color(0xFFEF4444),
                onError = Color.White
            )
        } else {
            lightColorScheme(
                primary = palette.primaryColor,
                onPrimary = Color.White,
                primaryContainer = palette.primaryColor.copy(alpha = 0.18f),
                onPrimaryContainer = Color(0xFF78350F),

                secondary = palette.secondaryColor,
                onSecondary = Color.White,
                secondaryContainer = palette.secondaryColor.copy(alpha = 0.18f),
                onSecondaryContainer = Color(0xFF4C1D95),

                tertiary = palette.accentColor,
                onTertiary = Color.White,
                tertiaryContainer = palette.accentColor.copy(alpha = 0.18f),
                onTertiaryContainer = Color(0xFF155E75),

                background = palette.lightBackground,
                onBackground = Color(0xFF0F172A),

                surface = Color(0xFFFFFFFF),
                onSurface = Color(0xFF0F172A),
                surfaceVariant = Color(0xFFF1F5F9),
                onSurfaceVariant = Color(0xFF475569),

                outline = palette.secondaryColor.copy(alpha = 0.35f),
                outlineVariant = palette.primaryColor.copy(alpha = 0.25f),

                error = Color(0xFFDC2626),
                onError = Color.White
            )
        }
    }
}
