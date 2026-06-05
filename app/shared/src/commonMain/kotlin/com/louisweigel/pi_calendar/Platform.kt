package com.louisweigel.pi_calendar

import com.louisweigel.pi_calendar.core.db.PiDatabase

interface Platform {
    val name: String
    val database: PiDatabase
}

expect fun getPlatform(): Platform