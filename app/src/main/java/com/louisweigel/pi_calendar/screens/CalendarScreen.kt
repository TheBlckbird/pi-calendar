package com.louisweigel.pi_calendar.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CalendarScreen() {
    CalendarGrid(Modifier.fillMaxSize(), listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So")) {
        repeat(42) { index ->
            val borderRadius = if (index == 0) {
                RoundedCornerShape(12.dp, 4.dp, 4.dp, 4.dp)
            } else if (index == 6) {
                RoundedCornerShape(4.dp, 12.dp, 4.dp, 4.dp)
            } else if (index == 35) {
                RoundedCornerShape(4.dp, 4.dp, 4.dp, 12.dp)
            } else if (index == 41) {
                RoundedCornerShape(4.dp, 4.dp, 12.dp, 4.dp)
            } else {
                RoundedCornerShape(4.dp)
            }

            CalendarCell((index + 1).toString(), borderRadius)
        }
    }

}

@Composable
fun CalendarGrid(
    modifier: Modifier = Modifier,
    columnLabels: List<String>,
    content: @Composable () -> Unit
) {
    val headerHeight = 23.dp

    Layout(
        modifier = modifier.fillMaxSize(),
        content = {
            // Header row first
            Row(
                modifier = Modifier
                    .height(headerHeight)
                    .fillMaxWidth()
            ) {
                columnLabels.forEach { label ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            label,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            content()
        }
    ) { measurables, constraints ->
        val numCols = 7
        val numRows = 6
        val headerHeight = headerHeight.roundToPx()
        val gridHeight = constraints.maxHeight - headerHeight
        val cellWidth = constraints.maxWidth / numCols
        val cellHeight = gridHeight / numRows

        val headerPlaceable = measurables[0].measure(constraints)

        val cellConstraints = Constraints.fixed(cellWidth, cellHeight)
        val cellPlaceables = measurables.drop(1).map { it.measure(cellConstraints) }

        layout(constraints.maxWidth, constraints.maxHeight) {
            headerPlaceable.place(0, 0)

            cellPlaceables.forEachIndexed { index, placeable ->
                val row = index / numCols
                val col = index % numCols
                placeable.place(
                    col * cellWidth,
                    headerHeight + row * cellHeight
                )
            }
        }
    }
}


@Composable
private fun CalendarCell(text: String, shape: Shape) {
    Button(
        {},
        modifier = Modifier
            .fillMaxSize()
            .padding(2.dp),

        shape = shape,

        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),

        contentPadding = PaddingValues(2.dp, 8.dp, 2.dp, 2.dp),
    ) {
        Column(modifier = Modifier.align(Alignment.Top)) {
            Text(text, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Box(modifier = Modifier)
        }
    }
}