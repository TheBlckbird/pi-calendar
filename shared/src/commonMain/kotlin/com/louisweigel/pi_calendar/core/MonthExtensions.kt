package com.louisweigel.pi_calendar.core

import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.StringResource
import pi_calendar_kmp.shared.generated.resources.Res
import pi_calendar_kmp.shared.generated.resources.month_april
import pi_calendar_kmp.shared.generated.resources.month_august
import pi_calendar_kmp.shared.generated.resources.month_dezember
import pi_calendar_kmp.shared.generated.resources.month_februar
import pi_calendar_kmp.shared.generated.resources.month_januar
import pi_calendar_kmp.shared.generated.resources.month_juli
import pi_calendar_kmp.shared.generated.resources.month_juni
import pi_calendar_kmp.shared.generated.resources.month_mai
import pi_calendar_kmp.shared.generated.resources.month_mearz
import pi_calendar_kmp.shared.generated.resources.month_november
import pi_calendar_kmp.shared.generated.resources.month_oktober
import pi_calendar_kmp.shared.generated.resources.month_september

/**
 * Get the translation key for this month in order to display it in the UI
 */
fun Month.getTranslationKey(): StringResource {
    return when (this) {
        Month.JANUARY -> Res.string.month_januar
        Month.FEBRUARY -> Res.string.month_februar
        Month.MARCH -> Res.string.month_mearz
        Month.APRIL -> Res.string.month_april
        Month.MAY -> Res.string.month_mai
        Month.JUNE -> Res.string.month_juni
        Month.JULY -> Res.string.month_juli
        Month.AUGUST -> Res.string.month_august
        Month.SEPTEMBER -> Res.string.month_september
        Month.OCTOBER -> Res.string.month_oktober
        Month.NOVEMBER -> Res.string.month_november
        Month.DECEMBER -> Res.string.month_dezember
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

/**
 * Get the previous month
 *
 * Wraps around from January to December
 */
fun Month.getPrevious(): Month {
    return when (this) {
        Month.JANUARY -> Month.DECEMBER
        Month.FEBRUARY -> Month.JANUARY
        Month.MARCH -> Month.FEBRUARY
        Month.APRIL -> Month.MARCH
        Month.MAY -> Month.APRIL
        Month.JUNE -> Month.MAY
        Month.JULY -> Month.JUNE
        Month.AUGUST -> Month.JULY
        Month.SEPTEMBER -> Month.AUGUST
        Month.OCTOBER -> Month.SEPTEMBER
        Month.NOVEMBER -> Month.OCTOBER
        Month.DECEMBER -> Month.NOVEMBER
    }
}

/**
 * Get the next month
 *
 * Wraps back around after December
 */
fun Month.getNext(): Month {
    return when (this) {
        Month.JANUARY -> Month.FEBRUARY
        Month.FEBRUARY -> Month.MARCH
        Month.MARCH -> Month.APRIL
        Month.APRIL -> Month.MAY
        Month.MAY -> Month.JUNE
        Month.JUNE -> Month.JULY
        Month.JULY -> Month.AUGUST
        Month.AUGUST -> Month.SEPTEMBER
        Month.SEPTEMBER -> Month.OCTOBER
        Month.OCTOBER -> Month.NOVEMBER
        Month.NOVEMBER -> Month.DECEMBER
        Month.DECEMBER -> Month.JANUARY
    }
}

fun LocalDate.isLeapYear(): Boolean = (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)