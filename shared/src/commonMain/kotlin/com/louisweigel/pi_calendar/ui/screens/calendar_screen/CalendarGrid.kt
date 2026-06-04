package com.louisweigel.pi_calendar.ui.screens.calendar_screen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import kotlinx.datetime.YearMonth
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.stringResource
import pi_calendar_kmp.shared.generated.resources.Res
import pi_calendar_kmp.shared.generated.resources.calendarScreen_friday
import pi_calendar_kmp.shared.generated.resources.calendarScreen_monday
import pi_calendar_kmp.shared.generated.resources.calendarScreen_saturday
import pi_calendar_kmp.shared.generated.resources.calendarScreen_sunday
import pi_calendar_kmp.shared.generated.resources.calendarScreen_thursday
import pi_calendar_kmp.shared.generated.resources.calendarScreen_tuesday
import pi_calendar_kmp.shared.generated.resources.calendarScreen_wednesday

@Composable
fun CalendarGrid(
    currentMonthYear: YearMonth,
    onMonthChange: (Boolean) -> Unit,
    calendarEntries: List<Pair<Calendar, CalendarEntry>>,
    onClick: (LocalDate) -> Unit,
    viewModel: CalendarGridViewModel = viewModel { CalendarGridViewModel () },
) {
    val daysData by viewModel.daysData.collectAsState()
    val pageCount = 10_000
    val initialPage = pageCount / 2
    val pagerState = rememberPagerState(pageCount = { pageCount }, initialPage = initialPage)

    LaunchedEffect(currentMonthYear, calendarEntries) {
        viewModel.calculateForMonth(currentMonthYear, calendarEntries)
    }

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
                    viewModel.calculateForMonth(currentMonthYear, calendarEntries)
                }
            }
    }

    HorizontalPager(
        pagerState,
    ) {
        if (daysData.isEmpty()) {
            return@HorizontalPager
        }

        CalendarGridComponent(
            Modifier.fillMaxSize(),
            listOf(
                stringResource(Res.string.calendarScreen_monday),
                stringResource(Res.string.calendarScreen_tuesday),
                stringResource(Res.string.calendarScreen_wednesday),
                stringResource(Res.string.calendarScreen_thursday),
                stringResource(Res.string.calendarScreen_friday),
                stringResource(Res.string.calendarScreen_saturday),
                stringResource(Res.string.calendarScreen_sunday)
            ),
        ) {
            repeat(42) { index ->
                val dayState = daysData[index]

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

                CalendarCell(
                    dayState.date.day.toString(),
                    borderRadius,
                    dayState.isToday,
                    dayState.entries,
                    dayState.isThisMonth,
                    { onClick(dayState.date) },
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
