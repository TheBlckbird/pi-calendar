package com.louisweigel.pi_calendar.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.louisweigel.pi_calendar.ui.screens.calendar_screen.CalendarScreen
import com.louisweigel.pi_calendar.ui.screens.calendarmanager.CalendarManagerScreen
import com.louisweigel.pi_calendar.ui.screens.singleday.SingleDayScreen
import com.louisweigel.pi_calendar.ui.theme.PiCalendarTheme
import com.louisweigel.pi_calendar.utils.serializableType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
private object CalendarRoute

@Serializable
private object CalendarManager

@Serializable
private data class SingleDay(val date: LocalDate)

@Composable
fun App(
    viewModel: MainViewModel = viewModel { MainViewModel() },
) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    PiCalendarTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            NavHost(
                navController,
                startDestination = CalendarRoute,
                enterTransition = { materialForwardEnter() },
                exitTransition = { materialForwardExit() },
                popEnterTransition = { materialBackEnter() },
                popExitTransition = { materialBackExit() },
            ) {
                composable<CalendarRoute> {
                    CalendarScreen(
                        uiState.lastYearMonth,
                        { viewModel.setMonthSelection(it) },
                        {
                            navController.navigate(CalendarManager)
                        },
                        { date ->
                            navController.navigate(SingleDay(date))
                        },
                    )
                }

                composable<CalendarManager> {
                    CalendarManagerScreen({
                        navController.popBackStack()
                    })
                }

                composable<SingleDay>(
                    typeMap = mapOf(
                        typeOf<LocalDate>() to serializableType<LocalDate>(),
                    )
                ) { backStackEntry ->
                    val singleDay = backStackEntry.toRoute<SingleDay>()
                    SingleDayScreen(
                        singleDay.date,
                        {
                            navController.popBackStack()
                        }
                    )
                }

            }
        }

    }
}