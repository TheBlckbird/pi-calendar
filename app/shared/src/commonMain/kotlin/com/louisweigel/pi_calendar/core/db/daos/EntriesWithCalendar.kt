package com.louisweigel.pi_calendar.core.db.daos

import androidx.room.Embedded
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.core.calendarentry.Reminder

data class EventWithCalendar(
    @Embedded(prefix = "event_")
    val event: Event,

    @Embedded(prefix = "calendar_")
    val calendar: Calendar
)

data class BirthdayWithCalendar(
    @Embedded(prefix = "birthday_")
    val birthday: Birthday,

    @Embedded(prefix = "calendar_")
    val calendar: Calendar
)

data class ReminderWithCalendar(
    @Embedded(prefix = "reminder_")
    val reminder: Reminder,

    @Embedded(prefix = "calendar_")
    val calendar: Calendar
)