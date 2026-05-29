package com.louisweigel.pi_calendar.core.calendarentry

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.db.Converters
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import com.louisweigel.pi_calendar.utils.toGenitive
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant
import kotlin.uuid.Uuid

@Entity(
    foreignKeys = [ForeignKey(
        entity = Calendar::class,
        parentColumns = ["uuid"],
        childColumns = ["calendarUuid"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )],
    indices = [Index("calendarUuid")]
)
@TypeConverters(Converters::class)
class Birthday(
    override val title: String,
    override val description: String,
    override val date: Instant,
    override val calendarUuid: Uuid,
    @PrimaryKey override val uuid: Uuid = Uuid.random(),
) : CalendarEntry(uuid, title, description, date, calendarUuid) {

    override fun includesDate(date: LocalDate): Boolean {
        val localDateOfBirth = dateToLocalDate()

        var dayToCheck = localDateOfBirth.day

        if (localDateOfBirth.day == 29 && localDateOfBirth.month == Month.FEBRUARY && !date.toJavaLocalDate().isLeapYear) {
            dayToCheck = 28
        }

        return date.day == dayToCheck
                && date.month == localDateOfBirth.month
                && date.year >= localDateOfBirth.year
    }

    override fun isInMonth(monthSelection: MonthSelection): Boolean {
        val localDateOfBirth = dateToLocalDate()
        return localDateOfBirth.month == monthSelection.month.toKotlinMonth()
    }

    /**
     * Calculates the age of the person for a given year
     */
    fun getAge(year: Int): Int {
        return year - this.date.toLocalDateTime(TimeZone.currentSystemDefault()).year
    }

    /**
     * Gets the title for the calendar entry
     */
    @Composable
    override fun getTitle(date: LocalDate): String {
        return "${this.title.toGenitive()} ${this.getAge(date.year)}. ${
            stringResource(
                R.string.calendarScreen_birthday
            )
        }"
    }

}
