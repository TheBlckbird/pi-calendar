package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntryType
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import java.util.Date
import kotlin.time.Instant

class Calendar(
    val name: String,
    val description: String,
    val color: Color,
    val isSystem: Boolean,
    val owner: Person?
) {
    val entries = mutableListOf<CalendarEntry>()
    private val sharedWith = mutableListOf<Person>()

    /**
     * Removes the given calendar entry if it exists
     */
    fun removeCalendarEntry(calendarEntry: CalendarEntry): Boolean {
        return entries.remove(calendarEntry)
    }

    /**
     * Find a list of calendar entries that match the given
     */
    fun findCalendarEntries(
        calendarEntryTypes: List<CalendarEntryType>,
        title: String?,
        description: String?,
        dateRange: Pair<Instant, Instant>?
    ): List<CalendarEntry> {
        return entries
            .filter { entry ->
                entry is Birthday && calendarEntryTypes.contains(CalendarEntryType.Birthday) ||
                        entry is Event && calendarEntryTypes.contains(CalendarEntryType.Event) ||
                        entry is Reminder && calendarEntryTypes.contains(CalendarEntryType.Reminder)
            }
            .filter { entry -> if (title != null) entry.title.contains(title) else true }
            .filter { entry -> if (description != null) entry.description.contains(description) else true }
            .filter { entry ->
                if (dateRange != null) {
                    entry.date > dateRange.component1() && entry.date < dateRange.component2()
                } else {
                    true
                }
            }
    }

    /**
     * Share this calendar with another person
     */
    fun shareWithPerson(person: Person) {
        sharedWith.add(person)
    }

    /**
     * Stop sharing this calendar with the given person
     */
    fun stopSharing(person: Person): Boolean {
        return sharedWith.remove(person)
    }
}