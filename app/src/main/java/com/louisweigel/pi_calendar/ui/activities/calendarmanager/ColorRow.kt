package com.louisweigel.pi_calendar.ui.activities.calendarmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


/**
 * Shows a circle filled with the color and its name to its right
 *
 * @param[colorNamePair] The combination of the color and its name
 */
@Composable
fun ColorRow(
    colorNamePair: Pair<String, Color>,
    textContent: @Composable (String) -> Unit = { text ->
        Text(text)
    }
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        ColorCircle(colorNamePair.component2())
        textContent(colorNamePair.component1())
    }
}

/**
 * A circle filled with a color and a given size (24dp by default)
 *
 * @param[color] The color of the circle
 * @param[size] The size of the circle
 */
@Composable
private fun ColorCircle(
    color: Color,
    size: Dp = 24.dp,
) {
    Box(
        Modifier
            .size(size)
            .background(color = color, shape = CircleShape)
    )
}