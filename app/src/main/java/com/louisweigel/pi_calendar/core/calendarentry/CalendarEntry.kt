package com.louisweigel.pi_calendar.core.calendarentry

import android.annotation.SuppressLint
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.Ignore
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.utils.toGenitive
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Instant
import kotlin.uuid.Uuid

/*@Serializable
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
}*/

@Serializable
sealed class CalendarEntry {
    abstract val uuid: Uuid
    abstract val title: String
    abstract val description: String
    abstract val date: Instant
    abstract val calendarUuid: Uuid

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