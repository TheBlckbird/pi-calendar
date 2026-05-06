package com.louisweigel.pi_calendar

import androidx.compose.ui.graphics.Color
import com.louisweigel.pi_calendar.core.CalendarManager
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntryType
import com.louisweigel.pi_calendar.core.calendarentry.Event
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.time.Instant

class CalendarManagerTest {
    lateinit var calendarManager: CalendarManager

    @Before
    fun initCalendarManager() {
        calendarManager = CalendarManager("/path/to/db")
    }

    @Test
    fun createFindDeleteCalendars_works() {
        assertEquals(0, calendarManager.getCalendars().count())

        calendarManager.addCalendar("Eltern", "", Color.Black)
        val workCalendar = calendarManager.addCalendar("Arbeit", "", Color.Yellow)
        assertEquals(2, calendarManager.getCalendars().count())

        assertEquals(null, calendarManager.addCalendar("Arbeit", "", Color.Yellow))
        assertEquals(2, calendarManager.getCalendars().count())

        val foundWorkCalendar = calendarManager.findCalendar("Arbeit")
        assertEquals(foundWorkCalendar, workCalendar)
    }

    @Test
    fun createFindDeletePersons_works() {
        assertEquals(0, calendarManager.getAllPersons().count(),)

        val max = calendarManager.addPerson("Max Mustermann")
        calendarManager.addPerson("Thomas Schmidt")
        assertEquals(2, calendarManager.getAllPersons().count())

        calendarManager.addPerson("Thomas Schmidt")
        assertEquals(3, calendarManager.getAllPersons().count())

        val foundPersons = calendarManager.findPersons("Max")
        assertEquals(1, foundPersons.count())
        assertEquals(max, foundPersons.first())

        val foundThomases = calendarManager.findPersons("omas")
        assertEquals(2, foundThomases.count())

        calendarManager.removePerson(foundPersons.first())
        assertEquals(2, calendarManager.getAllPersons().count())
    }

    @Test
    fun calendarEntries_work() {
        assertEquals(0, calendarManager.defaultEventsCalendar.entries.count())

        val event1 = Event(
            "Fahrstunde",
            "138€ hierfür...",
            Instant.parse("2020-07-30T18:00:00Z"),
            Instant.parse("2020-07-30T19:30:00Z"),
            false
        )
        calendarManager.defaultEventsCalendar.entries.add(event1)

        val event2 = Event(
            "Urlaub",
            "Norwegen!",
            Instant.parse("2020-07-30T00:00:00Z"),
            Instant.parse("2020-08-07T00:00:00Z"),
            true
        )
        calendarManager.defaultEventsCalendar.entries.add(event2)

        val event3 = Event(
            "ganz lange Fahrstunde",
            "nur 138€ hierfür?",
            Instant.parse("2020-08-30T18:00:00Z"),
            Instant.parse("2020-08-31T19:30:00Z"),
            false
        )
        calendarManager.defaultEventsCalendar.entries.add(event3)

        val event4 = Event(
            "Feier Andreas",
            "ok...",
            Instant.parse("2020-09-20T00:00:00Z"),
            Instant.parse("2020-09-20T00:00:00Z"),
            true
        )
        calendarManager.defaultEventsCalendar.entries.add(event4)

        assertEquals(4, calendarManager.defaultEventsCalendar.entries.count())

        assertEquals(calendarManager.defaultEventsCalendar.findCalendarEntries(
            listOf(CalendarEntryType.Event),
            "Urlaub",
            null,
            null,
        ).count(), 1)

        assertEquals(2, calendarManager.defaultEventsCalendar.findCalendarEntries(
            listOf(CalendarEntryType.Event),
            "Fahrstunde",
            null,
            null,
        ).count())

        assertEquals(2, calendarManager.defaultEventsCalendar.findCalendarEntries(
            listOf(CalendarEntryType.Event),
            null,
            "138€",
            null,
        ).count())

        assertEquals(1, calendarManager.defaultEventsCalendar.findCalendarEntries(
            listOf(CalendarEntryType.Event),
            "Fahrstunde",
            "...",
            null,
        ).count())

        assertEquals(1, calendarManager.defaultEventsCalendar.findCalendarEntries(
            listOf(CalendarEntryType.Event),
            "Fahrstunde",
            null,
            Pair(
                Instant.parse("2020-07-30T00:00:00Z"),
                Instant.parse("2020-08-02T00:00:00Z"),
            ),
        ).count())
    }
}