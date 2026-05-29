package com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import com.louisweigel.pi_calendar.ui.screens.components.DatePickerRow
import com.louisweigel.pi_calendar.ui.screens.components.ModalSaveCancelRow
import com.louisweigel.pi_calendar.ui.screens.components.TimePickerRow
import com.louisweigel.pi_calendar.utils.getInstantFromMillis
import com.louisweigel.pi_calendar.utils.getMillisNow
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.time.LocalTime
import kotlin.uuid.Uuid

@Composable
fun NewReminderSheet(
    onDismissRequest: () -> Unit,
    onSave: (Reminder) -> Unit,
    reminderCalendar: Calendar,
    modifier: Modifier = Modifier,
    editReminder: Reminder? = null,
) {
    val sheetState = rememberModalBottomSheetState()

    var isDatePickerOpen by rememberSaveable { mutableStateOf(false) }
    var dateState = rememberDatePickerState(initialSelectedDateMillis = getMillisNow())

    var isTimePickerOpen by rememberSaveable { mutableStateOf(false) }
    var timeState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isDone by remember { mutableStateOf(false) }

    if (editReminder != null) {
        name = editReminder.title
        description = editReminder.description
        isDone = editReminder.isDone

        // Do some date calculations to get the correct time
        val localDate = editReminder.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val utcDateFromMillis = localDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

        dateState = rememberDatePickerState(utcDateFromMillis)
    }

    val onSaveClick = {
        val date = getInstantFromMillis(dateState.selectedDateMillis!!)
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
            .atTime(timeState.hour, timeState.minute)
            .toInstant(TimeZone.currentSystemDefault())

        onSave(
            Reminder(
                name,
                description,
                date,
                reminderCalendar.uuid,
                isDone,
                editReminder?.uuid ?: Uuid.random(),
            ),
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier.padding(horizontal = 20.dp)
        ) {
            ModalSaveCancelRow(onDismissRequest, onSaveClick)

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = isDone,
                    onCheckedChange = { isDone = it },
                )

                Text(stringResource(R.string.addReminderMenu_isDone))
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.padding(bottom = 4.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.addEntryMenu_titleField)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.addMenu_descriptionField)) },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.padding(bottom = 4.dp))

            Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
                Text(
                    stringResource(R.string.addReminderMenu_dateTimeField),
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.labelMedium,
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    DatePickerRow(
                        isDatePickerOpen,
                        dateState,
                        { isDatePickerOpen = false },
                        { isDatePickerOpen = true },
                    )

                    TimePickerRow(
                        isTimePickerOpen,
                        timeState,
                        { isTimePickerOpen = false },
                        { isTimePickerOpen = true },
                    )
                }
            }
        }
    }
}
