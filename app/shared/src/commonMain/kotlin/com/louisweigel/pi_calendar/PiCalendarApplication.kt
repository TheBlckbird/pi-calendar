package com.louisweigel.pi_calendar

import com.louisweigel.pi_calendar.core.db.repositories.CalendarEntryRepository
import com.louisweigel.pi_calendar.core.db.repositories.CalendarRepository
import com.louisweigel.pi_calendar.core.db.repositories.PersonRepository
import kotlin.getValue

object PiCalendarApplication {
    val personRepository by lazy { PersonRepository(getPlatform().database.personDao()) }
    val calendarRepository by lazy { CalendarRepository(getPlatform().database.calendarDao()) }

    val calendarEntryRepository by lazy { CalendarEntryRepository(getPlatform().database.calendarEntryDao()) }
}