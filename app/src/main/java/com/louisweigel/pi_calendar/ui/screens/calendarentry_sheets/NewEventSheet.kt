package com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.ui.screens.calendarmanager.ColorRow
import com.louisweigel.pi_calendar.ui.screens.components.ClickableSwitchRow
import com.louisweigel.pi_calendar.ui.screens.components.DatePickerRow
import com.louisweigel.pi_calendar.ui.screens.components.ModalSaveCancelRow
import com.louisweigel.pi_calendar.ui.screens.components.TimePickerRow
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
import kotlin.uuid.Uuid

/**
 * Shows a sheet to add a new event
 *
 * @param[onDismissRequest] This is called when the sheet is dismissed. It should hide it
 * @param[onSave] This function is called with the new event that should be saved. It should also close this sheet
 * @param[calendars] A list of all available calendars for events
 * @param[editEvent] Sets this to editing mode and pre-fills the fields with its data
 */
@Composable
fun NewEventSheet(
    onDismissRequest: () -> Unit,
    onSave: (Event) -> Unit,
    modifier: Modifier = Modifier,
    calendars: List<Calendar>,
    editEvent: Event? = null,
) {
    val sheetState = rememberModalBottomSheetState()

    var selectedCalendar by remember { mutableStateOf(calendars.first()) }
    var isCalendarSelectionExpanded by remember { mutableStateOf(false) }

    var isDatePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    var dateFromState = rememberDatePickerState(
        initialSelectedDateMillis = getMillisNow()
    )

    var isTimePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    var timeFromState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )

    var isDatePickerUntilOpen by rememberSaveable { mutableStateOf(false) }
    var dateUntilState = rememberDatePickerState(
        initialSelectedDateMillis = getMillisNow()
    )

    var isTimePickerUntilOpen by rememberSaveable { mutableStateOf(false) }
    var timeUntilState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = LocalTime.now().minute,
    )

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isAllDay by remember { mutableStateOf(true) }

    if (editEvent != null) {
        selectedCalendar = calendars.find { it.uuid == editEvent.calendarUuid }!!

        // Do some date calculations to get the correct time
        val localDateFrom = editEvent.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val utcDateFromMillis = localDateFrom.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

        dateFromState = rememberDatePickerState(utcDateFromMillis)

        val fromDateTime = editEvent.date.toLocalDateTime(TimeZone.currentSystemDefault())
        timeFromState = rememberTimePickerState(
            initialHour = fromDateTime.hour,
            initialMinute = fromDateTime.minute,
        )

        // Do some more date calculations to get the correct time
        val localDateUntil = editEvent.until.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val adjustedDateUntil = if (editEvent.isAllDay) {
            // Subtract the day that's added when saving
            localDateUntil.plus(-1, DateTimeUnit.DAY)
        } else {
            localDateUntil
        }

        val utcDateUntilMillis =
            adjustedDateUntil.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

        dateUntilState = rememberDatePickerState(utcDateUntilMillis)

        val untilDateTime = editEvent.until.toLocalDateTime(TimeZone.currentSystemDefault())
        timeUntilState = rememberTimePickerState(
            initialHour = untilDateTime.hour,
            initialMinute = untilDateTime.minute,
        )

        title = editEvent.title
        description = editEvent.description
        isAllDay = editEvent.isAllDay
    }

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
            // Add one day because of exclusive rendering
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
                    selectedCalendar.uuid,
                    editEvent?.uuid ?: Uuid.random(),
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
            ModalSaveCancelRow(onDismissRequest, onSaveClick)

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

                    ColorRow(
                        selectedCalendar.name to selectedCalendar.color,
                        {
                            Text(
                                it,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                            )
                        }
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
                                ColorRow(
                                    calendar.name to calendar.color,
                                    { Text(it) }
                                )
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
                            {
                                isDatePickerFromOpen = false

                                val selected = dateFromState.selectedDateMillis

                                if (
                                    dateUntilState.selectedDateMillis != null
                                    && selected != null
                                    && selected > dateUntilState.selectedDateMillis!!
                                ) {
                                    dateUntilState.selectedDateMillis = selected
                                }
                            },
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
