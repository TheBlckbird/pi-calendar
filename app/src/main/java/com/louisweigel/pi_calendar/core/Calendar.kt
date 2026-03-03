package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.CalenderEntryType
import java.util.Date

class Calendar(val name: String, val description: String, val color: Color, val isSystem: Boolean) {
    private val entries = mutableListOf<CalendarEntry>()
    private val sharedWith = mutableListOf<Person>()

    /**
     * Adds a new calendar entry
     */
    fun addCalendarEntry(calendarEntry: CalendarEntry) {
        TODO()
    }

    /**
     * Removes the given calendar entry if it exists
     */
    fun removeCalendarEntry(calendarEntry: CalendarEntry) {
        TODO()
    }

    /**
     * Find a list of calendar entries that match the given
     */
    fun findCalendarEntries(
        calendarEntryTypes: List<CalenderEntryType>,
        title: String?,
        description: String?,
        date: Date?
    ): List<CalendarEntry> {
        TODO()
    }

    /**
     * Share this calendar with another person
     */
    fun shareWithPerson(person: Person) {
        TODO()
    }

    /**
     * Stop sharing this calendar with the given person
     */
    fun stopSharing(person: Person) {
        TODO()
    }
}