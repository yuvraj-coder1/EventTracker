package com.example.eventtracker.ui.userEventsScreen

import com.example.eventtracker.model.EventData

data class UserEventsScreenUiState(
    val isBookmarksSelected: Boolean = true,
    val bookmarkedEvents: List<EventData> = emptyList(),
    val hostedEvents: List<EventData> = emptyList()
)
