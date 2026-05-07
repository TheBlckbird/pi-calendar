package com.louisweigel.pi_calendar.core.calendarentry

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.db.Converters
import java.util.UUID
import kotlin.time.Instant

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
class Reminder(
    override val title: String,
    override val description: String,
    override val date: Instant,
    override val calendarUuid: UUID,
    @PrimaryKey override val uuid: UUID = UUID.randomUUID(),
) :
    CalendarEntry(
        uuid, title, description, date, calendarUuid
    )