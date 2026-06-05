package com.louisweigel.pi_calendar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

class CalendarEntryViewModel(
    private val calendarEntryRepository: CalendarEntryRepository = PiCalendarApplication.calendarEntryRepository
) : ViewModel() {
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
}