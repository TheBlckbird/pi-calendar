package com.louisweigel.pi_calendar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.asIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

class CounterViewModel : ViewModel() {
    private val _count = mutableIntStateOf(0)
    val count: State<Int>
        get() = _count

    fun increment() {
        _count.intValue += 1
    }

    fun decrement() {
        _count.intValue -= 1
    }

    fun reset() {
        _count.intValue = 0
    }
}


@Composable
fun CounterView(
    viewModel: CounterViewModel = viewModel(),
) {
    val count by viewModel.count.asIntState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Text("Count: $count", style = MaterialTheme.typography.headlineMedium)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.decrement() }) { Text("-") }
            Button(onClick = { viewModel.reset() }) { Text("Reset") }
            Button(onClick = { viewModel.increment() }) { Text("+") }
        }
    }
}