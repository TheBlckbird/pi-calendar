package com.louisweigel.pi_calendar.core.db

import androidx.compose.ui.graphics.Color
import androidx.room.TypeConverter
import kotlin.time.Instant

object Converters {
    @TypeConverter
    fun fromInstant(instant: Instant?): Long? = instant?.toEpochMilliseconds()

    @TypeConverter
    fun toInstant(value: Long?): Instant? = value?.let { Instant.fromEpochMilliseconds(it) }

    @TypeConverter
    fun fromColor(color: Color?): Long? = color?.value?.toLong()

    @TypeConverter
    fun toColor(value: Long?): Color? = value?.let { Color(it.toULong()) }
}