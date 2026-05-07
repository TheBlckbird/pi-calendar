package com.louisweigel.pi_calendar.core.db.repositories

import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.db.daos.CalendarDao
import kotlinx.coroutines.flow.Flow
import java.util.UUID

class CalendarRepository(
    private val dao: CalendarDao
) {
    suspend fun insert(calendar: Calendar) =
        dao.insertCalendar(calendar)

    suspend fun getAll(): List<Calendar> =
        dao.getAll()

    fun observeAll(): Flow<List<Calendar>> =
        dao.observeAll()

    suspend fun delete(calendar: Calendar) =
        dao.delete(calendar)

    suspend fun getByName(name: String): Calendar? =
        dao.getByName(name)

    suspend fun getByUuid(uuid: UUID): Calendar? =
        dao.getByUuid(uuid)

    suspend fun getDefaultEventsCalendar(): Calendar =
        dao.getByName(Calendar.DEFAULT_EVENTS_CALENDAR_NAME)!!

    suspend fun getDefaultBirthdaysCalendar(): Calendar =
        dao.getByName(Calendar.DEFAULT_BIRTHDAYS_CALENDAR_NAME)!!

    suspend fun getDefaultRemindersCalendar(): Calendar =
        dao.getByName(Calendar.DEFAULT_REMINDERS_CALENDAR_NAME)!!
}