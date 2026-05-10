package com.louisweigel.pi_calendar.ui.screens.calendar_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import com.louisweigel.pi_calendar.utils.toGenitive
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaMonth
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun CalendarGrid(
    currentMonthYear: MonthSelection,
    onMonthChange: (Boolean) -> Unit,
    calendarEntries: List<Pair<Calendar, CalendarEntry>>
) {
    val pageCount = 10_000
    val initialPage = pageCount / 2
    val pagerState = rememberPagerState(pageCount = { pageCount }, initialPage = initialPage)

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }
            .scan(Pair(pagerState.settledPage, pagerState.settledPage)) { (_, prev), new ->
                Pair(prev, new)
            }
            .distinctUntilChanged()
            .collect { (prev, current) ->
                if (current != prev) {
                    val isForward = current > prev
                    onMonthChange(isForward)
                }
            }
    }

    HorizontalPager(pagerState) {
        CalendarGridComponent(
            Modifier.fillMaxSize(),
            listOf(
                stringResource(R.string.calendarScreen_monday),
                stringResource(R.string.calendarScreen_tuesday),
                stringResource(R.string.calendarScreen_wednesday),
                stringResource(R.string.calendarScreen_thursday),
                stringResource(R.string.calendarScreen_friday),
                stringResource(R.string.calendarScreen_saturday),
                stringResource(R.string.calendarScreen_sunday)
            ),
        ) {
            val firstDayOfWeek = getFirstDayOfWeek(currentMonthYear)
            val daysBeforeFirst = when (firstDayOfWeek) {
                DayOfWeek.MONDAY -> 0
                DayOfWeek.TUESDAY -> 1
                DayOfWeek.WEDNESDAY -> 2
                DayOfWeek.THURSDAY -> 3
                DayOfWeek.FRIDAY -> 4
                DayOfWeek.SATURDAY -> 5
                DayOfWeek.SUNDAY -> 6
            }
            val monthLength = getMonthLength(currentMonthYear)

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

                val day = index - daysBeforeFirst + 1
                val isLastMonth = day < 1
                val isNextMonth = day > monthLength
                val isThisMonth = !isLastMonth && !isNextMonth
                var isToday = false

                var actualDate: LocalDate

                val displayDay = if (isLastMonth) {
                    val previousMonthYear = currentMonthYear.getPrevious()
                    val previousMonthLength =
                        getMonthLength(previousMonthYear)

                    val day = previousMonthLength - daysBeforeFirst + index + 1

                    actualDate = LocalDate(
                        previousMonthYear.year,
                        previousMonthYear.month.toKotlinMonth(),
                        day
                    )

                    day
                } else if (isNextMonth) {
                    val nextMonthYear = currentMonthYear.getNext()
                    val day = index - monthLength - daysBeforeFirst + 1
                    actualDate =
                        LocalDate(
                            nextMonthYear.year,
                            nextMonthYear.month.toKotlinMonth(),
                            day
                        )

                    day
                } else {
                    val date =
                        LocalDate(currentMonthYear.year, currentMonthYear.month.toIndex(), day)
                    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                    isToday = today == date

                    actualDate = date

                    day
                }

                val date = actualDate.atStartOfDayIn(TimeZone.currentSystemDefault())

                CalendarCell(
                    displayDay.toString(),
                    borderRadius,
                    isToday,
                    calendarEntries
                        .filter { (_, entry) ->
                            entry.includesDate(date)
                        }
                        .map {
                            val title = if (it.component2().title == "") {
                                stringResource(R.string.calendarEntry_noTitle)
                            } else if (it.component2() is Birthday) {
                                val birthday = it.component2() as Birthday
                                "${birthday.title.toGenitive()} ${birthday.getAge(actualDate.year)}. ${
                                    stringResource(
                                        R.string.calendarScreen_birthday
                                    )
                                }"
                            } else {
                                it.component2().title
                            }

                            val iconResource = if (it.component2() is Birthday) {
                                R.drawable.cake_24px
                            } else {
                                null
                            }

                            Triple(
                                iconResource,
                                title,
                                it.component1().color
                            )
                        },
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
    val date = LocalDate(month.year, month.month.toIndex(), 1)
    return date.dayOfWeek
}

private fun getMonthLength(month: MonthSelection): Int {
    val date = LocalDate(month.year, month.month.toIndex(), 1)
    return date.month.toJavaMonth().length(date.toJavaLocalDate().isLeapYear)
}