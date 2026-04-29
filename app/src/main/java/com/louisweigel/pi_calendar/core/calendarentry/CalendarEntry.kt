package com.louisweigel.pi_calendar.core.calendarentry

import com.louisweigel.pi_calendar.core.Person
import java.util.Date
import java.util.UUID
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

abstract class CalendarEntry(val title: String, val description: String, val date: Instant) {
    val uuid = UUID.randomUUID()
    private val sharedWith = mutableListOf<Person>()

    /**
     * Share this entry with another person
     */
    fun shareWithPerson(person: Person) {
        sharedWith.add(person)
    }

    /**
     * Stop sharing this entry with the given person
     */
    fun stopSharing(person: Person): Boolean {
        return sharedWith.remove(person)
    }

    /**
     * Checks whether the calendar entry is currently shared with the given person
     */
    fun isSharedWith(person: Person): Boolean {
        return sharedWith.contains(person)
    }
}

enum class CalendarEntryType {
    Birthday,
    Event,
    Reminder,
}