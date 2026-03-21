package com.louisweigel.pi_calendar.screens.calendarentry_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.screens.components.DatePickerRow
import com.louisweigel.pi_calendar.screens.components.TimePickerRow
import java.time.LocalTime
import kotlin.time.Instant

@Composable
fun NewEventSheet(
    onDismissRequest: () -> Unit,
    onSave: (Event) -> Unit,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    var isDatePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    val dateFromState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    var isTimePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    val timeFromState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )

    var isDatePickerUntilOpen by rememberSaveable { mutableStateOf(false) }
    val dateUntilState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
    )

    var isTimePickerUntilOpen by rememberSaveable { mutableStateOf(false) }
    val timeUntilState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier.padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text("Abbrechen")
                }

                Button(onClick = {
                    val title = if (title == "") "(Kein Name)" else title
                    val dateFrom = Instant.fromEpochMilliseconds(dateFromState.selectedDateMillis!!)
                    val dateUntil = Instant.fromEpochMilliseconds(dateUntilState.selectedDateMillis!!)

                    onSave(
                        Event(
                            title,
                            description,
                            dateFrom,
                            dateUntil,
                            true
                        )
                    )
                }) {
                    Text("Speichern")
                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Titel") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Beschreibung") },
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                DatePickerRow(
                    isDatePickerFromOpen,
                    dateFromState,
                    { isDatePickerFromOpen = false },
                    { isDatePickerFromOpen = true },
                )

                TimePickerRow(
                    isTimePickerFromOpen,
                    timeFromState,
                    { isTimePickerFromOpen = false },
                    { isTimePickerFromOpen = true },
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                DatePickerRow(
                    isDatePickerUntilOpen,
                    dateUntilState,
                    { isDatePickerUntilOpen = false },
                    { isDatePickerUntilOpen = true },
                )

                TimePickerRow(
                    isTimePickerUntilOpen,
                    timeUntilState,
                    { isTimePickerUntilOpen = false },
                    { isTimePickerUntilOpen = true },
                )
            }
        }
    }
}