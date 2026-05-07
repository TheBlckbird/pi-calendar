package com.louisweigel.pi_calendar.core.calendarentry

import androidx.room.Ignore
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.time.Instant

abstract class CalendarEntry(
    @Ignore open val uuid: UUID,
    @Ignore open val title: String,
    @Ignore open val description: String,
    @Ignore open val date: Instant,
    @Ignore open val calendarUuid: UUID
) {

    /**
     * Checks whether the given date is included in the time frame of the event
     */
    open fun includesDate(date: Instant): Boolean {
        val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val thisLocalDate = this.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return localDate == thisLocalDate
    }
}