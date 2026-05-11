package com.louisweigel.pi_calendar.ui.screens.calendar_screen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaMonth
import kotlinx.datetime.todayIn
import kotlin.time.Clock


class CalendarGridViewModel : ViewModel() {
    private val _daysData = MutableStateFlow<List<DayState>>(emptyList())
    val daysData: StateFlow<List<DayState>> = _daysData

    fun calculateForMonth(
        currentMonthYear: MonthSelection,
        calendarEntries: List<Pair<Calendar, CalendarEntry>>
    ) {
        viewModelScope.launch {
            _daysData.update {
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

                List(42) { index ->
                    val day = index - daysBeforeFirst + 1
                    val isLastMonth = day < 1
                    val isNextMonth = day > monthLength
                    val isThisMonth = !isLastMonth && !isNextMonth
                    var isToday = false

                    var actualDate: LocalDate

                    val calculatedDay = if (isLastMonth) {
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

                    DayState(
                        displayDay = calculatedDay,
                        date = actualDate,
                        isToday = isToday,
                        isThisMonth = isThisMonth,
                        entries = calendarEntries
                            .filter { (_, entry) ->
                                entry.includesDate(date)
                            }
                            .map {
                                val iconResource = if (it.component2() is Birthday) {
                                    R.drawable.cake_24px
                                } else {
                                    null
                                }

                                Triple(
                                    iconResource,
                                    "it.component2().getTitle(actualDate)",
                                    it.component1().color
                                )
                            }
                    )
                }
            }
        }
    }
}


data class DayState(
    val displayDay: Int,
    val date: LocalDate,
    val isToday: Boolean,
    val isThisMonth: Boolean,
    val entries: List<Triple<Int?, String, Color>>,
)

// Rework these functions (maybe)

private fun getFirstDayOfWeek(month: MonthSelection): DayOfWeek {
    val date = LocalDate(month.year, month.month.toIndex(), 1)
    return date.dayOfWeek
}

private fun getMonthLength(month: MonthSelection): Int {
    val date = LocalDate(month.year, month.month.toIndex(), 1)
    return date.month.toJavaMonth().length(date.toJavaLocalDate().isLeapYear)
}
