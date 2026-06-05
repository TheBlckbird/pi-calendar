package com.louisweigel.pi_calendar

import com.louisweigel.pi_calendar.core.isLeapYear
import kotlinx.datetime.LocalDate
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateTest {
    @Test
    fun `isLeapYear() works correctly`() {
        assertTrue(LocalDate(2024, 1, 1).isLeapYear())
        assertTrue(LocalDate(2000, 1, 1).isLeapYear())
        assertFalse(LocalDate(2023, 1, 1).isLeapYear())
        assertFalse(LocalDate(1900, 1, 1).isLeapYear())
    }
}