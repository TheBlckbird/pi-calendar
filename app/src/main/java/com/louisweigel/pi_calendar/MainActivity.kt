package com.louisweigel.pi_calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.louisweigel.pi_calendar.screens.CalendarScreen
import com.louisweigel.pi_calendar.screens.navigation.AddEventsMenu
import com.louisweigel.pi_calendar.screens.CounterView
import com.louisweigel.pi_calendar.screens.navigation.NavigationDrawerScreen
import com.louisweigel.pi_calendar.screens.navigation.TopBar
import com.louisweigel.pi_calendar.ui.theme.PicalendarTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isExpanded by remember { mutableStateOf(false) }
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
                            TopBar({
                                scope.launch {
                                    drawerState.open()
                                }
                            })
                        },

                        floatingActionButton = {
                            AddEventsMenu(
                                isExpanded,
                                { isExpanded = !isExpanded },
                                { isExpanded = false },
                            )
                        },

                        containerColor = MaterialTheme.colorScheme.surfaceContainer,


                    ) { innerPadding ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .pointerInput(Unit) {
                                    detectTapGestures(onTap = { isExpanded = false })
                                }
                                .background(MaterialTheme.colorScheme.surfaceContainer),

                        ) {
                            CalendarScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainContent() {
    Column {
        CounterView()

        LazyColumn(Modifier.fillMaxSize()) {
            items(10000000) { index ->
                Text(
                    text = "Number ${index + 1}",
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
