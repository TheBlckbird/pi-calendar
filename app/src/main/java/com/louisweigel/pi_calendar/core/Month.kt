package com.louisweigel.pi_calendar.core

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