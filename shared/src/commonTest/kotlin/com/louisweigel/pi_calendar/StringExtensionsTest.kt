package com.louisweigel.pi_calendar

import com.louisweigel.pi_calendar.utils.toGenitive
import kotlin.test.Test
import kotlin.test.assertEquals

class StringExtensionsTest {
    @Test
    fun `toGenitive works on non sibilant`() {
        assertEquals("Annas", "Anna".toGenitive())
        assertEquals("Peters", "Peter".toGenitive())
    }

    @Test
    fun `toGenitive works on sibilants`() {
        assertEquals("Max'", "Max".toGenitive())
        assertEquals("Grass'", "Grass".toGenitive())
        assertEquals("Franz'", "Franz".toGenitive())
        assertEquals("Kitz'", "Kitz".toGenitive())
        assertEquals("Watz'", "Watz".toGenitive())
        assertEquals("Alice'", "Alice".toGenitive())
    }

    @Test
    fun `toGenitive check is case insensitive`() {
        assertEquals("MAX'", "MAX".toGenitive())
        assertEquals("MaX'", "MaX".toGenitive())
    }

    @Test
    fun `toGenitive edge cases work`() {
        assertEquals("s'", "s".toGenitive())
        assertEquals("ß'", "ß".toGenitive())
        assertEquals("ẞ'", "ẞ".toGenitive())
        assertEquals("ALICE'", "ALICE".toGenitive())
        assertEquals("as", "a".toGenitive())
    }
}
