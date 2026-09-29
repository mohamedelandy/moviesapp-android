/**
 * File: Theme.kt
 * Brief: Part of the Movies App Showcase project.
 * It follows Clean Architecture, SOLID, and Result patterns.
 */

package com.nady.moviesapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import com.nady.moviesapp.domain.model.AppThemeMode

@Immutable
data class ExtendedSpatialColors(
    val glassBase: Color,
    val glassBorder: Color,
    val glassHighlight: Color,
    val neonGlow: Color,
    val textSecondary: Color,
    val textMuted: Color,
    val spatialCyan: Color,
    val spatialPurple: Color
)

val LocalExtendedSpatialColors = staticCompositionLocalOf {
    ExtendedSpatialColors(
        glassBase = DarkGlassBase,
        glassBorder = DarkGlassBorder,
        glassHighlight = DarkGlassHighlight,
        neonGlow = BrandAccentGlow,
        textSecondary = DarkOnSurfaceSecondary,
        textMuted = DarkOnSurfaceMuted,
        spatialCyan = SpatialCyan,
        spatialPurple = SpatialPurple
    )
}

private val DarkColorScheme = darkColorScheme(
    primary = BrandAccent,
    onPrimary = Color.White,
    primaryContainer = BrandAccent,
    onPrimaryContainer = Color.White,
    secondary = SpatialCyan,
    onSecondary = Color.Black,
    secondaryContainer = DarkSurfaceContainerHighest,
    onSecondaryContainer = DarkOnSurface,
    tertiary = SpatialPurple,
    onTertiary = Color.White,
    background = DarkSurfaceLowest,
    onBackground = DarkOnSurface,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceContainer,
    onSurfaceVariant = DarkOnSurfaceSecondary,
    surfaceContainerLowest = DarkSurfaceLowest,
    surfaceContainerLow = DarkSurfaceLowest,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    outline = DarkGlassBorder
)

private val LightColorScheme = lightColorScheme(
    primary = BrandAccent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFEBEB),
    onPrimaryContainer = Color(0xFF8A000A),
    secondary = SpatialCyan,
    onSecondary = Color.White,
    secondaryContainer = LightSurfaceContainerHigh,
    onSecondaryContainer = LightOnSurface,
    tertiary = SpatialPurple,
    onTertiary = Color.White,
    background = LightSurface,
    onBackground = LightOnSurface,
    surface = LightSurfaceContainer,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceContainerHigh,
    onSurfaceVariant = LightOnSurfaceSecondary,
    surfaceContainerLowest = LightSurfaceLowest,
    surfaceContainerLow = LightSurfaceLowest,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    outline = LightGlassBorder
)

@Composable
fun MoviesAppTheme(
    themeMode: AppThemeMode = AppThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode) {
        AppThemeMode.DARK -> true
        AppThemeMode.LIGHT -> false
        AppThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) DarkColorScheme else LightColorScheme

    val extendedColors = if (isDark) {
        ExtendedSpatialColors(
            glassBase = DarkGlassBase,
            glassBorder = DarkGlassBorder,
            glassHighlight = DarkGlassHighlight,
            neonGlow = BrandAccentGlow,
            textSecondary = DarkOnSurfaceSecondary,
            textMuted = DarkOnSurfaceMuted,
            spatialCyan = SpatialCyan,
            spatialPurple = SpatialPurple
        )
    } else {
        ExtendedSpatialColors(
            glassBase = LightGlassBase,
            glassBorder = LightGlassBorder,
            glassHighlight = LightGlassHighlight,
            neonGlow = BrandAccentGlow,
            textSecondary = LightOnSurfaceSecondary,
            textMuted = LightOnSurfaceMuted,
            spatialCyan = SpatialCyan,
            spatialPurple = SpatialPurple
        )
    }

    CompositionLocalProvider(LocalExtendedSpatialColors provides extendedColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}

val MaterialTheme.spatialColors: ExtendedSpatialColors
    @Composable
    get() = LocalExtendedSpatialColors.current
