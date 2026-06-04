package com.louisweigel.pi_calendar.core

import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import pi_calendar_kmp.shared.generated.resources.Res
import pi_calendar_kmp.shared.generated.resources.month_april
import pi_calendar_kmp.shared.generated.resources.month_august
import pi_calendar_kmp.shared.generated.resources.month_december
import pi_calendar_kmp.shared.generated.resources.month_february
import pi_calendar_kmp.shared.generated.resources.month_january
import pi_calendar_kmp.shared.generated.resources.month_july
import pi_calendar_kmp.shared.generated.resources.month_june
import pi_calendar_kmp.shared.generated.resources.month_may
import pi_calendar_kmp.shared.generated.resources.month_march
import pi_calendar_kmp.shared.generated.resources.month_november
import pi_calendar_kmp.shared.generated.resources.month_october
import pi_calendar_kmp.shared.generated.resources.month_september
import kotlin.time.Clock

/**
 * Get the translation key for this month in order to display it in the UI
 */
fun Month.getTranslationKey(): StringResource {
    return when (this) {
        Month.JANUARY -> Res.string.month_january
        Month.FEBRUARY -> Res.string.month_february
        Month.MARCH -> Res.string.month_march
        Month.APRIL -> Res.string.month_april
        Month.MAY -> Res.string.month_may
        Month.JUNE -> Res.string.month_june
        Month.JULY -> Res.string.month_july
        Month.AUGUST -> Res.string.month_august
        Month.SEPTEMBER -> Res.string.month_september
        Month.OCTOBER -> Res.string.month_october
        Month.NOVEMBER -> Res.string.month_november
        Month.DECEMBER -> Res.string.month_december
    }
}

/**
 * Get the index of the month in the enum
 */
fun Month.toIndex(): Int {
    return when (this) {
        Month.JANUARY -> 1
        Month.FEBRUARY -> 2
        Month.MARCH -> 3
        Month.APRIL -> 4
        Month.MAY -> 5
        Month.JUNE -> 6
        Month.JULY -> 7
        Month.AUGUST -> 8
        Month.SEPTEMBER -> 9
        Month.OCTOBER -> 10
        Month.NOVEMBER -> 11
        Month.DECEMBER -> 12
    }
}

fun LocalDate.isLeapYear(): Boolean = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)

fun YearMonth.Companion.now(): YearMonth {
    val current = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    return YearMonth(current.year, current.month)
}

@Composable
fun YearMonth.toTitle(): String {
    val yearNow = Clock.System.todayIn(TimeZone.currentSystemDefault()).year

    val yearPart = if (year != yearNow) {
        " $year"
    } else {
        ""
    }

    return stringResource(month.getTranslationKey()) + yearPart
}