package com.louisweigel.pi_calendar.core

import androidx.room.Entity
import kotlin.uuid.Uuid

@Entity(primaryKeys = ["calendarUuid", "personUuid"])
data class CalendarPersonCrossRef(
    val calendarUuid: Uuid,
    val personUuid: Uuid,
)