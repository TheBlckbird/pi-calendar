package com.louisweigel.pi_calendar.ui.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
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
        val instant = Instant.fromEpochMilliseconds(state.selectedDateMillis!!) // TODO: what about time zones?

        val format = DateTimeComponents.Format { // TODO: check if this formatter is working as intended (i18n)?
            dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED)
            chars(", ")
            dayOfMonth(Padding.ZERO)
            chars(". ")
            monthName(MonthNames.ENGLISH_FULL)
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