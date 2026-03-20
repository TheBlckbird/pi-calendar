package com.louisweigel.pi_calendar.screens.calendarentry_sheets

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@Composable
fun DatePickerRow(
    isDatePickerOpen: Boolean,
    state: DatePickerState,
    onDismissRequest: () -> Unit,
    onOpen: () -> Unit
) {
    Row {
        Button(onClick = onOpen) {
            Text("Datum auswählen")
        }

        if (state.selectedDateMillis == null) {
            Text("Kein Datum ausgewählt")
        } else {
            val instant =
                Instant.fromEpochMilliseconds(state.selectedDateMillis!!).toJavaInstant().atZone(
                    ZoneId.systemDefault()
                )
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            val formattedDate = formatter.format(instant)

            Text(formattedDate)
        }
    }

    if (isDatePickerOpen) {
        DatePickerDialog(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Auswälhen")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Abbrechen")
                }
            }
        ) {
            DatePicker(state = state)
        }
    }
}