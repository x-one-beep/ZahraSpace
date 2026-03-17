package com.zahra.space.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val LightColors = lightColorScheme(
    primary = IslamicGreen,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = IslamicGreenLight,
    onPrimaryContainer = androidx.compose.ui.graphics.Color.White,
    secondary = IslamicGold,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    secondaryContainer = IslamicGoldLight,
    onSecondaryContainer = androidx.compose.ui.graphics.Color.Black,
    tertiary = IslamicNavy,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    background = IslamicCream,
    onBackground = androidx.compose.ui.graphics.Color.Black,
    surface = androidx.compose.ui.graphics.Color.White,
    onSurface = androidx.compose.ui.graphics.Color.Black,
    error = StatusError,
    onError = androidx.compose.ui.graphics.Color.White
)

private val DarkColors = darkColorScheme(
    primary = IslamicGreen,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = IslamicGreenDark,
    onPrimaryContainer = androidx.compose.ui.graphics.Color.White,
    secondary = IslamicGold,
    onSecondary = androidx.compose.ui.graphics.Color.Black,
    secondaryContainer = IslamicGoldDark,
    onSecondaryContainer = androidx.compose.ui.graphics.Color.White,
    tertiary = IslamicNavy,
    onTertiary = androidx.compose.ui.graphics.Color.White,
    background = IslamicGreenDark,
    onBackground = androidx.compose.ui.graphics.Color.White,
    surface = IslamicGreenDark,
    onSurface = androidx.compose.ui.graphics.Color.White,
    error = StatusError,
    onError = androidx.compose.ui.graphics.Color.White
)

@Composable
fun ZahraSpaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColors
        else -> LightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
