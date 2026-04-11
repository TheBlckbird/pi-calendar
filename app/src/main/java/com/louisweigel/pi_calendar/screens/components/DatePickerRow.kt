package com.louisweigel.pi_calendar.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Instant
import kotlin.time.toJavaInstant

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
        val instant =
            Instant.fromEpochMilliseconds(state.selectedDateMillis!!).toJavaInstant().atZone(
                ZoneId.systemDefault()
            )
        val formatter = DateTimeFormatter.ofPattern("EEE, dd. MMMM uuuu", Locale.GERMAN)
        val formattedDate = formatter.format(instant)

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