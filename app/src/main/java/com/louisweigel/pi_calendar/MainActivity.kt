package com.louisweigel.pi_calendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
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
            var isExpanded by remember { mutableStateOf(false) }

            val boxAlphaValue = if (isExpanded) 0.7f else 0.0f
            val boxAlpha = animateFloatAsState(targetValue = boxAlphaValue)

            PicalendarTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = { TopBar() },
                    floatingActionButton = {
                        AddEventsMenu(
                            isExpanded,
                            { isExpanded = !isExpanded },
                            { isExpanded = false },
                        )
                    },
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .pointerInput(Unit) {
                                detectTapGestures(onTap = { isExpanded = false })
                            }
                    ) {
                        MainContent()
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
