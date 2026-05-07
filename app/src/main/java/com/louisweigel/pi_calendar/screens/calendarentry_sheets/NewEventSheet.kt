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
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.screens.components.ClickableSwitchRow
import com.louisweigel.pi_calendar.screens.components.DatePickerRow
import com.louisweigel.pi_calendar.screens.components.TimePickerRow
import com.louisweigel.pi_calendar.utils.getMillisNow
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import java.time.LocalTime
import kotlin.time.Instant

@Composable
fun NewEventSheet(
    onDismissRequest: () -> Unit,
    onSave: (Event) -> Unit,
    modifier: Modifier = Modifier,
    calendars: List<Calendar>,
) {
    val sheetState = rememberModalBottomSheetState()

    var selectedCalendar by remember { mutableStateOf(calendars.first()) }
    var isCalendarSelectionExpanded by remember { mutableStateOf(false) }

    var isDatePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    val dateFromState = rememberDatePickerState(
        initialSelectedDateMillis = getMillisNow()
    )

    var isTimePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    val timeFromState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )

    var isDatePickerUntilOpen by rememberSaveable { mutableStateOf(false) }
    val dateUntilState = rememberDatePickerState(
        initialSelectedDateMillis = getMillisNow()
    )

    var isTimePickerUntilOpen by rememberSaveable { mutableStateOf(false) }
    val timeUntilState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isAllDay by remember { mutableStateOf(true) }

    var showErrorAlert by remember { mutableStateOf(false) }

    val onSaveClick = {
        val timeZone = TimeZone.currentSystemDefault()

        val fromLocalDate = Instant
            .fromEpochMilliseconds(dateFromState.selectedDateMillis!!)
            .toLocalDateTime(TimeZone.UTC)
            .date

        val untilLocalDate = Instant
            .fromEpochMilliseconds(dateUntilState.selectedDateMillis!!)
            .toLocalDateTime(TimeZone.UTC)
            .date

        val dateFrom = if (isAllDay) {
            fromLocalDate.atStartOfDayIn(timeZone)
        } else {
            fromLocalDate
                .atTime(timeFromState.hour, timeFromState.minute)
                .toInstant(timeZone)
        }

        val dateUntil = if (isAllDay) {
            untilLocalDate.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
        } else {
            untilLocalDate
                .atTime(timeUntilState.hour, timeUntilState.minute)
                .toInstant(timeZone)
        }

        if (dateFrom > dateUntil) {
            showErrorAlert = true
        } else {
            onSave(
                Event(
                    title,
                    description,
                    dateFrom,
                    dateUntil,
                    isAllDay,
                    selectedCalendar.uuid
                )
            )
        }
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

            ClickableSwitchRow(
                isAllDay,
                { isAllDay = it },
                stringResource(R.string.addEventMenu_allDayEvent),
            )

            Spacer(modifier = Modifier.padding(bottom = 4.dp))

            Column {
                Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
                    Text(
                        stringResource(R.string.addEventMenu_from),
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

                        if (!isAllDay) {
                            TimePickerRow(
                                isTimePickerFromOpen,
                                timeFromState,
                                { isTimePickerFromOpen = false },
                                { isTimePickerFromOpen = true },
                            )
                        }
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
                    Text(
                        stringResource(R.string.addEventMenu_until),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.labelMedium,
                    )

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

                        if (!isAllDay) {
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

        }
    }

    if (showErrorAlert) {
        AlertDialog(
            onDismissRequest = onDismissRequest,
            dismissButton = null,
            confirmButton = {
                TextButton(onClick = { showErrorAlert = false }) {
                    Text(stringResource(R.string.okay))
                }
            },
            text = {
                Text(stringResource(R.string.addEventMenu_startdateAfterEnddate))
            },
        )
    }
}