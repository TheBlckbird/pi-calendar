package com.louisweigel.pi_calendar.core.calendarentry

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

class Birthday(title: String, description: String, date: Instant) : CalendarEntry(
    title, description, date
) {
    override fun includesDate(date: Instant): Boolean {
        val localDate = date.toLocalDateTime(TimeZone.currentSystemDefault())
        val localDateOfBirth = this.date.toLocalDateTime(TimeZone.currentSystemDefault())

        return localDate.day == localDateOfBirth.day
                && localDate.month == localDateOfBirth.month
                && localDate.year >= localDateOfBirth.year
    }

    /**
     * Calculates the age of the person for a given year
     */
    fun getAge(year: Int): Int {
        return year - this.date.toLocalDateTime(TimeZone.currentSystemDefault()).year
    }
}