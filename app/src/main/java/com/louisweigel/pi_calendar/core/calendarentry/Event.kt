package com.louisweigel.pi_calendar.core.calendarentry

import kotlin.time.Instant

class Event(
    title: String,
    description: String,
    date: Instant,
    val until: Instant,
    val isAllDay: Boolean
) : CalendarEntry(
    title, description, date
)