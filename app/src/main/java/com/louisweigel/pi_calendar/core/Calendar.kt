package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.core.db.Converters
import java.util.UUID

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
    val name: String,
    val description: String,
    val color: Color,
    val isSystem: Boolean,
    val ownerUuid: UUID? = null
) {
    @PrimaryKey var uuid: UUID = UUID.randomUUID()

    companion object {
        const val DEFAULT_EVENTS_CALENDAR_NAME = "Mein Kalender"
        const val DEFAULT_BIRTHDAYS_CALENDAR_NAME = "Geburtstage"
        const val DEFAULT_REMINDERS_CALENDAR_NAME = "Erinnerungen"
    }
}