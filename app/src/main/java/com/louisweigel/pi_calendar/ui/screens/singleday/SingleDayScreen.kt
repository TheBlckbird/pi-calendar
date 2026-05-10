package com.louisweigel.pi_calendar.ui.screens.singleday

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.louisweigel.pi_calendar.R
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.Birthday
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.calendarentry.Event
import com.louisweigel.pi_calendar.core.calendarentry.Reminder
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewBirthdaySheet
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewEventSheet
import com.louisweigel.pi_calendar.viewmodels.CalendarEntryViewModel
import com.louisweigel.pi_calendar.viewmodels.CalendarViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.todayIn
import kotlin.time.Clock

@Composable
fun SingleDayScreen(
    date: LocalDate,
    onNavigateBack: () -> Unit,
    entryViewModel: CalendarEntryViewModel = viewModel(factory = CalendarEntryViewModel.Factory),
    calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModel.Factory),
) {
    val entryUiState by entryViewModel.uiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()

    /**
     * Null if no calendar entry is currently being edited, contains the entry to edit otherwise
     */
    var editEntryData by remember { mutableStateOf<CalendarEntry?>(null) }
    var deleteEntryData by remember { mutableStateOf<CalendarEntry?>(null) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),

        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onNavigateBack) {
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
                    val isCurrentYear =
                        Clock.System.todayIn(TimeZone.currentSystemDefault()).year == date.year

                    val format = if (isCurrentYear) {
                        LocalDate.Format {
                            day(Padding.ZERO)
                            char('.')
                            monthNumber(Padding.ZERO)
                            char('.')
                        }
                    } else {
                        LocalDate.Format {
                            day(Padding.ZERO)
                            char('.')
                            monthNumber(Padding.ZERO)
                            char('.')
                            year(Padding.ZERO)
                        }
                    }

                    Text(date.format(format))
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
                for ((calendar, entry) in entryUiState.entriesWithCalendar.filter { (_, entry) ->
                    entry.includesDate(date.atStartOfDayIn(TimeZone.currentSystemDefault()))
                }) {
                    ListItem(
                        colors = ListItemDefaults.colors(
                            containerColor = calendar.color
                        ),
                        headlineContent = {
                            Text(
                                entry.getTitle(date),
                                color = Color.White,
                            )
                        },
                        trailingContent = {
                            Row {
                                IconButton(
                                    {
                                        editEntryData = entry
                                    },
                                ) {
                                    Icon(
                                        painterResource(R.drawable.edit_24px),
                                        null,
                                        tint = Color.White,
                                    )
                                }

                                IconButton(
                                    {
                                        deleteEntryData = entry
                                    },
                                ) {
                                    Icon(
                                        painterResource(R.drawable.delete_24px),
                                        null,
                                        tint = Color.White,
                                    )
                                }
                            }
                        },
                    )
                }

            }
        }



        if (editEntryData != null) {
            when (editEntryData!!) {
                is Event -> {
                    NewEventSheet(
                        {
                            editEntryData = null
                        },
                        onSave = { event ->
                            entryViewModel.upsertEntry(event)
                            editEntryData = null
                        },
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                        calendars = calendarUiState.calendars + calendarUiState.defaultEventsCalendar!!,
                        editEvent = editEntryData as Event,
                    )
                }

                is Birthday -> {
                    NewBirthdaySheet(
                        { editEntryData = null },
                        { birthday ->
                            editEntryData = null
                            entryViewModel.upsertEntry(birthday)
                        },
                        calendarUiState.defaultBirthdaysCalendar!!,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                        editBirthday = editEntryData as Birthday,
                    )
                }

                is Reminder -> {
                    TODO()
                }
            }
        }

        if (deleteEntryData != null) {
            AlertDialog(
                onDismissRequest = { deleteEntryData = null },

                title = {
                    Text(stringResource(R.string.confirmDeletion))
                },

                text = {
                    Text(stringResource(R.string.singleDay_entryDeletionDescription))
                },

                confirmButton = {
                    TextButton(onClick = {
                        entryViewModel.deleteEntry(deleteEntryData!!)
                        deleteEntryData = null
                    }) {
                        Text(stringResource(R.string.delete))
                    }
                },

                dismissButton = {
                    TextButton(onClick = { deleteEntryData = null }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}