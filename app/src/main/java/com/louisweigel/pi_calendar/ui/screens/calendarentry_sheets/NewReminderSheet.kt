package com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.louisweigel.pi_calendar.utils.getInstantFromMillis
import com.louisweigel.pi_calendar.utils.getMillisNow

@Composable
fun NewReminderSheet(
    onDismissRequest: () -> Unit,
    onSave: (Reminder) -> Unit,
    birthdayCalendar: Calendar,
    modifier: Modifier = Modifier,
) {
    val sheetState = rememberModalBottomSheetState()

    var isDatePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    val dateOfBirthState = rememberDatePickerState(initialSelectedDateMillis = getMillisNow())

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    val onSaveClick = {
        val date = getInstantFromMillis(dateOfBirthState.selectedDateMillis!!)

        onSave(
            Reminder(
                name,
                description,
                date,
                birthdayCalendar.uuid
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextButton(onClick = onDismissRequest) {
                    Text(stringResource(R.string.cancel))
                }

                Button(
                    onClick = onSaveClick,
                ) {
                    Text(stringResource(R.string.save))
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.addBirthdayMenu_nameField)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.addEntryMenu_descriptionField)) },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.padding(bottom = 4.dp))

            Column {
                Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
                    Text(
                        stringResource(R.string.addBirthdayMenu_dateOfBirth),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium,
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        DatePickerRow(
                            isDatePickerFromOpen,
                            dateOfBirthState,
                            { isDatePickerFromOpen = false },
                            { isDatePickerFromOpen = true },
                        )
                    }
                }
            }
        }
    }
}