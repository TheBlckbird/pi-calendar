package com.louisweigel.pi_calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.CalendarManager
import com.louisweigel.pi_calendar.core.Month
import com.louisweigel.pi_calendar.screens.CalendarScreen
import com.louisweigel.pi_calendar.screens.MonthSelection
import com.louisweigel.pi_calendar.screens.MonthSelectionScreen
import com.louisweigel.pi_calendar.screens.calendarentry_sheets.NewEventSheet
import com.louisweigel.pi_calendar.screens.navigation.AddEventsMenu
import com.louisweigel.pi_calendar.screens.navigation.NavigationDrawerScreen
import com.louisweigel.pi_calendar.screens.navigation.TopBar
import com.louisweigel.pi_calendar.ui.theme.PicalendarTheme
import kotlinx.coroutines.launch
import java.time.LocalDate

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Note: This will later be put into another class, but for now just use this for
            // referencing the calendar manager
            val stubManager = CalendarManager("/tmp/pi-calendar.db")

            var isFabExpanded by remember { mutableStateOf(false) }
            var isMonthSelectionExpanded by remember { mutableStateOf(false) }
            var isNewEventExpanded by remember { mutableStateOf(false) }

            var currentSelectedMonth by remember {
                mutableStateOf(
                    MonthSelection(
                        Month.from(LocalDate.now().month),
                        LocalDate.now().year
                    )
                )
            }

            val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
            val scope = rememberCoroutineScope()

            PicalendarTheme {
                ModalNavigationDrawer(
                    drawerContent = { NavigationDrawerScreen() },
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
                                    currentSelectedMonth = currentSelectedMonth.getToday()
                                }
                            )
                        },

                        floatingActionButton = {
                            AddEventsMenu(
                                isFabExpanded,
                                { isFabExpanded = !isFabExpanded },
                                { isFabExpanded = false },
                                { isNewEventExpanded = true },
                                {},
                                {},
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
                            CalendarScreen()
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
                                { event, calendar ->
                                    isNewEventExpanded = false
                                    calendar.entries.add(event)
                                },
                                modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding()),
                                listOf(
                                    stubManager.defaultEventsCalendar,
                                ) + stubManager.getAllCalenders(),
                            )
                        }
                    }
                }
            }
        }
    }
}