package com.louisweigel.pi_calendar.core

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.core.db.Converters
import kotlin.uuid.Uuid

@Entity(indices = [Index("name")])
@TypeConverters(Converters::class)
data class Person(
    val name: String,
) {
    @PrimaryKey var uuid: Uuid = Uuid.random()
}