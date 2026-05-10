package com.louisweigel.pi_calendar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.louisweigel.pi_calendar.PiCalendarApplication
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.calendarentry.CalendarEntry
import com.louisweigel.pi_calendar.core.db.repositories.CalendarEntryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalendarEntryUiState(
    val entriesWithCalendar: List<Pair<Calendar, CalendarEntry>> = emptyList()
)

class CalendarEntryViewModel(private val calendarEntryRepository: CalendarEntryRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarEntryUiState())
    val uiState: StateFlow<CalendarEntryUiState> = _uiState

    init {
        viewModelScope.launch {
            calendarEntryRepository.observeAllWithCalendar().collect { entriesWithCalendar ->
                _uiState.update { it.copy(entriesWithCalendar = entriesWithCalendar) }
            }
        }
    }

    fun upsertEntry(entry: CalendarEntry) {
        viewModelScope.launch {
            calendarEntryRepository.upsert(entry)
        }
    }

    fun deleteEntry(entry: CalendarEntry) {
        viewModelScope.launch {
            calendarEntryRepository.delete(entry)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PiCalendarApplication

                CalendarEntryViewModel(
                    calendarEntryRepository = app.calendarEntryRepository
                )
            }
        }
    }
}