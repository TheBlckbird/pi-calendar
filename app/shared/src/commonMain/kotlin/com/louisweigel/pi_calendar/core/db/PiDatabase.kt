package com.louisweigel.pi_calendar.core.db

import androidx.compose.ui.graphics.Color
import androidx.room.*
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.Person
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import com.louisweigel.pi_calendar.core.db.daos.CalendarDao
import com.louisweigel.pi_calendar.core.db.daos.CalendarEntryDao
import com.louisweigel.pi_calendar.core.db.daos.PersonDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

@Database(
    entities = [
        Calendar::class,
        Person::class,
        Event::class,
        Reminder::class,
        Birthday::class
    ],
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3),
        AutoMigration(from = 3, to = 4),
    ],
    version = 4,
    exportSchema = true
)
@ConstructedBy(PiDatabaseConstructor::class)
abstract class PiDatabase : RoomDatabase() {
    abstract fun personDao(): PersonDao
    abstract fun calendarDao(): CalendarDao
    abstract fun calendarEntryDao(): CalendarEntryDao
}

@Suppress("KotlinNoActualForExpect")
expect object PiDatabaseConstructor : RoomDatabaseConstructor<PiDatabase> {
    override fun initialize(): PiDatabase
}

fun getRoomDatabase(
    builder: RoomDatabase.Builder<PiDatabase>
): PiDatabase {
    lateinit var database: PiDatabase

    database = builder
        .setDriver(BundledSQLiteDriver())
        .setQueryCoroutineContext(Dispatchers.IO)
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(connection: SQLiteConnection) {
                super.onCreate(connection)

                CoroutineScope(Dispatchers.IO).launch {
                    database.calendarDao().insertCalendar(
                        Calendar(
                            Calendar.DEFAULT_EVENTS_CALENDAR_NAME,
                            "",
                            Color(0xFF2962FF),
                            true,
                        )
                    )

                    database.calendarDao().insertCalendar(
                        Calendar(
                            Calendar.DEFAULT_BIRTHDAYS_CALENDAR_NAME,
                            "",
                            Color.Gray,
                            true,
                        )
                    )

                    database.calendarDao().insertCalendar(
                        Calendar(
                            Calendar.DEFAULT_REMINDERS_CALENDAR_NAME,
                            "",
                            Color(0xFF1A8247),
                            true,
                        )
                    )
                }
            }
        })
        .build()

    return database
}