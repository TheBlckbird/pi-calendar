package com.louisweigel.pi_calendar.core.db.repositories

import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.db.daos.CalendarDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class CalendarRepository(
    private val dao: CalendarDao
) {
    /**
     * Tries to insert the given calendar into the database.
     *
     * Returns `false` if it failed due to the UNIQUE constraint failing
     */
    suspend fun insert(calendar: Calendar): Boolean =
        dao.insertCalendar(calendar) != -1L

    suspend fun getAll(): List<Calendar> =
        dao.getAllUserCalendars()

    fun observeAll(): Flow<List<Calendar>> =
        dao.observeAllUserCalendars()

    suspend fun delete(calendar: Calendar) =
        dao.delete(calendar)

    suspend fun update(calendar: Calendar) =
        dao.update(calendar)

    suspend fun getByName(name: String): Calendar? =
        dao.getByName(name)

    fun observeByName(name: String): Flow<Calendar?> =
        dao.observeByName(name)

    suspend fun getByUuid(uuid: UUID): Calendar? =
        dao.getByUuid(uuid)

    fun observeDefaultEventsCalendar(): Flow<Calendar> =
        dao.observeByName(Calendar.DEFAULT_EVENTS_CALENDAR_NAME).map { it!! }

    fun observeDefaultBirthdaysCalendar(): Flow<Calendar> =
        dao.observeByName(Calendar.DEFAULT_BIRTHDAYS_CALENDAR_NAME).map { it!! }

    fun observeDefaultRemindersCalendar(): Flow<Calendar> =
        dao.observeByName(Calendar.DEFAULT_REMINDERS_CALENDAR_NAME).map { it!! }
}