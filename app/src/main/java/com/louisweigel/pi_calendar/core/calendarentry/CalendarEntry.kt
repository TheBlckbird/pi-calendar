package com.louisweigel.pi_calendar.core.calendarentry

import java.util.Date
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

abstract class CalendarEntry constructor(val title: String, val description: String, val date: Instant) {
    val uuid = UUID.randomUUID()
}

enum class CalendarEntryType {
    Birthday,
    Event,
    Reminder,
}