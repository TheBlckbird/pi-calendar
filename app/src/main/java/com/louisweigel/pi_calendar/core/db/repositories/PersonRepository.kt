package com.louisweigel.pi_calendar.core.db.repositories

import com.louisweigel.pi_calendar.core.Person
import com.louisweigel.pi_calendar.core.db.daos.PersonDao
import kotlinx.coroutines.flow.Flow

class PersonRepository(
    private val dao: PersonDao
) {
    suspend fun getAll(): List<Person> =
        dao.getAll()

    suspend fun insert(person: Person) =
        dao.insert(person)

    fun observeAll(): Flow<List<Person>> =
        dao.observeAll()
}