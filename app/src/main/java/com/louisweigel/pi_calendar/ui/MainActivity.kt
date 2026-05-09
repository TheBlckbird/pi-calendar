package com.louisweigel.pi_calendar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.louisweigel.pi_calendar.ui.screens.CurrentView
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import com.louisweigel.pi_calendar.ui.screens.MonthSelectionScreen
import com.louisweigel.pi_calendar.ui.screens.calendar_manager.CalendarManager
import com.louisweigel.pi_calendar.ui.screens.calendar_screen.CalendarScreen
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewBirthdaySheet
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewEventSheet
import com.louisweigel.pi_calendar.ui.screens.calendarentry_sheets.NewReminderSheet
import com.louisweigel.pi_calendar.ui.screens.navigation.AddEventsMenu
import com.louisweigel.pi_calendar.ui.screens.navigation.NavigationDrawerScreen
import com.louisweigel.pi_calendar.ui.screens.navigation.TopBar
import com.louisweigel.pi_calendar.ui.theme.PicalendarTheme
import com.louisweigel.pi_calendar.viewmodels.CalendarEntryViewModel
import com.louisweigel.pi_calendar.viewmodels.CalendarViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val calendarViewModel: CalendarViewModel by viewModels { CalendarViewModel.Factory }
    private val entryViewModel: CalendarEntryViewModel by viewModels { CalendarEntryViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val entryUiState by entryViewModel.uiState.collectAsState()
            val calendarUiState by calendarViewModel.uiState.collectAsState()

            var isFabExpanded by remember { mutableStateOf(false) }
            var isMonthSelectionExpanded by remember { mutableStateOf(false) }
            var isNewEventExpanded by remember { mutableStateOf(false) }
            var isNewBirthdayExpanded by remember { mutableStateOf(false) }
            var isNewReminderExpanded by remember { mutableStateOf(false) }

            var currentSelectedMonth by remember {
                mutableStateOf(MonthSelection.getToday())
            }

            var currentView by remember { mutableStateOf(CurrentView.CalendarScreen) }

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            PicalendarTheme {
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
                                currentView = CurrentView.CalendarManager
                                scope.launch {
                                    drawerState.close()
                                }
                            },
                        )
                    },
                    drawerState = drawerState,
                ) {
                    when (currentView) {
                        CurrentView.CalendarScreen -> {
                            Scaffold(
                                modifier = Modifier.Companion.fillMaxSize(),

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
                                            currentSelectedMonth = MonthSelection.getToday()
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
                                    CalendarScreen(
                                        currentSelectedMonth, { isForward ->
                                            currentSelectedMonth = if (isForward) {
                                                currentSelectedMonth.getNext()
                                            } else {
                                                currentSelectedMonth.getPrevious()
                                            }
                                        },
                                        entryUiState.entriesWithCalendar
                                    )
                                }

                                if (isMonthSelectionExpanded) {
                                    MonthSelectionScreen(
                                        currentSelectedMonth,
                                        { newSelection ->
                                            currentSelectedMonth = newSelection
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
                                            entryViewModel.addEntry(event)
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
                                            entryViewModel.addEntry(birthday)
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
                                            entryViewModel.addEntry(reminder)
                                        },
                                        calendarUiState.defaultRemindersCalendar!!,
                                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                                    )
                                }
                            }
                        }


                        CurrentView.CalendarManager -> CalendarManager(
                            {
                                currentView = CurrentView.CalendarScreen
                            },
                        )
                    }
                }
            }
        }
    }
}