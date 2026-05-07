package com.louisweigel.pi_calendar

import android.app.Application
import com.louisweigel.pi_calendar.core.db.PiDatabase
import com.louisweigel.pi_calendar.core.db.repositories.CalendarRepository
import com.louisweigel.pi_calendar.core.db.repositories.CalendarEntryRepository
import com.louisweigel.pi_calendar.core.db.repositories.PersonRepository

class PiCalendarApplication : Application() {
    val database by lazy { PiDatabase.getInstance(this) }
    val personRepository by lazy { PersonRepository(database.personDao()) }
    val calendarRepository by lazy { CalendarRepository(database.calendarDao()) }

    val calendarEntryRepository by lazy { CalendarEntryRepository(database.calendarEntryDao()) }
}