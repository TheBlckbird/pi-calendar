package com.louisweigel.pi_calendar.core.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.louisweigel.pi_calendar.core.Calendar
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface CalendarDao {
    @Insert
    suspend fun insertCalendar(calendar: Calendar)

    @Query("SELECT * FROM calendar WHERE isSystem = 0")
    suspend fun getAllUserCalendars(): List<Calendar>

    @Query("SELECT * FROM calendar WHERE isSystem = 0")
    fun observeAllUserCalendars(): Flow<List<Calendar>>

    @Update
    suspend fun update(calendar: Calendar)

    @Delete
    suspend fun delete(calendar: Calendar)

    @Query("SELECT * FROM calendar WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): Calendar?

    @Query("SELECT * FROM calendar WHERE name = :name LIMIT 1")
    fun observeByName(name: String): Flow<Calendar?>

    @Query("SELECT * FROM calendar WHERE uuid = :uuid LIMIT 1")
    suspend fun getByUuid(uuid: UUID): Calendar?
}