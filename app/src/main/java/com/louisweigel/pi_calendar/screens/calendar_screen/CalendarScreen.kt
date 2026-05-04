package com.louisweigel.pi_calendar.screens.calendar_screen

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.screens.MonthSelection
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.Year

@Composable
fun CalendarScreen(currentMonth: MonthSelection) {
    val pagerState = rememberPagerState(pageCount = { 100 }, initialPage = 50)

    HorizontalPager(pagerState) { page ->
        CalendarGrid(
            Modifier.fillMaxSize(),
            listOf(stringResource(R.string.calendarScreen_monday),
                stringResource(R.string.calendarScreen_tuesday),
                stringResource(R.string.calendarScreen_wednesday),
                stringResource(R.string.calendarScreen_thursday),
                stringResource(R.string.calendarScreen_friday),
                stringResource(R.string.calendarScreen_saturday),
                stringResource(R.string.calendarScreen_sunday)
            ),
        ) {
            val firstDayOfWeek = getFirstDayOfWeek(currentMonth)
            val daysBeforeFirst = when (firstDayOfWeek) {
                DayOfWeek.MONDAY -> 0
                DayOfWeek.TUESDAY -> 1
                DayOfWeek.WEDNESDAY -> 2
                DayOfWeek.THURSDAY -> 3
                DayOfWeek.FRIDAY -> 4
                DayOfWeek.SATURDAY -> 5
                DayOfWeek.SUNDAY -> 6
            }
            val monthLength = getMonthLength(currentMonth)

            repeat(42) { index ->
                val borderRadius = when (index) {
                    0 -> {
                        RoundedCornerShape(12.dp, 4.dp, 4.dp, 4.dp)
                    }
                    6 -> {
                        RoundedCornerShape(4.dp, 12.dp, 4.dp, 4.dp)
                    }
                    35 -> {
                        RoundedCornerShape(4.dp, 4.dp, 4.dp, 12.dp)
                    }
                    41 -> {
                        RoundedCornerShape(4.dp, 4.dp, 12.dp, 4.dp)
                    }
                    else -> {
                        RoundedCornerShape(4.dp)
                    }
                }

                val entries = if (index == 25) {
                    listOf(Triple(R.drawable.cake_24px, "Andreas Geburtstag", Color.Blue))
                } else {
                    listOf<Triple<Int?, String, Color>>()
                }

                val day = index - daysBeforeFirst + 1
                val isLastMonth = day < 1
                val isNextMonth = day > monthLength
                val isThisMonth = !isLastMonth && !isNextMonth
                var isToday = false

                val displayDay = if (isLastMonth) {
                    val previousMonth = currentMonth.month.getPrevious()
                    val previousMonthLength = getMonthLength(MonthSelection(previousMonth, currentMonth.year))
                    (previousMonthLength - daysBeforeFirst + index + 1).toString()
                } else if (isNextMonth) {
                    (index - monthLength - daysBeforeFirst+ 1).toString()
                } else {
                    val date = LocalDate.of(currentMonth.year, currentMonth.month.toIndex(), day)
                    val today = LocalDate.now()
                    isToday = today == date

                    day.toString()
                }

                CalendarCell(
                    displayDay,
                    borderRadius,
                    isToday,
                    entries,
                    isThisMonth,
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

// Rework these functions (maybe)

private fun getFirstDayOfWeek(month: MonthSelection): DayOfWeek {
    val date = LocalDate.of(month.year, month.month.toIndex(), 1)
    return date.dayOfWeek
}

private fun getMonthLength(month: MonthSelection): Int {
    val date = LocalDate.of(month.year, month.month.toIndex(), 1)
    return date.month.length(date.isLeapYear)
}