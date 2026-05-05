package com.louisweigel.pi_calendar.core

import com.louisweigel.pi_calendar.R
import java.time.Month

enum class Month {
    JANUARY,
    FEBRUARY,
    MARCH,
    APRIL,
    MAY,
    JUNE,
    JULY,
    AUGUST,
    SEPTEMBER,
    OCTOBER,
    NOVEMBER,
    DECEMBER;

    override fun toString(): String {
        return when (this) {
            JANUARY -> "Januar"
            FEBRUARY -> "Februar"
            MARCH -> "März"
            APRIL -> "April"
            MAY -> "Mai"
            JUNE -> "Juni"
            JULY -> "Juli"
            AUGUST -> "August"
            SEPTEMBER -> "September"
            OCTOBER -> "Oktober"
            NOVEMBER -> "November"
            DECEMBER -> "Dezember"
        }
    }

    fun getTranslationKey(): Int {
        return when (this) {
            JANUARY -> R.string.month_januar
            FEBRUARY -> R.string.month_februar
            MARCH -> R.string.month_mearz
            APRIL -> R.string.month_april
            MAY -> R.string.month_mai
            JUNE -> R.string.month_juni
            JULY -> R.string.month_juli
            AUGUST -> R.string.month_august
            SEPTEMBER -> R.string.month_september
            OCTOBER -> R.string.month_oktober
            NOVEMBER -> R.string.month_november
            DECEMBER -> R.string.month_dezember
        }
    }

    fun toIndex(): Int {
        return when (this) {
            JANUARY -> 1
            FEBRUARY -> 2
            MARCH -> 3
            APRIL -> 4
            MAY -> 5
            JUNE -> 6
            JULY -> 7
            AUGUST -> 8
            SEPTEMBER -> 9
            OCTOBER -> 10
            NOVEMBER -> 11
            DECEMBER -> 12
        }
    }

    fun getPrevious(): com.louisweigel.pi_calendar.core.Month {
        return when (this) {
            JANUARY -> DECEMBER
            FEBRUARY -> JANUARY
            MARCH -> FEBRUARY
            APRIL -> MARCH
            MAY -> APRIL
            JUNE -> MAY
            JULY -> JUNE
            AUGUST -> JULY
            SEPTEMBER -> AUGUST
            OCTOBER -> SEPTEMBER
            NOVEMBER -> OCTOBER
            DECEMBER -> NOVEMBER
        }
    }

    fun getNext(): com.louisweigel.pi_calendar.core.Month {
        return when (this) {
            JANUARY -> FEBRUARY
            FEBRUARY -> MARCH
            MARCH -> APRIL
            APRIL -> MAY
            MAY -> JUNE
            JUNE -> JULY
            JULY -> AUGUST
            AUGUST -> SEPTEMBER
            SEPTEMBER -> OCTOBER
            OCTOBER -> NOVEMBER
            NOVEMBER -> DECEMBER
            DECEMBER -> JANUARY
        }
    }

    companion object {
        fun from(value: Month): com.louisweigel.pi_calendar.core.Month {
            return when (value) {
                Month.APRIL -> APRIL
                Month.AUGUST -> AUGUST
                Month.DECEMBER -> DECEMBER
                Month.FEBRUARY -> FEBRUARY
                Month.JANUARY -> JANUARY
                Month.JULY -> JULY
                Month.JUNE -> JUNE
                Month.MARCH -> MARCH
                Month.MAY -> MAY
                Month.NOVEMBER -> NOVEMBER
                Month.OCTOBER -> OCTOBER
                Month.SEPTEMBER -> SEPTEMBER
            }
        }
    }
}