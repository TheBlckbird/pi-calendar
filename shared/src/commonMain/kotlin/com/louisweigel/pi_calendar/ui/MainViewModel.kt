package com.louisweigel.pi_calendar.ui

import androidx.lifecycle.ViewModel
import com.louisweigel.pi_calendar.core.now
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.datetime.YearMonth

data class MainUiState(val lastYearMonth: YearMonth = YearMonth.now())

class MainViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState

    fun setMonthSelection(newMonthSelection: YearMonth) {
        _uiState.update { it.copy(lastYearMonth = newMonthSelection) }
    }
}