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
    //secondaryContainer = Color(0xFF232B3E),
    //onSecondaryContainer = Color(0xFFE2E6F0),

    tertiary = Color(0xFFF5B342),
    onTertiary = Color(0xFF3B2600),
    tertiaryContainer = Color(0xFF4A3410),
    onTertiaryContainer = Color(0xFFFFD58A),

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

private val LightColorScheme = lightColorScheme(
    primary = LightBlue,
    secondary = Amber,
    tertiary = Purple

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
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