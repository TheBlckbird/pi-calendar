package com.louisweigel.pi_calendar.ui.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.core.getTranslationKey
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import kotlinx.datetime.YearMonth
import org.jetbrains.compose.resources.stringResource

@Composable
fun MonthSelectionScreen(
    currentSelection: YearMonth,
    onSelectionChanged: (YearMonth) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            listState.scrollToItem((currentSelection.year - 2).coerceAtLeast(0))
        }
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        LazyRow(state = listState, modifier = modifier) {
            items(
                count = currentSelection.year + 1000,
                key = { year -> year }
            ) { year ->
                val isCurrentlySelected = year == currentSelection.year
                val cornerRadius = if (isCurrentlySelected) {
                    16.dp
                } else {
                    4.dp
                }
                val cornerShapeRadius = animateDpAsState(targetValue = cornerRadius)

                Button(
                    onClick = {
                        onSelectionChanged(YearMonth(year, currentSelection.month))
                    },
                    modifier = Modifier
                        .heightIn(min = 32.dp)
                        .widthIn(min = ButtonDefaults.MinWidth)
                        .padding(horizontal = 2.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentlySelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background,
                        contentColor = if (isCurrentlySelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground,
                    ),
                    shape = RoundedCornerShape(cornerShapeRadius.value)
                ) {
                    Text(year.toString(), style = MaterialTheme.typography.labelSmall)
                }
            }
        }

        LazyVerticalGrid(GridCells.Fixed(3)) {
            items(Month.entries.count()) { monthIndex ->
                val month = Month.entries[monthIndex]
                val isCurrentlySelected = month == currentSelection.month

                val cornerRadius = if (isCurrentlySelected) {
                    20.dp
                } else {
                    4.dp
                }
                val cornerShapeRadius = animateDpAsState(targetValue = cornerRadius)

                val shape = when (monthIndex) {
                    0 -> {
                        RoundedCornerShape(
                            topStart = 20.dp,
                            topEnd = cornerShapeRadius.value,
                            bottomEnd = cornerShapeRadius.value,
                            bottomStart = cornerShapeRadius.value
                        )
                    }

                    2 -> {
                        RoundedCornerShape(
                            topStart = cornerShapeRadius.value,
                            topEnd = 20.dp,
                            bottomEnd = cornerShapeRadius.value,
                            bottomStart = cornerShapeRadius.value
                        )
                    }

                    9 -> {
                        RoundedCornerShape(
                            topStart = cornerShapeRadius.value,
                            topEnd = cornerShapeRadius.value,
                            bottomEnd = cornerShapeRadius.value,
                            bottomStart = 20.dp
                        )
                    }

                    11 -> {
                        RoundedCornerShape(
                            topStart = cornerShapeRadius.value,
                            topEnd = cornerShapeRadius.value,
                            bottomEnd = 20.dp,
                            bottomStart = cornerShapeRadius.value
                        )
                    }

                    else -> {
                        RoundedCornerShape(
                            topStart = cornerShapeRadius.value,
                            topEnd = cornerShapeRadius.value,
                            bottomEnd = cornerShapeRadius.value,
                            bottomStart = cornerShapeRadius.value
                        )
                    }
                }

                Button(
                    {
                        onSelectionChanged(YearMonth(currentSelection.year, month))
                    },
                    modifier = Modifier
                        .height(40.dp)
                        .padding(2.dp),
                    shape = shape,

                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCurrentlySelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.background,
                        contentColor = if (isCurrentlySelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onBackground,
                    ),
                ) {
                    Text(stringResource(month.getTranslationKey()), style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
