package com.louisweigel.pi_calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.louisweigel.pi_calendar.screens.AddEventsMenu
import com.louisweigel.pi_calendar.screens.CounterView
import com.louisweigel.pi_calendar.screens.TopBar
import com.louisweigel.pi_calendar.ui.theme.PicalendarTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PicalendarTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar() },
                    floatingActionButton = { AddEventsMenu() },
                ) { innerPadding ->
                    MainContent(Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MainContent(modifier: Modifier) {
    Column(modifier) {
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
