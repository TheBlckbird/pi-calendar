package com.louisweigel.pi_calendar.core

import com.louisweigel.pi_calendar.R
import kotlinx.datetime.Month

/**
 * Get the translation key for this month in order to display it in the UI
 */
fun Month.getTranslationKey(): Int {
    return when (this) {
        Month.JANUARY -> R.string.month_januar
        Month.FEBRUARY -> R.string.month_februar
        Month.MARCH -> R.string.month_mearz
        Month.APRIL -> R.string.month_april
        Month.MAY -> R.string.month_mai
        Month.JUNE -> R.string.month_juni
        Month.JULY -> R.string.month_juli
        Month.AUGUST -> R.string.month_august
        Month.SEPTEMBER -> R.string.month_september
        Month.OCTOBER -> R.string.month_oktober
        Month.NOVEMBER -> R.string.month_november
        Month.DECEMBER -> R.string.month_dezember
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
