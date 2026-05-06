package com.louisweigel.pi_calendar

import com.louisweigel.pi_calendar.core.Month
import com.louisweigel.pi_calendar.screens.MonthSelection
import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.time.Clock
import kotlin.time.Instant

class MonthSelectionTest {
    @Test
    fun `getNext and getPrevious return expected month and year`() {
        var monthSelection = MonthSelection(Month.NOVEMBER, 2024)
        monthSelection = monthSelection.getNext()
        assertEquals(MonthSelection(Month.DECEMBER, 2024), monthSelection)

        monthSelection = monthSelection.getNext()
        assertEquals(MonthSelection(Month.JANUARY, 2025), monthSelection)

        monthSelection = monthSelection.getPrevious()
        assertEquals(MonthSelection(Month.DECEMBER, 2024), monthSelection)
    }

    @Test
    fun `getToday returns expected month and year`() {
        val clock = Clock.Fixed(Instant.parse("2026-05-05T00:00:00Z"))
        val result = MonthSelection.getToday(clock)

        assertEquals(Month.MAY, result.month)
        assertEquals(2026, result.year)
    }
}

/**
 * Used for mocking a clock
 */
private class FixedClock(private val fixedInstant: Instant): Clock {
    override fun now(): Instant = fixedInstant
}

/**
 * Used for mocking a clock
 */
private fun Clock.Companion.Fixed(fixedInstant: Instant): Clock = FixedClock(fixedInstant)