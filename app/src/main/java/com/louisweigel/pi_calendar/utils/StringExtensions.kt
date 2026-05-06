package com.louisweigel.pi_calendar.utils

fun String.endsWithAny(vararg suffixes: String, ignoreCase: Boolean = false): Boolean =
    suffixes.any { endsWith(it, ignoreCase) }

fun String.toGenitive(): String {
    return if (this.endsWithAny("s", "ss", "ß", "tz", "z", "x", "ce", ignoreCase = true)) {
        "$this'"
    } else {
        "${this}s"
    }
}