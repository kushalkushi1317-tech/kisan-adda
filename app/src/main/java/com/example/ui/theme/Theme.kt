package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = HarvestGreenPrimary,
    onPrimary = Color.White,
    primaryContainer = HarvestGreenContainer,
    onPrimaryContainer = OnHarvestGreenContainer,
    secondary = SunGold,
    onSecondary = Color.White,
    secondaryContainer = SunGoldContainer,
    onSecondaryContainer = OnSunGoldContainer,
    tertiary = HarvestGreenLight,
    background = AgriculturalBackground,
    surface = CardSurface,
    onBackground = TextDark,
    onSurface = TextDark,
    surfaceVariant = Color(0xFFEDF2EC),
    onSurfaceVariant = TextSubtle,
    outline = BorderSubtle
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4ADE80),
    onPrimary = Color(0xFF052E12),
    primaryContainer = HarvestGreenDark,
    onPrimaryContainer = Color(0xFFD6F5DC),
    secondary = SunGoldLight,
    onSecondary = Color(0xFF451A03),
    secondaryContainer = Color(0xFF78350F),
    onSecondaryContainer = Color(0xFFFEF3C7),
    background = Color(0xFF111813),
    surface = Color(0xFF19241C),
    onBackground = Color(0xFFF3F4F6),
    onSurface = Color(0xFFF3F4F6),
    surfaceVariant = Color(0xFF233226),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF374151)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep consistent agricultural branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
