package com.louisweigel.pi_calendar.core

import androidx.compose.ui.graphics.Color
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntryType
import kotlin.time.Instant

class CalendarManager(private val dbPath: String) {
    private val calendars = mutableListOf<Calendar>()
    private var allPersons = mutableListOf<Person>()
    var defaultEventsCalendar: Calendar =
        Calendar(
            "Mein Kalender",
            "Der Kalender für alle persönlichen Termine",
            Color.Green,
            false
        )

    var defaultRemindersCalendar: Calendar =
        Calendar("Erinnerungen", "Alle Erinnerungen", Color.Red, true)

    var defaultBirthdaysCalendar: Calendar =
        Calendar("Geburtstage", "Alle gespeicherten Geburtstage", Color.Blue, true)

    /**
     * Returns all non default calendars
     */
    fun getCalendars(): List<Calendar> {
        return calendars
    }

    /**
     * Returns all calendars, including the default calendars
     */
    private fun getAllCalendars(): List<Calendar> {
        return calendars + defaultBirthdaysCalendar + defaultRemindersCalendar + defaultEventsCalendar
    }

    /**
     * Checks whether the given calendar is one of the default calendars
     *
     * Returns `true` if it is
     */
    fun isDefaultCalendar(calendar: Calendar): Boolean {
        return defaultEventsCalendar == calendar
                || defaultRemindersCalendar == calendar
                || defaultBirthdaysCalendar == calendar
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
        if (isDefaultCalendar(calendar)) {
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
     * Finds all persons with the given search term in their name
     */
    fun findPersons(search: String): List<Person> {
        val foundPersons = mutableListOf<Person>()

        for (person in allPersons) {
            if (person.name.contains(search)) {
                foundPersons.add(person)
            }
        }

        return foundPersons
    }

    /**
     * Removes the given person if it exists
     */
    fun removePerson(person: Person): Boolean {
        return allPersons.remove(person)
    }

    /**
     * Returns a list with all the calendar entries and their corresponding calendar
     */
    fun getAllCalendarEntries(): List<Pair<Calendar, CalendarEntry>> {
        val entries = mutableListOf<Pair<Calendar, CalendarEntry>>()

        for (calendar in getAllCalendars()) {
            entries += calendar.entries.map { Pair(calendar, it) }
        }

        return entries
    }

    /**
     * Find a calendar entry that matches all the given properties
     */
    fun findCalendarEntries(
        calendarEntryTypes: List<CalendarEntryType>,
        title: String?,
        description: String?,
        dateRange: Pair<Instant, Instant>?,
    ): List<Pair<Calendar, CalendarEntry>> {
        val foundEntries = mutableListOf<Pair<Calendar, CalendarEntry>>()

        for (calendar in getAllCalendars()) {
            foundEntries +=
                calendar.findCalendarEntries(
                    calendarEntryTypes,
                    title,
                    description,
                    dateRange
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

    /**
     * Gets all the calendars that are shared with a specific person
     */
    fun getCalendarsSharedWith(person: Person): List<Calendar> {
        val sharedCalendars = mutableListOf<Calendar>()

        for (calendar in getAllCalendars()) {
            if (calendar.isSharedWith(person)) {
                sharedCalendars.add(calendar)
            }
        }

        return sharedCalendars
    }

    /**
     * Returns all calendar entries that are shared with a specific person.
     *
     * `includeCalendars` sets whether all entries from the calendars that are shared with the person should also be included
     */
    fun getEntriesSharedWith(person: Person, includeCalendars: Boolean = false): List<CalendarEntry> {
        val sharedEntries = mutableListOf<CalendarEntry>()

        if (includeCalendars) {
            val sharedCalendars = getCalendarsSharedWith(person)

            for (calendar in sharedCalendars) {
                sharedEntries += calendar.entries
            }
        }

        for (calendar in getAllCalendars()) {
            for (entry in calendar.entries) {
                if (entry.isSharedWith(person)) {
                    sharedEntries.add(entry)
                }
            }
        }

        return sharedEntries
    }
}