package com.louisweigel.pi_calendar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.louisweigel.pi_calendar.ui.activities.calendarmanager.CalendarManagerScreen
import com.louisweigel.pi_calendar.ui.screens.calendar_screen.CalendarScreen
import com.louisweigel.pi_calendar.ui.theme.PicalendarTheme
import kotlinx.serialization.Serializable

@Serializable
object Calendar

@Serializable
object CalendarManager

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
                            CalendarScreen({
                                navController.navigate(CalendarManager)
                            })
                        }

                        composable<CalendarManager> {
                            CalendarManagerScreen({
                                navController.popBackStack()
                            })
                        }
                    }
                }

            }

        }
    }
}