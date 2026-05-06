package com.louisweigel.pi_calendar.utils

import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Instant

/**
 * Returns the unix epoch but for the local time
 */
fun getMillisNow(): Long {
    return Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
        .atStartOfDayIn(TimeZone.UTC)
        .toEpochMilliseconds()
}

/**
 * Converts the unix epoch milliseconds back to an instant
 */
fun getInstantFromMillis(millis: Long): Instant {
    return Instant
        .fromEpochMilliseconds(millis)
        .toLocalDateTime(TimeZone.UTC)
        .date
        .atStartOfDayIn(TimeZone.currentSystemDefault())
}