package com.louisweigel.pi_calendar.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun platformColorScheme(
    darkTheme: Boolean,
    dynamicColor: Boolean,
    lightScheme: ColorScheme,
    darkScheme: ColorScheme,
): ColorScheme {
    return if (darkTheme) darkScheme else lightScheme
}