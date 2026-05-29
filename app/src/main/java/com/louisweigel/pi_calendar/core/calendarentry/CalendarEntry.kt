package com.louisweigel.pi_calendar.core.calendarentry

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.Ignore
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * Abstract class describing an entry in the calendar
 */
sealed class CalendarEntry(
    /**
     * Unique identifier of this entry
     */
    @Ignore open val uuid: Uuid,
    @Ignore open val title: String,
    @Ignore open val description: String,
    @Ignore open val date: Instant,
    /**
     * UUID of the calendar this belongs to
     */
    @Ignore open val calendarUuid: Uuid,
) {

    /**
     * Checks whether the given date is included in the time frame of the event
     */
    open fun includesDate(date: LocalDate): Boolean {
        val thisLocalDate = dateToLocalDate()
        return date == thisLocalDate
    }

    /**
     * Checks whether this entry is in the given month and year
     */
    open fun isInMonth(monthSelection: MonthSelection): Boolean {
        val localDate = dateToLocalDate()
        return localDate.year == monthSelection.year && localDate.month == monthSelection.month
    }

    /**
     * Returns the date as a `LocalDate`
     */
    protected fun dateToLocalDate(): LocalDate {
        return this.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    /**
     * Gets the title for the calendar entry
     */
    @Composable
    open fun getTitle(date: LocalDate): String {
        val title = if (this.title == "") {
            stringResource(R.string.calendarEntry_noTitle)
        } else {
            this.title
        }

        return title
    }
}
