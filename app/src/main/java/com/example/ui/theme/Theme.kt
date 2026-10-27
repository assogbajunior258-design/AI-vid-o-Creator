package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = ElectricViolet,
    onPrimary = Color.White,
    primaryContainer = ElectricVioletDark,
    onPrimaryContainer = ElectricVioletLight,
    secondary = NeonCyan,
    onSecondary = Color.Black,
    secondaryContainer = NeonCyanDark,
    onSecondaryContainer = NeonCyanLight,
    tertiary = AmberWarning,
    onTertiary = Color.Black,
    background = StudioDarkBg,
    onBackground = TextPrimary,
    surface = StudioDarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = StudioCardBg,
    onSurfaceVariant = TextSecondary,
    outline = StudioBorder,
    outlineVariant = StudioDivider,
    error = RoseError,
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricVioletDark,
    onPrimary = Color.White,
    primaryContainer = ElectricVioletLight,
    onPrimaryContainer = Color.White,
    secondary = NeonCyanDark,
    onSecondary = Color.White,
    secondaryContainer = NeonCyanLight,
    onSecondaryContainer = Color.Black,
    tertiary = AmberWarning,
    onTertiary = Color.Black,
    background = StudioLightBg,
    onBackground = TextPrimaryLight,
    surface = StudioLightSurface,
    onSurface = TextPrimaryLight,
    surfaceVariant = StudioLightCard,
    onSurfaceVariant = TextSecondaryLight,
    outline = StudioLightBorder,
    outlineVariant = StudioDivider,
    error = RoseError,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to studio dark aesthetic as requested in specifications
    dynamicColor: Boolean = false, // Preserve crafted studio neon violet branding
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
