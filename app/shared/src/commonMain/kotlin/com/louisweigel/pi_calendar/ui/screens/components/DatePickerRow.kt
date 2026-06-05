package com.louisweigel.pi_calendar.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.core.getTranslationKey
import kotlinx.datetime.Month
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import org.jetbrains.compose.resources.stringResource
import pi_calendar.app.shared.generated.resources.Res
import pi_calendar.app.shared.generated.resources.calendarScreen_friday
import pi_calendar.app.shared.generated.resources.calendarScreen_monday
import pi_calendar.app.shared.generated.resources.calendarScreen_saturday
import pi_calendar.app.shared.generated.resources.calendarScreen_sunday
import pi_calendar.app.shared.generated.resources.calendarScreen_thursday
import pi_calendar.app.shared.generated.resources.calendarScreen_tuesday
import pi_calendar.app.shared.generated.resources.calendarScreen_wednesday
import pi_calendar.app.shared.generated.resources.month_april
import pi_calendar.app.shared.generated.resources.month_august
import pi_calendar.app.shared.generated.resources.month_december
import pi_calendar.app.shared.generated.resources.month_february
import pi_calendar.app.shared.generated.resources.month_january
import pi_calendar.app.shared.generated.resources.month_july
import pi_calendar.app.shared.generated.resources.month_june
import pi_calendar.app.shared.generated.resources.month_may
import pi_calendar.app.shared.generated.resources.month_march
import pi_calendar.app.shared.generated.resources.month_november
import pi_calendar.app.shared.generated.resources.month_october
import pi_calendar.app.shared.generated.resources.month_september
import kotlin.time.Instant

@Composable
fun DatePickerRow(
    isPickerOpen: Boolean,
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onOpen: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onOpen,
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .padding(vertical = 8.dp),

        colors = ButtonDefaults.textButtonColors().copy(
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        val instant = Instant.fromEpochMilliseconds(state.selectedDateMillis!!)

        val dayOfWeekNames = DayOfWeekNames(
            listOf(
                stringResource(Res.string.calendarScreen_monday),
                stringResource(Res.string.calendarScreen_tuesday),
                stringResource(Res.string.calendarScreen_wednesday),
                stringResource(Res.string.calendarScreen_thursday),
                stringResource(Res.string.calendarScreen_friday),
                stringResource(Res.string.calendarScreen_saturday),
                stringResource(Res.string.calendarScreen_sunday),
            )
        )
        val monthNames = MonthNames(
            Month.entries.map { month ->
                stringResource(month.getTranslationKey())
            }
        )

        val format = DateTimeComponents.Format {
            dayOfWeek(dayOfWeekNames)
            chars(", ")
            day(Padding.ZERO)
            chars(". ")
            monthName(monthNames)
            chars(" ")
            year()
        }

        val formattedDate = instant.format(format)

        Text(
            formattedDate,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (isPickerOpen) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Auswählen")
                }
            },
            dismissButton = null,
        ) {
            DatePicker(state = state)
        }
    }
}