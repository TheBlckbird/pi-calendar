package com.louisweigel.pi_calendar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = androidx.compose.material3.darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = androidx.compose.material3.lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
expect fun platformColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme,
): ColorScheme

@Composable
fun PiCalendarTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = platformColorScheme(
        darkTheme = darkTheme,
        dynamicColor = dynamicColor,
        lightScheme = LightColorScheme,
        darkScheme = DarkColorScheme
    )

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}