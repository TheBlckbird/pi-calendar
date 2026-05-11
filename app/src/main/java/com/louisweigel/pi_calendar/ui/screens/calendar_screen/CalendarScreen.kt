package com.louisweigel.pi_calendar.ui.screens.calendar_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import com.louisweigel.pi_calendar.ui.screens.MonthSelectionScreen
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewBirthdaySheet
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewEventSheet
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewReminderSheet
import com.louisweigel.pi_calendar.ui.screens.navigation.AddEventsMenu
import com.louisweigel.pi_calendar.ui.screens.navigation.NavigationDrawerScreen
import com.louisweigel.pi_calendar.ui.screens.navigation.TopBar
import com.louisweigel.pi_calendar.viewmodels.CalendarEntryViewModel
import com.louisweigel.pi_calendar.viewmodels.CalendarViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
fun CalendarScreen(
    lastMonthSelection: MonthSelection,
    onMonthSelectionChange: (MonthSelection) -> Unit,
    onNavigateToCalendarManager: () -> Unit,
    onNavigateToSingleDay: (LocalDate) -> Unit,

    calendarViewModel: CalendarViewModel = viewModel(factory = CalendarViewModel.Factory),
    entryViewModel: CalendarEntryViewModel = viewModel(factory = CalendarEntryViewModel.Factory),
) {
    val entryUiState by entryViewModel.uiState.collectAsState()
    val calendarUiState by calendarViewModel.uiState.collectAsState()

    var isFabExpanded by remember { mutableStateOf(false) }
    var isMonthSelectionExpanded by remember { mutableStateOf(false) }
    var isNewEventExpanded by remember { mutableStateOf(false) }
    var isNewBirthdayExpanded by remember { mutableStateOf(false) }
    var isNewReminderExpanded by remember { mutableStateOf(false) }

    var currentSelectedMonth by remember {
        mutableStateOf(lastMonthSelection)
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val changeMonthSelection: (MonthSelection) -> Unit = { monthSelection ->
        currentSelectedMonth = monthSelection
        onMonthSelectionChange(monthSelection)
    }

    ModalNavigationDrawer(
        drawerContent = {
            // Get all available calendars including the system calendars

            val calendars = calendarUiState.calendars.toMutableList()

            if (calendarUiState.defaultEventsCalendar != null) {
                calendars += calendarUiState.defaultEventsCalendar!!
            }

            if (calendarUiState.defaultBirthdaysCalendar != null) {
                calendars += calendarUiState.defaultBirthdaysCalendar!!
            }

            if (calendarUiState.defaultRemindersCalendar != null) {
                calendars += calendarUiState.defaultRemindersCalendar!!
            }

            NavigationDrawerScreen(
                calendars,
                { calendar, newState ->
                    val updatedCalendar = calendar.copy(isShown = newState)
                    calendarViewModel.updateCalendar(updatedCalendar)
                },
                {
                    onNavigateToCalendarManager()
                    scope.launch {
                        drawerState.close()
                    }
                },
            )
        },
        drawerState = drawerState,
    ) {

        Scaffold(
            modifier = Modifier.fillMaxSize(),

            topBar = {
                TopBar(
                    {
                        scope.launch {
                            drawerState.open()
                        }
                    },
                    {
                        isMonthSelectionExpanded = true
                    },
                    currentSelectedMonth,
                    {
                        changeMonthSelection(MonthSelection.getToday())
                    }
                )
            },

            floatingActionButton = {
                AddEventsMenu(
                    isFabExpanded,
                    { isFabExpanded = !isFabExpanded },
                    { isFabExpanded = false },
                    { isNewEventExpanded = true },
                    { isNewBirthdayExpanded = true },
                    { isNewReminderExpanded = true },
                )
            },

            containerColor = MaterialTheme.colorScheme.surfaceContainer,
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .pointerInput(Unit) {
                        detectTapGestures(onTap = { isFabExpanded = false })
                    }
                    .background(MaterialTheme.colorScheme.surfaceContainer),

                ) {
                CalendarGrid(
                    currentSelectedMonth, { isForward ->
                        changeMonthSelection(
                            if (isForward) {
                                currentSelectedMonth.getNext()
                            } else {
                                currentSelectedMonth.getPrevious()
                            }
                        )
                    },
                    entryUiState.entriesWithCalendar,
                    { date ->
                        onNavigateToSingleDay(date)
                    }
                )
            }

            if (isMonthSelectionExpanded) {
                MonthSelectionScreen(
                    currentSelectedMonth,
                    { newSelection ->
                        changeMonthSelection(newSelection)
                    },
                    { isMonthSelectionExpanded = false },
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                )
            }

            if (isNewEventExpanded) {
                NewEventSheet(
                    { isNewEventExpanded = false },
                    { event ->
                        isNewEventExpanded = false
                        entryViewModel.upsertEntry(event)
                    },
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                    calendarUiState.calendars + calendarUiState.defaultEventsCalendar!!
                )
            }

            if (isNewBirthdayExpanded && calendarUiState.defaultBirthdaysCalendar != null) {
                NewBirthdaySheet(
                    { isNewBirthdayExpanded = false },
                    { birthday ->
                        isNewBirthdayExpanded = false
                        entryViewModel.upsertEntry(birthday)
                    },
                    calendarUiState.defaultBirthdaysCalendar!!,
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                )
            }

            if (isNewReminderExpanded) {
                NewReminderSheet(
                    { isNewReminderExpanded = false },
                    { reminder ->
                        isNewReminderExpanded = false
                        entryViewModel.upsertEntry(reminder)
                    },
                    calendarUiState.defaultRemindersCalendar!!,
                    modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                )
            }
        }
    }
}
