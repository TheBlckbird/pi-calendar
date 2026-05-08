package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.core.db.Converters
import java.util.UUID

/**
 * `Calendar` represents an object that can hold calendar entries
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Person::class,
        parentColumns = ["uuid"],
        childColumns = ["ownerUuid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("ownerUuid")]

)
@TypeConverters(Converters::class)
data class Calendar(
    /**
     * Name of the calendar
     */
    val name: String,
    /**
     * Description of what the calendar does
     */
    val description: String,
    /**
     * Color the events are shown in
     */
    val color: Color,
    /**
     * Whether this is one of the default calendars or not.
     *
     * Default calendars can't be deleted and won't be included in an all calendars query
     */
    val isSystem: Boolean,
    /**
     * Whether the entries of this calendar should currently be shown
     */
    @ColumnInfo(defaultValue = "true") val isShown: Boolean = true,
    /**
     * UUID of the owner
     *
     * _Currently unused_
     */
    val ownerUuid: UUID? = null,
    @PrimaryKey val uuid: UUID = UUID.randomUUID(),
) {
    companion object {
        const val DEFAULT_EVENTS_CALENDAR_NAME = "Mein Kalender"
        const val DEFAULT_BIRTHDAYS_CALENDAR_NAME = "Geburtstage"
        const val DEFAULT_REMINDERS_CALENDAR_NAME = "Erinnerungen"
    }
}