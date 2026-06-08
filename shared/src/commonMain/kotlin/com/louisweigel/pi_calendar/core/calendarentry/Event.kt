package com.louisweigel.pi_calendar.core.calendarentry

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.db.Converters
import kotlinx.datetime.YearMonth
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

/**
 * A simple event from a date until another date
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Calendar::class,
        parentColumns = ["uuid"],
        childColumns = ["calendarUuid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("calendarUuid")]
)
@TypeConverters(Converters::class)
class Event(
    override val title: String,
    override val description: String,
    override val date: Instant,
    val until: Instant,
    /**
     * Whether this event occupies whole days or only time frames
     */
    val isAllDay: Boolean,
    override val calendarUuid: Uuid,
    @PrimaryKey override val uuid: Uuid = Uuid.generateV7(),
) : CalendarEntry(uuid, title, description, date, calendarUuid) {

    override fun includesDate(date: LocalDate): Boolean {
        val startDate = this.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val endDate = until.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return date in startDate..<endDate
    }

    override fun isInMonth(monthSelection: YearMonth): Boolean {
        val untilDate = until.toLocalDateTime(TimeZone.currentSystemDefault())

        return super.isInMonth(monthSelection)
                || (untilDate.year == monthSelection.year && untilDate.month == monthSelection.month)
    }
}
