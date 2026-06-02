package com.louisweigel.pi_calendar.core.db

import androidx.room.RoomDatabase
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.louisweigel.pi_calendar.core.Calendar
import kotlinx.coroutines.runBlocking

object SeedDefaultCalendars : RoomDatabase.Callback() {
    override fun onCreate(connection: SQLiteConnection) {
        super.onCreate(connection)

        runBlocking {
            seedDefaults(connection)
        }
    }
}

private suspend fun seedDefaults(connection: SQLiteConnection) {
    connection.execSQL("""
        INSERT INTO Calendar(name, description, colorArgb, isEditable)
        VALUES ('${Calendar.DEFAULT_EVENTS_CALENDAR_NAME}', '', ${0xFF2962FF.toInt()}, 1)
    """.trimIndent())
}