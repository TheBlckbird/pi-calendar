package com.louisweigel.pi_calendar.core

import androidx.room.Entity
import java.util.UUID

@Entity(primaryKeys = ["calendarUuid", "personUuid"])
data class CalendarPersonCrossRef(
    val calendarUuid: UUID,
    val personUuid: UUID,
)