package com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.ui.screens.components.DatePickerRow
import com.louisweigel.pi_calendar.ui.screens.components.ModalSaveCancelRow
import com.louisweigel.pi_calendar.utils.getInstantFromMillis
import com.louisweigel.pi_calendar.utils.getMillisNow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import pi_calendar_kmp.shared.generated.resources.Res
import pi_calendar_kmp.shared.generated.resources.addBirthdayMenu_dateOfBirth
import pi_calendar_kmp.shared.generated.resources.addMenu_descriptionField
import pi_calendar_kmp.shared.generated.resources.addMenu_nameField
import kotlin.uuid.Uuid


/**
 * Shows a sheet to add a new birthday
 *
 * @param[onDismissRequest] This is called when the sheet is dismissed. It should hide it
 * @param[onSave] This function is called with the new birthday that should be saved. It should also close this sheet
 * @param[birthdayCalendar] The calendar this birthday will be saved to. It should just be the default calendar for birthday
 * @param[editBirthday] Sets this to editing mode and pre-fills the fields with its data
 */
@Composable
fun NewBirthdaySheet(
    onDismissRequest: () -> Unit,
    onSave: (Birthday) -> Unit,
    birthdayCalendar: Calendar,
    modifier: Modifier = Modifier,
    editBirthday: Birthday? = null,
    defaultDate: LocalDate? = null,
) {
    val sheetState = rememberModalBottomSheetState()

    var isDatePickerFromOpen by rememberSaveable { mutableStateOf(false) }
    var dateOfBirthState = rememberDatePickerState(initialSelectedDateMillis = getMillisNow())

    if (defaultDate != null) {
        dateOfBirthState.selectedDateMillis = defaultDate.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()
    }

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    if (editBirthday != null) {
        name = editBirthday.title
        description = editBirthday.description

        // Do some date calculations to get the correct time
        val localDateOfBirth = editBirthday.date.toLocalDateTime(TimeZone.currentSystemDefault()).date
        val utcDateFromMillis = localDateOfBirth.atStartOfDayIn(TimeZone.UTC).toEpochMilliseconds()

        dateOfBirthState = rememberDatePickerState(utcDateFromMillis)
    }

    val onSaveClick = {
        val date = getInstantFromMillis(dateOfBirthState.selectedDateMillis!!)

        onSave(
            Birthday(
                name,
                description,
                date,
                birthdayCalendar.uuid,
                editBirthday?.uuid ?: Uuid.generateV7(),
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

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(Res.string.addMenu_nameField)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(Res.string.addMenu_descriptionField)) },
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            Spacer(modifier = Modifier.padding(bottom = 4.dp))

            Column {
                Column(verticalArrangement = Arrangement.spacedBy((-10).dp)) {
                    Text(
                        stringResource(Res.string.addBirthdayMenu_dateOfBirth),
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
