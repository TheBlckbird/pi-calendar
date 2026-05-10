package com.louisweigel.pi_calendar.core.calendarentry

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.Ignore
import com.louisweigel.pi_calendar.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid


sealed class CalendarEntry(
    @Ignore open val uuid: Uuid,
    @Ignore open val title: String,
    @Ignore open val description: String,
    @Ignore open val date: Instant,
    @Ignore open val calendarUuid: Uuid,
) {

    /**
     * Checks whether the given date is included in the time frame of the event
     */
    open fun includesDate(date: Instant): Boolean {
        val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val thisLocalDate = this.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return localDate == thisLocalDate
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