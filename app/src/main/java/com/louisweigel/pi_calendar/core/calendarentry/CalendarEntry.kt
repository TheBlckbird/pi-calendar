package com.louisweigel.pi_calendar.core.calendarentry

import com.louisweigel.pi_calendar.core.Person
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
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

    /**
     * Checks whether the given date is included in the time frame of the event
     */
    open fun includesDate(date: Instant): Boolean {
        val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val thisLocalDate = this.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        return localDate == thisLocalDate
    }
}

enum class CalendarEntryType {
    Birthday,
    Event,
    Reminder,
}