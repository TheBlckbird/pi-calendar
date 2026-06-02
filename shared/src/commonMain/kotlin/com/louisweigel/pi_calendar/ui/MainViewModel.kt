package com.louisweigel.pi_calendar.ui

import androidx.lifecycle.ViewModel
import com.louisweigel.pi_calendar.ui.screens.MonthSelection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

data class MainUiState(val lastMonthSelection: MonthSelection = MonthSelection.getToday())

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState

    fun setMonthSelection(newMonthSelection: MonthSelection) {
        _uiState.update { it.copy(lastMonthSelection = newMonthSelection) }
    }
}