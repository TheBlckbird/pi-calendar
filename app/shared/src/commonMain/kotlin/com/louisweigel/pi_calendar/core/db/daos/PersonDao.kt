package com.louisweigel.pi_calendar.core.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.core.Person
import com.louisweigel.pi_calendar.core.db.Converters
import kotlinx.coroutines.flow.Flow
import kotlin.uuid.Uuid

@Dao
@TypeConverters(Converters::class)
interface PersonDao {
    @Insert
    suspend fun insert(person: Person)

    @Delete
    suspend fun delete(person: Person)

    @Query("SELECT * FROM person")
    suspend fun getAll(): List<Person>

    @Query("SELECT * FROM person")
    fun observeAll(): Flow<List<Person>>

    @Query("SELECT * FROM person WHERE name = :name LIMIT 1")
    suspend fun getByName(name: String): Person?

    @Query("SELECT * FROM person WHERE uuid = :uuid LIMIT 1")
    suspend fun getByUuid(uuid: Uuid): Person?
}