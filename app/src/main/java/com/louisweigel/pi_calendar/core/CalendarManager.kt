package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.CalenderEntryType
import java.util.Date

class CalendarManager(private val dbPath: String) {
    private val calendars = mutableListOf<Calendar>()
    private var allPersons = mutableListOf<Person>()
    private var defaultCalendar: Calendar? = null

    /**
     * Returns all known calendars
     */
    fun getAllCalenders(): List<Calendar> {
        return calendars
    }

    /**
     * Returns the current default calendar
     */
    fun getDefaultCalendar(): Calendar? {
        return defaultCalendar
    }

    /**
     * Adds a new calendar to the list of calendars
     *
     * Returns `null` if the name is already taken else the newly created Calendar
     */
    fun addCalendar(name: String, description: String, color: Color): Calendar? {
        val calendar = Calendar(name, description, color, false)
        if (findCalendar(name) != null) {
            return null
        }

        calendars.add(calendar)
        return calendar
    }

    /**
     * Sets the default calendar
     *
     * This is the calendar that's selected by default when adding a new entry
     */
    fun setDefaultCalendar(calendar: Calendar) {
        defaultCalendar = calendar
    }

    /**
     * Try finding a calendar by its name
     *
     * There can't be more than one calendar with the same name
     */
    fun findCalendar(name: String): Calendar? {
        for (calendar in calendars) {
            if (calendar.name == name) {
                return calendar
            }
        }

        return null
    }

    /**
     * Removes the given calendar from the list of all calendars
     *
     * Returns `null` if it doesn't exist or `false` if ti is currently set as the default calendar
     */
    fun removeCalendar(calendar: Calendar): Boolean? {
        if (defaultCalendar == calendar) {
            return false
        }

        if (calendars.remove(calendar)) {
            return true
        }

        return null
    }

    /**
     * Gets a list of all existing persons in the application
     */
    fun getAllPersons(): List<Person> {
        return allPersons
    }

    /**
     * Adds a new person
     *
     * Returns the newly created Person
     */
    fun addPerson(name: String): Person {
        val person = Person(name)
        allPersons.add(person)
        return person
    }

    /**
     * Removes the given person if it exists
     */
    fun removePerson(person: Person): Boolean {
        return allPersons.remove(person)
    }

    /**
     * Find a calendar entry that matches all the given properties
     */
    fun findCalendarEntries(
        calendarEntryTypes: List<CalenderEntryType>,
        title: String?,
        description: String?,
        date: Date?,
    ): List<Pair<Calendar, CalendarEntry>> {
        val foundEntries = mutableListOf<Pair<Calendar, CalendarEntry>>()

        for (calendar in calendars) {
            foundEntries +=
                calendar.findCalendarEntries(
                    calendarEntryTypes,
                    title,
                    description,
                    date
                ).map { entry -> Pair(calendar, entry) }
        }

        return foundEntries
    }

    /**
     * Saves the content of this manager to a database/local file
     *
     * Returns `false` if it doesn't succeed
     */
    fun saveToDb(): Boolean {
        TODO("needs more planning")
    }
}