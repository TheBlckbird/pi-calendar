package com.louisweigel.pi_calendar.core.db.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.louisweigel.pi_calendar.core.Person
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
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
    suspend fun getByUuid(uuid: UUID): Person?
}