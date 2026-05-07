package com.louisweigel.pi_calendar.core.db

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.Person
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import com.louisweigel.pi_calendar.core.db.daos.CalendarDao
import com.louisweigel.pi_calendar.core.db.daos.CalendarEntryDao
import com.louisweigel.pi_calendar.core.db.daos.PersonDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Calendar::class,
        Person::class,
        Event::class,
        Reminder::class,
        Birthday::class
    ],
    version = 1
)
abstract class PiDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun calendarDao(): CalendarDao
    abstract fun calendarEntryDao(): CalendarEntryDao

    companion object {
        @Volatile
        private var INSTANCE: PiDatabase? = null

        fun getInstance(context: Context): PiDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context): PiDatabase {
            lateinit var db: PiDatabase

            db = Room.databaseBuilder(
                context.applicationContext,
                PiDatabase::class.java,
                "pi-calendar.db" // TODO: change debug
            )
                .addCallback(object : Callback() {
                    override fun onCreate(dbSqlite: SupportSQLiteDatabase) {
                        super.onCreate(dbSqlite)

                        CoroutineScope(Dispatchers.IO).launch {
                            db.calendarDao().insertCalendar(
                                Calendar(
                                    Calendar.DEFAULT_EVENTS_CALENDAR_NAME,
                                    "",
                                    Color.Green,
                                    true,
                                    null,
                                )
                            )

                            db.calendarDao().insertCalendar(
                                Calendar(
                                    Calendar.DEFAULT_BIRTHDAYS_CALENDAR_NAME,
                                    "",
                                    Color.Red,
                                    true,
                                    null,
                                )
                            )

                            db.calendarDao().insertCalendar(
                                Calendar(
                                    Calendar.DEFAULT_REMINDERS_CALENDAR_NAME,
                                    "",
                                    Color.Blue,
                                    true,
                                    null,
                                )
                            )
                        }
                    }
                })
                .build()

            return db
        }
    }
}