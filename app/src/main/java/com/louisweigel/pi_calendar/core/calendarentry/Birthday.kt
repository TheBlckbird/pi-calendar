package com.louisweigel.pi_calendar.core.calendarentry

import kotlin.time.Instant

class Birthday(title: String, description: String, date: Instant) : CalendarEntry(
    title, description, date
)