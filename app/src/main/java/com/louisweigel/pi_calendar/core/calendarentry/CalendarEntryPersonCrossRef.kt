package com.louisweigel.pi_calendar.core.calendarentry

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(primaryKeys = ["calendarEntryUuid", "personUuid"])
data class CalendarEntryPersonCrossRef(
    val calendarEntryUuid: Uuid,
    val personUuid: Uuid
)