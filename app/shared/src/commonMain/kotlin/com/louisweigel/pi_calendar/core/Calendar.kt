package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.core.db.Converters
import pi_calendar.app.shared.generated.resources.Res
import pi_calendar.app.shared.generated.resources.color_blue
import pi_calendar.app.shared.generated.resources.color_darkGreen
import pi_calendar.app.shared.generated.resources.color_green
import pi_calendar.app.shared.generated.resources.color_indigo
import pi_calendar.app.shared.generated.resources.color_orange
import pi_calendar.app.shared.generated.resources.color_purple
import pi_calendar.app.shared.generated.resources.color_red
import pi_calendar.app.shared.generated.resources.color_teal
import pi_calendar.app.shared.generated.resources.color_terracotta
import kotlin.uuid.Uuid

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
    indices = [
        Index("ownerUuid"),
        Index("name", unique = true),
    ],
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

    val color: Color,
    /**
     * Whether this is one of the default calendars or not.
     *
     * Default calendars can't be deleted and won't be included in an all calendars query
     */
    val isSystem: Boolean = false,
    /**
     * Whether the entries of this calendar should currently be shown
     */
    @ColumnInfo(defaultValue = "true") val isShown: Boolean = true,
    /**
     * UUID of the owner
     *
     * _Currently unused_
     */
    val ownerUuid: Uuid? = null,
    @PrimaryKey val uuid: Uuid = Uuid.random(),
) {
    companion object {
        const val DEFAULT_EVENTS_CALENDAR_NAME = "Mein Kalender"
        const val DEFAULT_BIRTHDAYS_CALENDAR_NAME = "Geburtstage"
        const val DEFAULT_REMINDERS_CALENDAR_NAME = "Erinnerungen"

        val POSSIBLE_COLORS = arrayOf(
            Res.string.color_purple to Color(0xFF5B2E91),
            Res.string.color_indigo to Color(0xFF3949AB),
            Res.string.color_blue to Color(0xFF1565C0),
            Res.string.color_teal to Color(0xFF006D77),
            Res.string.color_darkGreen to Color(0xFF2E7D32),
            Res.string.color_green to Color(0xFF558B2F),
            Res.string.color_orange to Color(0xFFB85C00),
            Res.string.color_terracotta to Color(0xFFB04A2F),
            Res.string.color_red to Color(0xFFB71C1C),
        )
    }
}