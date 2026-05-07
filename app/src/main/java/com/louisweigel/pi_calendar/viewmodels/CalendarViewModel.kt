package com.louisweigel.pi_calendar.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.louisweigel.pi_calendar.PiCalendarApplication
import com.louisweigel.pi_calendar.core.Calendar
import com.louisweigel.pi_calendar.core.db.repositories.CalendarRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CalendarUiState(
    val calendars: List<Calendar> = emptyList(),
    val defaultBirthdaysCalendar: Calendar? = null,
    val defaultRemindersCalendar: Calendar? = null,
)

class CalendarViewModel(private val calendarRepository: CalendarRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState

    init {
        viewModelScope.launch {
            val defaultEventsCalendar = calendarRepository.getDefaultEventsCalendar()
            val defaultBirthdaysCalendar = calendarRepository.getDefaultBirthdaysCalendar()
            val defaultRemindersCalendar = calendarRepository.getDefaultRemindersCalendar()

            _uiState.update {
                it.copy(
                    calendars = it.calendars + defaultEventsCalendar,
                    defaultBirthdaysCalendar = defaultBirthdaysCalendar,
                    defaultRemindersCalendar = defaultRemindersCalendar,
                )
            }

            calendarRepository.observeAll().collect { calendars ->
                _uiState.update { it.copy(calendars = calendars + defaultEventsCalendar) }
            }
        }
    }

    fun addCalendar(calendar: Calendar) {
        viewModelScope.launch {
            calendarRepository.insert(calendar)
        }
    }

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