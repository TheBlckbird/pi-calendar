package com.louisweigel.pi_calendar.ui.screens.calendarmanager

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
import org.jetbrains.compose.resources.painterResource

import androidx.compose.ui.unit.dp

import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.ui.screens.components.ModalSaveCancelRow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import pi_calendar.app.shared.generated.resources.Res
import pi_calendar.app.shared.generated.resources.addMenu_descriptionField
import pi_calendar.app.shared.generated.resources.addMenu_nameField
import pi_calendar.app.shared.generated.resources.calendarManager_needName
import pi_calendar.app.shared.generated.resources.calendarManager_needNameDescription
import pi_calendar.app.shared.generated.resources.calendarManager_selectColor
import pi_calendar.app.shared.generated.resources.check_24px
import pi_calendar.app.shared.generated.resources.okay
import kotlin.uuid.Uuid

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
            onSave(
                Calendar(
                    name,
                    description,
                    selectedColor,
                    uuid = editCalendar?.uuid ?: Uuid.random()
                )
            )
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
                label = { Text(stringResource(Res.string.addMenu_nameField) + "*") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(Res.string.addMenu_descriptionField)) },
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
                        ?: Res.string.calendarManager_selectColor)
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
                                    painter = painterResource(Res.drawable.check_24px),
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
                Text(stringResource(Res.string.calendarManager_needName))
            },

            text = {
                Text(stringResource(Res.string.calendarManager_needNameDescription))
            },

            confirmButton = {
                TextButton(onClick = { showNoNameDialog = false }) {
                    Text(stringResource(Res.string.okay))
                }
            }
        )
    }
}