package com.example.eventtracker.ui.postNewEvent

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostNewEventViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(PostNewEventUiState())
    val uiState: StateFlow<PostNewEventUiState> = _uiState.asStateFlow()
    fun updateEventName(eventName: String) {
        _uiState.value = _uiState.value.copy(eventName = eventName)
    }
    fun updateEventDescription(eventDescription: String) {
        _uiState.value = _uiState.value.copy(eventDescription = eventDescription)
    }
    fun updateEventCategory(eventCategory: String) {
        _uiState.value = _uiState.value.copy(eventCategory = eventCategory)
    }
    fun updateEventDate(eventDate: String) {
        _uiState.value = _uiState.value.copy(eventDate = eventDate)
    }
    fun updateEventTime(eventTime: String) {
        _uiState.value = _uiState.value.copy(eventTime = eventTime)
    }
    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }
}

data class PostNewEventUiState(
    val eventName:String="",
    val eventDescription:String="",
    val eventCategory: String="",
    val eventDate:String="",
    val eventTime:String="",
    val location:String="",
)