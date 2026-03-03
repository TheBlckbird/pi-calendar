package com.louisweigel.pi_calendar.core.calendarentry

import java.util.Date
import java.util.UUID

abstract class CalendarEntry(val title: String, val description: String, val date: Date) {
    val uuid = UUID.randomUUID()
}

enum class CalenderEntryType {
    Birthday,
    Event,
    Reminder,
}