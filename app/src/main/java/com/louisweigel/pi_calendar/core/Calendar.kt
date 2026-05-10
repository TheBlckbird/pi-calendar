package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.db.Converters
import com.louisweigel.pi_calendar.utils.ColorAsLongSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
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
@Serializable
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
    @Serializable(with = ColorAsLongSerializer::class)
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
            R.string.color_purple to Color(0xFF5B2E91),
            R.string.color_indigo to Color(0xFF3949AB),
            R.string.color_blue to Color(0xFF1565C0),
            R.string.color_teal to Color(0xFF006D77),
            R.string.color_darkGreen to Color(0xFF2E7D32),
            R.string.color_green to Color(0xFF558B2F),
            R.string.color_orange to Color(0xFFB85C00),
            R.string.color_terracotta to Color(0xFFB04A2F),
            R.string.color_red to Color(0xFFB71C1C),
        )
    }
}