package com.louisweigel.pi_calendar.core.calendarentry

import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["calendarEntryUuid", "personUuid"])
data class CalendarEntryPersonCrossRef(
    val calendarEntryUuid: UUID,
    val personUuid: UUID
)