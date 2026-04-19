package com.louisweigel.pi_calendar.screens.calendarentry_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.screens.components.ClickableSwitchRow
import com.louisweigel.pi_calendar.screens.components.DatePickerRow
import java.time.LocalTime
import kotlin.time.Instant

@Composable
fun NewBirthdaySheet(
    onDismissRequest: () -> Unit,
    onSave: (Birthday, Calendar) -> Unit,
    modifier: Modifier = Modifier,
    calendars: List<Calendar>,
) {
    val sheetState = rememberModalBottomSheetState()

    var selectedCalendar by remember { mutableStateOf(calendars.first()) }
    var isCalendarSelectionExpanded by remember { mutableStateOf(false) }

    var isDatePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    val dateFromState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )


    var isDatePickerUntilOpen by rememberSaveable { mutableStateOf(false) }
    val dateUntilState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
    )


    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }


    val onSaveClick = {
        val date = Instant.fromEpochMilliseconds(dateFromState.selectedDateMillis!!)

        onSave(
            Birthday(
                title, description, date
            ),
            selectedCalendar,
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

            Row(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                TextButton(
                    onClick = {
                        isCalendarSelectionExpanded = !isCalendarSelectionExpanded
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp),
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        selectedCalendar.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                DropdownMenu(
                    expanded = isCalendarSelectionExpanded,
                    onDismissRequest = { isCalendarSelectionExpanded = false },
                ) {
                    for (calendar in calendars) {
                        DropdownMenuItem(
                            leadingIcon = {
                                if (calendar == selectedCalendar) {
                                    Icon(
                                        painter = painterResource(R.drawable.check_24px),
                                        contentDescription = null,
                                    )
                                }
                            },
                            text = {
                                Text(calendar.name)
                            },
                            onClick = {
                                selectedCalendar = calendar
                                isCalendarSelectionExpanded = false
                            }
                        )
                    }

                }
            }

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(stringResource(R.string.addEntryMenu_titleField) + "*") },
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
                        "Wann",
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium,
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
                    }
                }
            }
        }
    }
}

