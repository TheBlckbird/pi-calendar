package com.louisweigel.pi_calendar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.ui.screens.calendarmanager.CalendarManagerScreen
import com.louisweigel.pi_calendar.ui.screens.calendar_screen.CalendarScreen
import com.louisweigel.pi_calendar.ui.screens.singleday.SingleDayScreen
import com.louisweigel.pi_calendar.ui.theme.PicalendarTheme
import com.louisweigel.pi_calendar.utils.serializableType
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

@Serializable
private object Calendar

@Serializable
private object CalendarManager

@Serializable
private data class SingleDay(val date: LocalDate)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            PicalendarTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surfaceContainer,
                ) {
                    NavHost(
                        navController,
                        startDestination = Calendar,
                        enterTransition = { materialForwardEnter() },
                        exitTransition = { materialForwardExit() },
                        popEnterTransition = { materialBackEnter() },
                        popExitTransition = { materialBackExit() },
                    ) {
                        composable<Calendar> {
                            CalendarScreen(
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
    }
}