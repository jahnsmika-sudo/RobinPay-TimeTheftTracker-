package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val RobinPayDarkColorScheme = darkColorScheme(
    primary = Emerald400,
    onPrimary = Slate950,
    primaryContainer = Emerald600,
    onPrimaryContainer = Slate50,

    secondary = Amber400,
    onSecondary = Slate950,
    secondaryContainer = Amber600,
    onSecondaryContainer = Slate50,

    tertiary = Sky400,
    onTertiary = Slate950,

    background = Slate950,
    onBackground = Slate100,

    surface = Slate900,
    onSurface = Slate50,
    surfaceVariant = Slate800,
    onSurfaceVariant = Slate300,

    outline = Slate700,
    outlineVariant = Slate800,

    error = Rose500,
    onError = Color.White
)

private val RobinPayLightColorScheme = lightColorScheme(
    primary = Emerald600,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),

    secondary = Amber600,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFEF3C7),
    onSecondaryContainer = Color(0xFF78350F),

    tertiary = Sky500,
    onTertiary = Color.White,

    background = Color(0xFFF8FAFC),
    onBackground = Slate900,

    surface = Color.White,
    onSurface = Slate900,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Slate600,

    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),

    error = Rose600,
    onError = Color.White
)

@Composable
fun RobinPayTheme(
    darkTheme: Boolean = true, // Default to sleek Slate dark theme
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) RobinPayDarkColorScheme else RobinPayLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
