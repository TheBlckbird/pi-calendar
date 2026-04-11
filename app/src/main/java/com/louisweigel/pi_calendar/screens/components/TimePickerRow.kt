package com.louisweigel.pi_calendar.screens.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TimePickerRow(
    isPickerOpen: Boolean,
    state: TimePickerState,
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
        val hour = if (state.hour < 10) "0${state.hour}" else "${state.hour}"
        val minute = if (state.minute < 10) "0${state.minute}" else "${state.minute}"

        Text(
            "${hour}:${minute}",
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (isPickerOpen) {
        TimePickerDialog(
            onDismissRequest = onDismissRequest,
            confirmButton = {
                TextButton(onClick = onDismissRequest) {
                    Text("Auswählen")
                }
            },
        ) {
            TimePicker(state = state)
        }
    }
}

@Composable
fun TimePickerDialog(
    onDismissRequest: () -> Unit,
    confirmButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        dismissButton = null,
        confirmButton = confirmButton,
        text = { content() },
        modifier = modifier
    )
}