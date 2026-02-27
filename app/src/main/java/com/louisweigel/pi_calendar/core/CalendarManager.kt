package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.CalenderEntryTypes
import java.util.Date

class CalendarManager(private val dbPath: String) {
    private var calendars = mutableListOf<Calendar>()
    private var allPersons = listOf<Person>()
    private var defaultCalendar: Calendar? = null

    /**
     * Returns all known calendars
     */
    fun getAllCalenders(): List<Calendar> {
        TODO()
    }

    /**
     * Returns the current default calendar
     */
    fun getDefaultCalendar(): Calendar {
        TODO()
    }

    /**
     * Adds a new calendar to the list of calendars
     *
     * Returns `null` if the name is already taken else the newly created Calendar
     */
    fun addCalendar(name: String, description: String, color: Color): Calendar? {
        TODO()
    }

    /**
     * Sets the default calendar
     *
     * This is the calendar that's selected by default when adding a new entry
     */
    fun setDefaultCalendar(calendar: Calendar) {
        TODO()
    }

    /**
     * Try finding a calendar by its name
     *
     * There can't be more than one calendar with the same name
     */
    fun findCalendar(name: String): Calendar? {
        TODO()
    }

    /**
     * Removes the given calendar from the list of all calendars
     *
     * Returns `null` if it doesn't exist or `false` iti is currently set as the default calendar
     */
    fun removeCalendar(calendar: Calendar): Boolean? {
        TODO()
    }

    /**
     * Gets a list of all existing persons in the application
     */
    fun getAllPersons(): List<Person> {
        TODO()
    }

    /**
     * Adds a new person
     *
     * Returns the newly created Person
     */
    fun addPerson(name: String): Person {
        TODO()
    }

    /**
     * Removes the given person if it exists
     */
    fun removePerson(person: Person) {
        TODO()
    }

    /**
     * Find a calendar entry that matches all the given properties
     */
    fun findCalendarEntry(
        calendarEntryTypes: CalenderEntryTypes,
        title: String?,
        description: String?,
        date: Date?,
    ): Pair<Calendar, CalendarEntry> {
        TODO()
    }

    /**
     * Saves the content of this manager to a database/local file
     *
     * Returns `false` if it doesn't succeed
     */
    fun saveToDb(): Boolean {
        TODO()
    }
}