package com.louisweigel.pi_calendar.core

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(indices = [Index("name")])
data class Person(
    val name: String,
) {
    @PrimaryKey var uuid: UUID = UUID.randomUUID()
}