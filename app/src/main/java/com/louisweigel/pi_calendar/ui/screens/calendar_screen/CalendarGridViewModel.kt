package com.louisweigel.pi_calendar.ui.screens.calendar_screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import com.louisweigel.pi_calendar.core.toIndex
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaMonth
import kotlinx.datetime.todayIn
import kotlin.time.Clock

class CalendarGridViewModel : ViewModel() {
    private val _daysData = MutableStateFlow<List<DayState>>(emptyList())

    /**
     * List of the data for the 42 days
     */
    val daysData: StateFlow<List<DayState>> = _daysData

    /**
     * Calculate the data for the 42 days in the calendar grid
     *
     * @param[currentMonthYear] The current month selection
     * @param[calendarEntries] A list of all calendar entries
     * (This can and should be reduced to just the calendar entries in this month and the one before and after this)
     */
    fun calculateForMonth(
        currentMonthYear: MonthSelection,
        calendarEntries: List<Pair<Calendar, CalendarEntry>>
    ) {
        // Run it in a background coroutine in order to avoid lagging the UI thread
        viewModelScope.launch {
            _daysData.update {
                val firstDayOfWeek = getFirstDayOfWeek(currentMonthYear)
                // Get the amount of days before the first day in the month
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

                    // Compute the date of the day
                    var date: LocalDate
                    if (isLastMonth) {
                        val previousMonthYear = currentMonthYear.getPrevious()
                        val previousMonthLength =
                            getMonthLength(previousMonthYear)

                        val day = previousMonthLength - daysBeforeFirst + index + 1

                        date = LocalDate(
                            previousMonthYear.year,
                            previousMonthYear.month,
                            day
                        )
                    } else if (isNextMonth) {
                        val nextMonthYear = currentMonthYear.getNext()
                        val day = index - monthLength - daysBeforeFirst + 1
                        date =
                            LocalDate(
                                nextMonthYear.year,
                                nextMonthYear.month,
                                day
                            )
                    } else {
                        date =
                            LocalDate(currentMonthYear.year, currentMonthYear.month.toIndex(), day)
                    }

                    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                    isToday = today == date

                    DayState(
                        date = date,
                        isToday = isToday,
                        isThisMonth = isThisMonth,
                        entries = calendarEntries
                            .filter { (_, entry) -> entry.includesDate(date) }
                            .map { (calendar, entry) ->
                                var isDone = false

                                val iconResource = if (entry is Birthday) {
                                    R.drawable.cake_24px
                                } else if (entry is Reminder) {
                                    if (entry.isDone) {
                                        isDone = true
                                        R.drawable.radio_button_checked_24px
                                    } else {
                                        R.drawable.radio_button_unchecked_24px
                                    }
                                } else {
                                    null
                                }

                                CalendarEntryState(
                                    @Composable { entry.getTitle(date) },
                                    iconResource,
                                    calendar.color,
                                    isDone,
                                )
                            }
                    )
                }
            }
        }
    }
}

/**
 * Represents the data needed to display a single cell in the calendar grid
 */
data class DayState(
    /**
     * The date of this day
     */
    val date: LocalDate,
    /**
     * Whether this day is the current day
     */
    val isToday: Boolean,
    /**
     * Whether this date is in the current month
     */
    val isThisMonth: Boolean,
    /**
     * The calendar entries for this day
     *
     * - The first part is the texture index of an icon, if needed
     * - The second component is the text content that should be shown
     * - The third component is the color of the calendar entry
     */
    val entries: List<CalendarEntryState>,
)

/**
 * State for an entry
 */
data class CalendarEntryState(
    /**
     * A closure returning the content for the entry
     */
    val content: @Composable () -> String,
    /**
     * A possible icon for the entry
     */
    val icon: Int?,
    /**
     * The color of the entry
     */
    val color: Color,
    /**
     * Whether the entry is stroke through
     */
    val isStrikeThrough: Boolean,
)

/**
 * Returns which day the first day of the month is (Monday-Sunday)
 */
private fun getFirstDayOfWeek(month: MonthSelection): DayOfWeek {
    val date = LocalDate(month.year, month.month.toIndex(), 1)
    return date.dayOfWeek
}

/**
 * Returns the length of the given month in that year
 *
 * Does take leap years into account
 */
private fun getMonthLength(month: MonthSelection): Int {
    val date = LocalDate(month.year, month.month.toIndex(), 1)
    return date.month.toJavaMonth().length(date.toJavaLocalDate().isLeapYear)
}
