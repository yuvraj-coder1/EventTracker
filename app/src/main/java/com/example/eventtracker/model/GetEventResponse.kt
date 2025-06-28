package com.example.eventtracker.model

import com.example.eventtracker.dto.EventDto

data class GetEventResponse(
    val success: Boolean,
    val message: String,
    val data: List<EventDto>?
)
