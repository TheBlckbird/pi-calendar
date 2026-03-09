package com.louisweigel.pi_calendar.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.louisweigel.pi_calendar.R

@Composable
fun CalendarScreen() {
    val pagerState = rememberPagerState(pageCount = { 100 }, initialPage = 50)


    HorizontalPager(pagerState) { page ->
        CalendarGrid(
            Modifier.fillMaxSize(),
            listOf("Mo", "Di", "Mi", "Do", "Fr", "Sa", "So"),
        ) {
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

                val entries = if (index == 25) {
                    listOf(Triple(R.drawable.cake_24px, "Andreas Geburtstag", Color.Blue))
                } else {
                    listOf<Triple<Int?, String, Color>>()
                }

                CalendarCell(
                    (index + 1).toString(),
                    borderRadius,
                    index == 10,
                    entries,
                    index in 5..<39,
                    modifier = if (index % 7 == 0) {
                        Modifier.padding(start = 2.dp)
                    } else if (index % 7 == 6) {
                        Modifier.padding(end = 2.dp)
                    } else {
                        Modifier
                    },
                )
            }
        }
    }

}

@Composable
private fun CalendarGrid(
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
private fun CalendarCell(
    text: String,
    shape: Shape,
    isToday: Boolean,
    entries: List<Triple<Int?, String, Color>>,
    isThisMonth: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        {},
        modifier = modifier
            .fillMaxSize()
            .padding(2.dp),

        shape = shape,

        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),

        contentPadding = PaddingValues(0.dp, 8.dp, 0.dp, 0.dp),
    ) {
        Column(modifier = Modifier.align(Alignment.Top)) {
            val textModifier = if (isToday) {
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .width(28.dp)
            } else {
                Modifier
            }

            val textColor = if (isToday) {
                MaterialTheme.colorScheme.onPrimary
            } else if (!isThisMonth) {
                MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.onBackground
            }

            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Text(
                    text,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = textModifier.fillMaxWidth(),
                    color = textColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Column {
                for (entry in entries) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .fillMaxWidth()
                            .height(15.dp)
                            .background(entry.component3())
                            .padding(4.dp, 1.dp, 0.dp, 1.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row {
                            if (entry.component1() != null) {
                                Icon(
                                    painter = painterResource(entry.component1()!!),
                                    contentDescription = null
                                )

                                Spacer(Modifier.width(2.dp))
                            }

                            Text(
                                entry.component2(),
                                fontSize = 11.sp,
                                softWrap = false,
                                lineHeight = 11.sp
                            )
                        }
                    }
                }
            }
        }
    }
}