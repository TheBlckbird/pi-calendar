package com.louisweigel.pi_calendar.core.db.repositories

import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import com.louisweigel.pi_calendar.core.db.daos.CalendarEntryDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.util.UUID

class CalendarEntryRepository(
    private val dao: CalendarEntryDao
) {
    suspend fun getAll(): List<CalendarEntry> =
        dao.getAll()

    fun observeAll(): Flow<List<CalendarEntry>> = combine(
        dao.observeEvents(), dao.observeBirthdays(), dao.observeReminders()
    ) { events, birthdays, reminders ->
        (events + birthdays + reminders).sortedBy { it.date }
    }

    /**
     * Gets all calendar entries with their respective calendar as an observable
     *
     * Doesn't include entries from hidden calendars
     */
    fun observeAllWithCalendar(): Flow<List<Pair<Calendar, CalendarEntry>>>
         = combine(
            dao.observeAllEventsWithCalendar(),
            dao.observeAllBirthdaysWithCalendar(),
            dao.observeAllRemindersWithCalendar()
        ) { events, birthdays, reminders ->
            (
                    events.map { Pair(it.calendar, it.event) }
                            + birthdays.map { Pair(it.calendar, it.birthday) }
                            + reminders.map { Pair(it.calendar, it.reminder) }
                    )
        }

    suspend fun insert(entry: CalendarEntry) {
        when (entry) {
            is Event -> {
                dao.insertEvent(entry)
            }

            is Birthday -> {
                dao.insertBirthday(entry)
            }

            is Reminder -> {
                dao.insertReminder(entry)
            }
        }
    }

    suspend fun delete(entry: CalendarEntry) {
        when (entry) {
            is Event -> {
                dao.deleteEvent(entry)
            }

            is Birthday -> {
                dao.deleteBirthday(entry)
            }

            is Reminder -> {
                dao.deleteReminder(entry)
            }
        }
    }
}