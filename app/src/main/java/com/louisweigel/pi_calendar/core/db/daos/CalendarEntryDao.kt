package com.louisweigel.pi_calendar.core.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Dao
interface CalendarEntryDao {
    @Query("SELECT * FROM event")
    suspend fun getEvents(): List<Event>

    @Query("SELECT * FROM birthday")
    suspend fun getBirthdays(): List<Birthday>

    @Query("SELECT * FROM reminder")
    suspend fun getReminders(): List<Reminder>

    @Transaction
    suspend fun getAll(): List<CalendarEntry> {
        return buildList {
            addAll(getEvents())
            addAll(getBirthdays())
            addAll(getReminders())
        }.sortedBy { it.date }
    }

    @Query("SELECT * FROM event")
    fun observeEvents(): Flow<List<Event>>

    @Query("SELECT * FROM birthday")
    fun observeBirthdays(): Flow<List<Birthday>>

    @Query("SELECT * FROM reminder")
    fun observeReminders(): Flow<List<Reminder>>

    /**
     * Gets all events with their respective calendar as an observable
     *
     * Doesn't include events from hidden calendars
     */
    @Query(
        """
    SELECT
        e.uuid AS event_uuid,
        e.title AS event_title,
        e.description AS event_description,
        e.date AS event_date,
        e.until AS event_until,
        e.isAllDay AS event_isAllDay,
        e.calendarUuid AS event_calendarUuid,

        c.uuid AS calendar_uuid,
        c.name AS calendar_name,
        c.description AS calendar_description,
        c.color AS calendar_color,
        c.isSystem AS calendar_isSystem,
        c.ownerUuid AS calendar_ownerUuid,
        c.isShown AS calendar_isShown
    FROM Event e
    INNER JOIN Calendar c ON c.uuid = e.calendarUuid
    WHERE calendar_isShown = 1
"""
    )
    fun observeAllEventsWithCalendar(): Flow<List<EventWithCalendar>>

    /**
     * Gets all birthdays with their respective calendar as an observable
     *
     * Doesn't include birthdays from hidden calendars
     */
    @Query(
        """
    SELECT
        b.uuid AS birthday_uuid,
        b.title AS birthday_title,
        b.description AS birthday_description,
        b.date AS birthday_date,
        b.calendarUuid AS birthday_calendarUuid,

        c.uuid AS calendar_uuid,
        c.name AS calendar_name,
        c.description AS calendar_description,
        c.color AS calendar_color,
        c.isSystem AS calendar_isSystem,
        c.ownerUuid AS calendar_ownerUuid,
        c.isShown AS calendar_isShown
    FROM Birthday b
    INNER JOIN Calendar c ON c.uuid = b.calendarUuid
    WHERE calendar_isShown = 1
"""
    )
    fun observeAllBirthdaysWithCalendar(): Flow<List<BirthdayWithCalendar>>

    /**
     * Gets all reminders with their respective calendar as an observable
     *
     * Doesn't include reminders from hidden calendars
     */
    @Query(
        """
    SELECT
        r.uuid AS reminder_uuid,
        r.title AS reminder_title,
        r.description AS reminder_description,
        r.date AS reminder_date,
        r.calendarUuid AS reminder_calendarUuid,

        c.uuid AS calendar_uuid,
        c.name AS calendar_name,
        c.description AS calendar_description,
        c.color AS calendar_color,
        c.isSystem AS calendar_isSystem,
        c.ownerUuid AS calendar_ownerUuid,
        c.isShown AS calendar_isShown
    FROM Reminder r
    INNER JOIN Calendar c ON c.uuid = r.calendarUuid
    WHERE calendar_isShown = 1
"""
    )
    fun observeAllRemindersWithCalendar(): Flow<List<ReminderWithCalendar>>

    @Insert
    suspend fun insertEvent(event: Event)

    @Insert
    suspend fun insertBirthday(birthday: Birthday)

    @Insert
    suspend fun insertReminder(reminder: Reminder)

    @Delete
    suspend fun deleteEvent(event: Event)

    @Delete
    suspend fun deleteBirthday(birthday: Birthday)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)
}