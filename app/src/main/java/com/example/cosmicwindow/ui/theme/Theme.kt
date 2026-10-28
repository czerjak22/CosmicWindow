package com.example.cosmicwindow.ui.theme

import android.app.Activity
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

    primary = Color(0xFF38BDF8),
    onPrimary = Color(0xFF004965),
    primaryContainer = Color(0xFF080E1D),
    onPrimaryContainer = Color(0xFF8ED5FF),

    secondary = Color(0xFF151B2B),
    onSecondary = Color(0xFFBDC8D1),
    secondaryContainer = Color(0xFF242A3A),
    //onSecondaryContainer = Color(0xFFE2E6F0),

    //tertiary = Color(0xFFEE9800),
    //onTertiary = Color(0xFFFFB95F),
    tertiaryContainer = Color(0xFFEE9800),
    onTertiaryContainer = Color(0xFFFFB95F),

    error = Color(0xFF93000A),
    onError = Color(0xFFFFB4AB),
//    errorContainer = Color(0xFF93000A),
//    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF0D1322),
    onBackground = Color(0xFFDDE2F8),
    surface = Color(0xFF151B2B),
    onSurface = Color(0xFFBDC8D1),
    surfaceVariant = Color(0xFF191F2F),
    onSurfaceVariant = Color(0xFF87929A),

    outline = Color(0xFF3E484F),
    //outlineVariant = Color(0xFFFFFFFF),
)

private val LightColorScheme = lightColorScheme(

    primary = Color(0xFF38BDF8),
    onPrimary = Color(0xFF004965),
    primaryContainer = Color(0xFF080E1D),
    onPrimaryContainer = Color(0xFF8ED5FF),

    secondary = Color(0xFF151B2B),
    onSecondary = Color(0xFFBDC8D1),
    secondaryContainer = Color(0xFF242A3A),
    //onSecondaryContainer = Color(0xFFE2E6F0),

    //tertiary = Color(0xFFEE9800),
    //onTertiary = Color(0xFFFFB95F),
    tertiaryContainer = Color(0xFFEE9800),
    onTertiaryContainer = Color(0xFFFFB95F),

    error = Color(0xFF93000A),
    onError = Color(0xFFFFB4AB),
//    errorContainer = Color(0xFF93000A),
//    onErrorContainer = Color(0xFFFFDAD6),

    background = Color(0xFF0D1322),
    onBackground = Color(0xFFDDE2F8),
    surface = Color(0xFF151B2B),
    onSurface = Color(0xFFBDC8D1),
//    surfaceVariant = Color(0xFF1A2236),
//    onSurfaceVariant = Color(0xFFC5CBD6),

    outline = Color(0xFF3E484F),
    //outlineVariant = Color(0xFFFFFFFF),
)

@Composable
fun CosmicWindowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}