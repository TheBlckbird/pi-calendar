package com.louisweigel.pi_calendar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.louisweigel.pi_calendar.PiCalendarApplication
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.db.repositories.CalendarRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalendarUiState(
    /**
     * All user created calendars
     */
    val calendars: List<Calendar> = emptyList(),
    val defaultEventsCalendar: Calendar? = null,
    val defaultBirthdaysCalendar: Calendar? = null,
    val defaultRemindersCalendar: Calendar? = null,
)

class CalendarViewModel(private val calendarRepository: CalendarRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    private val _addCalendarResult = MutableSharedFlow<Boolean>()
    val addCalendarResult = _addCalendarResult.asSharedFlow()

    init {
        viewModelScope.launch {
            calendarRepository.observeAll().collect { calendars ->
                _uiState.update { it.copy(calendars = calendars) }
            }
        }

        viewModelScope.launch {
            calendarRepository.observeDefaultEventsCalendar().collect { calendar ->
                _uiState.update { it.copy(defaultEventsCalendar = calendar) }
            }
        }

        viewModelScope.launch {
            calendarRepository.observeDefaultBirthdaysCalendar().collect { calendar ->
                _uiState.update { it.copy(defaultBirthdaysCalendar = calendar) }
            }
        }

        viewModelScope.launch {
            calendarRepository.observeDefaultRemindersCalendar().collect { calendar ->
                _uiState.update { it.copy(defaultRemindersCalendar = calendar) }
            }
        }
    }

    /**
     * Add a new calendar and store it
     */
    fun addCalendar(calendar: Calendar) {
        viewModelScope.launch {
            val result = calendarRepository.insert(calendar)
            _addCalendarResult.emit(result)
        }
    }

    /**
     * Update the given calendar
     */
    fun updateCalendar(calendar: Calendar) {
        viewModelScope.launch {
            calendarRepository.update(calendar)
        }
    }

    /**
     * Delete the calendar
     */
    fun deleteCalendar(calendar: Calendar) {
        viewModelScope.launch {
            calendarRepository.delete(calendar)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as PiCalendarApplication

                CalendarViewModel(
                    calendarRepository = app.calendarRepository
                )
            }
        }
    }
}