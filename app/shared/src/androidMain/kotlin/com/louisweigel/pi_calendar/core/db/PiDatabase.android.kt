package com.louisweigel.pi_calendar.core.db

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<PiDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("pi-calendar.db")

    return Room.databaseBuilder(
        context = appContext,
        name = dbFile.absolutePath,
    )
}