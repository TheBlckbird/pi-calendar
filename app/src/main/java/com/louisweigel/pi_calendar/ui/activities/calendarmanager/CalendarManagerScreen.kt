package com.louisweigel.pi_calendar.ui.activities.calendarmanager

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.viewmodels.CalendarViewModel

@Composable
fun CalendarManagerScreen(
    onBackToMain: () -> Unit,
    calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModel.Factory),
) {
    val calendarUiState by calendarViewModel.uiState.collectAsState()

    var showNewCalendarSheet by remember { mutableStateOf(false) }

    /**
     * Null if no calendar is currently being edited, contains the calendar to edit otherwise
     */
    var editCalendarData by remember { mutableStateOf<Calendar?>(null) }
    var showNameTakenDialog by remember { mutableStateOf(false) }
    var deleteCalendarData by remember { mutableStateOf<Calendar?>(null) }

    LaunchedEffect(Unit) {
        calendarViewModel.addCalendarResult.collect { result ->
            if (result) {
                showNewCalendarSheet = false
            } else {
                showNameTakenDialog = true
            }
        }
    }

    // hide edit dialog

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onBackToMain) {
                        Icon(
                            painter = painterResource(R.drawable.arrow_back_24px),
                            null
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                ),

                title = {
                    Text(stringResource(R.string.calendarManager))
                },
            )

        },

        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.surfaceContainer),
        ) {
            Column {
                val listItemColors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
                for (calendar in (
                        calendarUiState.calendars
                                + calendarUiState.defaultEventsCalendar
                                + calendarUiState.defaultBirthdaysCalendar
                                + calendarUiState.defaultRemindersCalendar
                        )) {
                    if (calendar == null) continue

                    ListItem(
                        colors = listItemColors,
                        headlineContent = {
                            ColorRow(
                                calendar.name to calendar.color
                            )
                        },
                        trailingContent = {
                            Row {
                                IconButton(
                                    {
                                        editCalendarData = calendar
                                    },
                                    enabled = !calendar.isSystem,
                                ) {
                                    Icon(
                                        painterResource(R.drawable.edit_24px),
                                        null,
                                    )
                                }

                                IconButton(
                                    {
                                        deleteCalendarData = calendar
                                    },
                                    enabled = !calendar.isSystem,
                                ) {
                                    Icon(
                                        painterResource(R.drawable.delete_24px),
                                        null,
                                    )
                                }
                            }
                        },
                    )
                }

                ListItem(
                    colors = listItemColors,
                    headlineContent = {
                        TextButton(
                            { showNewCalendarSheet = true },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    stringResource(R.string.calendarManager_addCalendar),
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Icon(painterResource(R.drawable.add_2_24px), null)
                            }
                        }
                    }
                )
            }
        }

    }

    if (showNewCalendarSheet) {
        NewCalendarSheet(
            { showNewCalendarSheet = false },
            { calendar ->
                calendarViewModel.addCalendar(calendar)
            },
        )
    }

    if (editCalendarData != null) {
        NewCalendarSheet(
            {
                editCalendarData = null
            },
            { calendar ->
                calendarViewModel.updateCalendar(calendar)
                editCalendarData = null
            },
            editCalendar = editCalendarData
        )
    }

    if (showNameTakenDialog) {
        AlertDialog(
            onDismissRequest = { showNameTakenDialog = false },

            title = {
                Text(stringResource(R.string.nameTaken))
            },

            text = {
                Text(stringResource(R.string.calendarManager_chooseOtherName))
            },

            confirmButton = {
                TextButton(onClick = { showNameTakenDialog = false }) {
                    Text(stringResource(R.string.okay))
                }
            }
        )
    }

    if (deleteCalendarData != null) {
        AlertDialog(
            onDismissRequest = { deleteCalendarData = null },

            title = {
                Text(stringResource(R.string.confirmDeletion))
            },

            text = {
                Text(stringResource(R.string.calendarManager_calendarDeletionDescription))
            },

            confirmButton = {
                TextButton(onClick = {
                    calendarViewModel.deleteCalendar(deleteCalendarData!!)
                    deleteCalendarData = null
                }) {
                    Text(stringResource(R.string.delete))
                }
            },

            dismissButton = {
                TextButton(onClick = { deleteCalendarData = null }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }
}