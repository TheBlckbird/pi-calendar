package com.louisweigel.pi_calendar.ui.activities.calendarmanager

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.ui.screens.components.ModalSaveCancelRow

@Composable
fun NewCalendarSheet(
    onDismissRequest: () -> Unit,
    onSave: (Calendar) -> Unit,
    modifier: Modifier = Modifier,
    editCalendar: Calendar? = null,
) {
    val sheetState = rememberModalBottomSheetState()

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var showNoNameDialog by remember { mutableStateOf(false) }
    var selectedColor by remember { mutableStateOf(Calendar.POSSIBLE_COLORS.random().component2()) }
    var isColorDropdownExpanded by remember { mutableStateOf(false) }

    if (editCalendar != null) {
        name = editCalendar.name
        description = editCalendar.description
        selectedColor = editCalendar.color
    }

    val onSaveClick = {
        val name = name.trim()
        val description = description.trim()

        if (name.isEmpty()) {
            showNoNameDialog = true
        } else {
            val calendar = if (editCalendar == null) {
                Calendar(
                    name,
                    description,
                    selectedColor,
                )
            } else {
                Calendar(
                    name,
                    description,
                    selectedColor,
                    uuid = editCalendar.uuid,
                )
            }

            onSave(calendar)
        }
    }

    ModalBottomSheet(
        onDismissRequest,
        sheetState = sheetState,
    ) {
        Column(
            modifier = modifier.padding(horizontal = 20.dp)
        ) {
            ModalSaveCancelRow(onDismissRequest, onSaveClick)

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.addMenu_nameField) + "*") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.addMenu_descriptionField)) },
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = {
                    isColorDropdownExpanded = !isColorDropdownExpanded
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp),
                shape = RoundedCornerShape(8.dp),
            ) {
                ColorRow(
                    stringResource(Calendar.POSSIBLE_COLORS.find { it.component2() == selectedColor }
                        ?.component1()
                        ?: R.string.calendarManager_selectColor)
                            to selectedColor
                ) { text ->
                    Text(
                        text,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }

            DropdownMenu(
                expanded = isColorDropdownExpanded,
                onDismissRequest = { isColorDropdownExpanded = false },
            ) {
                for (color in Calendar.POSSIBLE_COLORS) {
                    DropdownMenuItem(
                        leadingIcon = {
                            if (color.component2() == selectedColor) {
                                Icon(
                                    painter = painterResource(R.drawable.check_24px),
                                    contentDescription = null,
                                )
                            }
                        },
                        text = {
                            ColorRow(stringResource(color.component1()) to color.component2())
                        },
                        onClick = {
                            selectedColor = color.component2()
                            isColorDropdownExpanded = false
                        }
                    )
                }

            }
        }
    }

    if (showNoNameDialog) {
        AlertDialog(
            onDismissRequest = { showNoNameDialog = false },

            title = {
                Text(stringResource(R.string.calendarManager_needName))
            },

            text = {
                Text(stringResource(R.string.calendarManager_needNameDescription))
            },

            confirmButton = {
                TextButton(onClick = { showNoNameDialog = false }) {
                    Text(stringResource(R.string.okay))
                }
            }
        )
    }
}