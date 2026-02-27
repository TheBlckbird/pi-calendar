package com.louisweigel.pi_calendar.core.calendarentry

import java.util.Date

class Event(
    title: String,
    description: String,
    date: Date,
    val until: Date,
    val isAllDay: Boolean
) : CalendarEntry(
    title, description, date
)