package com.example.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import com.example.data.model.AdvancedArchetype
import com.example.data.model.AdvancedArchetypesCatalog
import com.example.data.model.ArchetypeCategory
import com.example.data.model.SoulIdentity

/**
 * Elemental & Archetypal Theme Classification.
 * Determines the color temperature, particle aura, and ambient tones.
 */
enum class ElementThemeFamily(
    val displayName: String,
    val rune: String,
    val description: String
) {
    FIRE_SOLAR("Solar Incandescence", "🔥", "Warmer gold, solar amber, molten flame, and auric warmth."),
    CELESTIAL_ASTRAL("Celestial Starlight", "✨", "Cooler ethereal amethyst, starlight cyan, nebula violet, and astral shimmer."),
    SHADOW_NETHER("Abyssal Nether", "🌑", "Deep crimson ruby, nether violet, obsidian umbra, and eclipse flares."),
    PRIMORDIAL_EARTH("Primordial Earth", "🌿", "Verdant emerald, tectonic jade, earth amber, and living vitality."),
    COSMIC_SINGULARITY("Cosmic Singularity", "🌌", "Quantum indigo, radiant gold, prismatic opal, and sovereign aether."),
    INITIATE_AETHER("Initiate Aether", "💠", "Clear sky blue, ethereal cyan, and awakening dawn gold.")
}

/**
 * Complete Theme Profile tailored to an Archetype or Class.
 */
data class ArchetypeThemeProfile(
    val archetypeId: String,
    val name: String,
    val subtitle: String,
    val characterClass: String,
    val celestialRace: String,
    val element: String,
    val family: ElementThemeFamily,
    val primary: Color,
    val secondary: Color,
    val tertiary: Color,
    val accentGlow: Color,
    val surfaceDark: Color,
    val surfaceVariantDark: Color,
    val borderDark: Color,
    val backgroundDark: Color,
    val backgroundLight: Color,
    val surfaceLight: Color,
    val textHighlight: Color,
    val isWarm: Boolean,
    val isCoolEthereal: Boolean,
    val ambientGlowList: List<Color>,
    val flavorDescription: String
) {
    fun toDarkColorScheme(): ColorScheme {
        return darkColorScheme(
            primary = primary,
            onPrimary = Color.Black,
            primaryContainer = primary.copy(alpha = 0.22f),
            onPrimaryContainer = textHighlight,

            secondary = secondary,
            onSecondary = Color.Black,
            secondaryContainer = secondary.copy(alpha = 0.22f),
            onSecondaryContainer = Color(0xFFDDD6FE),

            tertiary = tertiary,
            onTertiary = Color.Black,
            tertiaryContainer = tertiary.copy(alpha = 0.20f),
            onTertiaryContainer = Color(0xFFCFFAFE),

            background = backgroundDark,
            onBackground = Color(0xFFF8FAFC),

            surface = surfaceDark,
            onSurface = Color(0xFFF8FAFC),
            surfaceVariant = surfaceVariantDark,
            onSurfaceVariant = Color(0xFFCBD5E1),

            outline = borderDark,
            outlineVariant = primary.copy(alpha = 0.25f),

            error = Color(0xFFEF4444),
            onError = Color.White
        )
    }

    fun toLightColorScheme(): ColorScheme {
        return lightColorScheme(
            primary = primary,
            onPrimary = Color.White,
            primaryContainer = primary.copy(alpha = 0.16f),
            onPrimaryContainer = if (isWarm) Color(0xFF78350F) else Color(0xFF1E1B4B),

            secondary = secondary,
            onSecondary = Color.White,
            secondaryContainer = secondary.copy(alpha = 0.16f),
            onSecondaryContainer = Color(0xFF4C1D95),

            tertiary = tertiary,
            onTertiary = Color.White,
            tertiaryContainer = tertiary.copy(alpha = 0.16f),
            onTertiaryContainer = Color(0xFF155E75),

            background = backgroundLight,
            onBackground = Color(0xFF0F172A),

            surface = surfaceLight,
            onSurface = Color(0xFF0F172A),
            surfaceVariant = Color(0xFFF1F5F9),
            onSurfaceVariant = Color(0xFF475569),

            outline = secondary.copy(alpha = 0.35f),
            outlineVariant = primary.copy(alpha = 0.25f),

            error = Color(0xFFDC2626),
            onError = Color.White
        )
    }
}

/**
 * CompositionLocal for ambient access to active Archetype Theme Profile across the entire UI.
 */
val LocalArchetypeTheme = compositionLocalOf {
    ArchetypeThemeEngine.getThemeForArchetype("arch_seeker")
}

object ArchetypeThemeEngine {

    /**
     * Resolves the full ArchetypeThemeProfile by archetype id.
     */
    fun getThemeForArchetype(archetypeId: String): ArchetypeThemeProfile {
        val arch = AdvancedArchetypesCatalog.getArchetypeById(archetypeId)
        return buildProfileForArchetype(arch)
    }

    /**
     * Resolves the theme from the user's SoulIdentity.
     */
    fun getThemeForSoul(soul: SoulIdentity?): ArchetypeThemeProfile {
        val archetypeId = soul?.attunedArchetypeId ?: "arch_seeker"
        return getThemeForArchetype(archetypeId)
    }

    private fun buildProfileForArchetype(arch: AdvancedArchetype): ArchetypeThemeProfile {
        return when (arch.id) {
            // ==========================================
            // WARM SOLAR / FIRE / DAWN CLASSES
            // ==========================================
            "arch_solar_phoenix" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.FIRE_SOLAR,
                primary = Color(0xFFFB923C), // Warm Solar Flame
                secondary = Color(0xFFEA580C), // Molten Orange
                tertiary = Color(0xFFFDE047), // Auric Spark
                accentGlow = Color(0xFFFF7828),
                surfaceDark = Color(0xFF0F0704),
                surfaceVariantDark = Color(0xFF1C0E07),
                borderDark = Color(0xFF7C2D12).copy(alpha = 0.6f),
                backgroundDark = Color(0xFF040201),
                backgroundLight = Color(0xFFFFF7ED),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFED7AA),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFFB923C), Color(0xFFEA580C), Color(0xFFFDE047)),
                flavorDescription = "Everlasting Solar Flame • Warm Molten Amber & Crimson Radiance"
            )

            "arch_luminous_aegis" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.FIRE_SOLAR,
                primary = Color(0xFFFBBF24), // Solar Gold
                secondary = Color(0xFFD97706), // Warm Amber
                tertiary = Color(0xFFFB7185), // Coral Warmth
                accentGlow = Color(0xFFF59E0B),
                surfaceDark = Color(0xFF0D0A04),
                surfaceVariantDark = Color(0xFF1A1408),
                borderDark = Color(0xFF78350F).copy(alpha = 0.5f),
                backgroundDark = Color(0xFF030201),
                backgroundLight = Color(0xFFFFFBEB),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFDE68A),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFFBBF24), Color(0xFFD97706), Color(0xFFFDE047)),
                flavorDescription = "Solar Luminescence • Golden Sunbeam Barrier & Amber Heat"
            )

            "arch_solar_seraph" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.FIRE_SOLAR,
                primary = Color(0xFFFCD34D), // Seventh Dawn
                secondary = Color(0xFFF59E0B), // Radiant Sun
                tertiary = Color(0xFFFB7185), // Sunset Glow
                accentGlow = Color(0xFFFEF08A),
                surfaceDark = Color(0xFF0E0B05),
                surfaceVariantDark = Color(0xFF1B150A),
                borderDark = Color(0xFF854D0E).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF030201),
                backgroundLight = Color(0xFFFFFDF0),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFEF08A),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFFCD34D), Color(0xFFF59E0B), Color(0xFFFEF08A)),
                flavorDescription = "Seventh Dawn Luminescence • Radiant Solar Seraphic Flare"
            )

            "arch_celestial_archon" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.FIRE_SOLAR,
                primary = Color(0xFFEAB308), // Imperial Crown Plasma
                secondary = Color(0xFFF59E0B), // Auric Saffron
                tertiary = Color(0xFFA855F7), // Royal Amethyst
                accentGlow = Color(0xFFFDE047),
                surfaceDark = Color(0xFF0D0A06),
                surfaceVariantDark = Color(0xFF1A130A),
                borderDark = Color(0xFF854D0E).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF020101),
                backgroundLight = Color(0xFFFEFCE8),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFEF08A),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFEAB308), Color(0xFFF59E0B), Color(0xFFA855F7)),
                flavorDescription = "Solar Crown Plasma • Sovereign Auric Fire & Royal Amethyst"
            )

            "arch_solar_justiciar" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.FIRE_SOLAR,
                primary = Color(0xFFFBBF24), // Blade of Truth
                secondary = Color(0xFFEA580C), // Solar Fire
                tertiary = Color(0xFFE0E7FF), // Pure Light
                accentGlow = Color(0xFFFDE68A),
                surfaceDark = Color(0xFF0E0803),
                surfaceVariantDark = Color(0xFF1C1108),
                borderDark = Color(0xFF7C2D12).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF030100),
                backgroundLight = Color(0xFFFFFBEB),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFDE68A),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFFBBF24), Color(0xFFEA580C), Color(0xFFE0E7FF)),
                flavorDescription = "Pure White Light • Incandescent Solar Discernment Blade"
            )

            "arch_solar_deity" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.FIRE_SOLAR,
                primary = Color(0xFFF59E0B), // Apex Solar Gold
                secondary = Color(0xFFEA580C), // Supernova Orange
                tertiary = Color(0xFFFDE047), // Infinite Dawn
                accentGlow = Color(0xFFFEF08A),
                surfaceDark = Color(0xFF0D0602),
                surfaceVariantDark = Color(0xFF1B0E06),
                borderDark = Color(0xFF9A3412).copy(alpha = 0.6f),
                backgroundDark = Color(0xFF020100),
                backgroundLight = Color(0xFFFFF7ED),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFED7AA),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFF59E0B), Color(0xFFEA580C), Color(0xFFFEF08A)),
                flavorDescription = "Hyper-Luminescent Dawn • Living Creation Sun & Supernova Heat"
            )

            // ==========================================
            // COOL ETHEREAL / CELESTIAL / ASTRAL CLASSES
            // ==========================================
            "arch_astral_sage" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.CELESTIAL_ASTRAL,
                primary = Color(0xFF818CF8), // Astral Indigo
                secondary = Color(0xFFA855F7), // Nebula Violet
                tertiary = Color(0xFF38BDF8), // Starlight Cyan
                accentGlow = Color(0xFFC7D2FE),
                surfaceDark = Color(0xFF070712),
                surfaceVariantDark = Color(0xFF0F1022),
                borderDark = Color(0xFF3730A3).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF010105),
                backgroundLight = Color(0xFFEEF2FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFC7D2FE),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFF818CF8), Color(0xFFA855F7), Color(0xFF38BDF8)),
                flavorDescription = "Nebula Starlight • Cool Ethereal Indigo & Violet Harmonics"
            )

            "arch_ethereal_weaver" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.CELESTIAL_ASTRAL,
                primary = Color(0xFFC084FC), // Ethereal Orchid
                secondary = Color(0xFF06B6D4), // Astral Cyan
                tertiary = Color(0xFF38BDF8), // Ethereal Sky
                accentGlow = Color(0xFFE9D5FF),
                surfaceDark = Color(0xFF090614),
                surfaceVariantDark = Color(0xFF130E26),
                borderDark = Color(0xFF581C87).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF020106),
                backgroundLight = Color(0xFFFAF5FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFE9D5FF),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFFC084FC), Color(0xFF06B6D4), Color(0xFFE9D5FF)),
                flavorDescription = "Aether Harmony • Cool Celestial Orchid & Luminous Sky Cyan"
            )

            "arch_astral_chronomancer" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.CELESTIAL_ASTRAL,
                primary = Color(0xFFA78BFA), // Chrono Violet
                secondary = Color(0xFF06B6D4), // Temporal Cyan
                tertiary = Color(0xFFFDE68A), // Stardust Gold
                accentGlow = Color(0xFFDDD6FE),
                surfaceDark = Color(0xFF080612),
                surfaceVariantDark = Color(0xFF110E24),
                borderDark = Color(0xFF4C1D95).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF020105),
                backgroundLight = Color(0xFFF5F3FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFDDD6FE),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFFA78BFA), Color(0xFF06B6D4), Color(0xFFFDE68A)),
                flavorDescription = "Chrono-Aether • Ethereal Temporal Waves & Starlight Lavender"
            )

            "arch_starborne_oracle" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.CELESTIAL_ASTRAL,
                primary = Color(0xFFD946EF), // Cosmic Magenta
                secondary = Color(0xFF6366F1), // Deep Nebula Indigo
                tertiary = Color(0xFF38BDF8), // Ethereal Cyan
                accentGlow = Color(0xFFF5D0FE),
                surfaceDark = Color(0xFF0C0412),
                surfaceVariantDark = Color(0xFF1A0A26),
                borderDark = Color(0xFF701A75).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF030106),
                backgroundLight = Color(0xFFFDF4FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFF5D0FE),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFFD946EF), Color(0xFF6366F1), Color(0xFF38BDF8)),
                flavorDescription = "Nebula Consciousness • Celestial Orchid Magenta & Cosmic Blue"
            )

            "arch_alchemical_sovereign" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.CELESTIAL_ASTRAL,
                primary = Color(0xFFA855F7), // Astral Quicksilver
                secondary = Color(0xFF06B6D4), // Quicksilver Cyan
                tertiary = Color(0xFFF59E0B), // Philosopher Amber
                accentGlow = Color(0xFFE9D5FF),
                surfaceDark = Color(0xFF090612),
                surfaceVariantDark = Color(0xFF130E24),
                borderDark = Color(0xFF581C87).copy(alpha = 0.5f),
                backgroundDark = Color(0xFF020105),
                backgroundLight = Color(0xFFFAF5FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFE9D5FF),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFFA855F7), Color(0xFF06B6D4), Color(0xFFF59E0B)),
                flavorDescription = "Astral Quicksilver • Dual Harmonics of Mystic Violet & Cyan"
            )

            // ==========================================
            // SHADOW / VOID / NETHER CLASSES
            // ==========================================
            "arch_shadow_weaver" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.SHADOW_NETHER,
                primary = Color(0xFFEF4444), // Abyssal Ruby
                secondary = Color(0xFF7C3AED), // Nether Violet
                tertiary = Color(0xFFFB7185), // Blood Rose
                accentGlow = Color(0xFFFCA5A5),
                surfaceDark = Color(0xFF0F0407),
                surfaceVariantDark = Color(0xFF1E080E),
                borderDark = Color(0xFF881337).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF030102),
                backgroundLight = Color(0xFFFFF1F2),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFECDD3),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFEF4444), Color(0xFF7C3AED), Color(0xFFFB7185)),
                flavorDescription = "Nether Umbra • Transmuted Shadow Ruby & Sanguine Sparks"
            )

            "arch_void_walker" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.SHADOW_NETHER,
                primary = Color(0xFFF43F5E), // Singularity Eclipse
                secondary = Color(0xFF6B21A8), // Abyssal Purple
                tertiary = Color(0xFF06B6D4), // Void Spark
                accentGlow = Color(0xFFFDA4AF),
                surfaceDark = Color(0xFF0E0308),
                surfaceVariantDark = Color(0xFF1C0610),
                borderDark = Color(0xFF881337).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF030102),
                backgroundLight = Color(0xFFFFF1F2),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFECDD3),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFFF43F5E), Color(0xFF6B21A8), Color(0xFF06B6D4)),
                flavorDescription = "Singularity Eclipse • Phantom Blood Rose & Deep Abyss Velvet"
            )

            "arch_abyssal_leviathan" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.SHADOW_NETHER,
                primary = Color(0xFF0284C7), // Deep Ocean Azure
                secondary = Color(0xFF0EA5E9), // Leviathan Cyan
                tertiary = Color(0xFF0F766E), // Abyssal Trench Teal
                accentGlow = Color(0xFF7DD3FC),
                surfaceDark = Color(0xFF030910),
                surfaceVariantDark = Color(0xFF071422),
                borderDark = Color(0xFF075985).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF010206),
                backgroundLight = Color(0xFFF0F9FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFBAE6FD),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFF0284C7), Color(0xFF0EA5E9), Color(0xFF0F766E)),
                flavorDescription = "Deep Ocean Umbra • Subterranean Azure & Bioluminescent Trench"
            )

            "arch_nether_monarch" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.SHADOW_NETHER,
                primary = Color(0xFF991B1B), // Black Solar Fire
                secondary = Color(0xFF581C87), // Royal Umbra
                tertiary = Color(0xFFD97706), // Dark Flame
                accentGlow = Color(0xFFEF4444),
                surfaceDark = Color(0xFF0D0204),
                surfaceVariantDark = Color(0xFF1B0509),
                borderDark = Color(0xFF7F1D1D).copy(alpha = 0.6f),
                backgroundDark = Color(0xFF020001),
                backgroundLight = Color(0xFFFEF2F2),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFECACA),
                isWarm = true,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFF991B1B), Color(0xFF581C87), Color(0xFFD97706)),
                flavorDescription = "Black Solar Fire • Sovereign Crimson Obsidian & Dark Sun Flares"
            )

            // ==========================================
            // PRIMORDIAL / EARTH / TITAN / NATURE CLASSES
            // ==========================================
            "arch_earth_titan" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.PRIMORDIAL_EARTH,
                primary = Color(0xFF10B981), // Primordial Jade
                secondary = Color(0xFF059669), // Tectonic Emerald
                tertiary = Color(0xFFD97706), // Earth Magma
                accentGlow = Color(0xFF6EE7B7),
                surfaceDark = Color(0xFF030C07),
                surfaceVariantDark = Color(0xFF071B0F),
                borderDark = Color(0xFF065F46).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF010402),
                backgroundLight = Color(0xFFECFDF5),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFA7F3D0),
                isWarm = false,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFF10B981), Color(0xFF059669), Color(0xFFD97706)),
                flavorDescription = "Tectonic Magma • Immovable Primordial Emerald & Core Magma"
            )

            "arch_tempest_primordial" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.PRIMORDIAL_EARTH,
                primary = Color(0xFF06B6D4), // Galactic Lightning Cyan
                secondary = Color(0xFF38BDF8), // Storm Electric Blue
                tertiary = Color(0xFF8B5CF6), // Thunder Violet
                accentGlow = Color(0xFF67E8F9),
                surfaceDark = Color(0xFF030B10),
                surfaceVariantDark = Color(0xFF061822),
                borderDark = Color(0xFF155E75).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF010306),
                backgroundLight = Color(0xFFECFEFF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFA5F3FC),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFF06B6D4), Color(0xFF38BDF8), Color(0xFF8B5CF6)),
                flavorDescription = "Galactic Tempest • Living Celestial Lightning & Electric Sky"
            )

            "arch_yggdrasil_titan" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.PRIMORDIAL_EARTH,
                primary = Color(0xFF15803D), // Cosmic Arbor
                secondary = Color(0xFF10B981), // Verdant Jade
                tertiary = Color(0xFFF59E0B), // Solar Sap
                accentGlow = Color(0xFF86EFAC),
                surfaceDark = Color(0xFF020B05),
                surfaceVariantDark = Color(0xFF061A0C),
                borderDark = Color(0xFF166534).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF010301),
                backgroundLight = Color(0xFFF0FDF4),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFBBF7D0),
                isWarm = false,
                isCoolEthereal = false,
                ambientGlowList = listOf(Color(0xFF15803D), Color(0xFF10B981), Color(0xFFF59E0B)),
                flavorDescription = "Cosmic Arbor • Ancient World Tree Jade & Radiant Solar Sap"
            )

            // ==========================================
            // COSMIC SINGULARITY & INITIATE
            // ==========================================
            "arch_fate_weaver" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.COSMIC_SINGULARITY,
                primary = Color(0xFF6366F1), // Quantum Indigo
                secondary = Color(0xFF8B5CF6), // Probability Violet
                tertiary = Color(0xFF06B6D4), // Cosmic Cyan
                accentGlow = Color(0xFFC7D2FE),
                surfaceDark = Color(0xFF060614),
                surfaceVariantDark = Color(0xFF0D0E26),
                borderDark = Color(0xFF3730A3).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF010106),
                backgroundLight = Color(0xFFEEF2FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFC7D2FE),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFF6366F1), Color(0xFF8B5CF6), Color(0xFF06B6D4)),
                flavorDescription = "Quantum Probability • Weaver of Timelines & Indigo Causality"
            )

            "arch_old_god_avatar" -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.COSMIC_SINGULARITY,
                primary = Color(0xFFF59E0B), // Omnipresent Godhead Gold
                secondary = Color(0xFF8B5CF6), // Cosmic Singularity
                tertiary = Color(0xFF06B6D4), // Ethereal Cyan
                accentGlow = Color(0xFFFDE047),
                surfaceDark = Color(0xFF090610),
                surfaceVariantDark = Color(0xFF140D20),
                borderDark = Color(0xFF6B21A8).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF020104),
                backgroundLight = Color(0xFFFAF5FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFFEF08A),
                isWarm = true,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFFF59E0B), Color(0xFF8B5CF6), Color(0xFF06B6D4)),
                flavorDescription = "Omnipresent Cosmos • Transcendent Singularity Gold & Amethyst"
            )

            // Default / Initiate Seeker
            else -> ArchetypeThemeProfile(
                archetypeId = arch.id,
                name = arch.name,
                subtitle = arch.subtitle,
                characterClass = arch.characterClass,
                celestialRace = arch.celestialRace,
                element = arch.element,
                family = ElementThemeFamily.INITIATE_AETHER,
                primary = Color(0xFF60A5FA), // Awakening Sky Blue
                secondary = Color(0xFF8B5CF6), // Celestial Violet
                tertiary = Color(0xFFF59E0B), // Dawn Gold
                accentGlow = Color(0xFF93C5FD),
                surfaceDark = Color(0xFF060912),
                surfaceVariantDark = Color(0xFF0E1322),
                borderDark = Color(0xFF1E3A8A).copy(alpha = 0.55f),
                backgroundDark = Color(0xFF010206),
                backgroundLight = Color(0xFFEFF6FF),
                surfaceLight = Color(0xFFFFFFFF),
                textHighlight = Color(0xFFBFDBFE),
                isWarm = false,
                isCoolEthereal = true,
                ambientGlowList = listOf(Color(0xFF60A5FA), Color(0xFF8B5CF6), Color(0xFFF59E0B)),
                flavorDescription = "Aether / Unattuned • Pristine Awakening Blue & Dawn Gold"
            )
        }
    }
}
